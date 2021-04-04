package Simulator.LocationSystem;

import GUI.GUI;
import Simulator.NPC.NPC;
import Simulator.Pathfinding.Pathfinding;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ClassRoomBehavior
{
    public ArrayList<Seat> seats;
    public Seat teacherSeat;
    private Point2D entry;
    private ArrayList<NPC> handeld = new ArrayList<>();
    private int ofSett;

    public ClassRoomBehavior(ArrayList<Seat> seats, Seat teacherSeat, Point2D entry, int ofSett)
    {
        this.seats = seats;
        this.teacherSeat = teacherSeat;
        this.entry = entry;
        this.ofSett = ofSett;
    }

    public void ScriptedStudentStart(NPC student)
    {
        if (!handeld.contains(student))
        {
            try
            {
                Seat selectedSeat = claimEmptySeat(student);
                student.appearance.setSitting(true, selectedSeat.getOrientation());
                student.appearance.setOfset(ofSett);
                student.resetDestination();
                student.getCurrentPathfinding().setDestination((int) selectedSeat.getSeat().getX(), (int) selectedSeat.getSeat().getY());
                handeld.add(student);
            }
            catch (IllegalArgumentException e)
            {
            }
        }
    }

    public void ScriptedTeacherStart(NPC teacher)
    {
        teacher.appearance.setSitting(true, teacherSeat.getOrientation());
        teacher.resetDestination();
        teacher.getCurrentPathfinding().setDestination((int) teacherSeat.getSeat().getX(), (int) teacherSeat.getSeat().getY());
    }

    public Seat claimEmptySeat(NPC student) throws IllegalArgumentException
    {
        Seat seat = getFurthestSeat(student.getCurrentLocation());
        if (seat != null)
        {
            seat.setStudent(student);
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
            if (s.isEmpty() && s != teacherSeat)
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

    public void leaveFilledSeat(NPC student)
    {

        for (Seat s : seats)
        {
            if (s.getStudent() == student)
            {
                handeld.remove(student);
                s.setStudent(null);
                return;
            }
        }
    }

    public Point2D getEntry()
    {
        return entry;
    }

    public int getOfSett()
    {
        return ofSett;
    }
}
