package Simulator.CameraSystem;

import Simulator.Maploading.Tile;
import Simulator.Pathfinding.Pathfinding;
import Simulator.Pathfinding.PathfindingTile;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;

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

    public void draw(FXGraphics2D fxGraphics2D) {
        fxGraphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

        int x = (int)camera.getRelativeWidth() - 500;
        int y = (int)-camera.getY() + 200;

        fxGraphics2D.setColor(Color.CYAN);
        fxGraphics2D.fill(new Rectangle2D.Double(x, y, 500, 50));
        fxGraphics2D.setColor(Color.BLACK);

        // Draw npc information
        if (tile != null) {
            fxGraphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
            fxGraphics2D.drawString("Selected tile at: " + tile.getX() + " " + tile.getY(), x, y + 30);
            fxGraphics2D.drawString("Amount of people walked on this tile: " + tile.getWalkedOnCounter(), x, y + 45);
        }

        fxGraphics2D.setComposite(AlphaComposite.Clear);
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
