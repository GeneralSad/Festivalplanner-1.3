package Simulator;

import Data.*;
import Simulator.Controller.StudentController;
import Simulator.Controller.TeacherController;
import Simulator.LocationSystem.AuditoriumBehavior;
import Simulator.LocationSystem.LocationDatabase;
import Simulator.LocationSystem.LocationManager;
import Simulator.Maploading.Tile;
import Simulator.Maploading.TiledMap;
import Simulator.NPC.NPC;
import Simulator.NPC.NPCFollower;
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

    //time managers stuff
    private NPCManager npcManager = new NPCManager();
    private TimeManager timeManager;
    private static TiledMap tiledmap = new TiledMap("/TiledMaps/MapFinal.json");
    private int speedfactor = 1;


    //chache
    private ArrayList<NPC> npcOnScreen = new ArrayList<>();

    //controllers
    private StudentController studentController = new StudentController();
    private TeacherController teacherController = new TeacherController();
    private LocationManager locationManager = new LocationManager();

    private LocalTime lastSave;
    private LocationDatabase base = new LocationDatabase();

    private double yComponent = 450;
    private double xComponent = 1300;
    private Camera camera;


    public Simulator(Schedule schedule)
    {
        timeManager = new TimeManager(schedule, new NormalTime(LocalTime.of(9, 0, 0)));
        lastSave = LocalTime.of(8, 0, 0);

    }

    public static TiledMap getTiledmap()
    {
        return tiledmap;
    }

    /**
     * updates the canvas of the simulation and it assest and time managment.
     * @param deltatime is the time it took to draw the prevouis frame.
     */
    public void update(long deltatime)
    {
        //time goodness
        double deltaTimeMultiplier = 1;
        if (speedfactor > 0)
        {
            deltaTimeMultiplier = 1 + this.speedfactor / 10.0;
        }
        npcManager.update((deltatime / 1e9) * deltaTimeMultiplier);
        timeManager.update(deltatime);

        //if the time is changed the location will be updated
        if (timeManager.isChanged() || speedfactor < 0) {
            ArrayList<Lesson> lessons = timeManager.getCurrentLessons();

            //updates the students and teachers controls
            studentController.update(lessons, locationManager, npcManager);
            teacherController.update(lessons, locationManager, npcManager);

            //clears the cache
            npcOnScreen.clear();

            //loads the cache that is needed for other parts of this class
            npcOnScreen.addAll(studentController.getNpcStudentsOnScreen());
            npcOnScreen.addAll(teacherController.getNpcTeacherOnScreen());
        }

        //checks if it is at the entrance of a classroom or autrium if that is true then
        //it will collect a seat
        studentController.checkingFunction(locationManager);
        teacherController.checkingFunction(locationManager);


        //save a backup of the npc for going backward
        if (!npcOnScreen.isEmpty() && lastSave.until(timeManager.getTime(), ChronoUnit.MINUTES) > 15)
        {
            saveNPCs();
            lastSave = timeManager.getTime();
        }

        // if the camera is following an npc, update it so it adjusts to the new npc positions
        if (camera.getNpcFollower().isFollowing()) {
            camera.getNpcFollower().update();
        }
    }

    /**
     * draws the simulation with debug options
     * @param fxGraphics2D is the graphichs that draws everthing
     */
    public void draw(FXGraphics2D fxGraphics2D, double canvasWidth, double canvasHeight)
    {
        tiledmap.draw(fxGraphics2D);
        npcManager.draw(fxGraphics2D, true);


        //debug for all the part in the simulator that have something to do with the seats and locations
        if (true)
        {
            fxGraphics2D.setColor(Color.blue);
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

        if (camera.getNpcFollower().isFollowing()) {
            camera.getNpcFollower().draw(fxGraphics2D);
        }
    }

    public void generateComponents() {

        xComponent += 16;
        if (xComponent > 1575) {
            xComponent = 1310;
            yComponent += 16;
        }

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

    public NPC getNPCAtPosition(double x, double y) {
        for (NPC npc : this.npcOnScreen) {
            Rectangle2D hitbox = npc.getHitbox();
            if (hitbox.contains(x, y)) {
                return npc;
            }
        }
        return null;
    }


    public void mouseClicked(double x, double y) {
        NPCFollower npcFollower = camera.getNpcFollower();
        NPC npc = getNPCAtPosition(x, y);
        System.out.println("Clicking on: " + (x) + " " + (y));
        if (npc != null) {
            npcFollower.setNpc(npc);
            npcFollower.setFollowing(true);
            System.out.println("Following an npc");
        } else {
            npcFollower.setNpc(null);
            npcFollower.setFollowing(false);
        }
    }

    public Camera getCamera()
    {
        return camera;
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }
}
