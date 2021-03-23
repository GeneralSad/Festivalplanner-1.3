package Simulator.LocationSystem;

import Simulator.NPC.NPC;
import Simulator.Pathfinding.Pathfinding;
import Simulator.Simulator;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ClassRoom
{
    private ArrayList<Seat> seats;
    private Point2D entry;

    public ClassRoom(ArrayList<Seat> seats, Point2D entry)
    {
        this.seats = seats;
        this.entry = entry;
    }

    public void ScriptedStudentStart(NPC student){
        Seat selectedSeat = claimEmptySeat(student);
        student.appearance.setSitting(true, selectedSeat.getOrientation());

        Pathfinding pathfinding = new Pathfinding(Simulator.getTiledmap());
        student.setPathfinding(pathfinding);
        pathfinding.addNpc(student);
        pathfinding.setDestination((int)selectedSeat.getSeat().getX(), (int)selectedSeat.getSeat().getY());

    }

    public void ScriptedTeacherStart(NPC teacher){

    }

    public void ScriptedStudentEnd(NPC student){
        student.appearance.setSitting(false, leaveFilledSeat(student).getOrientation());

        Pathfinding pathfinding = new Pathfinding(Simulator.getTiledmap());
        student.setPathfinding(pathfinding);
        pathfinding.addNpc(student);
        pathfinding.setDestination((int)entry.getX(), (int)entry.getY());
    }

    public void ScriptedTeacherEnd(NPC teacher){

    }

    public Seat claimEmptySeat(NPC student) throws IllegalArgumentException{
        for (Seat s : seats)
        {
            if (s.isEmpte()){
                s.setStudent(student);
                return s;
            }
        }
        throw new IllegalArgumentException("Not enough seats!");
    }

    public Seat leaveFilledSeat(NPC student){
        for (Seat s : seats)
        {
            if (s.getStudent() == student){
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
