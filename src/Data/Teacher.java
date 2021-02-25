package Data;

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
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\nAge: " + this.age + "\nSubject: " + this.subject;
    }
}
