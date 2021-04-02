package Simulator;

import Data.Group;
import Data.Lesson;
import Data.Schedule;
import Data.Student;
import Simulator.LocationSystem.LocationManager;
import Simulator.Maploading.Tile;
import Simulator.Maploading.TiledMap;
import Simulator.NPC.NPC;
import Simulator.NPC.NPCManager;
import Simulator.Pathfinding.Pathfinding;
import Simulator.Time.NormalTime;
import Simulator.Time.TimeManager;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

//TODO making time go backward can be done by saving npc data every ~15 min and allowing to go backwards to the saved points

/**
 * Auteurs: Leon
 * <p>
 * Deze klasse is de basis voor de simulator
 */

public class Simulator
{
    private NPCManager npcManager = new NPCManager();
    private TimeManager timeManager;
    private Schedule schedule;
    private static TiledMap tiledmap = new TiledMap("/TiledMaps/MapFinal.json");
    private int speedfactor = 1;
    private ArrayList<Student> studentsOnScreen = new ArrayList<>();
    private LocationManager locationManager;
    private ArrayList<NPC> npcOnScreen = new ArrayList<>();
    private int maxSpeedFactor = 100;

    private double yComponent = 450;
    private double xComponent = 1300;

    public Simulator(Schedule schedule)
    {
        timeManager = new TimeManager(schedule, new NormalTime(LocalTime.of(9, 0, 0)));
        this.schedule = schedule;
        locationManager = new LocationManager();
    }

    public static TiledMap getTiledmap()
    {
        return tiledmap;
    }

    public void update(long deltatime)
    {


        //Deze pointers zijn voor de goede waarden bepalen van de hoeken van het spawnveld
/*        NPC pointer = new NPC(new Student("C1", 1, new Group("1")), 1310, 750);
        NPC pointer1 = new NPC(new Student("C2", 1, new Group("1")), 1575, 750);
        NPC pointer2 = new NPC(new Student("C3", 1, new Group("1")), 1575, 450);
        NPC pointer3 = new NPC(new Student("C4", 1, new Group("1")), 1310, 450);

        npcManager.addNPC(pointer);
        npcManager.addNPC(pointer1);
        npcManager.addNPC(pointer2);
        npcManager.addNPC(pointer3);*/

        double deltaTimeMultiplier = 1;
        if (speedfactor > 0)
        {
            deltaTimeMultiplier = 1 + this.speedfactor / 10.0;
        }
        npcManager.update((deltatime / 1e9) * deltaTimeMultiplier);
        timeManager.update(deltatime);

        if (timeManager.isChanged() || speedfactor < 0)
        {
            System.out.println(timeManager.getTime());
            ArrayList<Lesson> lessons = timeManager.getCurrentLessons();
            ArrayList<Student> studentsWithLesson = new ArrayList<>();


            for (Lesson lesson : lessons)
            {
                for (Group group : lesson.getGroups())
                {
                    studentsWithLesson.addAll(group.getStudents());
                }
            }

            for (Student student : studentsWithLesson)
            {
                if (studentsOnScreen.contains(student))
                {
                    //System.out.println(student.getName() + ": Student word van huidige locatie naar nieuwe les verplaatst");
                    //TODO check if necessary to move to new lesson
                }
                else
                {
                    //System.out.println(student.getName() + ": de student komt de school binnen en gaat naar zijn les");

                    ArrayList<Point2D> currentLocations = new ArrayList<>();
                    ArrayList<Point2D> availableLocations = new ArrayList<>();
                    NPC npc;

                    if (!npcManager.getNpcs().isEmpty()) {

                        for (NPC existingNPC : npcManager.getNpcs()) {
                            Point2D location = existingNPC.getCurrentLocation();
                            currentLocations.add(location);
                            //generateComponents();
                            availableLocations.add(getAvailability(location));
                        }

                        Point2D fullAvailableLocation = getFullAvailability(currentLocations, availableLocations);

                        npc = new NPC(student, (int)fullAvailableLocation.getX(), (int)fullAvailableLocation.getY());
                        System.out.println("X: " + (int)fullAvailableLocation.getX() + " Y: " + (int)fullAvailableLocation.getY());

                    } else {
                        //generateComponents();
                        npc = new NPC(student, (int)xComponent, (int)yComponent);
                        System.out.println("X: " + xComponent + " Y: " + yComponent);

                    }

                    Pathfinding pathfinding = new Pathfinding(tiledmap/*GUI.getWalkablemap()*/);
                    npc.setPathfinding(pathfinding);
                    pathfinding.addNpc(npc);

                    npcOnScreen.add(npc);

                    if (pathfinding.getExactDestination() == null)
                    {
                        pathfinding.setDestination((int) student.getGroup().getClassroom().getEntry().getX(), (int) student.getGroup().getClassroom().getEntry().getY());
                    }

                    npcManager.addNPC(npc);
                    studentsOnScreen.add(student);
                }
            }


            ArrayList<Student> studentsOnScreenPlaceHolder = new ArrayList<>(studentsOnScreen);
            for (Student student : studentsOnScreenPlaceHolder)
            {
                if (!studentsWithLesson.contains(student))
                {
                    studentsOnScreen.remove(student);
                }
            }


        }

        //TODO De for loop hieronder is misschien niet goed
        //TODO Check
        for (NPC npc : npcOnScreen)
        {
            locationManager.scriptedStartedLesson(npc, npc.getCurrentPathfinding());
        }
    }

