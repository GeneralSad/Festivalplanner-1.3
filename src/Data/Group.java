package Data;

import java.util.ArrayList;

public class Group
{

    private String className;
    private ArrayList<Student> students;

    public Group(String className)
    {
        this.className = className;
    }

    public void addStudent(Student student) {
        students.add(student);
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
}
