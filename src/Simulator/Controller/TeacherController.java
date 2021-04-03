package Simulator.Controller;

import Data.Lesson;
import Data.Schedule;
import Simulator.LocationSystem.AuditoriumBehavior;
import Simulator.LocationSystem.LocationManager;
import Simulator.Maploading.TiledMap;
import Simulator.NPC.NPC;
import Simulator.NPC.NPCManager;
import Simulator.Pathfinding.Pathfinding;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TeacherController {


    private ArrayList<NPC> npcTeacherOnScreen = new ArrayList<>();
    private ArrayList<NPC> inAula = new ArrayList<>();
    private ArrayList<Lesson> lessonsPassed = new ArrayList<>();
    private TiledMap tiledmap = new TiledMap("/TiledMaps/MapFinal.json");
    private ArrayList<NPC> skippable = new ArrayList<>();

    public void update(ArrayList<Lesson> lessons, LocationManager locationManager, NPCManager npcManager, Schedule schedule){


        ArrayList<Lesson> lessonsAllday = schedule.getLessonArrayList();

        for (NPC npc: npcTeacherOnScreen) {
            if (!skippable.contains(npc)) {
                boolean stillLesson = true;
                for (Lesson lesson : lessonsAllday) {
                    if (lesson.getTeacher() == npc.getPerson()) {
                        if (lessonsPassed.contains(lesson) && !lessons.contains(lesson)) {

                        } else {
                            stillLesson = false;
                        }
                    }
                }

                if (stillLesson) {
                    locationManager.scriptedEndLesson(npc);
                    npc.resetDestination();
                    npc.getCurrentPathfinding().setDestination(1425, 725);
                    skippable.add(npc);
                }
            }
        }






        for (Lesson lesson : lessons)
            if (!lessonsPassed.contains(lesson)) {
                if (npcTeacherOnScreen.contains(new NPC(lesson.getTeacher())))
                {
                    System.out.println(lesson.getTeacher().getName() + ": Teacher word van huidige locatie naar nieuwe les verplaatst");
                    for (NPC npc : npcTeacherOnScreen)
                    {
                        if (npc.getPerson().equals(lesson.getTeacher()))
                        {
                            for (int i = 0; i < inAula.size(); i++) {
                                if (lesson.getTeacher() == inAula.get(i).getPerson()){
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
                    System.out.println(lesson.getTeacher().getName() + ": de Teacher komt de school binnen en gaat naar zijn les");
                    NPC npc = new NPC(lesson.getTeacher());
                    npc.setCollisionEnabler(true);
                    Pathfinding pathfinding = new Pathfinding(tiledmap/*GUI.getWalkablemap()*/);
                    npc.setPathfinding(pathfinding);
                    pathfinding.addNpc(npc);
                    npcTeacherOnScreen.add(npc);
                    pathfinding.setDestination((int) lesson.getClassroom().getEntry().getX(), (int) lesson.getClassroom().getEntry().getY());

                    npcManager.addNPC(npc);
                }
                lessonsPassed.add(lesson);
            }


        //used list
        ArrayList<NPC> used = new ArrayList<>();

        //checks if npc is used else it is send to the auditorium
        for (Lesson lesson : lessons) {
            for ( NPC npc : npcTeacherOnScreen) {
                if (npc.getPerson() == lesson.getTeacher()){
                    used.add(npc);
                }
            }
        }


        //sends to auditorium
        for (int i = 0; i < npcTeacherOnScreen.size(); i++)
        {
            boolean onscreen = true;
            for (int j = 0; j < used.size(); j++)
            {
                if (used.get(j) == npcTeacherOnScreen.get(i))
                {
                    onscreen = false;
                }
            }
            for (int j = 0; j < inAula.size(); j++) {
                if (inAula.get(j) == npcTeacherOnScreen.get(i)){
                    onscreen = false;
                }
            }
            for (int j = 0; j < skippable.size(); j++) {
                if (skippable.get(j) == npcTeacherOnScreen.get(i)){
                    onscreen = false;
                }
            }

            if (onscreen)
            {
                locationManager.scriptedEndLesson(npcTeacherOnScreen.get(i));
                npcTeacherOnScreen.get(i).resetDestination();
                npcTeacherOnScreen.get(i).getCurrentPathfinding().setDestination((int) AuditoriumBehavior.entry.getX(), (int) AuditoriumBehavior.entry.getY());
                inAula.add(npcTeacherOnScreen.get(i));
            }
        }


    }

    public void checkingFunction(LocationManager locationManager){
        //sends to seats
        for (NPC npc : npcTeacherOnScreen)
        {
            locationManager.scriptedStartedTeacherLesson(npc);
        }
    }

    public void teacherDespawining(NPCManager npcManager){
        for (int i = 0; i < skippable.size(); i++) {
            if (skippable.get(i).isAtDestination() && skippable.get(i).getCurrentLocation().distance(new Point2D.Double(1424, 725)) < 10){

                for (int j = 0; j < npcTeacherOnScreen.size(); j++) {
                    if (npcTeacherOnScreen.get(j) == skippable.get(i)){
                        npcTeacherOnScreen.remove(j);
                    }
                }
                npcManager.removeNPC(skippable.get(i));

                skippable.remove(i);

            }
        }
    }



    public ArrayList<NPC> getNpcTeacherOnScreen() {
        return npcTeacherOnScreen;
    }
}
