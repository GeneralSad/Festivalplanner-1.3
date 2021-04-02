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
        classRoomBehaviors = locations.ClassRoomData();
        auditorium = locations.AudioriumData();
    }

    public boolean scriptedStartedLesson(NPC student){

        if (student.isAtDestination())
        {
            //checks if it is standing at a classroom
            for (int i = 0; i < classRoomBehaviors.size(); i++)
            {
                if (student.getCurrentLocation().distance(classRoomBehaviors.get(i).getEntry()) < 10)
                {
                    classRoomBehaviors.get(i).ScriptedStudentStart(student);
                    return true;
                }
            }

            //checks if standing at a auditorium
            if (student.getCurrentLocation().distance(auditorium.getEntry()) < 20){
                auditorium.ScriptedStart(student);
                return true;
            }
        }

        return false;
    }

    public void scriptedEndLesson(NPC student){
        //removes the student when leaving from classroom
        for (int i = 0; i < classRoomBehaviors.size(); i++)
        {
                classRoomBehaviors.get(i).leaveFilledSeat(student);
        }

        //does the same but then for auditorium
        auditorium.leaveFilledSeat(student);
    }
}
