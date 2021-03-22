package Simulator.Room;

import Simulator.Maploading.Tile;
import Simulator.NPC.NPC;

import java.awt.geom.Point2D;

public class Seat
{
    private Point2D seat;
    private NPC student;

    public Seat(Point2D seat, NPC student)
    {
        this.seat = seat;
        this.student = student;
    }

    public boolean isEmpte(){
        if (student == null){
            return true;
        }
        return false;
    }

    public Point2D getSeat()
    {
        return seat;
    }

    public void setSeat(Point2D seat)
    {
        this.seat = seat;
    }

    public NPC getStudent()
    {
        return student;
    }

    public void setStudent(NPC student)
    {
        this.student = student;
    }
}