    public void draw(FXGraphics2D fxGraphics2D)
    {
        tiledmap.draw(fxGraphics2D);
        npcManager.draw(fxGraphics2D, false);

        fxGraphics2D.setColor(Color.blue);

        // draw seat numbers
        int number = 0;
        for (Tile tile : getTiledmap().getSeatableLayer().getTilesInLayer())
        {
            number++;
            fxGraphics2D.drawString(("" + number), tile.getX(), tile.getY());
        }

        fxGraphics2D.setColor(Color.BLACK);

        if (false)
        {
            for (int j = 0; j < npcOnScreen.size(); j++)
            {
                Point2D test = npcOnScreen.get(j).getCurrentPathfinding().getDestinationTile().getMiddlePoint();
                fxGraphics2D.fill(new Rectangle.Double(test.getX() - 5, test.getY() - 5, 10, 10));
            }
        }
    }

    public void generateComponents() {

        xComponent += 16;
        if (xComponent > 1575) {
            xComponent = 1310;
            yComponent += 16;
        }

    }

    //TODO temporary for testing
    public int yComponent(){
        if (yComponent > 750){
            yComponent = 450;
        }
        return (int)yComponent;
    }

    //TODO temporary for testing
    public int xComponent(){
        return (int)xComponent;
    }

    public Point2D getAvailability(Point2D location) {

        generateComponents();
        if (location.distance(new Point2D.Double(xComponent, yComponent)) > 16) {
            return new Point2D.Double(xComponent, yComponent);
        } else {
            return getAvailability(location);
        }

    }

    private ArrayList<Point2D> fullAvailableLocations = new ArrayList<>();

    public Point2D getFullAvailability(ArrayList<Point2D> currentLocations, ArrayList<Point2D> availableLocations ) {

        if (!fullAvailableLocations.isEmpty()) {
            fullAvailableLocations.remove(0);
        }

        for (Point2D location : availableLocations){

            for (Point2D currentLocation : currentLocations) {

                if (currentLocation.distance(location) > 16 && !fullAvailableLocations.contains(location)) {

                    fullAvailableLocations.add(location);

                }

            }

        }

        return fullAvailableLocations.get(0);

    }

    public String getFormattedTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        return timeManager.getTime().format(dtf);
    }

    public int getSpeedfactor()
    {
        return timeManager.getSpeedFactor();
    }

    public void setSpeedfactor(int speedFactor)
    {
        if (speedFactor >= -maxSpeedFactor && speedFactor <= maxSpeedFactor)
        {
            this.speedfactor = speedFactor;
        }
        timeManager.setSpeedFactor(this.speedfactor);
    }
}
