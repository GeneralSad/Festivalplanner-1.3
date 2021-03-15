package Simulator;

import Data.Person;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NPCManager
{
    private ArrayList<NPC> npcs;

    public NPCManager()
    {
        this.npcs = new ArrayList<>();
    }

    public void addNPC(Person person, int x, int y, int xSpeed, int ySpeed, int width, int height, int rotation) {
        this.npcs.add(new NPC(person, x, y, xSpeed, ySpeed, width, height, rotation));
    }

    public void addNPC(NPC npc) {
        this.npcs.add(npc);
    }

    public void removeNPC(NPC npc) {
        this.npcs.remove(npc);
    }

    public void removeNPC(Person person) {
        NPC npc = getNPCFromPerson(person);
        if (npc != null) {
            this.npcs.remove(npc);
        }
    }

    public void setLocation(Point2D point2D){
        for (NPC npc: npcs)
        {
            npc.goToDestination((int)point2D.getX(), (int)point2D.getY());
        }
    }

    public NPC getNPC(int index) {
        try
        {
            return this.npcs.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public NPC getNPC(Person person) {
        return getNPCFromPerson(person);
    }

    private NPC getNPCFromPerson(Person person) {
        for (NPC npc : npcs) {
            if (npc.getPerson() == person) {
                return npc;
            }
        }
        return null;
    }

    public void update(double deltaTime) {
        for (NPC npc : this.npcs) {
            npc.update(deltaTime, this.npcs);
        }
    }

    public void draw(FXGraphics2D fxGraphics2D) {
        for (NPC npc : this.npcs) {
            npc.draw(fxGraphics2D);
        }
    }
}
