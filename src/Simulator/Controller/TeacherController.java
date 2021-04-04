package Simulator.Controller;

import Data.Lesson;
import Data.Schedule;
import Simulator.LocationSystem.AuditoriumBehavior;
import Simulator.LocationSystem.LocationManager;
import Simulator.Maploading.TiledMap;
import Simulator.NPC.NPC;
import Simulator.NPC.NPCManager;
import Simulator.Pathfinding.Pathfinding;
import Simulator.Simulator;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TeacherController
{


    private ArrayList<NPC> npcTeacherOnScreen;
    private ArrayList<NPC> inAula;
    private ArrayList<Lesson> lessonsPassed;
    private TiledMap tiledmap;
    private ArrayList<NPC> skippable;

    public TeacherController()
    {
        this.npcTeacherOnScreen = new ArrayList<>();
        this.inAula = new ArrayList<>();
        this.lessonsPassed = new ArrayList<>();
        this.tiledmap = Simulator.getTiledmap();
        this.skippable = new ArrayList<>();
    }


    public void update(ArrayList<Lesson> lessons, LocationManager locationManager, NPCManager npcManager, Schedule schedule)
    {

        ArrayList<Lesson> lessonsAllday = schedule.getLessonArrayList();

        for (NPC npc : npcTeacherOnScreen)
        {
            if (!skippable.contains(npc))
            {
                boolean stillLesson = true;
                for (Lesson lesson : lessonsAllday)
                {
                    if (lesson.getTeacher().equals(npc.getPerson()))
                    {
                        if (lessonsPassed.contains(lesson) && !lessons.contains(lesson))
                        {

                        }
                        else
                        {
                            stillLesson = false;
                        }
                    }
                }

                if (stillLesson)
                {
                    locationManager.scriptedEndLesson(npc);
                    npc.resetDestination();
                    npc.getCurrentPathfinding().setDestination(1425, 725);
                    skippable.add(npc);
                }
            }
        }


        for (Lesson lesson : lessons)
        {
            if (!lessonsPassed.contains(lesson))
            {
                if (npcTeacherOnScreen.contains(new NPC(lesson.getTeacher())))
                {
                    //                    System.out.println(lesson.getTeacher().getName() + ": Teacher word van huidige locatie naar nieuwe les verplaatst");
                    for (NPC npc : npcTeacherOnScreen)
                    {
                        if (npc.getPerson().equals(lesson.getTeacher()))
                        {
                            for (int i = 0; i < inAula.size(); i++)
                            {
                                if (lesson.getTeacher().equals(inAula.get(i).getPerson()))
                                {
                                    inAula.remove(i);
                                }
                            }
                            locationManager.scriptedEndLesson(npc);
                            npc.resetDestination();
                            npc.getCurrentPathfinding().setDestination((int) lesson.getClassroom().getEntry().getX(), (int) lesson.getClassroom().getEntry().getY());
                        }
                    }
                }
                else
                {
                    //                    System.out.println(lesson.getTeacher().getName() + ": de Teacher komt de school binnen en gaat naar zijn les");
                    NPC npc = new NPC(lesson.getTeacher());
                    npc.setCollisionEnabler(true);
                    Pathfinding pathfinding = new Pathfinding(tiledmap);
                    npc.setPathfinding(pathfinding);
                    pathfinding.addNpc(npc);
                    npcTeacherOnScreen.add(npc);
                    pathfinding.setDestination((int) lesson.getClassroom().getEntry().getX(), (int) lesson.getClassroom().getEntry().getY());

                    npcManager.addNPC(npc);
                }
                lessonsPassed.add(lesson);
            }
        }


        //used list
        ArrayList<NPC> used = new ArrayList<>();

        //checks if npc is used else it is send to the auditorium
        for (Lesson lesson : lessons)
        {
            for (NPC npc : npcTeacherOnScreen)
            {
                if (npc.getPerson().equals(lesson.getTeacher()))
                {
                    used.add(npc);
                }
            }
        }


        //sends to auditorium
        for (NPC npc : npcTeacherOnScreen)
        {
            boolean onscreen = true;
            for (NPC npc1 : used)
            {
                if (npc1.equals(npc))
                {
                    onscreen = false;
                }
            }
            for (NPC npc1 : inAula)
            {
                if (npc1.equals(npc))
                {
                    onscreen = false;
                }
            }
            for (NPC npc1 : skippable)
            {
                if (npc1.equals(npc))
                {
                    onscreen = false;
                }
            }

            if (onscreen)
            {
                locationManager.scriptedEndLesson(npc);
                npc.resetDestination();
                npc.getCurrentPathfinding().setDestination((int) AuditoriumBehavior.entry.getX(), (int) AuditoriumBehavior.entry.getY());
                inAula.add(npc);
            }
        }


    }

    public void checkingFunction(LocationManager locationManager)
    {
        //sends to seats
        for (NPC npc : npcTeacherOnScreen)
        {
            locationManager.scriptedStartedTeacherLesson(npc);
        }
    }

    public void teacherDespawining(NPCManager npcManager)
    {
        for (int i = 0; i < skippable.size(); i++)
        {
            if (skippable.get(i).isAtDestination() && skippable.get(i).getCurrentLocation().distance(new Point2D.Double(1424, 725)) < 10)
            {

                for (int j = 0; j < npcTeacherOnScreen.size(); j++)
                {
                    if (npcTeacherOnScreen.get(j).equals(skippable.get(i)))
                    {
                        npcTeacherOnScreen.remove(j);
                    }
                }
                npcManager.removeNPC(skippable.get(i));

                skippable.remove(i);

            }
        }
    }

    public ArrayList<NPC> getNpcTeacherOnScreen()
    {
        return npcTeacherOnScreen;
    }


    public void sendToExit()
    {
        for (NPC npc : npcTeacherOnScreen)
        {
            npc.resetDestination();
            npc.getCurrentPathfinding().setDestination(1425, 725);
            skippable.add(npc);
        }
    }


    public void setNpcTeacherOnScreen(ArrayList<NPC> npcTeacherOnScreen)
    {
        reset();
        this.npcTeacherOnScreen.addAll(npcTeacherOnScreen);
    }

    private void reset()
    {
        npcTeacherOnScreen.clear();
        inAula.clear();
        lessonsPassed.clear();
        skippable.clear();
    }
}
