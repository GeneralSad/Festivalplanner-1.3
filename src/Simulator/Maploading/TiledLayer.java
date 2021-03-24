package Simulator.Maploading;

import org.jfree.fx.FXGraphics2D;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Represents a layer of drawn tiles, possible to draw all the tiles using a draw() method
 */
public class TiledLayer
{
    // bit flags for rotated tiles
    private static final long ROTATION_LEFT_ONCE = 0b011;
    private static final long ROTATION_LEFT_TWICE = 0b110;
    private static final long ROTATION_LEFT_THRICE = 0b101;
    private static final long ROTATION_VERTICAL = 0b100;
    private static final long ROTATION_HORIZONTAL = 0b010;
    private static final long ROTATION_HORIZONTAL_AND_LEFT_ONCE = 0b001;
    private static final long ROTATION_HORIZONTAL_AND_LEFT_THRICE = 0b111;

    private ArrayList<Long> data;
    private boolean visible;
    private TiledMap tiledMap;

    private ArrayList<Tile> tilesInLayer;

    //    private LinkedHashMap<AffineTransform, Double> transform = new LinkedHashMap<>();

    public TiledLayer(JsonObject jsonObject, TiledMap tiledMap)
    {
        this.tiledMap = tiledMap;
        this.visible = jsonObject.getBoolean("visible");
        this.data = new ArrayList<>();
        this.tilesInLayer = new ArrayList<>();

        // Get all the different tile id's in order and store them in a list
        JsonArray jsonArray = jsonObject.getJsonArray("data");
        int jsonArraySize = jsonArray.size();
        for (int i = 0; i < jsonArraySize; i++)
        {
            this.data.add(jsonArray.getJsonNumber(i).longValue());
        }


        // read custom properties
        JsonArray properties = jsonObject.getJsonArray("properties");
        // area
        if (properties.getJsonObject(0).getBoolean("value"))
        {
            tiledMap.setAreaLayer(this);
        }
        // seatable
        if (properties.getJsonObject(1).getBoolean("value"))
        {
            tiledMap.setSeatableLayer(this);
        }
        // walkable
        if (properties.getJsonObject(2).getBoolean("value"))
        {
            tiledMap.setWalkableLayer(this);
        }

        // initialise
        init();
    }

