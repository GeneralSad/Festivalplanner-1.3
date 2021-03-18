package Simulator;

import Data.Group;
import Data.Lesson;
import Data.Schedule;
import Data.Student;
import Simulator.NPC.NPC;
import Simulator.NPC.NPCManager;
import Simulator.Time.SpeedFactoredTime;
import Simulator.Time.TimeManager;
import org.jfree.fx.FXGraphics2D;

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Auteurs: Leon
 * <p>
 * Deze klasse is de basis voor de simulator
 */

public class Simulator
{
    private NPCManager npcManager = new NPCManager();
    private TimeManager timeManager;


    public Simulator(Schedule schedule)
    {
        timeManager = new TimeManager(schedule, new SpeedFactoredTime(LocalTime.of(9, 0, 0), 100));
    }

    public void update(long deltatime)
    {
        npcManager.update(deltatime / 1000000000.0);
        timeManager.update(deltatime);

        if (timeManager.isChanged())
        {
            ArrayList<Lesson> lessons = timeManager.getCurrentLessons();
            ArrayList<Student> students = new ArrayList<>();

            for (Lesson lesson : lessons)
            {
                for (Group group : lesson.getGroups())
                {
                    students.addAll(group.getStudents());
                }
            }

            for (Student student : students)
            {
                NPC npc = new NPC(student);
                npc.goToDestinationXY(0, 1000);


                npcManager.addNPC(npc);

            }

        }

    }


    public void draw(FXGraphics2D fxGraphics2D)
    {
        npcManager.draw(fxGraphics2D);
    }
}
