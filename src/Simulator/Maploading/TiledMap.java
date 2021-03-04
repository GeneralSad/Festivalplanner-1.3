package Simulator.Maploading;

import javax.json.*;
import java.util.ArrayList;

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

    private void init()
    {
        System.out.println("File path: " + this.jsonFileName);
        JsonReader jsonReader = null;
        jsonReader = Json.createReader(getClass().getResourceAsStream(jsonFileName));
        JsonObject root = jsonReader.readObject();

        this.width = root.getInt("width");
        this.height = root.getInt("height");
        this.tileHeight = root.getInt("tileheight");
        this.tileWidth = root.getInt("tilewidth");

        System.out.println("Data:");
        System.out.println("width: " + this.width);
        System.out.println("height: " + this.height);
        System.out.println("tilewidth: " + this.tileWidth);
        System.out.println("tileheight: " + this.tileHeight);

        JsonArray tilesets = root.getJsonArray("tilesets");
        System.out.println("Amount of tilesets: " + tilesets.size());
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
            TiledLayer tiledLayer = new TiledLayer(tileLayers.getJsonObject(i));
            tiledLayers.add(tiledLayer);
        }
        System.out.println("Done!");
    }
}
