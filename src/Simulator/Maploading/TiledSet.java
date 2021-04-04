package Simulator.Maploading;

import javax.imageio.ImageIO;
import javax.json.JsonObject;
import java.awt.image.BufferedImage;
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
    private int tileCount;
    private int tileHeight;
    private int tileWidth;
    private ArrayList<BufferedImage> tileImages;

    public TiledSet(JsonObject jsonObject) throws NullPointerException
    {
        this.columns = jsonObject.getInt("columns");
        this.tileCount = jsonObject.getInt("tilecount");
        this.tileHeight = jsonObject.getInt("tileheight");
        this.tileWidth = jsonObject.getInt("tilewidth");
        this.firstGID = jsonObject.getInt("firstgid");
        this.lastGID = this.firstGID + this.tileCount;
        this.imagePath = jsonObject.getString("image");
        this.tileImages = new ArrayList<>();
        // initialise loading the images
        init();
    }

    /**
     * Slices all the tileImages from the original image and stores them as Tile objects in a list
     */
    private void init()
    {

        try
        {
            BufferedImage image = ImageIO.read(getClass().getResource("/TiledMaps/" + this.imagePath));
            for (int i = 0; i < this.tileCount; i++)
            {
                tileImages.add(image.getSubimage(tileWidth * (i % columns), tileHeight * (i / columns), tileWidth, tileHeight));
            }
        }
        catch (Exception e)
        {
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
     * Return the tile associated with the gid by taking that gid as an index out of the tileImages list
     *
     * @param gid
     * @return
     */
    public BufferedImage getTileImage(int gid)
    {
        if (gid < tileImages.size())
        {
            return this.tileImages.get(gid);
        }
        return null;
    }

    public String getImagePath()
    {
        return imagePath;
    }


}
