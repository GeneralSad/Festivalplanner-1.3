package Data;

import java.io.Serializable;

/**
 * Auteurs: Leon
 * <p>
 * Deze code zorgt ervoor dat een leraar aangemaakt kan worden en de nodige functies heeft die later nodig zijn
 */

public class Teacher extends Person implements Serializable
{

    private String subject;

    public Teacher(String name, int age, String subject)
    {
        super(name, age);
        this.subject = subject;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String toDetailString()
    {
        return "Naam: " + super.name + "\nLeeftijd: " + super.age + "\nVak: " + this.subject;
    }

}
