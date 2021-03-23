package Simulator;

import Data.Group;
import Data.Lesson;
import Data.Schedule;
import Data.Student;
import GUI.GUI;
import Simulator.LocationSystem.LocationDatabase;
import Simulator.LocationSystem.LocationManager;
import Simulator.NPC.NPC;
import Simulator.NPC.NPCManager;
import Simulator.Pathfinding.Pathfinding;
import Simulator.Time.NormalTime;
import Simulator.Time.SpeedFactoredTime;
import Simulator.Time.TimeManager;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

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
    private int speedfactor = 4;
    private ArrayList<Student> studentsOnScreen = new ArrayList<>();
    private LocationManager locationManager;



    public Simulator(Schedule schedule)
    {
        timeManager = new TimeManager(schedule, new SpeedFactoredTime(LocalTime.of(9,0,0), speedfactor));
        this.schedule = schedule;


    }

    public void update(long deltatime)
    {
        npcManager.update((deltatime / 1000000000.0)*speedfactor);
        timeManager.update(deltatime);

        ArrayList<NPC> npcOnScreen = new ArrayList<>();

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
                    for (int i = 0; i < 3; i++)
                    {
                        System.out.println(student.getName() + ": de student komt de school binnen en gaat naar zijn les");


                        NPC npc = new NPC(student);
                        npcOnScreen.add(npc);
                        Pathfinding pathfinding = new Pathfinding(GUI.getTiledmap());
                        npc.setPathfinding(pathfinding);
                        pathfinding.addNpc(npc);




                        if (locationManager == null)
                        {
                            locationManager = new LocationManager();

                        }else if (pathfinding.getDestinationTile() == null){

                            pathfinding.setDestination((int) student.getGroup().getClassroom().getEntry().getX(), (int) student.getGroup().getClassroom().getEntry().getY());

                        }

                        npcManager.addNPC(npc);
                        studentsOnScreen.add(student);
                        }
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

        for (NPC npc : npcOnScreen)
        {
             locationManager.scriptedLesson(npc, npc.getCurrentPathfinding());

        }

    }


    public void draw(FXGraphics2D fxGraphics2D)
    {
        npcManager.draw(fxGraphics2D, false);

        fxGraphics2D.setColor(Color.blue);

        int i = 0;
        for (Map.Entry<Point2D, Double> entry: GUI.getTiledmap().getAllSitableTiles().entrySet())
        {
            i++;
            fxGraphics2D.drawString((""+i),(int)entry.getKey().getX(), (int)entry.getKey().getY());
        }
    }
}
