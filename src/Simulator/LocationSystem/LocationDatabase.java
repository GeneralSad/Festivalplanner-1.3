package Simulator.LocationSystem;

import Data.Classroom;
import GUI.GUI;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map;


public class LocationDatabase
{

    /**
     * searches for chairs in the area that is selected with a algoritm
     * @param indexstart the index of a tile were it starts searching needs to be the most left bove tile.
     * @return a list with all the seats in a classroom
     */

    public ArrayList<Seat> collectSeats(int indexstart){
        ArrayList<Point2D> areas = new ArrayList<>();
        ArrayList<Seat> seats = new ArrayList<>();

        for (Map.Entry<Point2D, Double> entry: GUI.getTiledmap().getAllAreaTiles().entrySet())
        {
            areas.add(entry.getKey());
        }



        Point2D selected = areas.get(indexstart);
        while (true){
            if (GUI.getTiledmap().IsWalkableTile(selected)){

                if (GUI.getTiledmap().IsSitableTile(selected)){

                    seats.add(new Seat(new Point2D.Double(selected.getX() + 8, selected.getY()), null,GUI.getTiledmap().getAllSitableTiles().get(selected)));



                }
                selected = new Point2D.Double(selected.getX() + GUI.getTiledmap().getTileWidth(), selected.getY());
            } else {

                selected = new Point2D.Double(areas.get(indexstart).getX(),
                        selected.getY() + GUI.getTiledmap().getTileHeight());
                if (!GUI.getTiledmap().IsWalkableArea(selected)){
                    break;
                }
            }


        }

        return seats;

    }

    /**
     * subjected to change with a area funtion that checks a area and reads all the seats.
     * @return
     */
    public ArrayList<ClassRoomBehavior> ClassRoomStudentData(){
        //return type
        ArrayList<ClassRoomBehavior> classRoomBehaviors = new ArrayList<>();

        //room1
        int index = 0;
        classRoomBehaviors.add(new ClassRoomBehavior(collectSeats(index)
                , collectSeats(index).get(12), new Point2D.Double(550, 550)));

        //room2
        index = 15;
        classRoomBehaviors.add(new ClassRoomBehavior(collectSeats(index)
                , collectSeats(index).get(12), new Point2D.Double(1000, 550)));

        //room3
        index = 232;
        classRoomBehaviors.add(new ClassRoomBehavior(collectSeats(index)
                , collectSeats(index).get(0), new Point2D.Double(500, 820)));


        //room4
        index = 241;
        classRoomBehaviors.add(new ClassRoomBehavior(collectSeats(index)
                , collectSeats(index).get(0), new Point2D.Double(600, 820)));


        //room5
        index = 250;
        classRoomBehaviors.add(new ClassRoomBehavior(collectSeats(index)
                , collectSeats(index).get(0), new Point2D.Double(950, 820)));


        //room6
        index = 259;
        classRoomBehaviors.add(new ClassRoomBehavior(collectSeats(index)
                , collectSeats(index).get(0), new Point2D.Double(1050, 820)));


        //room7
        index = 736;
        classRoomBehaviors.add(new ClassRoomBehavior(collectSeats(index)
                , collectSeats(index).get(0), new Point2D.Double(500, 1120)));


        //room8
        index = 745;
        classRoomBehaviors.add(new ClassRoomBehavior(collectSeats(index)
                , collectSeats(index).get(0), new Point2D.Double(600, 1120)));





        return classRoomBehaviors;
    }


}
