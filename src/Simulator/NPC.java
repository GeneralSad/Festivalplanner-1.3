package Simulator;

import Data.Person;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class NPC
{
    private Person person;
    private double x;
    private double y;
    private double xSpeed;
    private double ySpeed;
    private int width;
    private int height;
    private Point2D destination;
    // rotation from 0 to 360 degrees
    private int rotation;

    public NPC(Person person, double x, double y, double xSpeed, double ySpeed, int width, int height, int rotation)
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
        // Position update
        this.x += deltaTime * xSpeed;
        this.y += deltaTime * ySpeed;

        // NPC collision

        // Find the four edges of the current npc square hitbox
        Point2D.Double point1 = new Point2D.Double(this.x, this.y);
        Point2D.Double point2 = new Point2D.Double(this.x + this.width, this.y);
        Point2D.Double point3 = new Point2D.Double(this.x, this.y + this.height);
        Point2D.Double point4 = new Point2D.Double(this.x + this.width, this.y + this.height);

        for (NPC npc : npcs)
        {
            if (npc != this)
            {
                // find the other npc hitbox
                Rectangle2D npcHitBox = new Rectangle2D.Double(npc.x, npc.y, npc.width, npc.height);

                // if any of the edges is contained within the other npcs hitbox then a collision happened
                // so reverse the movement
                if (npcHitBox.contains(point1) || npcHitBox.contains(point2) || npcHitBox.contains(point3) || npcHitBox.contains(point4))
                {
                    this.x -= deltaTime * xSpeed;
                    this.y -= deltaTime * ySpeed;
                }
            }
        }

        // Destination check
        if (destination != null)
        {
            Rectangle2D currentHitBox = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
            if (currentHitBox.contains(destination))
            {
                this.xSpeed = 0;
                this.ySpeed = 0;
                System.out.println("Reached destination");
            }
        }
    }

    public void goToDestination(int x, int y)
    {
        destination = new Point2D.Double(x, y);
        // calculate the total distance from a point and set the x and y speed to be a part of 10 based on how much of the total distance is on the x or y
        double totalDistance = Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
        this.xSpeed = ((x - this.x) / totalDistance * 10);
        this.ySpeed = ((y - this.y) / totalDistance * 10);
        System.out.println("Going to destination " + destination);
    }

    public void draw(FXGraphics2D fxGraphics2D)
    {
        fxGraphics2D.draw(new Rectangle2D.Double(x, y, width, height));
    }

    public Person getPerson()
    {
        return person;
    }
}
