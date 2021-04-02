package Simulator;

import Data.*;
import Simulator.LocationSystem.AuditoriumBehavior;
import Simulator.LocationSystem.LocationDatabase;
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
import java.awt.geom.Rectangle2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

//TODO making time go backward can be done by saving npc data every ~15 min and allowing to go backwards to the saved points

/**
 * Auteurs: Leon, Luuk
 * <p>
 * Deze klasse is de basis voor de simulator
 */


public class Simulator
{
    private NPCManager npcManager = new NPCManager();
    private TimeManager timeManager;
    private static TiledMap tiledmap = new TiledMap("/TiledMaps/MapFinal.json");
    private int speedfactor = 1;

    private LocationManager locationManager;

    private ArrayList<NPC> npcOnScreen = new ArrayList<>();
    // private ArrayList<Student> studentsOnScreen = new ArrayList<>();
    private ArrayList<Lesson> lessonsPassed = new ArrayList<>();
    private LocalTime lastSave;
    private LocationDatabase base = new LocationDatabase();

    private double yComponent = 450;
    private double xComponent = 1300;

    public Simulator(Schedule schedule)
    {
        timeManager = new TimeManager(schedule, new NormalTime(LocalTime.of(9, 0, 0)));
        locationManager = new LocationManager();
        lastSave = LocalTime.of(8, 0, 0);

    }

    public static TiledMap getTiledmap()
    {
        return tiledmap;
    }

    public void update(long deltatime)
    {
        double deltaTimeMultiplier = 1;
        if (speedfactor > 0)
        {
            deltaTimeMultiplier = 1 + this.speedfactor / 10.0;
        }
        npcManager.update((deltatime / 1e9) * deltaTimeMultiplier);
        timeManager.update(deltatime);

        if (timeManager.isChanged() || speedfactor < 0)
        {
            ArrayList<Lesson> lessons = timeManager.getCurrentLessons();

            for (Lesson lesson : lessons)
                if (!lessonsPassed.contains(lesson))
                {
                    ArrayList<Group> groups = lesson.getGroups();
                    for (Group group : groups)
                    {
                        ArrayList<Student> students = group.getStudents();
                        for (Student student : students)
                        {
                            if (npcOnScreen.contains(new NPC(student)))
                            {
                                System.out.println(student.getName() + ": Student word van huidige locatie naar nieuwe les verplaatst");
                                for (NPC npc : npcOnScreen)
                                {
                                    if (npc.getPerson().equals(student))
                                    {
                                        locationManager.scriptedEndLesson(npc);
                                        npc.resetDestination();
                                        npc.getCurrentPathfinding().setDestination((int) lesson.getClassroom().getEntry().getX(), (int) lesson.getClassroom().getEntry().getY());
                                    }
                                }
                            }
                            else
                            {
                                System.out.println(student.getName() + ": de student komt de school binnen en gaat naar zijn les");
                                NPC npc = new NPC(student);
                                Pathfinding pathfinding = new Pathfinding(tiledmap/*GUI.getWalkablemap()*/);
                                npc.setPathfinding(pathfinding);
                                pathfinding.addNpc(npc);
                                npcOnScreen.add(npc);

                                if (pathfinding.getExactDestination() == null)
                                {
                                    pathfinding.setDestination((int) lesson.getClassroom().getEntry().getX(), (int) lesson.getClassroom().getEntry().getY());
                                }
                                npcManager.addNPC(npc);
                            }
                        }
                    }
                    //add lesson in list of passed ones
                    lessonsPassed.add(lesson);
                }

            //used list
            ArrayList<NPC> used = new ArrayList<>();

            //checks if npc is used else it is send to the auditorium
            for (Lesson lesson : lessons) {
                for (Group group : lesson.getGroups()){
                    for (Person person : group.getStudents()){
                        for (NPC npc : npcOnScreen){
                            if (npc.getPerson() == person){
                                used.add(npc);
                            }
                        }
                    }
                }
            }


            //sends to auditorium
            for (int i = 0; i < npcOnScreen.size(); i++)
            {
                boolean onscreen = true;
                for (int j = 0; j < used.size(); j++)
                {
                    if (used.get(j) == npcOnScreen.get(i))
                    {
                        onscreen = false;
                    }
                }

                if (onscreen)
                {
                    locationManager.scriptedEndLesson(npcOnScreen.get(i));
                    npcOnScreen.get(i).resetDestination();
                    npcOnScreen.get(i).getCurrentPathfinding().setDestination((int) AuditoriumBehavior.entry.getX(), (int) AuditoriumBehavior.entry.getY());
                }
            }

        }

        if (!npcOnScreen.isEmpty() && lastSave.until(timeManager.getTime(), ChronoUnit.MINUTES) > 15)
        {
            saveNPCs();
            lastSave = timeManager.getTime();
        }

        // update all the npcs on screen, see if they are at the entrance of their target classroom, if so enter it
        for (NPC npc : npcOnScreen)
        {
            locationManager.scriptedStartedLesson(npc);
        }
    }

