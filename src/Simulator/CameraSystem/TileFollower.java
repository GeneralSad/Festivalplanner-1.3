package Simulator.CameraSystem;

import Simulator.Maploading.Tile;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class TileFollower
{
    private Tile tile;
    private boolean followingATile;
    private Camera camera;

    public TileFollower(Camera camera)
    {
        this.tile = null;
        this.followingATile = false;
        this.camera = camera;
    }

    public void draw(FXGraphics2D fxGraphics2D)
    {

        int x = (int) camera.getRelativeWidth() - 500;
        int y = (int) -camera.getY() + 220;

        float alpha = 0.75f;
        Color colorField = new Color(0, 0, 0, alpha);
        fxGraphics2D.setColor(colorField);

        fxGraphics2D.fill(new RoundRectangle2D.Double(x, y, 500, 60, 20 ,20));
        fxGraphics2D.setColor(Color.WHITE);


        // Draw npc information
        if (tile != null)
        {
            fxGraphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
            fxGraphics2D.drawString("Selected npc information:", x + 10, y + 19);

            fxGraphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
            fxGraphics2D.drawString("Selected tile at: " + tile.getX() + " " + tile.getY(), x + 10, y + 35);
            fxGraphics2D.drawString("Amount of people walked on this tile: " + tile.getWalkedOnCounter(), x  + 10, y + 50);
        }

    }

    public Tile getTile()
    {
        return tile;
    }

    public void setTile(Tile tile)
    {
        this.tile = tile;
    }

    public boolean isFollowingATile()
    {
        return followingATile;
    }

    public void setFollowingATile(boolean followingATile)
    {
        this.followingATile = followingATile;
    }
}
