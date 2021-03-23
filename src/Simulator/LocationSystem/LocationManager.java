package Simulator.LocationSystem;

import Simulator.NPC.NPC;
import Simulator.Pathfinding.Pathfinding;

import java.util.ArrayList;

public class LocationManager
{
    private LocationDatabase locations = new LocationDatabase();
    private ArrayList<ClassRoomBehavior> classRoomBehaviors;
    private AuditoriumBehavior auditorium;

    public LocationManager()
    {
        classRoomBehaviors = locations.ClassRoomStudentData();

    }

    public boolean scriptedLesson(NPC student, Pathfinding pathfinding){


        for (int i = 0; i < classRoomBehaviors.size(); i++)
        {

               if (student.isAtDestination()){
                   pathfinding.removeNpc(student);
                   student.resetDestination();
                   classRoomBehaviors.get(i).ScriptedStudentStart(student);

                   return true;
               }
        }

        return false;
    }
}
