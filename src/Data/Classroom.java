package Data;

import java.io.Serializable;

public class Classroom implements Serializable
{
    // All possible classrooms to choose from
    public enum classRooms {
        classRoomOne ("Lokaal 1"),
        classRoomTwo ("Lokaal 2"),
        classRoomThree ("Lokaal 3"),
        classRoomFour ("Lokaal 4"),
        classRoomFive ("Lokaal 5"),
        classRoomSix ("Lokaal 6"),
        classRoomSeven ("Lokaal 7"),
        classRoomEight ("Lokaal 8");

        public final String roomName;
        classRooms(String roomName)
        {
            this.roomName = roomName;
        }
    }

    private int classroom;

    public Classroom(int classroom)
    {
        this.classroom = classroom;
    }

    public int getClassroom()
    {
        return classroom;
    }

    public void setClassroom(int classroom)
    {
        this.classroom = classroom;
    }

    @Override
    public String toString()
    {
        return classRooms.values()[classroom-1].roomName;
    }
}
