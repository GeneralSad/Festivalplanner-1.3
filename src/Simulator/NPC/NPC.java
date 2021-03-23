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
        this(person, x, y, xSpeed, ySpeed, width, height, 0, 10, 20, imageLocation);
    }
    
    public NPC(Person person)
    {
        this(person, 200, 100, 10, 0, 10, 10, "/NPC/NPC1 male.png");
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

        targetRotation = angle;
    }

    /**
     * Draw the npc hitbox and sprite
     * Only draw the hitbox if the debug parameter is true
     * @param fxGraphics2D
     * @param debug
     */
    public void draw(FXGraphics2D fxGraphics2D, boolean debug)
    {
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

        //draws the sprite
        this.appearance.draw(fxGraphics2D, this.atDestination, this.x, this.y, this.person.getName());
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
        Point2D exactDestination = currentPathfinding.getExactDestination();
        PathfindingTile currentTile = currentPathfinding.getTile((int) (y / currentPathfinding.getTileHeight()), (int) (x / currentPathfinding.getTileWidth()));
        if (currentTile != null)
        {
            if (currentTile == currentPathfinding.getDestinationTile())
            {
                onTargetTile = true;
                goToDestination((int) exactDestination.getX(), (int) exactDestination.getY());
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
}
