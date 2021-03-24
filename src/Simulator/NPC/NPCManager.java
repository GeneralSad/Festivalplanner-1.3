package Simulator.NPC;

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

    public void addNPC(Person person, double x, double y, int width, int height, int rotation, int speed, int rotationSpeed, String image)
    {
        // only add an npc if the person doesn't already exist
        if (getNPCFromPerson(person) == null)
        {
            this.npcs.add(new NPC(person, x, y, width, height, rotation, speed, rotationSpeed, image));
        }
    }

    public void addNPC(Person person, double x, double y, int width, int height, String image)
    {
        // only add an npc if the person doesn't already exist
        if (getNPCFromPerson(person) == null)
        {
            this.npcs.add(new NPC(person, x, y, width, height, image));
        }
    }

    public void addNPC(NPC npc)
    {
        if (!this.npcs.contains(npc) && getNPCFromPerson(npc.getPerson()) == null) {
            this.npcs.add(npc);
        }
    }

    public void removeNPC(NPC npc)
    {
        this.npcs.remove(npc);
    }

    public void removeNPC(Person person)
    {
        NPC npc = getNPCFromPerson(person);
        if (npc != null)
        {
            this.npcs.remove(npc);
        }
    }

    public void setDirectTargetLocation(Point2D point2D){
        for (NPC npc: npcs)
        {
            npc.goToDestination((int)point2D.getX(), (int)point2D.getY());
        }
    }

    public NPC getNPC(int index)
    {
        try
        {
            return this.npcs.get(index);
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    public NPC getNPC(Person person)
    {
        return getNPCFromPerson(person);
    }

    /**
     * Look for an NPC based on the Person object it contains
     * Used by the getNPC and removeNPC methods
     * @param person
     * @return
     */
    private NPC getNPCFromPerson(Person person) {
        for (NPC npc : npcs) {
            if (npc.getPerson() == person) {
                return npc;
            }
        }
        return null;
    }

    public void update(double deltaTime)
    {
        for (NPC npc : this.npcs)
        {
            npc.update(deltaTime, this.npcs);
        }
    }

    public void draw(FXGraphics2D fxGraphics2D, boolean debug) {
        for (NPC npc : this.npcs) {
            npc.draw(fxGraphics2D, debug);
        }
    }
}
