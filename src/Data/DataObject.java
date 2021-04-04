package Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Auteurs: Ewout
 * <p>
 * Deze code zorgt ervoor dat de inhoud van een schedule later opgeslagen kan worden en de nodige functies heeft die later nodig zijn
 */

public class DataObject implements Serializable
{
    private List<Lesson> lessons;
    private List<Teacher> teachers;
    private List<Group> groups;
    private List<Classroom> classrooms;

    public DataObject(ArrayList<Lesson> lessons, ArrayList<Teacher> teachers, ArrayList<Group> groups, ArrayList<Classroom> classrooms)
    {
        this.lessons = lessons;
        this.teachers = teachers;
        this.groups = groups;
        this.classrooms = classrooms;
    }

    public ArrayList<Lesson> getLessons()
    {
        return new ArrayList<>(lessons);
    }

    public ArrayList<Teacher> getTeachers()
    {
        return new ArrayList<>(teachers);
    }

    public ArrayList<Group> getGroups()
    {
        return new ArrayList<>(groups);
    }

    public ArrayList<Classroom> getClassrooms()
    {
        return new ArrayList<>(classrooms);
    }
}

