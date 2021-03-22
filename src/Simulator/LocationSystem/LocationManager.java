package Simulator.LocationSystem;

import Data.Schedule;
import GUI.GUI;
import Simulator.NPC.NPC;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class LocationManager
{
    private LocationDatabase locations = new LocationDatabase();
    private ArrayList<ClassRoom> classRooms;
    private Auditorium auditorium;

    public LocationManager()
    {
        classRooms = locations.ClassRoomStudentData();
    }

    public void scriptedLesson(NPC student){

        classRooms.get(0).ScriptedStudentStart(student);
    }
}
