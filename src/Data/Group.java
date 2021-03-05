package Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Auteurs: Leon
 *
 * Deze code zorgt ervoor dat een klas aangemaakt kan worden en de nodige functies heeft die later nodig zijn
 *
 */

public class Group implements Serializable
{
    private String className;
    private ArrayList<Student> students;


    public Group(String className)
    {
        this.className = className;
        this.students = new ArrayList<>();

    }

    public void addStudent(Student student)
    {
        students.add(student);
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
}
