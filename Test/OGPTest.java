import Data.*;
import GUI.ErrorWindow;
import Simulator.NPC.NPC;
import Simulator.Simulator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.time.LocalTime;
import java.util.ArrayList;

public class OGPTest
{

    @Test
    void testDataStorage() {
        // Arrange
        ArrayList<Group> groups = new ArrayList<>();
        ArrayList<Lesson> lessons = new ArrayList<>();
        ArrayList<Teacher> teachers = new ArrayList<>();
        ArrayList<Classroom> classrooms = new ArrayList<>();

        groups.add(new Group("C2"));
        groups.add(new Group("C3"));
        groups.add(new Group("C4"));

        Classroom classroom = new Classroom(1, new ClassroomEntryPoint(550, 550));
        classrooms.add(classroom);

        Teacher teacher = new Teacher("Johan", 35, "OGP");
        teachers.add(teacher);

        lessons.add(new Lesson(LocalTime.of(12, 0), LocalTime.of(13, 0), teacher, classroom, groups));

        Schedule schedule = new Schedule(lessons, teachers, groups, classrooms);

        //Act
        DataStorage dataStorage = new DataStorage();
        dataStorage.saveSchedule("SaveTest.dat", schedule);
        Schedule scheduleOutput = DataStorage.loadSchedule("SaveTest.dat");

        // Assert
        Assertions.assertTrue(schedule.equals(scheduleOutput), "Loading and saving didn't work");

    }

    @Test
    void testGetAvailability() {
        // Arrange
        ArrayList<Group> groups = new ArrayList<>();
        ArrayList<Lesson> lessons = new ArrayList<>();
        ArrayList<Teacher> teachers = new ArrayList<>();
        ArrayList<Classroom> classrooms = new ArrayList<>();
        ArrayList<NPC> npcs = new ArrayList<>();

        groups.add(new Group("C2"));
        groups.add(new Group("C3"));
        groups.add(new Group("C4"));

        Student student1 = new Student("Q", 12, groups.get(0));
        groups.get(0).addStudent(student1);
        NPC npc1 = new NPC(student1, 1300, 450);
        npcs.add(npc1);
        Student student2 = new Student("E", 34, groups.get(0));
        groups.get(0).addStudent(student2);
        NPC npc2 = new NPC(student1, 1300, 450);
        npcs.add(npc2);
        Student student3 = new Student("Z", 56, groups.get(0));
        groups.get(0).addStudent(student3);
        NPC npc3 = new NPC(student1, 1300, 450);
        npcs.add(npc3);
        Student student4 = new Student("C", 78, groups.get(0));
        groups.get(0).addStudent(student4);
        NPC npc4 = new NPC(student1, 1300, 450);
        npcs.add(npc4);

        Classroom classroom = new Classroom(1, new ClassroomEntryPoint(550, 550));
        classrooms.add(classroom);

        Teacher teacher = new Teacher("Johan", 35, "OGP");
        teachers.add(teacher);

        lessons.add(new Lesson(LocalTime.of(12, 0), LocalTime.of(13, 0), teacher, classroom, groups));

        Schedule schedule = new Schedule(lessons, teachers, groups, classrooms);

        Simulator simulator = new Simulator(schedule);

        ArrayList<Point2D> currentLocations = new ArrayList<>();
        ArrayList<Point2D> availableLocations = new ArrayList<>();
        ArrayList<Point2D> returnedLocations = new ArrayList<>();

        // Act
        for (NPC npc : npcs) {
            Point2D location = npc.getCurrentLocation();
            currentLocations.add(location);
            availableLocations.add(simulator.getAvailability(location));
        }

        returnedLocations.add(simulator.getFullAvailability(currentLocations, availableLocations));
        returnedLocations.add(simulator.getFullAvailability(currentLocations, availableLocations));
        returnedLocations.add(simulator.getFullAvailability(currentLocations, availableLocations));
        returnedLocations.add(simulator.getFullAvailability(currentLocations, availableLocations));

        // Assert
        for (int i = 0; i < returnedLocations.size(); i++)
        {

            if (i + 1 < returnedLocations.size()) {

                Assertions.assertEquals(16, returnedLocations.get(i).distance(returnedLocations.get(i + 1)), "The distance between points was not 16");

            }

        }

    }

}
