package Data;

import GUI.GUI;

import java.io.Serializable;

public class Student implements Serializable
{

    private String name;
    private int age;

    public Student(String name, int age, Group group)
    {
        this.name = name;
        this.age = age;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        GUI.mainWindowController.update();
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
        GUI.mainWindowController.update();
    }

}
