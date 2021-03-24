package Data;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Auteurs: Leon
 *
 * Deze code zorgt ervoor dat een klaslokaal aangemaakt kan worden en de nodige functies heeft die later nodig zijn
 *
 */


public class Classroom implements Serializable
{
    private ClassroomEntryPoint entry;

    @Override
    public String toString()
    {
        return classRooms.values()[classroom - 1].roomName;
    }

    private int classroom;

    public Classroom(int classroom, ClassroomEntryPoint entry)
    {
        this.classroom = classroom;
        this.entry = entry;
    }

    public int getClassroom()
    {
        return classroom;
    }

    public void setClassroom(int classroom)
    {
        this.classroom = classroom;
    }

    public ClassroomEntryPoint getEntry()
    {
        return entry;
    }

    // All possible classrooms to choose from
    public enum classRooms
    {
        classRoomOne("Lokaal 1"), classRoomTwo("Lokaal 2"), classRoomThree("Lokaal 3"), classRoomFour("Lokaal 4"), classRoomFive("Lokaal 5"), classRoomSix("Lokaal 6"), classRoomSeven("Lokaal 7"), classRoomEight("Lokaal 8");

        public final String roomName;

        classRooms(String roomName)
        {
            this.roomName = roomName;
        }
    }
}
