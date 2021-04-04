package Simulator.LocationSystem;

import Simulator.NPC.NPC;
import Simulator.Pathfinding.Pathfinding;
import Simulator.Simulator;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class AuditoriumBehavior
{
    private ArrayList<Seat> seats;
    public static Point2D entry;
    private ArrayList<NPC> handeld = new ArrayList<>();

    public AuditoriumBehavior(ArrayList<Seat> seats, Point2D entry)
    {
        this.seats = seats;
        this.entry = entry;
    }

    public void ScriptedStart(NPC person)
    {
        if (!handeld.contains(person))
        {
            Seat selectedSeat = claimEmptySeat(person);
            person.appearance.setSitting(true, selectedSeat.getOrientation());
            person.resetDestination();
            person.getCurrentPathfinding().setDestination((int) selectedSeat.getSeat().getX(), (int) selectedSeat.getSeat().getY());
            handeld.add(person);
        }
    }

    public Seat claimEmptySeat(NPC person) throws IllegalArgumentException
    {
        Seat seat = getFurthestSeat(person.getCurrentLocation());
        if (seat != null)
        {
            seat.setStudent(person);
            return seat;
        }
        throw new IllegalArgumentException("Not enough seats! Total seats is: " + seats.size());
    }

    public Seat getFurthestSeat(Point2D from)
    {
        Seat furthest = null;
        double furthestDistanceSoFar = 0;
        for (Seat s : seats)
        {
            if (s.isEmpty())
            {
                double distance = from.distance(s.getSeat());
                if (distance > furthestDistanceSoFar)
                {
                    furthestDistanceSoFar = distance;
                    furthest = s;
                }
            }
        }
        return furthest;
    }

    public void leaveFilledSeat(NPC person)
    {
        for (Seat s : seats)
        {
            if (s.getStudent() == person)
            {
                handeld.remove(person);
                s.setStudent(null);
                return;
            }
        }
    }

    public Point2D getEntry()
    {
        return entry;
    }
}
