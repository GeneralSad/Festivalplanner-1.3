package Simulator.Maploading;

import org.jfree.fx.FXGraphics2D;

import javax.json.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a full tiledmap with all the different tiledsets and tiledlayers
 * Possible to draw all the tiledlayers contained within using the tiledsets contained within
 */
public class TiledMap
{
    private int height;
    private int width;
    private int tileHeight;
    private int tileWidth;
    private ArrayList<TiledLayer> tiledLayers;
    private ArrayList<TiledSet> tiledSets;
    private String jsonFileName;

    public TiledMap(String jsonFileName)
    {
        this.jsonFileName = jsonFileName;
        this.tiledLayers = new ArrayList<>();
        this.tiledSets = new ArrayList<>();
        init();
    }

    public int getTileHeight()
    {
        return tileHeight;
    }

    public int getTileWidth()
    {
        return tileWidth;
    }

    /**
     * Read the Json file given in the constructor to make all attributes, tiledlayers, tiledsets and tiles
     */
    private void init()
    {
        JsonReader jsonReader = null;
        jsonReader = Json.createReader(getClass().getResourceAsStream(jsonFileName));
        JsonObject root = jsonReader.readObject();

        this.width = root.getInt("width");
        this.height = root.getInt("height");
        this.tileHeight = root.getInt("tileheight");
        this.tileWidth = root.getInt("tilewidth");

        JsonArray tilesets = root.getJsonArray("tilesets");
        for (int i = 0; i < tilesets.size(); i++) {
            try {
                TiledSet tiledSet = new TiledSet(tilesets.getJsonObject(i));
                this.tiledSets.add(tiledSet);
            } catch (NullPointerException e) {
                System.out.println("Tileset nr: " + i + " couldn't load");
            }

        }

        JsonArray tileLayers = root.getJsonArray("layers");
        for (int i = 0; i < tileLayers.size(); i++) {
            TiledLayer tiledLayer = new TiledLayer(tileLayers.getJsonObject(i), this);
            tiledLayers.add(tiledLayer);
        }
    }

    /**
     * Draw all tiledlayers in the list of tiledlayers
     * @param fxGraphics2D
     */
    public void draw(FXGraphics2D fxGraphics2D) {
        fxGraphics2D.clearRect(-4000, -4000, 4000, 4000);
        fxGraphics2D.setBackground(Color.white);

        for (TiledLayer tiledLayer : this.tiledLayers) {
            tiledLayer.draw(fxGraphics2D);
        }
    }

    public ArrayList<TiledSet> getTiledSets()
    {
        return tiledSets;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }
}
