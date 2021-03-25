package Simulator.LocationSystem;

import Simulator.NPC.NPC;
import Simulator.Pathfinding.Pathfinding;
import Simulator.Simulator;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class AuditoriumBehavior
{
    private ArrayList<Seat> seats;
    private Point2D entry;

    public AuditoriumBehavior(ArrayList<Seat> seats, Point2D entry)
    {
        this.seats = seats;
        this.entry = entry;
    }

    public void ScriptedStudentStart(NPC student){
        Point2D selectedSeat = claimEmptySeat(student);
        Pathfinding pathfinding = new Pathfinding(Simulator.getTiledmap());
        student.setPathfinding(pathfinding);
        pathfinding.addNpc(student);
        pathfinding.setDestination((int)selectedSeat.getX(), (int)selectedSeat.getY());

    }

    public void ScriptedStudentEnd(NPC student){
        leaveFilledSeat(student);
        Pathfinding pathfinding = new Pathfinding(Simulator.getTiledmap());
        student.setPathfinding(pathfinding);
        pathfinding.addNpc(student);
        pathfinding.setDestination((int)entry.getX(), (int)entry.getY());
    }

    private Point2D claimEmptySeat(NPC student) throws IllegalArgumentException{
        for (Seat s : seats)
        {
            if (s.isEmpty()){
                s.setStudent(student);
                return s.getSeat();
            }
        }
        throw new IllegalArgumentException("Not enough seats!");
    }

    private void leaveFilledSeat(NPC student) throws IllegalArgumentException{
        for (Seat s : seats)
        {
            if (s.getStudent() == student){
                s.setStudent(null);
                return;
            }
        }
        throw new IllegalArgumentException("Student not seated!");
    }
}
