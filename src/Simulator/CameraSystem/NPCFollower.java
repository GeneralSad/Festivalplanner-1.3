package Simulator.CameraSystem;

import Simulator.NPC.NPC;
import org.jfree.fx.FXGraphics2D;
import org.testfx.api.FxAssert;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;

public class NPCFollower
{
    private NPC npc;
    private boolean isFollowing;
    private Camera camera;

    public NPCFollower(Camera camera)
    {
        this.npc = null;
        this.isFollowing = false;
        this.camera = camera;
    }

    public NPC getNpc()
    {
        return npc;
    }

    public void setNpc(NPC npc)
    {
        this.npc = npc;
    }

    public boolean isFollowing()
    {
        return isFollowing;
    }

    public void setFollowing(boolean following)
    {
        isFollowing = following;
    }

    public void update()
    {
        camera.setX(-npc.getCurrentLocation().getX() + camera.getTotalWidth() / 2);
        camera.setY(-npc.getCurrentLocation().getY() + camera.getTotalHeight() / 2);
    }

    public void draw(FXGraphics2D fxGraphics2D)
    {

        int x = (int) camera.getRelativeWidth() - 500;
        int y = (int) -camera.getY();

        float alpha = 0.75f;
        Color colorField = new Color(0, 0, 0, alpha);
        fxGraphics2D.setColor(colorField);

        fxGraphics2D.fill(new RoundRectangle2D.Double(x, y, 500, 180, 20 ,20));
        fxGraphics2D.setColor(Color.white);

        // teken NPC informatie
        if (npc != null)
        {
             y = y -25;
            fxGraphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
            int yOffset = 50;
            int yJump = 15;
            DecimalFormat decimalFormat = new DecimalFormat("#.000");
            fxGraphics2D.drawString("Selected npc information:", x + 10, y + yOffset);

            fxGraphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
            yOffset += yJump;
            fxGraphics2D.drawString("Person name: " + npc.getPerson() + " Age: " + npc.getPerson().getAge(), x + 10, y + yOffset);
            yOffset += yJump;
            fxGraphics2D.drawString("Width and Height: " + npc.getWidth() + " " + npc.getHeight(), x + 10, y + yOffset);
            yOffset += yJump;
            fxGraphics2D.drawString("Position: " + decimalFormat.format(npc.getCurrentLocation().getX()) + " " + decimalFormat.format(npc.getCurrentLocation().getY()), x + 10, y + yOffset);
            yOffset += yJump;
            if (npc.getDestination() != null)
            {
                fxGraphics2D.drawString("Destination: " + decimalFormat.format(npc.getDestination().getX()) + " " + decimalFormat.format(npc.getDestination().getY()), x + 10, y + yOffset);
            }
            else
            {
                fxGraphics2D.drawString("Destination: none", x + 10, y + yOffset);
            }
            yOffset += yJump;
            fxGraphics2D.drawString("At destination?: " + npc.isAtDestination(), x + 10, y + yOffset);
            yOffset += yJump;
            if (npc.getCurrentPathfinding() != null && npc.getCurrentPathfinding().getDestinationTile() != null)
            {
                fxGraphics2D.drawString("Pathfinding destination tile column and row: " + npc.getCurrentPathfinding().getDestinationTile().getColumn() + " " + npc.getCurrentPathfinding().getDestinationTile().getRow(), x + 10, y + yOffset);
            }
            else
            {
                fxGraphics2D.drawString("Pathfinding has no destination tile", x + 10, y + yOffset);
            }
            yOffset += yJump;
            fxGraphics2D.drawString("Rotation: " + decimalFormat.format(npc.getRotation()), x + 10, y + yOffset);
            yOffset += yJump;
            fxGraphics2D.drawString("Target rotation: " + decimalFormat.format(npc.getTargetRotation()), x + 10, y + yOffset);
            yOffset += yJump;
            fxGraphics2D.drawString("Collided recently: " + npc.isCollidedRecently(), x + 10, y + yOffset);
        }

    }
}
