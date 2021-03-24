import Data.*;

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Auteurs: Leon, Martijn, Luuk, Ewout, Eddy
 *
 * Deze klasse is het begin van ons project, de code hier wordt niet gebruikt
 *
 */

public class Main
{

    public static void main(String[] args)
    {
        System.out.println("Martijn has entered the chat");
        System.out.println("Luuk dfjkdfjsd");
        System.out.println("ook hallo, van ewout");
        System.out.println("Kusjes van Leon");

        LocalTime beginTime = LocalTime.of(10, 30);
        LocalTime endTime = LocalTime.of(17, 30);
        Teacher teacher = new Teacher("Johan", 32, "OGP");
       // Classroom classroom = new Classroom(5);

        ArrayList<Group> groups = new ArrayList<>();
        Group group = new Group("A1");
        groups.add(group);
        groups.add(new Group("A2"));

        Student student = new Student("Adam", 18, group);

        //Lesson lesson = new Lesson(beginTime, endTime, teacher, classroom, groups);

        //System.out.println(lesson);

    }
}