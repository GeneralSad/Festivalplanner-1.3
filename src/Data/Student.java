package Data;

import java.io.Serializable;

/**
 * Auteurs: Leon
 *
 * Deze code zorgt ervoor dat een student aangemaakt kan worden en de nodige functies heeft die later nodig zijn
 *
 */

public class Student implements Serializable
{

    private String name;
    private int age;
    private Group group;

    public Student(String name, int age, Group group)
    {
        this.name = name;
        this.age = age;
        this.group = group;
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

    @Override
    public String toString()
    {
        return this.name;
    }

    public String toDetailString()
    {
        return "Naam: " + this.name + "\nLeeftijd: " + this.age;
    }

}
