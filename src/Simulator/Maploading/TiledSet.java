package Simulator.Maploading;

import javax.imageio.ImageIO;
import javax.json.JsonObject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class TiledSet
{
    private int columns;
    private int firstGID;
    private int lastGID;
    private String imagePath;
    private BufferedImage image;
    private int imageHeight;
    private int imageWidth;
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
        this.imageHeight = jsonObject.getInt("imageheight");
        this.imageWidth = jsonObject.getInt("imagewidth");
        this.imagePath = jsonObject.getString("image");
        this.tiles = new ArrayList<>();
        init();
    }

    private void init() {
        try {
            image = ImageIO.read(getClass().getResource("/TiledMaps/" + this.imagePath));
            System.out.println("Loaded image");
            for (int i = 0; i < this.tileCount; i++) {
                tiles.add(new Tile(image.getSubimage(tileWidth * (i % columns),tileHeight * (i / columns), tileWidth, tileHeight)));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
