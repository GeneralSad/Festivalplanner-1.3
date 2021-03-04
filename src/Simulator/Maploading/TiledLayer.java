package Simulator.Maploading;

import org.jfree.fx.FXGraphics2D;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;

/**
 * Represents a layer of drawn tiles, possible to draw all the tiles using a draw() method
 */
public class TiledLayer
{
    //TODO if layers which start at an x and y not 0 then make it so it still draws properly
    private int x;
    private int y;
    private int width;
    private int height;
    private ArrayList<Integer> data;
    private boolean visible;
    private TiledMap tiledMap;

    public TiledLayer(JsonObject jsonObject, TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        this.x = jsonObject.getInt("x");
        this.y = jsonObject.getInt("y");
        this.width = jsonObject.getInt("width");
        this.height = jsonObject.getInt("height");
        this.visible = jsonObject.getBoolean("visible");
        this.data = new ArrayList<>();

        // Get all the different tile id's in order and store them in a list
        JsonArray jsonArray = jsonObject.getJsonArray("data");
        int jsonArraySize = jsonArray.size();
        for (int i = 0; i < jsonArraySize; i++) {
            this.data.add(jsonArray.getInt(i));
        }
    }

    /**
     * Draw all the tiles in the layer
     * @param fxGraphics2D
     */
    public void draw(FXGraphics2D fxGraphics2D) {
        for (int i = 0; i < data.size(); i++) {
            int gid = this.data.get(i);
            if (gid != 0) {
                TiledSet tiledSet = null;
                for (TiledSet tempTiledSet : this.tiledMap.getTiledSets()) {
                    if (gid >= tempTiledSet.getFirstGID() && gid <= tempTiledSet.getLastGID()) {
                        tiledSet = tempTiledSet;
                        break;
                    }
                }
                if (tiledSet != null) {
                    fxGraphics2D.drawImage(tiledSet.getTile(gid), this.tiledMap.getTileWidth() * (i % tiledMap.getWidth()), this.tiledMap.getTileHeight() * (i / tiledMap.getHeight()), null);
                }
            }
        }
    }
}
