package Simulator.Maploading;

import org.jfree.fx.FXGraphics2D;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Represents a layer of drawn tiles, possible to draw all the tiles using a draw() method
 */
public class TiledLayer
{
    // bit flags for rotated tiles
    private static final long ROTATION_LEFT_ONCE =      0b011;
    private static final long ROTATION_LEFT_TWICE =     0b110;
    private static final long ROTATION_LEFT_THRICE =    0b101;
    private static final long ROTATION_VERTICAL =       0b100;
    private static final long ROTATION_HORIZONTAL =     0b010;
    // check for any rotation at all
    private static final long ROTATED =                 0b111;
//    private static final long ROTATED_CHECK =           0b00010000000000000000000000000000;

//    private int x;
//    private int y;
//    private int width;
//    private int height;
    private ArrayList<Long> data;
    private boolean visible;
    private TiledMap tiledMap;

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
        for (int i = 0; i < jsonArraySize; i++) {
            this.data.add(jsonArray.getJsonNumber(i).longValue());
        }
    }

    /**
     * Draw all the tiles in the layer
     * @param fxGraphics2D
     */
    public void draw(FXGraphics2D fxGraphics2D) {
        if (visible) {
            for (int i = 0; i < 12/*data.size()*/; i++) {
                long gid = this.data.get(i);
                if (gid != 0) {
                    long tempGid = gid >> (32 - 3);
                    if (tempGid > 0) {
                        if ((tempGid & ROTATION_LEFT_ONCE) == ROTATION_LEFT_ONCE) {
                            System.out.println("left once flip at " + i);
                        } else if ((tempGid & ROTATION_LEFT_TWICE) == ROTATION_LEFT_TWICE) {
                            System.out.println("left twice flip at " + i);
                        } else if ((tempGid & ROTATION_LEFT_THRICE) == ROTATION_LEFT_THRICE) {
                            System.out.println("left thrice flip at " + i);
                        } else if ((tempGid & ROTATION_HORIZONTAL) == ROTATION_HORIZONTAL) {
                            System.out.println("horizontal flip at " + i);
                        } else if ((tempGid & ROTATION_VERTICAL) == ROTATION_VERTICAL) {
                            System.out.println("vertical flip at " + i);
                        }
                    }

                    // Reset the rotation bits to be able to find the tile
                    gid &= ROTATED;

                    // look if the base tile exists
                    TiledSet tiledSet = null;
                    for (TiledSet tempTiledSet : this.tiledMap.getTiledSets()) {
                        if (gid >= tempTiledSet.getFirstGID() && gid <= tempTiledSet.getLastGID()) {
                            tiledSet = tempTiledSet;
                            break;
                        }
                    }
                    if (tiledSet != null) {
                        fxGraphics2D.drawImage(tiledSet.getTile((int)gid), this.tiledMap.getTileWidth() * (i % tiledMap.getWidth()), this.tiledMap.getTileHeight() * (i / tiledMap.getHeight()), null);
                    }
                }
            }
        }

    }
}
