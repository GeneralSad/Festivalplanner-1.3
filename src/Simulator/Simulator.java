package Simulator;

import Data.Group;
import Data.Lesson;
import Data.Schedule;
import Data.Student;
import Simulator.LocationSystem.LocationDatabase;
import Simulator.LocationSystem.LocationManager;
import Simulator.Maploading.Tile;
import Simulator.Maploading.TiledMap;
import Simulator.NPC.NPC;
import Simulator.NPC.NPCManager;
import Simulator.Pathfinding.Pathfinding;
import Simulator.Time.NormalTime;
import Simulator.Time.TimeManager;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

//TODO making time go backward can be done by saving npc data every ~15 min and allowing to go backwards to the saved points

/**
 * Auteurs: Leon
 * <p>
 * Deze klasse is de basis voor de simulator
 */


public class Simulator
{
    private NPCManager npcManager = new NPCManager();
    private TimeManager timeManager;
    private static TiledMap tiledmap = new TiledMap("/TiledMaps/MapFinal.json");
    private int speedfactor = 1;

    private LocationManager locationManager;

    private ArrayList<NPC> npcOnScreen = new ArrayList<>();
    // private ArrayList<Student> studentsOnScreen = new ArrayList<>();
    private ArrayList<Lesson> lessonsPassed = new ArrayList<>();
    private LocationDatabase base = new LocationDatabase();


    public Simulator(Schedule schedule)
    {
        timeManager = new TimeManager(schedule, new NormalTime(LocalTime.of(9, 0, 0)));
        locationManager = new LocationManager();
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
        npcManager.update((deltatime / 1e9) * deltaTimeMultiplier);
        timeManager.update(deltatime);

        if (timeManager.isChanged() || speedfactor < 0)
        {
            ArrayList<Lesson> lessons = timeManager.getCurrentLessons();

            for (Lesson lesson : lessons)
            {
                if (!lessonsPassed.contains(lesson))
                {
                    ArrayList<Group> groups = lesson.getGroups();
                    for (Group group : groups)
                    {
                        ArrayList<Student> students = group.getStudents();
                        for (Student student : students)
                        {
                            if (npcOnScreen.contains(new NPC(student)))
                            {
                                System.out.println(student.getName() + ": Student word van huidige locatie naar nieuwe les verplaatst");
                                for (NPC npc : npcOnScreen)
                                {
                                    if (npc.getPerson().equals(student))
                                    if (npcOnScreen.get(k).getPerson().equals(s))
                                    {
                                        npcOnScreen.get(k).resetDestination();
                                        npcOnScreen.get(k).getCurrentPathfinding().setDestination((int) lesson.getClassroom().getEntry().getX(), (int) lesson.getClassroom().getEntry().getY());
                                    }
                                }
                            }
                            else
                            {
                                System.out.println(student.getName() + ": de student komt de school binnen en gaat naar zijn les");
                                NPC npc = new NPC(student);
                                Pathfinding pathfinding = new Pathfinding(tiledmap/*GUI.getWalkablemap()*/);
                                npc.setPathfinding(pathfinding);
                                pathfinding.addNpc(npc);
                                npcOnScreen.add(npc);

                                if (pathfinding.getExactDestination() == null)
                                {
                                    pathfinding.setDestination((int) lesson.getClassroom().getEntry().getX(), (int) lesson.getClassroom().getEntry().getY());
                                }
                                npcManager.addNPC(npc);
                            }

                        }

                    }


                }

                lessonsPassed.add(lesson);


            }
        }


        //
        //            for (Lesson lesson : lessons)
        //            {
        //                for (Group group : lesson.getGroups())
        //                {
        //                    studentsWithLesson.addAll(group.getStudents());
        //                }
        //            }
        //
        //            for (Student student : studentsWithLesson)
        //            {
        //                if (studentsOnScreen.contains(student))
        //                {
        //                    System.out.println(student.getName() + ": Student word van huidige locatie naar nieuwe les verplaatst");
        //                    for (int i = 0; i < npcOnScreen.size(); i++)
        //                    {
        //                        if (npcOnScreen.get(i).getPerson().equals(student)){
        //                            npcOnScreen.get(i).resetDestination();
        //                            npcOnScreen.get(i).getCurrentPathfinding().setDestination((int)student.getGroup().getClassroom().getEntry().getX(), (int)student.getGroup().getClassroom().getEntry().getY());
        //                        }
        //
        //                    }
        //                }
        //                else
        //                {
        //                    System.out.println(student.getName() + ": de student komt de school binnen en gaat naar zijn les");
        //                    NPC npc = new NPC(student);
        //                    Pathfinding pathfinding = new Pathfinding(tiledmap/*GUI.getWalkablemap()*/);
        //                    npc.setPathfinding(pathfinding);
        //                    pathfinding.addNpc(npc);
        //
        //                    npcOnScreen.add(npc);
        //
        //                    if (pathfinding.getExactDestination() == null)
        //                    {
        //                        pathfinding.setDestination((int) student.getGroup().getClassroom().getEntry().getX(), (int) student.getGroup().getClassroom().getEntry().getY());
        //                    }
        //
        //                    npcManager.addNPC(npc);
        //                    studentsOnScreen.add(student);
        //                }
        //            }


        //            ArrayList<Student> studentsOnScreenPlaceHolder = new ArrayList<>(studentsOnScreen);
        //            for (Student student : studentsOnScreenPlaceHolder)
        //            {
        //                if (!studentsWithLesson.contains(student))
        //                {
        //                    System.out.println(student.getName() + ": wordt verwijderd");
        //                    studentsOnScreen.remove(student);
        //                }
        //            }
        //
        //
        //        }

        //TODO De for loop hieronder is misschien niet goed
        //TODO Check
        for (NPC npc : npcOnScreen)
        {
            locationManager.scriptedStartedLesson(npc, npc.getCurrentPathfinding());
        }
    }

