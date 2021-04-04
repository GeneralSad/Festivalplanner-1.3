package Simulator.LocationSystem;

import Data.Schedule;
import GUI.GUI;
import Simulator.Maploading.Tile;
import Simulator.Maploading.TiledMap;
import Simulator.Simulator;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public class LocationDatabase
{

    /**
     * Searches for seats in a rectangle part of tiles marked as being in the area for classrooms
     * Starts looking at the given row and column, searches each row untill the end of the area is reached
     * Stops when on a new row the tile is no longer part of the area
     *
     * @param rowFrom
     * @param columnFrom
     * @return
     */

    public ArrayList<Seat> collectSeats(int rowFrom, int columnFrom)
    {
        ArrayList<Seat> seats = new ArrayList<>();
        TiledMap tiledMap = Simulator.getTiledmap();

        Tile start = Simulator.getTiledmap().getAreaLayer().getTile(rowFrom, columnFrom);
        if (start != null)
        {
            // position keeps track of the position of tiles to check
            Point2D position = new Point2D.Double(start.getX(), start.getY());

            boolean running = true;
            while (running)
            {
                // check for the row of the current tile all subsequent tiles untill a tile was found which is not walkable
                if (tiledMap.isPartOfArea(position))
                {
                    if (tiledMap.isSitableTile(position))
                    {
                        Tile selected = tiledMap.getSeatableLayer().getTile(position);
                        if (selected != null)
                        {
                            // if the tile is walkable, seatable and exists in the seatablelayer then add a new seat at that tile position
                            seats.add(new Seat(new Point2D.Double(selected.getX() + 8, selected.getY() + 8), null, selected.getRotation()));
                        }
                    }
                    position.setLocation(position.getX() + tiledMap.getTileWidth(), position.getY());

                }
                else
                {
                    // go to a row lower
                    position.setLocation(start.getX(), position.getY() + tiledMap.getTileHeight());
                    // if the tile on the lower row is not walkable, then the classroom is finished so stop looking for seats
                    if (!tiledMap.isPartOfArea(position))
                    {
                        running = false;
                    }
                }
            }


        }
        else
        {
            System.out.println("Collecting seats, starting tile was null");
        }

        return seats;
    }


    /**
     * subjected to change with a area funtion that checks a area and reads all the seats.
     *
     * @return
     */
    public ArrayList<ClassRoomBehavior> ClassRoomData()
    {
        //return type
        ArrayList<ClassRoomBehavior> classRoomBehaviors = new ArrayList<>();

        // Rows and columns are read from Tiled, hovering with the mouse shows the column and row of the tile hovered over, which allows for seeing the starting part of an area

        //room1
        ArrayList<Seat> seats = collectSeats(24, 32);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(12), new Point2D.Double(550, 550), 0, new Point2D.Double(560 , 400)));

        //room2
        seats = collectSeats(24, 52);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(12), new Point2D.Double(1000, 550), 0, new Point2D.Double(880, 400)));

        //room3
        seats = collectSeats(45, 21);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(500, 820), 0, new Point2D.Double(336, 800)));


        //room4
        seats = collectSeats(45, 38);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(600, 820), 8, new Point2D.Double(604, 800)));


        //room5
        seats = collectSeats(45, 49);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(950, 820), 0, new Point2D.Double(784, 800)));


        //room6
        seats = collectSeats(45, 66);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(1050, 820), 8, new Point2D.Double(1056, 800)));


        //room7
        seats = collectSeats(62, 21);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(500, 1110), 0, new Point2D.Double(336, 1070)));


        //room8
        seats = collectSeats(62, 38);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(600, 1110), 8, new Point2D.Double(604, 1070)));


        return classRoomBehaviors;
    }

    public AuditoriumBehavior AudioriumData()
    {
        ArrayList<Seat> seats = collectSeats(62, 49);
        AuditoriumBehavior auditoriumBehavior = new AuditoriumBehavior(seats, new Point2D.Double(1000, 980));

        return auditoriumBehavior;
    }


}
