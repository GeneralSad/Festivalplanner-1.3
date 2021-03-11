package Simulator;

import Data.Person;
import org.jfree.fx.FXGraphics2D;

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

    public void update(double deltaTime, ArrayList<NPC> npcs) {
        this.x += deltaTime * xSpeed;
        this.y += deltaTime * ySpeed;
    }

    public void draw(FXGraphics2D fxGraphics2D) {
        fxGraphics2D.draw(new Rectangle2D.Double(x, y, width, height));
    }
}
