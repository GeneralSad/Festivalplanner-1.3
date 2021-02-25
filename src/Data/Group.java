package Data;

import GUI.GUI;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable
{
    private String className;
    private ArrayList<Student> students;

    public Group(String className)
    {
        this.className = className;
    }

    public void addStudent(Student student) {
        students.add(student);
        GUI.mainWindowController.update();
    }

    public void removeStudent(Student student) {
        students.remove(student);
        GUI.mainWindowController.update();
    }

    public String getGroupName()
    {
        return className;
    }

    public void setGroupName(String className)
    {
        this.className = className;
        GUI.mainWindowController.update();
    }

    public ArrayList<Student> getStudents()
    {
        return students;
    }

    public void setStudents(ArrayList<Student> students)
    {
        this.students = students;
        GUI.mainWindowController.update();
    }

    @Override
    public String toString()
    {
        return this.className;
    }
}
