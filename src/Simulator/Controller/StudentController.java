package Simulator.Controller;

import Data.*;
import Simulator.LocationSystem.AuditoriumBehavior;
import Simulator.LocationSystem.LocationManager;
import Simulator.Maploading.TiledMap;
import Simulator.NPC.NPC;
import Simulator.NPC.NPCManager;
import Simulator.Pathfinding.Pathfinding;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class StudentController
{

    private ArrayList<NPC> npcStudentsOnScreen;
    private ArrayList<NPC> inAula;
    private ArrayList<Lesson> lessonsPassed;
    private TiledMap tiledmap;
    private ArrayList<NPC> skippable;

    public StudentController()
    {
        this.npcStudentsOnScreen = new ArrayList<>();
        this.inAula = new ArrayList<>();
        this.lessonsPassed = new ArrayList<>();
        this.tiledmap = new TiledMap("/TiledMaps/MapFinal.json");
        this.skippable = new ArrayList<>();
    }

    public void update(ArrayList<Lesson> lessons, LocationManager locationManager, NPCManager npcManager, Schedule schedule)
    {

        //de-spawning logic
        ArrayList<Lesson> lessonsAllday = schedule.getLessonArrayList();

        for (NPC npc : npcStudentsOnScreen)
        {

            if (!skippable.contains(npc))
            {
                //checks if there are still lessons
                boolean stillLesson = true;
                for (Lesson lesson : lessonsAllday)
                {
                    for (Group group : lesson.getGroups())
                    {
                        for (Student student : group.getStudents())
                        {
                            if (student.equals(npc.getPerson()))
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
                    }
                }

                //if there are non the de-spawn is initiated
                if (stillLesson)
                {
                    //                    System.out.println(npc.getPerson().getName() + "Is going to be removed");
                    locationManager.scriptedEndLesson(npc);
                    npc.resetDestination();
                    npc.getCurrentPathfinding().setDestination(1425, 725);
                    npc.setCollisionEnabler(false);
                    skippable.add(npc);
                }
            }
        }

        //lesssons change and spawn logic
        for (Lesson lesson : lessons)
        {
            if (!lessonsPassed.contains(lesson))
            {
                // for the students
                ArrayList<Group> groups = lesson.getGroups();
                for (Group group : groups)
                {
                    ArrayList<Student> students = group.getStudents();
                    for (Student student : students)
                    {

                        //if the npc existed lesson change will be iniated
                        if (npcStudentsOnScreen.contains(new NPC(student)))
                        {
                            //                            System.out.println(student.getName() + ": Student word van huidige locatie naar nieuwe les verplaatst");
                            for (NPC npc : npcStudentsOnScreen)
                            {
                                if (npc.getPerson().equals(student))
                                {
                                    for (int i = 0; i < inAula.size(); i++)
                                    {
                                        if (inAula.get(i).getPerson().equals(student))
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

                        //else there will spawn a new npc
                        else
                        {
                            //                            System.out.println(student.getName() + ": de student komt de school binnen en gaat naar zijn les");
                            NPC npc = new NPC(student);
                            Pathfinding pathfinding = new Pathfinding(tiledmap/*GUI.getWalkablemap()*/);
                            npc.setPathfinding(pathfinding);
                            pathfinding.addNpc(npc);
                            npc.setCollisionEnabler(true);
                            npcStudentsOnScreen.add(npc);

                            if (pathfinding.getExactDestination() == null)
                            {
                                pathfinding.setDestination((int) lesson.getClassroom().getEntry().getX(), (int) lesson.getClassroom().getEntry().getY());
                            }
                            npcManager.addNPC(npc);
                        }
                    }
                }
                //add lesson in list of passed ones
                lessonsPassed.add(lesson);
            }
        }

        //used list
        ArrayList<NPC> used = new ArrayList<>();

        //checks if npc is used else it is send to the auditorium
        for (Lesson lesson : lessons)
        {
            for (Group group : lesson.getGroups())
            {
                for (Person person : group.getStudents())
                {
                    for (NPC npc : npcStudentsOnScreen)
                    {
                        if (npc.getPerson().equals(person))
                        {
                            used.add(npc);
                        }
                    }
                }
            }
        }


        //sends to auditorium
        for (NPC npc : npcStudentsOnScreen)
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
                npc.setCollisionEnabler(false);
                npc.getCurrentPathfinding().setDestination((int) AuditoriumBehavior.entry.getX(), (int) AuditoriumBehavior.entry.getY());
                inAula.add(npc);

            }
        }


    }

    public void checkingFunction(LocationManager locationManager)
    {
        //sends to seats
        for (NPC npc : npcStudentsOnScreen)
        {
            locationManager.scriptedStartedStudentLesson(npc);
        }
    }

    public void studentDespawining(NPCManager npcManager)
    {
        for (int i = 0; i < skippable.size(); i++)
        {
            if (skippable.get(i).isAtDestination() && skippable.get(i).getCurrentLocation().distance(new Point2D.Double(1424, 725)) < 10)
            {

                for (int j = 0; j < npcStudentsOnScreen.size(); j++)
                {
                    if (npcStudentsOnScreen.get(j).equals(skippable.get(i)))
                    {
                        npcStudentsOnScreen.remove(j);
                    }
                }
                npcManager.removeNPC(skippable.get(i));

                skippable.remove(i);

            }
        }

    }


    public ArrayList<NPC> getNpcStudentsOnScreen()
    {
        return npcStudentsOnScreen;
    }

    public void sendToExit()
    {
        for (NPC npc : npcStudentsOnScreen)
        {
            npc.resetDestination();
            npc.getCurrentPathfinding().setDestination(1425, 725);
            skippable.add(npc);
        }
    }

    public void setNpcStudentsOnScreen(ArrayList<NPC> npcStudentsOnScreen)
    {
        reset();
        this.npcStudentsOnScreen.addAll(npcStudentsOnScreen);

    }

    private void reset()
    {
        this.npcStudentsOnScreen.clear();
        inAula.clear();
        lessonsPassed.clear();
        skippable.clear();
    }
}
