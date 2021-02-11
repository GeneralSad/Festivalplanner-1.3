package Data;

import java.util.ArrayList;

public class Schedule
{

    private ArrayList<Lesson> lessons;
    private ArrayList<Teacher> teachers;
    private ArrayList<Group> groups;

    public Schedule(ArrayList<Lesson> lessons, ArrayList<Teacher> teachers, ArrayList<Group> groups)
    {
        this.lessons = lessons;
        this.teachers = teachers;
        this.groups = groups;
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public void addLesson(Lesson lesson){
        lessons.add(lesson);
    }

    public void addGroup (Group group) {
        groups.add(group);
    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
    }

    public void removeLesson(Lesson lesson){
        lessons.remove(lesson);
    }

    public void removeGroup (Group group) {
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
}
