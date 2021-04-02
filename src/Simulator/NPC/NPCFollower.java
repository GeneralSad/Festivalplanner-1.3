package Simulator.NPC;

import Simulator.Camera;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
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

    public void update() {
        camera.setX(-npc.getCurrentLocation().getX() + camera.getTotalWidth() / 2);
        camera.setY(-npc.getCurrentLocation().getY() + camera.getTotalHeight() / 2);
    }

    public void draw(FXGraphics2D fxGraphics2D) {
        fxGraphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

        int x = (int)camera.getRelativeWidth() - 500;
        int y = (int)-camera.getY();

        fxGraphics2D.setColor(Color.CYAN);
        fxGraphics2D.fill(new Rectangle2D.Double(x, y, 500, 200));
        fxGraphics2D.setColor(Color.BLACK);

        // Draw npc information
        if (npc != null) {
            fxGraphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
            int yOffset = 50;
            int yJump = 15;
            DecimalFormat decimalFormat = new DecimalFormat("#.000");
            fxGraphics2D.drawString("Selected npc information:", x, y + yOffset);
            yOffset += yJump;
            fxGraphics2D.drawString("Person: " + npc.getPerson(), x, y + yOffset);
            yOffset += yJump;
            fxGraphics2D.drawString("Width and Height: " + npc.getWidth() + " " + npc.getHeight(), x, y + yOffset);
            yOffset += yJump;
            fxGraphics2D.drawString("Position: " + decimalFormat.format(npc.getCurrentLocation().getX()) + " " + decimalFormat.format(npc.getCurrentLocation().getY()), x, y + yOffset);
            yOffset += yJump;
            if (npc.getDestination() != null)
            {
                fxGraphics2D.drawString("Destination: " + decimalFormat.format(npc.getDestination().getX()) + " " + decimalFormat.format(npc.getDestination().getY()), x, y + yOffset);
            } else {
                fxGraphics2D.drawString("Destination: none", x, y + yOffset);
            }
            yOffset += yJump;
            fxGraphics2D.drawString("At destination?: " + npc.isAtDestination(), x, y + yOffset);
            yOffset += yJump;
            fxGraphics2D.drawString("Rotation: " + decimalFormat.format(npc.getRotation()), x, y + yOffset);
            yOffset += yJump;
            fxGraphics2D.drawString("Target rotation: " + decimalFormat.format(npc.getTargetRotation()), x, y + yOffset);
        }

        fxGraphics2D.setComposite(AlphaComposite.Clear);
    }
}
