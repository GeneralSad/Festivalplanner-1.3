package Simulator.Room;

import Simulator.Maploading.Tile;
import Simulator.NPC.NPC;

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
        Point2D selectedSeat = getEmptySeat(student);
        student.goToDestination((int)selectedSeat.getX(), (int)selectedSeat.getY());
        if (student.isAtDestination()){
        }
    }

    public void ScriptedTeacherStart(NPC teacher){

    }

    public void ScriptedStudentEnd(NPC student){

    }

    public void ScriptedTeacherEnd(NPC teacher){

    }

    public Point2D getEmptySeat(NPC student) throws IllegalArgumentException{
        for (Seat s : seats)
        {
            if (s.isEmpte()){
                s.setStudent(student);
                return s.getSeat();
            }
        }
        throw new IllegalArgumentException("Not enough seats!");
    }




}
