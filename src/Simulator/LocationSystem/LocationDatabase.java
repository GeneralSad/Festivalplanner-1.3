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
     * zoekt voor stoelen in de vierkant van de tiles van de area die is gemarkeerd in de json file
     * Start op de tile van de gegeven rij en colom en zoekt elke rij totdat het einde van area is bereikt
     * Stop waaneer een nieuwe rij niet meer onderdeel is van de area
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
            // postie houdt de postie bij van de tiles
            Point2D position = new Point2D.Double(start.getX(), start.getY());

            boolean running = true;
            while (running)
            {
                //checkt of een rij van de current tile alle volgende tiles todat een tile is gevonden die neit walkable is.
                if (tiledMap.isPartOfArea(position))
                {
                    if (tiledMap.isSitableTile(position))
                    {
                        Tile selected = tiledMap.getSeatableLayer().getTile(position);
                        if (selected != null)
                        {
                            // Als de tile walkable, seatable en bestaat in de laag seatable dan voeg de stoel toe met de momentele postie van de tile
                            seats.add(new Seat(new Point2D.Double(selected.getX() + 8, selected.getY() + 8), null, selected.getRotation()));
                        }
                    }
                    position.setLocation(position.getX() + tiledMap.getTileWidth(), position.getY());

                }
                else
                {
                    // ga een rij lager
                    position.setLocation(start.getX(), position.getY() + tiledMap.getTileHeight());

                    //als de tile op een lagere rij is en niet walkable, dan is de klaslook af en stop het met zoeken
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
        //teruggeef bestand
        ArrayList<ClassRoomBehavior> classRoomBehaviors = new ArrayList<>();

        // Rijen en coloms zijn alleen van tiled, met je muis over het iets toont het colomn en de rij van de tile waar over een wordt bewogen, wat toelaaat om te zien voor een arau onderdeel

        //lokaal 1
        ArrayList<Seat> seats = collectSeats(24, 32);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(12), new Point2D.Double(550, 550), 0, new Point2D.Double(560 , 400)));

        //lokaal 2
        seats = collectSeats(24, 52);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(12), new Point2D.Double(1000, 550), 0, new Point2D.Double(880, 400)));

        //lokaal 3
        seats = collectSeats(45, 21);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(500, 820), 0, new Point2D.Double(336, 800)));


        //lokaal 4
        seats = collectSeats(45, 38);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(600, 820), 8, new Point2D.Double(604, 800)));


        //lokaal 5
        seats = collectSeats(45, 49);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(950, 820), 0, new Point2D.Double(784, 800)));


        //lokaal 6
        seats = collectSeats(45, 66);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(1050, 820), 8, new Point2D.Double(1056, 800)));


        //lokaal 7
        seats = collectSeats(62, 21);
        classRoomBehaviors.add(new ClassRoomBehavior(seats, seats.get(0), new Point2D.Double(500, 1110), 0, new Point2D.Double(336, 1070)));


        //lokaal 8
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
