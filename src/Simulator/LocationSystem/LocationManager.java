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

    public boolean scriptedStartedLesson(NPC student, Pathfinding pathfinding){

        if (student.isAtDestination())
        {
            for (int i = 0; i < classRoomBehaviors.size(); i++)
            {
                if (student.getCurrentLocation().distance(classRoomBehaviors.get(i).getEntry()) < 10)
                {
                    classRoomBehaviors.get(i).ScriptedStudentStart(student);
                    return true;
                }
            }
        }

        return false;
    }
}
