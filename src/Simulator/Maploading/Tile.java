package Simulator.Maploading;

import org.jfree.fx.FXGraphics2D;

import java.awt.image.BufferedImage;

/**
 * Represents a single tile with the images associated with it
 * Can be accessed through TiledSet
 */
public class Tile
{
    private BufferedImage tileImage;
    private int x;
    private int y;
    private int row;
    private int column;
    private long gid;
    double rotation;
    private int walkedOnCounter;

    public Tile(BufferedImage tileImage, int x, int y, int row, int column, long gid, double rotation)
    {
        this.tileImage = tileImage;
        this.x = x;
        this.y = y;
        this.row = row;
        this.column = column;
        this.gid = gid;
        this.rotation = rotation;
    }

    public void draw(FXGraphics2D fxGraphics2D)
    {
        if (tileImage != null)
        {
            fxGraphics2D.drawImage(tileImage, x, y, null);
        }
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public double getRotation()
    {
        return rotation;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public void incrementWalkedOnCounter()
    {
        this.walkedOnCounter++;
    }

    public int getWalkedOnCounter()
    {
        return walkedOnCounter;
    }
}
