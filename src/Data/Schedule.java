package Data;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Schedule implements Serializable
{


    private ArrayList<Lesson> lessons;
    private ArrayList<Teacher> teachers;
    private ArrayList<Group> groups;
    private ArrayList<Classroom> classrooms;

    public Schedule()
    {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),new ArrayList<>());
    }



    public Schedule(ArrayList<Lesson> lessons, ArrayList<Teacher> teachers, ArrayList<Group> groups, ArrayList<Classroom> classrooms)
    {
        this.lessons = lessons;
        this.teachers = teachers;
        this.groups = groups;
        this.classrooms = classrooms;
    }

    public void addTeacher(Teacher teacher)
    {
        teachers.add(teacher);
    }

    /**
     * Try to add a lesson, if conflicts exists with existing lessons then an exception is thrown with text as to why
     * @param lesson lesson to try to add
     * @throws IllegalArgumentException exeption thrown in cases of conflicts
     */
    public void addLesson(Lesson lesson) throws IllegalArgumentException
    {
        // First check if there are no conflicts with other lessons
        LocalTime beginTime = lesson.getBeginTime();
        LocalTime endTime = lesson.getEndTime();

        for (Lesson existingLesson : this.lessons) {
            LocalTime tempBeginTime = existingLesson.getBeginTime();
            LocalTime tempEndTime = existingLesson.getEndTime();

            // Time overlap with an existing lesson
            if ((beginTime.equals(tempBeginTime) && endTime.equals(tempEndTime)) ||
                    (endTime.isBefore(tempEndTime) && endTime.equals(tempBeginTime)) ||
                    (beginTime.isAfter(tempBeginTime) && beginTime.isBefore(tempEndTime))) {

                // Check for teacher overlap
                if (lesson.getTeacher().equals(existingLesson.getTeacher())) {
                    throw new IllegalArgumentException("The teacher already has a lesson at that moment!");
                }

                // Check for classroom overlap
                if (lesson.getClassroom().equals(existingLesson.getClassroom())) {
                    throw new IllegalArgumentException("That classroom is already preoccupied at that moment!");
                }

                // Check for group overlap
                ArrayList<Group> newLessonGroups = lesson.getGroups();
                for (Group group : existingLesson.getGroups()) {
                    if (newLessonGroups.contains(group)) {
                        throw new IllegalArgumentException("The group " + group + " already has a different lesson at that moment!");
                    }
                }
            }
        }

        // if an exception was thrown then the method stops so the following statement isn't reached
        lessons.add(lesson);
    }

    public void addGroup(Group group)
    {
        groups.add(group);
    }

    public void removeTeacher(Teacher teacher)
    {
        teachers.remove(teacher);
    }

    public void removeLesson(Lesson lesson)
    {
        lessons.remove(lesson);
    }

    public void removeGroup(Group group)
    {
        groups.remove(group);
    }

    public ArrayList<Lesson> getLessons()
    {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons)
    {
        this.lessons = lessons;
    }

    public ArrayList<Teacher> getTeachers()
    {
        return teachers;
    }

    public void setTeachers(ArrayList<Teacher> teachers)
    {
        this.teachers = teachers;
    }

    public ArrayList<Group> getGroups()
    {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups)
    {
        this.groups = groups;
    }


    public ArrayList<Classroom> getClassrooms()
    {
        return classrooms;
    }


    public ArrayList<LocalTime> getLocalTimes()
    {
        ArrayList<LocalTime> localTimes = new ArrayList<>();
        for (int i = 9; i < 18; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                localTimes.add(LocalTime.of(i,j*30));
            }
        }

        return localTimes;
    }
}
