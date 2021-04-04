package Simulator.Pathfinding;

import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class PathfindingTile
{
    private int row;
    private int column;
    private int width;
    private int height;
    private boolean walkable;
    private Direction direction;

    public PathfindingTile(int row, int column, int width, int height, boolean walkable)
    {
        this.row = row;
        this.column = column;
        this.width = width;
        this.height = height;
        this.walkable = walkable;
        this.direction = Direction.NONE;
    }

    /**
     * Return a vector which is at the middle of the tile
     *
     * @return
     */
    public Point2D getMiddlePoint()
    {
        return new Point2D.Double(column * width + width / 2.0, row * height + height / 2.0);
    }

    public boolean isWalkable()
    {
        return walkable;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    public void draw(FXGraphics2D fxGraphics2D, boolean debug)
    {
        fxGraphics2D.draw(new Rectangle2D.Double(column * width, row * height, width, height));
        if (debug)
        {
            int x = column * width + width / 2;
            int y = row * height + height / 2;
            if (direction == Direction.UP)
            {
                fxGraphics2D.drawLine(x, y, x, y - height / 2);
            }
            else if (direction == Direction.LEFT)
            {
                fxGraphics2D.drawLine(x, y, x - width / 2, y);
            }
            else if (direction == Direction.RIGHT)
            {
                fxGraphics2D.drawLine(x, y, x + width / 2, y);
            }
            else if (direction == Direction.DOWN)
            {
                fxGraphics2D.drawLine(x, y, x, y + height / 2);
            }
        }
    }

    public void setWalkable(boolean walkable)
    {
        this.walkable = walkable;
    }
}
