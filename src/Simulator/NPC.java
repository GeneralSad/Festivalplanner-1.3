package Simulator;

import Data.Person;
import org.dyn4j.geometry.Vector2;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class NPC
{
    private Person person;
    private double x;
    private double y;
    private int xSpeed;
    private int ySpeed;
    private int width;
    private int height;
    // rotation from 0 to 360 degrees
    private int rotation;

    public NPC(Person person, int x, int y, int xSpeed, int ySpeed, int width, int height, int rotation)
    {
        this.person = person;
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public void update(double deltaTime, ArrayList<NPC> npcs)
    {
        this.x += deltaTime * xSpeed;
        this.y += deltaTime * ySpeed;

        for (NPC npc : npcs)
        {
            if (npc != this)
            {
                // Find the four edges of the current npc square hitbox
                Point2D.Double point1 = new Point2D.Double(this.x, this.y);
                Point2D.Double point2 = new Point2D.Double(this.x + this.width, this.y);
                Point2D.Double point3 = new Point2D.Double(this.x, this.y + this.height);
                Point2D.Double point4 = new Point2D.Double(this.x + this.width, this.y + this.height);

                // find the other npc hitbox
                Rectangle2D rectangle2D = new Rectangle2D.Double(npc.x, npc.y, npc.width, npc.height);

                // if any of the edges is contained within the other npcs hitbox then a collision happened
                // so reverse the movement
                if (rectangle2D.contains(point1) || rectangle2D.contains(point2) || rectangle2D.contains(point3) || rectangle2D.contains(point4)) {
                    this.x -= deltaTime * xSpeed;
                    this.y -= deltaTime * ySpeed;
                }
            }
        }
    }

    private void collisionCheck(int x, int y, int otherX, int otherY) {

    }

    public void draw(FXGraphics2D fxGraphics2D)
    {
        fxGraphics2D.draw(new Rectangle2D.Double(x, y, width, height));
    }
}
