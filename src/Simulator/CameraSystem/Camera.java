package Simulator.CameraSystem;

public class Camera
{
    private double x;
    private double y;
    private double totalWidth;
    private double totalHeight;
    private NPCFollower npcFollower;
    private TileFollower tileFollower;

    public Camera(double totalWidth, double totalHeight)
    {
        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;
        this.npcFollower = new NPCFollower(this);
        this.tileFollower = new TileFollower(this);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public NPCFollower getNpcFollower()
    {
        return npcFollower;
    }

    public void addToPosition(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public double getRelativeWidth() {
        return totalWidth - x;
    }

    public double getTotalWidth()
    {
        return totalWidth;
    }

    public double getTotalHeight()
    {
        return totalHeight;
    }

    public TileFollower getTileFollower()
    {
        return tileFollower;
    }
}
