package Data;

import java.io.Serializable;

/**
 * Auteurs: Leon
 * <p>
 * Deze code zorgt ervoor dat een student aangemaakt kan worden en de nodige functies heeft die later nodig zijn
 */

public class Student extends Person implements Serializable
{


    private Group group;

    public Student(String name, int age, Group group)
    {
        super(name, age);
        this.group = group;
        group.addStudent(this);
    }

    public Group getGroup()
    {
        return group;
    }

    public String toDetailString()
    {
        return "Naam: " + super.name + "\nLeeftijd: " + super.age;
    }
}
