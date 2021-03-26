import Data.*;
import GUI.ErrorWindow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;

public class DataTest
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
        if (schedule.equals(scheduleOutput)) {
            Assertions.assertTrue(true);
        } else {
            Assertions.fail("Loading and saving didnt work");
        }

    }

    @Test
    void test() {
        // Arrange

        // Act


        // Assert

    }

}
