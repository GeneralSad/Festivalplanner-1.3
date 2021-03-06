package Simulator.NPC;


import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class NPCSprites
{
    //sprites loading
    private ArrayList<BufferedImage[]> movements;

    //cache
    private double previousX;
    private double previousY;
    private double previousROT;

    //refreshable
    private int dir = 0;
    private double refresh = 2;

    //state
    private int frame = (int) (Math.random() * 5);
    private boolean onPhone = false;
    private boolean onName = true;
    private boolean sitting = false;
    private int sittingRotation = 0;
    private int ofset = 0;


    public NPCSprites()
    {
        this.movements = NPCManager.subImage.randomSprite();
    }

    /**
     * responsible for het framesdata of the image.
     */

    public BufferedImage[] getStanding()
    {
        return movements.get(1);
    }

    public BufferedImage[] getRunning()
    {
        return movements.get(2);
    }

    public BufferedImage[] getSitting()
    {
        return movements.get(5);
    }

    public BufferedImage[] getPhonening()
    {
        return movements.get(6);
    }

    /**
     * draw methods
     */

    /**
     * Draw method that draws the npc
     *
     * @param graphics2D    for the drawing
     * @param atDestination if at location than the behavior will change
     * @param x             location where to draw
     * @param y             location where to draw
     */

    public void draw(FXGraphics2D graphics2D, boolean atDestination, double x, double y, String name)
    {
        //draws the standing still behavior this is not updated.
        if (atDestination)
        {

            if (sitting)
            {
                if (sittingRotation == 0 || sittingRotation == 12)
                {
                    if (sittingRotation == 0)
                    {
                        graphics2D.drawImage(getSitting()[frame], (int) x - 4, (int) y - 14, null);
                    }
                    else
                    {
                        graphics2D.drawImage(getSitting()[frame + 6], (int) x - 4, (int) y - 14, null);
                    }

                }
                else
                {
                    graphics2D.drawImage(getStanding()[sittingRotation], (int) x - 8 + ofset, (int) y - 12, null);
                }

            }
            else if (onPhone)
            {
                if (dir == 18)
                {
                    graphics2D.drawImage(getPhonening()[frame], (int) x - 4, (int) y - 12, null);
                }
            }
            else
            {
                graphics2D.drawImage(getStanding()[frame + dir], (int) x - 4, (int) y - 12, null);
            }

        }
        else
        {
            graphics2D.drawImage(getRunning()[frame + dir], (int) x - 4, (int) y - 12, null);

            if (dir == 18 && frame == 4)
            {
                onPhone = !onPhone;
            }
        }

        if (onName)
        {
            float alpha = 0.75f;
            Color colorField = new Color(0, 0, 0, alpha);
            graphics2D.setColor(colorField);

            RoundRectangle2D nameSign = new RoundRectangle2D.Double(x - 20, y - 15, 50, 10, 5, 5);
            graphics2D.fill(nameSign);

            Font font = new Font(Font.MONOSPACED, Font.PLAIN, 10);
            graphics2D.setFont(font);
            graphics2D.setColor(Color.white);
            graphics2D.drawString(name, (int) x - 18, (int) y - 7);
            graphics2D.setColor(Color.BLACK);
        }
    }


    /**
     * update methods
     */

    /**
     * this function is responsible for updating the frame of the npc while walking
     *
     * @param x location x of the npc
     * @param y location y of the npc
     */
    public void locationUpdater(double x, double y)
    {
        //updates while walking
        double xDif = x - this.previousX;
        double yDif = y - this.previousY;

        double distance = Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));


        if (distance >= refresh)
        {

            if (frame < 5)
            {
                frame++;
            }
            else
            {
                frame = 0;
            }
            previousX = x;
            previousY = y;
        }
    }

    /**
     * this function is responisble for updating the frame while rotating
     *
     * @param rotation rotation of the npc
     */
    public void directionUpdater(double rotation)
    {
        //updates while rotating on its place
        if (Math.abs(Math.toDegrees(previousROT - rotation)) >= 45)
        {
            if (frame < 5)
            {
                frame++;
            }
            else
            {
                frame = 0;
            }
            previousROT = rotation;
        }
    }

    /**
     * this function calculates the rotation of the sprite so the correct front is facing forward with the rotation
     *
     * @param rotation rotation of the npc
     */
    public void calculateUpdater(double rotation)
    {
        //loads the correct orientation

        double angleDegrees = Math.toDegrees(rotation);
        if ((angleDegrees <= 45 && angleDegrees >= 0) || (angleDegrees >= 315 && angleDegrees <= 180))
        {
            dir = 0;
        }
        else if (angleDegrees > 45 && angleDegrees < 135)
        {
            dir = 6;
        }
        else if (angleDegrees >= 135 && angleDegrees <= 225)
        {
            dir = 12;
        }
        else if (angleDegrees > 225 && angleDegrees < 315)
        {
            dir = 18;
        }
    }

    public void setSitting(boolean sitting, double rotation)
    {
        this.sitting = sitting;
        double angleDegrees = rotation;
        if ((angleDegrees <= 45 && angleDegrees >= 0) || (angleDegrees >= 315 && angleDegrees <= 180))
        {
            sittingRotation = 0;
        }
        else if (angleDegrees > 45 && angleDegrees < 135)
        {
            sittingRotation = 6;
        }
        else if (angleDegrees >= 135 && angleDegrees <= 225)
        {
            sittingRotation = 12;
        }
        else if (angleDegrees > 225 && angleDegrees < 315)
        {
            sittingRotation = 18;
        }
    }

    public void setOfset(int ofset)
    {
        this.ofset = ofset;
    }
}
