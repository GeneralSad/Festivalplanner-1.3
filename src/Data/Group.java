package Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Auteurs: Leon
 * <p>
 * Deze code zorgt ervoor dat een klas aangemaakt kan worden en de nodige functies heeft die later nodig zijn
 */

public class Group implements Serializable
{
    private String className;
    private ArrayList<Student> students;
    private Classroom classroom;


    public Group(String className)
    {
        this.className = className;
        this.students = new ArrayList<>();

    }

    public boolean addStudent(Student student)
    {
        if (!students.contains(student))
        {
            students.add(student);
            return true;
        }
        return false;
    }

    public void removeStudent(Student student)
    {
        students.remove(student);
    }

    public String getGroupName()
    {
        return className;
    }


    public void setGroupName(String className)
    {
        this.className = className;
    }

    public ArrayList<Student> getStudents()
    {
        return students;
    }

    public void setStudents(ArrayList<Student> students)
    {
        this.students = students;
    }

    @Override
    public String toString()
    {
        return this.className;
    }

    public Classroom getClassroom()
    {
        return classroom;
    }

    public void setClassroom(Classroom classroom)
    {
        this.classroom = classroom;
    }
}
