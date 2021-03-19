package Simulator.Maploading;

import java.awt.image.BufferedImage;

/**
 * Represents a single tile with the images associated with it
 * Can be accessed through TiledSet
 */
public class Tile
{
    private BufferedImage tileImage;
    private boolean sitable;
    private boolean walkable;

    public Tile(BufferedImage tileImage)
    {
        this.tileImage = tileImage;
    }

    public BufferedImage getTileImage()
    {
        return tileImage;
    }
}
