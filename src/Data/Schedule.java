package Data;

import GUI.GUI;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Schedule implements Serializable
{

    private ArrayList<Lesson> lessons;
    private ArrayList<Teacher> teachers;
    private ArrayList<Group> groups;

    public Schedule()
    {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public Schedule(ArrayList<Lesson> lessons, ArrayList<Teacher> teachers, ArrayList<Group> groups)
    {
        this.lessons = lessons;
        this.teachers = teachers;
        this.groups = groups;
    }

    public void addTeacher(Teacher teacher)
    {
        teachers.add(teacher);
        GUI.mainWindowController.update();
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
        GUI.mainWindowController.update();
    }

    public void addGroup(Group group)
    {
        groups.add(group);
        GUI.mainWindowController.update();
    }

    public void removeTeacher(Teacher teacher)
    {
        teachers.remove(teacher);
        GUI.mainWindowController.update();
    }

    public void removeLesson(Lesson lesson)
    {
        lessons.remove(lesson);
        GUI.mainWindowController.update();
    }

    public void removeGroup(Group group)
    {
        groups.remove(group);
        GUI.mainWindowController.update();
    }

    public ArrayList<Lesson> getLessons()
    {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons)
    {
        this.lessons = lessons;
        GUI.mainWindowController.update();
    }

    public ArrayList<Teacher> getTeachers()
    {
        return teachers;
    }

    public void setTeachers(ArrayList<Teacher> teachers)
    {
        this.teachers = teachers;
        GUI.mainWindowController.update();
    }

    public ArrayList<Group> getGroups()
    {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups)
    {
        this.groups = groups;
        GUI.mainWindowController.update();
    }
}
