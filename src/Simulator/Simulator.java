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
    private Schedule schedule;
    private int speedfactor = 1000;
    private ArrayList<Student> studentsOnScreen = new ArrayList<>();

    public Simulator(Schedule schedule)
    {
        timeManager = new TimeManager(schedule, new SpeedFactoredTime(LocalTime.of(9, 0, 0), speedfactor));
        this.schedule = schedule;
    }

    public void update(long deltatime)
    {
        npcManager.update(deltatime / 1000000000.0);
        timeManager.update(deltatime);

        if (timeManager.isChanged())
        {
            System.out.println(timeManager.getTime());
            ArrayList<Lesson> lessons = timeManager.getCurrentLessons();
            ArrayList<Student> studentsWithLesson = new ArrayList<>();


            for (Lesson lesson : lessons)
            {
                for (Group group : lesson.getGroups())
                {
                    studentsWithLesson.addAll(group.getStudents());
                }
            }

            for (Student student : studentsWithLesson)
            {
                if (studentsOnScreen.contains(student))
                {
                    System.out.println(student.getName() + ": Student word van huidige locatie naar nieuwe les verplaatst");
                }
                else
                {
                    System.out.println(student.getName() + ": de student komt de school binnen en gaat naar zijn les");
                    NPC npc = new NPC(student);
                    npc.goToDestination(100, 100);

                    npcManager.addNPC(npc);
                    studentsOnScreen.add(student);
                }
            }

            ArrayList<Student> studentsOnScreenPlaceHolder = new ArrayList<>(studentsOnScreen);
            for (Student student : studentsOnScreenPlaceHolder)
            {
                if (!studentsWithLesson.contains(student))
                {
                    if (!schedule.hasFutureLesson(student, timeManager.getTime()))
                    {
                        System.out.println(student.getName() + ": Student heeft geen les meer");
                        studentsOnScreen.remove(student);
                    }
                    else
                    {
                        System.out.println(student.getName() + ": Student heeft nog een les maar nu niet");
                    }
                }
            }


        }

    }


    public void draw(FXGraphics2D fxGraphics2D)
    {
        npcManager.draw(fxGraphics2D, false);
    }
}
