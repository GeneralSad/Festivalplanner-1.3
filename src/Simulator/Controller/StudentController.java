package Simulator.Controller;

import Data.*;
import Simulator.LocationSystem.AuditoriumBehavior;
import Simulator.LocationSystem.LocationManager;
import Simulator.Maploading.TiledMap;
import Simulator.NPC.NPC;
import Simulator.NPC.NPCManager;
import Simulator.Pathfinding.Pathfinding;

import java.util.ArrayList;

public class StudentController {

    private ArrayList<NPC> npcStudentsOnScreen = new ArrayList<>();
    private ArrayList<NPC> inAula = new ArrayList<>();
    private ArrayList<Lesson> lessonsPassed = new ArrayList<>();
    private TiledMap tiledmap = new TiledMap("/TiledMaps/MapFinal.json");

    public void update(ArrayList<Lesson> lessons, LocationManager locationManager, NPCManager npcManager, Schedule schedule){
        for (Lesson lesson : lessons)
            if (!lessonsPassed.contains(lesson))
            {
                // for the students
                ArrayList<Group> groups = lesson.getGroups();
                for (Group group : groups)
                {
                    ArrayList<Student> students = group.getStudents();
                    for (Student student : students)
                    {
                        if (npcStudentsOnScreen.contains(new NPC(student)))
                        {
                            System.out.println(student.getName() + ": Student word van huidige locatie naar nieuwe les verplaatst");
                            for (NPC npc : npcStudentsOnScreen)
                            {
                                if (npc.getPerson().equals(student))
                                {
                                    for (int i = 0; i < inAula.size(); i++) {
                                        if (inAula.get(i).getPerson() == student){
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
                            System.out.println(student.getName() + ": de student komt de school binnen en gaat naar zijn les");
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

        //used list
        ArrayList<NPC> used = new ArrayList<>();

        //checks if npc is used else it is send to the auditorium
        for (Lesson lesson : lessons) {
            for (Group group : lesson.getGroups()){
                for (Person person : group.getStudents()){
                    for (NPC npc : npcStudentsOnScreen){
                        if (npc.getPerson() == person){
                            used.add(npc);
                        }
                    }
                }
            }
        }


        //sends to auditorium
        for (int i = 0; i < npcStudentsOnScreen.size(); i++)
        {
            boolean onscreen = true;
            for (int j = 0; j < used.size(); j++)
            {
                if (used.get(j) == npcStudentsOnScreen.get(i))
                {
                    onscreen = false;
                }
            }
            for (int j = 0; j < inAula.size(); j++) {
                if (inAula.get(j) == npcStudentsOnScreen.get(i)){
                    onscreen = false;
                }
            }

            if (onscreen)
            {
                locationManager.scriptedEndLesson(npcStudentsOnScreen.get(i));
                npcStudentsOnScreen.get(i).resetDestination();
                npcStudentsOnScreen.get(i).getCurrentPathfinding().setDestination((int) AuditoriumBehavior.entry.getX(), (int) AuditoriumBehavior.entry.getY());
                inAula.add(npcStudentsOnScreen.get(i));
            }
        }


    }

    public void checkingFunction(LocationManager locationManager){
        //sends to seats
        for (NPC npc : npcStudentsOnScreen)
        {
            locationManager.scriptedStartedStudentLesson(npc);
        }
    }


    public ArrayList<NPC> getNpcStudentsOnScreen() {
        return npcStudentsOnScreen;
    }


}
