package Simulator.LocationSystem;

import GUI.GUI;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map;


public class LocationDatabase
{
    public ArrayList<ClassRoom> ClassRoomStudentData(){
        ArrayList<ClassRoom> classRooms = new ArrayList<>();


        ArrayList<Point2D> locations = new ArrayList<>();

        ArrayList<Double> orientations = new ArrayList<>();

        for (Map.Entry<Point2D, Double> entry: GUI.getTiledmap().getAllSitableTiles().entrySet())
        {
            locations.add(entry.getKey());
            orientations.add(entry.getValue());
        }

        System.out.println(GUI.getTiledmap().getAllSitableTiles().size());

        //room 1
        ArrayList<Seat> seats = new ArrayList<>();

        int index = 0;
        while (index <= 30){
            seats.add(new Seat(locations.get(index), null, orientations.get(index)));

            index++;
            if ((index) % 4 == 0){
                index += 4;
            } else if (index == 20){
                index += 6;
            }
            System.out.println(index);
        }


        classRooms.add(new ClassRoom(seats, new Point2D.Double(550, 600)));

        return classRooms;

        //TODO more to come.......
    }
}
