package Simulator.LocationSystem;

import GUI.GUI;
import Simulator.NPC.NPC;
import Simulator.Pathfinding.Pathfinding;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ClassRoomBehavior
{
    private ArrayList<Seat> seats;
    public Seat teacherSeat;
    private Point2D entry;
    private ArrayList<NPC> handeld = new ArrayList<>();

    public ClassRoomBehavior(ArrayList<Seat> seats, Seat teacherSeat, Point2D entry)
    {
        this.seats = seats;
        this.teacherSeat = teacherSeat;
        this.entry = entry;
    }

    public void ScriptedStudentStart(NPC student)
    {
        if (!handeld.contains(student))
        {
            Seat selectedSeat = claimEmptySeat(student);
            student.appearance.setSitting(true, selectedSeat.getOrientation());
            student.resetDestination();
            student.getCurrentPathfinding().setDestination((int) selectedSeat.getSeat().getX(), (int) selectedSeat.getSeat().getY());
            handeld.add(student);
        }


    }

    public void ScriptedTeacherStart(NPC teacher)
    {

    }

    public void ScriptedStudentEnd(NPC student)
    {
        student.appearance.setSitting(false, leaveFilledSeat(student).getOrientation());

        Pathfinding pathfinding = new Pathfinding(GUI.getTiledmap());
        student.setPathfinding(pathfinding);
        pathfinding.addNpc(student);
        pathfinding.setDestination((int) entry.getX(), (int) entry.getY());
    }

    public void ScriptedTeacherEnd(NPC teacher)
    {

    }

    public Seat claimEmptySeat(NPC student) throws IllegalArgumentException
    {
        for (Seat s : seats)
        {

            if (s.isEmpty())
            {
                s.setStudent(student);
                return s;
            }
        }

        throw new IllegalArgumentException("Not enough seats! Total seats is: " + seats.size());
    }

    public Seat leaveFilledSeat(NPC student)
    {
        for (Seat s : seats)
        {
            if (s.getStudent() == student)
            {
                s.setStudent(null);
                return s;
            }
        }
        throw new IllegalArgumentException("Student not seated!");
    }

    public Point2D getEntry()
    {
        return entry;
    }
}