    public void draw(FXGraphics2D fxGraphics2D)
    {
        tiledmap.draw(fxGraphics2D);
        npcManager.draw(fxGraphics2D, true);

        fxGraphics2D.setColor(Color.blue);



        if (true)
        {
            // draw seat numbers
            int number = 0;
            for (Tile tile : getTiledmap().getSeatableLayer().getTilesInLayer())
            {
                number++;
                fxGraphics2D.drawString(("" + number), tile.getX(), tile.getY());
            }

            fxGraphics2D.setColor(Color.BLACK);

            //draws npc targets
            for (int j = 0; j < npcOnScreen.size(); j++)
            {
                Point2D test = npcOnScreen.get(j).getCurrentPathfinding().getDestinationTile().getMiddlePoint();
                fxGraphics2D.fill(new Rectangle.Double(test.getX() - 5, test.getY() - 5, 10, 10));
            }

            // draws entry points classroom
            for (int i = 0; i < base.ClassRoomData().size(); i++)
            {
                fxGraphics2D.setColor(Color.red);
                Point2D point2D = base.ClassRoomData().get(i).getEntry();
                fxGraphics2D.fill(new Rectangle2D.Double(point2D.getX()-5, point2D.getY()-5, 10, 10));
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

    private Map<LocalTime, NPCManager> timeNPCManagerMap = new LinkedHashMap<>();

    public void setSpeedfactor(int speedFactor)
    {
        int maxSpeedFactor = 100;
        if (speedFactor > -1 && speedFactor <= maxSpeedFactor)
        {
            this.speedfactor = speedFactor;
        }
        timeManager.setSpeedFactor(this.speedfactor);
    }

    public void loadNPCs()
    {
        if (!timeNPCManagerMap.isEmpty())
        {
            Set<LocalTime> localTimes = timeNPCManagerMap.keySet();
            ArrayList<LocalTime> localTimeArrayList = new ArrayList<>(localTimes);
            LocalTime localTime = localTimeArrayList.get(localTimeArrayList.size() - 1);
            if (timeNPCManagerMap.containsKey(localTime))
            {
                NPCManager npcManager = timeNPCManagerMap.get(localTime);
                this.npcManager = npcManager;
                this.timeManager.setTimeType(new NormalTime(localTime));
                setSpeedfactor(0);
                npcOnScreen.clear();
                npcOnScreen.addAll(npcManager.getNpcs());
                timeNPCManagerMap.remove(localTime);
                lastSave = localTime;
            }
        }
    }


    public void saveNPCs()
    {
        NPCManager newNpcManager = new NPCManager();

        for (NPC npc : this.npcManager.getNpcs())
        {
            newNpcManager.addNPC(npc.clone());
        }

        timeNPCManagerMap.put(timeManager.getTime(), newNpcManager);

        System.out.println("npcs saved");

    }


}
