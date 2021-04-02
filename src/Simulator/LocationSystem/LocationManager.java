package Simulator.LocationSystem;

import Data.Schedule;
import Data.Teacher;
import Simulator.NPC.NPC;
import Simulator.Pathfinding.Pathfinding;

import java.io.Serializable;
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

    public boolean scriptedStartedStudentLesson(NPC student){

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

    public boolean scriptedStartedTeacherLesson(NPC teacher){

        if (teacher.isAtDestination())
        {
            //checks if it is standing at a classroom
            for (int i = 0; i < classRoomBehaviors.size(); i++)
            {
                if (teacher.getCurrentLocation().distance(classRoomBehaviors.get(i).getEntry()) < 10)
                {
                    classRoomBehaviors.get(i).ScriptedTeacherStart(teacher);
                    return true;
                }
            }

            //checks if standing at a auditorium
            if (teacher.getCurrentLocation().distance(auditorium.getEntry()) < 20){
                auditorium.ScriptedStart(teacher);
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
