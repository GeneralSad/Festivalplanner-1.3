package Simulator.Room;

import Simulator.Maploading.Tile;
import Simulator.NPC.NPC;

public class Seat
{
    private Tile seat;
    private NPC student;

    public Seat(Tile seat, NPC student)
    {
        this.seat = seat;
        this.student = student;
    }
}
