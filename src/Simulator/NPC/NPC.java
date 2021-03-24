package Simulator.NPC;

import Data.Person;
import Simulator.LocationSystem.Seat;
import Simulator.Maploading.TiledMap;
import Simulator.Pathfinding.Direction;
import Simulator.Pathfinding.Pathfinding;
import Simulator.Pathfinding.PathfindingTile;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class NPC
{
    private Person person;
    private double x;
    private double y;
    private int width;
    private int height;
    private Point2D destination;
    // rotation from 0 to 2 * Math.PI
    private double rotation;
    // Rotation direction, 1 for left, -1 for right.
    private int rotationDirection;
    private PathfindingTile currentTile;

    private int speed;
    private double targetRotation;
    private int rotationSpeed;

    private boolean atDestination;
    public NPCSprites appearance;

    private Pathfinding currentPathfinding;
    private boolean onTargetTile;

    //TODO Temporary for testing class behavior.
    private static double yCompontent = 575;
    private static double xComponent = 1300;


    public NPC(Person person, double x, double y, int width, int height, int rotation, int speed, int rotationSpeed, String npcAppearance)
    {
        this.person = person;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.rotationDirection = 1;
        this.currentTile = null;
        this.speed = speed;
        this.targetRotation = rotation;
        this.rotationSpeed = rotationSpeed;
        this.atDestination = true;
        this.appearance = new NPCSprites(npcAppearance);
    }

    public NPC(Person person, double x, double y, int width, int height, String imageLocation)
    {
        this(person, x, y, width, height, 0, 10, 20, imageLocation);
    }
    
    public NPC(Person person)
    {
        this(person, xComponent(), yComponent(), 8, 16, randomSprite());
    }

    /**
     * Main update method
     *
     * @param deltaTime
     * @param npcs
     */
    public void update(double deltaTime, ArrayList<NPC> npcs)
    {
        // Only move and update if not at the destination
        // if at the destination the npc shouldn't do anything regarding movement
        if (!atDestination)
        {
            // don't update pathfinding movement if already on the target tile
            if (currentPathfinding.getDestinationTile() != null && !onTargetTile)
            {
                pathfindingUpdate(deltaTime);
            } else {
                rotationUpdate(deltaTime);
            }

            // NPC collision
            if (collisionUpdate(npcs))
            {
                // if a collision would occur, reverse the previous made movement
                rotationUpdate(-deltaTime);
            }

            // Destination check
            destinationUpdate();

            // prints the texture of the npc correctly
            this.appearance.locationUpdater(x, y);
            this.appearance.directionUpdater(rotation);
            this.appearance.calculateUpdater(rotation);
        }
    }

    /**
     * Check if the npc is at the destination
     */
    private void destinationUpdate()
    {
        Rectangle2D currentHitBox = new Rectangle2D.Double(this.x, this.y, this.width, this.height);
        if (destination != null && currentHitBox.contains(destination))
        {
            atDestination = true;
        }
    }

    /**
     * Check collision with other npcs
     *
     * @param npcs
     */
    private boolean collisionUpdate(ArrayList<NPC> npcs)
    {
        Rectangle2D thisNpcHitBox = new Rectangle2D.Double(this.x, this.y, this.width, this.height);

        // check for all other npcs if this npc would collide
        for (NPC npc : npcs)
        {
            if (npc != this)
            {
                // find the other npc hitbox
                Rectangle2D npcHitBox = new Rectangle2D.Double(npc.x, npc.y, npc.width, npc.height);

                if (thisNpcHitBox.intersects(npcHitBox))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Update the rotation
     * If the rotation matches the target rotation then move the npc in the direction of it's rotation
     *
     * @param deltaTime
     */
    public void rotationUpdate(double deltaTime)
    {
        // only move if the rotation matches the target rotation
        if (rotation == targetRotation || rotation == targetRotation + Math.PI * 2)
        {
            double distance = deltaTime * speed;

            // Determine the x and y distance made with the rotation and adjust the position
            double sin = Math.sin(rotation);
            double cos = Math.cos(rotation);
            double xDiff = (cos * distance);
            double yDiff = (sin * distance);

            // ignore negligible differences
            if (xDiff < 0.00001 && xDiff > -0.00001)
            {
                xDiff = 0;
            }
            if (yDiff < 0.00001 && yDiff > -0.00001)
            {
                yDiff = 0;
            }

            x += xDiff;
            y -= yDiff;
        }
        else
        {
            double rotationModifier = 0.05;

            // slowly rotate
            double oldRotation = rotation;
            rotation += rotationModifier * deltaTime * rotationSpeed * rotationDirection;
            //System.out.println("Oldrotation: " + oldRotation + " new rotation: " + rotation + " target rotation: " + targetRotation);

            if (rotation > Math.PI * 2)
            {
                rotation = 0;
            }

            double epsilon = 0.1;
            if ((rotation + epsilon >= targetRotation && rotation - epsilon <= targetRotation) ||
            rotation + Math.PI * 2 + epsilon >= targetRotation && rotation + Math.PI * 2 - epsilon <= targetRotation)
            {
                // if the rotation is within a small margin of the target rotation just set the rotation to equal the targetrotation
                // Otherwise it won't ever exactly become the same with modifying it with deltaTime
                //System.out.println("Setting rotation to equal target rotation with rotation: " + rotation + " and targetrotation: " + targetRotation);
                rotation = targetRotation;
            }
        }
    }


    /**
     * Sets a new exact x,y destination
     * @param x
     * @param y
     */
    private void setNewDestination(int x, int y)
    {
        destination = new Point2D.Double(x, y);
        atDestination = false;
    }

    /**
     * Go to an exact x,y destination
     * Determines the required angle to get to that destination and sets that as the targetRotation
     * @param x
     * @param y
     */
    public void goToDestination(int x, int y)
    {
        setNewDestination(x, y);

        // determine the rotation angle and set it as the target rotation
        double xDiff = x - (this.x + width / 2);
        double yDiff = (this.y + height / 2) - y;
        double angle = Math.atan2(yDiff, xDiff);

        if (angle < 0)
        {
            // if it goes over 180 degrees it starts to count in the negative, so adjust for that
            angle += 2 * Math.PI;
        }

        setTargetRotation(angle);
    }

    /**
     * Draw the npc hitbox and sprite
     * Only draw the hitbox if the debug parameter is true
     * @param fxGraphics2D
     * @param debug
     */
    public void draw(FXGraphics2D fxGraphics2D, boolean debug)
    {
        //draws the sprite
        this.appearance.draw(fxGraphics2D, this.atDestination, this.x, this.y, this.person.getName());

        if (debug) {
            // draw the hitbox and exact destination
            fxGraphics2D.draw(new Rectangle2D.Double(x, y, width, height));

            if (destination != null)
            {
                fxGraphics2D.setColor(Color.RED);
                fxGraphics2D.draw(new Ellipse2D.Double(destination.getX(), destination.getY(), 1, 1));
                fxGraphics2D.setColor(Color.BLACK);
            }
        }
    }

    public Person getPerson()
    {
        return person;
    }

    /**
     * Set it so the NPC is not at any destination and doesn't have any destination stored
     */
    public void resetDestination() {
        atDestination = false;
        destination = null;
        onTargetTile = false;
        this.currentTile = null;
    }

    /**
     * Set the pathfinding to a new pathfinding object
     * @param pathfinding
     */
    public void setPathfinding(Pathfinding pathfinding)
    {
        if (this.currentPathfinding != null)
        {
            this.currentPathfinding.removeNpc(this);
        }
        this.currentPathfinding = pathfinding;
        this.currentPathfinding.addNpc(this);
        if (pathfinding.getDestinationTile() != null) {
            resetDestination();
        }
    }

    /**
     * Update the pathfinding check
     * looks in what direction the npc has to move according to what tile on the pathfinding field it is on
     * if the npc is in the destination tile it then starts to move to the exact point on the tile it needs to go to
     * @param deltaTime
     */
    private void pathfindingUpdate(double deltaTime)
    {
        PathfindingTile newTile = currentPathfinding.getTile((int) (y / currentPathfinding.getTileHeight()), (int) (x / currentPathfinding.getTileWidth()));
        if (newTile != null)
        {
            if (newTile == this.currentTile) {
                // if still on the same time only move
                rotationUpdate(deltaTime);
            }
            else if (newTile == currentPathfinding.getDestinationTile())
            {
                // if within the destination tile move towards the exact destination
                Point2D exactDestination = currentPathfinding.getExactDestination();
                onTargetTile = true;
                goToDestination((int) exactDestination.getX(), (int) exactDestination.getY());
            }
            else
            {
                this.currentTile = newTile;
                // if within a new tile, get new directions to the next tile
                Direction tileDirection = this.currentTile.getDirection();
                // set the target rotation to be the same as the current tile
                if (tileDirection == Direction.UP)
                {
                    setTargetRotation(Math.PI * 0.5);
                }
                else if (tileDirection == Direction.LEFT)
                {
                    setTargetRotation(Math.PI);
                }
                else if (tileDirection == Direction.RIGHT)
                {
                    setTargetRotation(0);
                }
                else if (tileDirection == Direction.DOWN)
                {
                    setTargetRotation(Math.PI * 1.5);
                }

                // update rotation,
                // rotate if not at the target rotation,
                // move forward in the rotation direction if at the proper target rotation
                rotationUpdate(deltaTime);
            }
        }
    }

    public boolean isAtDestination()
    {
        return atDestination;
    }

    public Point2D getCurrentLocation(){
        return new Point2D.Double(this.x, this.y);
    }

    public static String randomSprite(){
        ArrayList<String> imagePaths = new ArrayList<>();
        imagePaths.add("/NPC/NPC1 male.png");
        imagePaths.add("/NPC/NPC2 male.png");
        imagePaths.add("/NPC/NPC3 female.png");
        imagePaths.add("/NPC/NPC4 female.png");

        return imagePaths.get((int)(Math.random()*4));
    }


    //TODO temprotray for testing
    public static int yComponent(){
        yCompontent +=25;
        if (yCompontent > 650){
            yCompontent = 575;
        }
        return (int)yCompontent;
    }

    //TODO temprotray for testing
    public static int xComponent(){
        xComponent += 25;
        if (xComponent > 1400){
            xComponent = 1300;
        }
        return (int)xComponent;
    }

    /**
     * Set the targetrotation to a given angle
     * Also determines the rotation direction (leftwards or rightwards) to rotate in, always takes the rotation direction that has to rotate the least
     * @param angle
     */
    public void setTargetRotation(double angle)
    {
        // If the angle isn't within the bounds then adjust it untill it is
        while (!(angle >= 0 && angle < Math.PI * 2))
        {
            if (angle < 0)
            {
                angle += Math.PI * 2;
            }
            else if (angle >= Math.PI * 2)
            {
                angle -= Math.PI * 2;
            }
        }

        // so as to not have to calculate with negative numbers, add a full circle to both the current and target rotation
        double currentRotation = rotation + Math.PI * 2;
        double easierAngle = angle + Math.PI * 2;
        //System.out.println("setting rotation to: " + angle + " with current rotation at: " + rotation);

        // if the angle falls into the 180 degrees lower than the currentrotation, or if the angle falls outside of the 180 degrees higher than the currentrotation then rotate to the left
        // otherwise rotate to the right
        if ((easierAngle <= currentRotation && easierAngle > currentRotation - Math.PI) || (easierAngle >= currentRotation + Math.PI))
        {
            //System.out.println("Setting rotation direction to 1 with current: " + rotation + " and target: " + angle);
            //System.out.println("ROTATING TO THE LEFT");
            rotationDirection = -1;
        }
        else
        {
            //System.out.println("Setting rotation direction to -1 with current: " + rotation + " and target: " + angle);
            //System.out.println("ROTATING TO THE RIGHT");
            rotationDirection = 1;
        }

        targetRotation = angle;
    }

    public Pathfinding getCurrentPathfinding()
    {
        return currentPathfinding;
    }
}
