package Data;

import java.io.Serializable;

public class Classroom implements Serializable
{

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
}