    private void init()
    {
        // intialise all valid tiles
        for (int i = 0; i < this.data.size(); i++)
        {
            long gid = this.data.get(i);
            if (gid != 0)
            {
                // Take the first 3 its of the gid
                // these are the rotation bits
                long currentRotation = gid >> (32 - 3);

                // Take the rotation bits out of the gid
                // uses a XOR operator using the detected rotation,
                // this returns all differing bits as 1 and common bits as 0, meaning the rotation bits are removed but the tile id bits stay
                gid ^= currentRotation << (32 - 3);

                // looks for the tiledset that contains the tile id for the current tile
                TiledSet tiledSet = null;
                for (TiledSet tempTiledSet : this.tiledMap.getTiledSets())
                {
                    if (gid >= tempTiledSet.getFirstGID() && gid < tempTiledSet.getLastGID())
                    {
                        tiledSet = tempTiledSet;
                        break;
                    }
                }
                // if a tiledset was found draw the current tile
                if (tiledSet != null)
                {
                    AffineTransform affineTransform = new AffineTransform();

                    // if there is any rotation change the affinetransform accordingly
                    if (currentRotation != 0)
                    {

                        if (currentRotation == ROTATION_LEFT_ONCE)
                        {
                            // Rotate once to the left, so -0.5 PI
                            // image is a bit up so compensate on the y
                            affineTransform.translate(0, this.tiledMap.getTileHeight());
                            affineTransform.rotate(Math.PI * -0.5);


                        }
                        else if (currentRotation == ROTATION_LEFT_TWICE)
                        {
                            // Rotate twice to the left, so -1 PI
                            // image is a bit up and to the left so compensate on the y and x
                            affineTransform.translate(this.tiledMap.getTileWidth(), this.tiledMap.getTileHeight());
                            affineTransform.rotate(Math.PI * -1);

                        }
                        else if (currentRotation == ROTATION_LEFT_THRICE)
                        {
                            // Rotate three times to the left, so -1.5 PI
                            // image is a bit to the left so compensate on the x
                            affineTransform.translate(this.tiledMap.getTileWidth(), 0);
                            affineTransform.rotate(Math.PI * -1.5);


                        }
                        else if (currentRotation == ROTATION_HORIZONTAL)
                        {
                            // Scale the y axis to be reversed and compensate the translation for the reversed axis
                            affineTransform.scale(1, -1);
                            affineTransform.translate(0, -this.tiledMap.getTileHeight());
                        }
                        else if (currentRotation == ROTATION_VERTICAL)
                        {
                            // Scale the x axis to be reversed and compensate the translation of the reversed axis
                            affineTransform.scale(-1, 1);
                            affineTransform.translate(-this.tiledMap.getTileWidth(), 0);

                        }
                        else if (currentRotation == ROTATION_HORIZONTAL_AND_LEFT_ONCE)
                        {
                            // Scale the y axis to be reversed and rotate once to the left
                            affineTransform.scale(1, -1);
                            affineTransform.rotate(Math.PI * -0.5);

                        }
                        else if (currentRotation == ROTATION_HORIZONTAL_AND_LEFT_THRICE)
                        {
                            // Scale the y axis to be reversed and rotate three times to the left
                            // compensate in the x and y axis
                            affineTransform.scale(1, -1);
                            affineTransform.translate(this.tiledMap.getTileWidth(), -this.tiledMap.getTileHeight());
                            affineTransform.rotate(Math.PI * -1.5);

                        }
                    }

                    // Get the image and apply the affinetransform to it for rotations
                    BufferedImage srcImage = tiledSet.getTileImage((int) (gid - tiledSet.getFirstGID()));
                    if (srcImage != null)
                    {
                        BufferedImage tileImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), srcImage.getType());

                        Graphics2D imageGraphics = tileImage.createGraphics();
                        imageGraphics.drawRenderedImage(srcImage, affineTransform);

                        // rotation for seats
                        double rotation = 0.0;
                        if ((gid - tiledSet.getFirstGID() == 896))
                        {
                            rotation = 0.0;
                        }
                        else if ((int) gid - tiledSet.getFirstGID() == 5)
                        {
                            rotation = 180.0;
                        }
                        else if ((int) gid - tiledSet.getFirstGID() == 4)
                        {
                            rotation = 90;
                        }
                        else if ((int) gid - tiledSet.getFirstGID() == 2)
                        {
                            rotation = 270;
                        }

                        int column = (i % tiledMap.getWidth());
                        int row = (i / tiledMap.getHeight());
                        Tile tile = new Tile(tileImage, this.tiledMap.getTileWidth() * column, this.tiledMap.getTileHeight() * row, row, column, gid, rotation);

                        this.tilesInLayer.add(tile);
                    }
                    else
                    {
                        System.out.println("Reading from tiledset with image path: " + tiledSet.getImagePath() + " first gid: " + tiledSet.getFirstGID() + " last gid: " + tiledSet.getLastGID() + " returned null");
                    }
                }
            }
        }
    }

    public void draw(FXGraphics2D fxGraphics2D)
    {
        if (visible)
        {
            for (Tile tile : tilesInLayer)
            {
                tile.draw(fxGraphics2D);
            }
        }
    }

    /**
     * Returns true if the given point is within the layer and has a non zero tile gid
     *
     * @param point2D
     * @return
     */
    public boolean isPositionValidTile(Point2D point2D)
    {
        int row = (int)point2D.getY() / this.tiledMap.getTileHeight();
        int column = (int)point2D.getX() / this.tiledMap.getTileWidth();
        boolean withinBounds = isPositionWithinBounds(row, column);

        // Don't allow multiplication by 0 when there is in fact a valid gid
        if (row == 0 && column != 0) {
            row = 1;
        } else if (row != 0 && column == 0) {
            column = 1;
        }
        boolean validTile = this.data.get(row * tiledMap.getWidth() + column) != 0;
        return withinBounds && validTile;
    }

    /**
     * Checks if a given row and column is within the layer or not
     *
     * @param row
     * @param column
     * @return
     */
    private boolean isPositionWithinBounds(int row, int column)
    {
        return (row >= 0 && row < tiledMap.getHeight()) && (column >= 0 && column < tiledMap.getWidth());
    }

    public ArrayList<Tile> getTilesInLayer()
    {
        return tilesInLayer;
    }

    public Tile getTile(Point2D location)
    {
        int row = (int) (location.getY() / tiledMap.getTileHeight());
        int column = (int) (location.getX() / tiledMap.getTileWidth());
        return getTile(row, column);
    }

    public Tile getTile(int row, int column)
    {
        if (isPositionWithinBounds(row, column))
        {
            for (Tile tile : tilesInLayer)
            {
                int tileRow = tile.getRow();
                int tileColumn = tile.getColumn();
                if (tile.getRow() == row && tile.getColumn() == column)
                {
                    return tile;
                }
            }
        }
        return null;
    }
}









