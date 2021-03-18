package Simulator.NPC;

import Data.Person;
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
    private double xSpeed;
    private double ySpeed;
    private int width;
    private int height;
    private Point2D destination;
    // rotation from 0 to 2 * Math.PI
    private double rotation;

    private int speed;
    private double targetRotation;
    private int rotationSpeed;

    private boolean atDestination;
    public NPCSprites appearance;

    private Pathfinding currentPathfinding;
    private boolean onTargetTile;

    public NPC(Person person, double x, double y, double xSpeed, double ySpeed, int width, int height, int rotation, int speed, int rotationSpeed, String npcAppearance)
    {
        this.person = person;
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.speed = speed;
        this.targetRotation = rotation;
        this.rotationSpeed = rotationSpeed;
        this.atDestination = true;
        this.appearance = new NPCSprites(npcAppearance);
    }

    public NPC(Person person, double x, double y, double xSpeed, double ySpeed, int width, int height, String imageLocation)
    {
        this(person, x, y, xSpeed, ySpeed, width, height, 0, 100, 100, imageLocation);
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
            // Either do the simple x/y update or a rotation update
            //                    xyUpdate(deltaTime);
            //            rotationUpdate(deltaTime);
            // pathfinding movement
            // don't update pathfinding movement if already on the target tile
            if (currentPathfinding.getDestinationTile() != null && !onTargetTile)
            {
                pathfindingUpdate(deltaTime);
            } else {
                System.out.println("Moving without pathfinding");
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
            this.appearance.frameUpdater(x, y);
            this.appearance.directionUpdater(rotation);
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
            System.out.println("Reached destination");
        }
        if (currentPathfinding != null)
        {
            PathfindingTile destination = currentPathfinding.getDestinationTile();
            if (destination != null && currentHitBox.contains(destination.getMiddlePoint()))
            {
                atDestination = true;
            }
        }
    }

    /**
     * Simple xy update for position
     *
     * @param deltaTime
     */
    private void xyUpdate(double deltaTime)
    {
        // Position update
        this.x += deltaTime * xSpeed;
        this.y += deltaTime * ySpeed;
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
        if (rotation == targetRotation)
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
            double rotationModifier = 0.1;

            // slowly rotate
            rotation += rotationModifier * deltaTime * rotationSpeed;

            //            System.out.println("New rotation: " + rotation);

            if (rotation > Math.PI * 2)
            {
                rotation = 0;
            }

            double epsilon = 0.1;
            if (rotation + epsilon >= targetRotation && rotation - epsilon <= targetRotation)
            {
                // if the rotation is within a small margin of the target rotation just set the rotation to equal the targetrotation
                // Otherwise it won't ever exactly become the same with modifying it with deltaTime
                rotation = targetRotation;
            }
        }
    }

    public void setTargetRotation(double targetRotation)
    {
        // negative values aren't allowed, as those won't ever be reached
        this.targetRotation = Math.abs(targetRotation);
    }

    /**
     * Go to a target destination based on simple x and y position
     *
     * @param x
     * @param y
     */
    public void goToDestinationXY(int x, int y)
    {
        setNewDestination(x, y);
        // calculate the total distance from a point and set the x and y speed to be a part of 10 based on how much of the total distance is on the x or y
        double totalDistance = Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
        this.xSpeed = ((x - this.x) / totalDistance * 10);
        this.ySpeed = ((y - this.y) / totalDistance * 10);
    }

    private void setNewDestination(int x, int y)
    {
        destination = new Point2D.Double(x, y);
        System.out.println("setting destination: " + x + " " + y);
        atDestination = false;
    }

    public void goToDestinationRotational(int x, int y)
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

        targetRotation = angle;
    }

    public void draw(FXGraphics2D fxGraphics2D)
    {
        fxGraphics2D.draw(new Rectangle2D.Double(x, y, width, height));
        // Draw the destination as a small dot

        if (destination != null)
        {
            fxGraphics2D.setColor(Color.RED);
            fxGraphics2D.draw(new Ellipse2D.Double(destination.getX(), destination.getY(), 1, 1));
            fxGraphics2D.setColor(Color.BLACK);
        }

        this.appearance.draw(fxGraphics2D, this.atDestination, this.x, this.y, this.rotation);
    }

    public Person getPerson()
    {
        return person;
    }

    public void resetDestination() {
        atDestination = false;
        destination = null;
        onTargetTile = false;
    }

    public void setPathfinding(Pathfinding pathfinding)
    {
        if (this.currentPathfinding != null)
        {
            this.currentPathfinding.removeNpc(this);
        }
        this.currentPathfinding = pathfinding;
        this.currentPathfinding.addNpc(this);
        resetDestination();
    }

    private void pathfindingUpdate(double deltaTime)
    {
        PathfindingTile currentTile = currentPathfinding.getTile((int) (y / currentPathfinding.getTileHeight()), (int) (x / currentPathfinding.getTileWidth()));
        if (currentTile != null)
        {
            if (currentTile == currentPathfinding.getDestinationTile())
            {
                onTargetTile = true;
                goToDestinationRotational((int) currentTile.getExactDestination().getX(), (int) currentTile.getExactDestination().getY());
            }
            else
            {
                Direction tileDirection = currentTile.getDirection();
                // set the target rotation to be the same as the current tile
                if (tileDirection == Direction.UP)
                {
                    targetRotation = Math.PI / 2;
                }
                else if (tileDirection == Direction.LEFT)
                {
                    targetRotation = Math.PI;
                }
                else if (tileDirection == Direction.RIGHT)
                {
                    targetRotation = 0;
                }
                else if (tileDirection == Direction.DOWN)
                {
                    targetRotation = 1.5 * Math.PI;
                }

                // update rotation,
                // rotate if not at the target rotation,
                // move forward in the rotation direction if at the proper target rotation
                rotationUpdate(deltaTime);
            }
        }
    }

    public void setAtDestination(boolean atDestination)
    {
        this.atDestination = atDestination;
    }
}
