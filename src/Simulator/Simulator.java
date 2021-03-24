package Simulator;

import Data.Group;
import Data.Lesson;
import Data.Schedule;
import Data.Student;
import Simulator.LocationSystem.LocationManager;
import Simulator.Maploading.TiledMap;
import Simulator.NPC.NPC;
import Simulator.NPC.NPCManager;
import Simulator.Pathfinding.Pathfinding;
import Simulator.Time.NormalTime;
import Simulator.Time.TimeManager;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private static TiledMap tiledmap = new TiledMap("/TiledMaps/MapFinal.json");
    private int speedfactor = 100;
    private ArrayList<Student> studentsOnScreen = new ArrayList<>();
    private LocationManager locationManager;
    private      ArrayList<NPC> npcOnScreen = new ArrayList<>();
    private int maxSpeedFactor = 100;


    public Simulator(Schedule schedule)
    {
        timeManager = new TimeManager(schedule, new NormalTime(LocalTime.of(9, 0, 0)));
        this.schedule = schedule;


    }

    public static TiledMap getTiledmap()
    {
        return tiledmap;
    }

    public void update(long deltatime)
    {


        double deltaTimeMultiplier = 1;
        if (speedfactor > 0)
        {
            deltaTimeMultiplier = 1 + this.speedfactor / 10.0;
        }
        npcManager.update((deltatime / 1000000000.0) * deltaTimeMultiplier);


        timeManager.update(deltatime);

        if (timeManager.isChanged() || speedfactor < 0)
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
                    //System.out.println(student.getName() + ": Student word van huidige locatie naar nieuwe les verplaatst");

                }
                else
                {
                    for (int i = 0; i < 3; i++)
                    {
                        //System.out.println(student.getName() + ": de student komt de school binnen en gaat naar zijn les");
                        NPC npc = new NPC(student);
                        Pathfinding pathfinding = new Pathfinding(GUI.getWalkablemap());
                        npc.setPathfinding(pathfinding);
                        pathfinding.addNpc(npc);

                        npcOnScreen.add(npc);

                        if (locationManager == null)
                        {
                            locationManager = new LocationManager();

                        }
                        if (pathfinding.getExactDestination() == null)
                        {
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
                    if (!studentsWithLesson.contains(student))
                    {
                        //System.out.println(student.getName() + ": Student heeft geen les meer");
                        studentsOnScreen.remove(student);
                    }
                    else
                    {
                        //System.out.println(student.getName() + ": Student heeft nog een les maar nu niet");
                    }
                }


            }

            //TODO De for loop hieronder is misschien niet goed
        for (NPC npc : npcOnScreen)
        {
             locationManager.scriptedStartedLesson(npc, npc.getCurrentPathfinding());
        }

    }

    public void draw(FXGraphics2D fxGraphics2D)
    {
        tiledmap.draw(fxGraphics2D);
        npcManager.draw(fxGraphics2D, false);

        fxGraphics2D.setColor(Color.blue);

        int i = 0;
        for (Map.Entry<Point2D, Double> entry : getTiledmap().getAllSitableTiles().entrySet())
        {
            i++;
            fxGraphics2D.drawString(("" + i), (int) entry.getKey().getX(), (int) entry.getKey().getY());
        }


        if (false)
        {
            for (int j = 0; j < npcOnScreen.size(); j++)
            {
                Point2D test = npcOnScreen.get(j).getCurrentPathfinding().getDestinationTile().getMiddlePoint();
                fxGraphics2D.fill(new Rectangle.Double(test.getX() - 5, test.getY() - 5, 10, 10));
            }
        }
    }

    public String getFormattedTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        return timeManager.getTime().format(dtf);
    }

    public int getSpeedfactor()
    {
        return timeManager.getSpeedFactor();
    }

    public void setSpeedfactor(int speedFactor)
    {
        if (speedFactor >= -maxSpeedFactor && speedFactor <= maxSpeedFactor)
        {
            this.speedfactor = speedFactor;
        }
        timeManager.setSpeedFactor(this.speedfactor);
    }





}