    public void draw(FXGraphics2D fxGraphics2D)
    {
        tiledmap.draw(fxGraphics2D);
        npcManager.draw(fxGraphics2D, true);

        fxGraphics2D.setColor(Color.blue);

        // draw seat numbers
        int number = 0;
        for (Tile tile : getTiledmap().getSeatableLayer().getTilesInLayer())
        {
            number++;
            fxGraphics2D.drawString(("" + number), tile.getX(), tile.getY());
        }

        fxGraphics2D.setColor(Color.BLACK);

        if (true)
        {
            for (NPC npc : npcOnScreen)
            {
                Point2D test = npc.getCurrentPathfinding().getDestinationTile().getMiddlePoint();
                fxGraphics2D.fill(new Rectangle.Double(test.getX() - 5, test.getY() - 5, 10, 10));
            }

            for (int i = 0; i < base.ClassRoomStudentData().size(); i++)
            {
                fxGraphics2D.setColor(Color.red);
                Point2D point2D = base.ClassRoomStudentData().get(i).getEntry();
                fxGraphics2D.fill(new Rectangle2D.Double(point2D.getX()-5, point2D.getY()-5, 10, 10));
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

    private Map<LocalTime, NPCManager> timeNPCManagerMap = new LinkedHashMap<>();

    public void setSpeedfactor(int speedFactor)
    {
        int maxSpeedFactor = 100;
        if (speedFactor > -1 && speedFactor <= maxSpeedFactor)
        {
            this.speedfactor = speedFactor;
        }
        timeManager.setSpeedFactor(this.speedfactor);
    }

    public void loadNPCs()
    {
        Set<LocalTime> localTimes = timeNPCManagerMap.keySet();
        ArrayList<LocalTime> localTimeArrayList = new ArrayList<>(localTimes);
        LocalTime localTime = localTimeArrayList.get(localTimeArrayList.size() - 1);
        NPCManager npcManager = timeNPCManagerMap.get(localTime);
        this.npcManager = npcManager;
        this.timeManager.setTimeType(new NormalTime(localTime));
        npcOnScreen.clear();
        npcOnScreen.addAll(npcManager.getNpcs());
    }


    public void saveNPCs()
    {
        NPCManager newNpcManager = new NPCManager();

        for (NPC npc : this.npcManager.getNpcs())
        {
            newNpcManager.addNPC(npc.clone());
        }

        timeNPCManagerMap.put(timeManager.getTime(), newNpcManager);


    }






}
