package Simulator.Maploading;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;

public class TiledLayer
{
    private int x;
    private int y;
    private int width;
    private int height;
    private ArrayList<Integer> data;
    private boolean visible;

    public TiledLayer(JsonObject jsonObject) {
        this.x = jsonObject.getInt("x");
        this.y = jsonObject.getInt("y");
        this.width = jsonObject.getInt("width");
        this.height = jsonObject.getInt("height");
        this.visible = jsonObject.getBoolean("visible");
        this.data = new ArrayList<>();

        JsonArray jsonArray = jsonObject.getJsonArray("data");
        int jsonArraySize = jsonArray.size();
        for (int i = 0; i < jsonArraySize; i++) {
            this.data.add(jsonArray.getInt(i));
        }
    }
}
