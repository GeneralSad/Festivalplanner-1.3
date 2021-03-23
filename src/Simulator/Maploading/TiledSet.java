package Simulator.Maploading;

import javax.imageio.ImageIO;
import javax.json.JsonObject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Represents a set of multiple Tiles with their associated images
 */
public class TiledSet
{
    private int columns;
    private int firstGID;
    private int lastGID;
    private String imagePath;
    private BufferedImage image;
//    private int imageHeight;
//    private int imageWidth;
    private int tileCount;
    private int tileHeight;
    private int tileWidth;
    private ArrayList<Tile> tiles;

    public TiledSet(JsonObject jsonObject) throws NullPointerException {
        this.columns = jsonObject.getInt("columns");
        this.tileCount = jsonObject.getInt("tilecount");
        this.tileHeight = jsonObject.getInt("tileheight");
        this.tileWidth = jsonObject.getInt("tilewidth");
        this.firstGID = jsonObject.getInt("firstgid");
        this.lastGID = this.firstGID + this.tileCount;
//        this.imageHeight = jsonObject.getInt("imageheight");
//        this.imageWidth = jsonObject.getInt("imagewidth");
        this.imagePath = jsonObject.getString("image");
        this.tiles = new ArrayList<>();
        init(jsonObject);
    }

    /**
     * Slices all the tiles from the original image and stores them as Tile objects in a list
     */
    private void init(JsonObject jsonObject) {

        try {
            image = ImageIO.read(getClass().getResource("/TiledMaps/" + this.imagePath));
            for (int i = 0; i < this.tileCount; i++) {
                tiles.add(new Tile(image.getSubimage(tileWidth * (i % columns),tileHeight * (i / columns), tileWidth, tileHeight)));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int getFirstGID()
    {
        return firstGID;
    }

    public int getLastGID()
    {
        return lastGID;
    }

    /**
     * Return the tile associated with the gid by taking that gid as an index out of the tiles list
     * @param gid
     * @return
     */
    public BufferedImage getTile(int gid) {
        return this.tiles.get(gid).getTileImage();
    }

    public Tile TileData(int gid){
        return this.tiles.get(gid);
    }


}
