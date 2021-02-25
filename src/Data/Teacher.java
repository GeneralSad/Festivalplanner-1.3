package Data;

import GUI.GUI;

import java.io.Serializable;

public class Teacher implements Serializable
{

    private String name;
    private int age;
    private String subject;

    public Teacher(String name, int age, String subject)
    {
        this.name = name;
        this.age = age;
        this.subject = subject;
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

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
        GUI.mainWindowController.update();
    }
}
