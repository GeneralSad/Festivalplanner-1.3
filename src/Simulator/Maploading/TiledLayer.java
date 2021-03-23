package Simulator.Maploading;

import org.jfree.fx.FXGraphics2D;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.lang.model.element.NestingKind;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.lang.annotation.AnnotationFormatError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    // These attributes are for an infinite map which has tiledlayers with defined coordinates, for now they're not in use
    //    private int x;
    //    private int y;
    //    private int width;
    //    private int height;
    private ArrayList<Long> data;
    private boolean visible;
    private TiledMap tiledMap;

    private LinkedHashMap<AffineTransform, Double> transform = new LinkedHashMap<>();

    public TiledLayer(JsonObject jsonObject, TiledMap tiledMap)
    {
        this.tiledMap = tiledMap;
        //        this.x = jsonObject.getInt("x");
        //        this.y = jsonObject.getInt("y");
        //        this.width = jsonObject.getInt("width");
        //        this.height = jsonObject.getInt("height");
        this.visible = jsonObject.getBoolean("visible");
        this.data = new ArrayList<>();



        // Get all the different tile id's in order and store them in a list
        JsonArray jsonArray = jsonObject.getJsonArray("data");
        int jsonArraySize = jsonArray.size();
        for (int i = 0; i < jsonArraySize; i++)
        {
            this.data.add(jsonArray.getJsonNumber(i).longValue());
        }
    }

    /**
     * Draw all the tiles in the layer
     *
     * @param fxGraphics2D
     */

    BufferedImage cache = null;

    public void draw(FXGraphics2D fxGraphics2D)
    {

        if (cache == null)
        {
            cache = new BufferedImage(tiledMap.getWidth() * tiledMap.getTileWidth(), tiledMap.getHeight() * tiledMap.getTileHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D cashGraphics = cache.createGraphics();


        for (int i = 0; i < data.size(); i++)
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
                    if (gid >= tempTiledSet.getFirstGID() && gid <= tempTiledSet.getLastGID())
                    {
                        tiledSet = tempTiledSet;
                        break;
                    }
                }
                // if a tiledset was found draw the current tile
                if (tiledSet != null)
                {
                    AffineTransform affineTransform = new AffineTransform();
                    affineTransform.translate(this.tiledMap.getTileWidth() * (i % tiledMap.getWidth()), this.tiledMap.getTileHeight() * (i / tiledMap.getHeight()));

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


                    double rotation = 0.0;
                    if ((gid - tiledSet.getFirstGID() == 896)){
                        rotation = 0.0;
                    } else if ((int) gid - tiledSet.getFirstGID() == 5){
                        rotation = 180.0;
                    } else if ((int) gid - tiledSet.getFirstGID() == 4){
                        rotation = 90;
                    } else if ((int) gid - tiledSet.getFirstGID() == 2){
                        rotation = 270;
                    }
                    //puts the affinetranform and the boolean data in an hashmap of afffinetransmos
                    transform.put(affineTransform, rotation);

                    if (visible)
                    {
                        // -1 compensation because gid starts at 1 and arrays start at 0
                        cashGraphics.drawImage(tiledSet.getTile((int) gid - tiledSet.getFirstGID()), affineTransform, null);
                    }
                }
            }
        }
        if (visible)
        {
            fxGraphics2D.drawImage(cache, 0, 0, null);
        }
    }

    public boolean containsMethodeBoolean(Point2D point2D){
        for (Map.Entry<AffineTransform, Double> entry:transform.entrySet())
        {
            if (entry.getKey().getTranslateX() <= point2D.getX() && entry.getKey().getTranslateX() + tiledMap.getTileWidth() > point2D.getX()
                && entry.getKey().getTranslateY() <= point2D.getY() && entry.getKey().getTranslateY() + tiledMap.getTileHeight() > point2D.getY()){

                return true;
            }

        }
        return false;
    }

    public LinkedHashMap<Point2D, Double> allMethodeBoolean(){
        LinkedHashMap<Point2D, Double> temporary = new LinkedHashMap<>();

        for (Map.Entry<AffineTransform, Double> entry:transform.entrySet())
        {
            temporary.put(new Point2D.Double(entry.getKey().getTranslateX(), entry.getKey().getTranslateY()), entry.getValue());

        }
        return temporary;
    }

}









