package Simulator;

import Data.Person;
import org.jfree.fx.FXGraphics2D;

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

    public void removeNPC(NPC npc) {
        this.npcs.remove(npc);
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
