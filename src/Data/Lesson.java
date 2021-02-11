package Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Lesson
{

    private LocalTime beginTime;
    private LocalTime endTime;
    private Teacher teacher;
    private Classroom classroom;
    private ArrayList<Group> groups;

    public Lesson(LocalTime beginTime, LocalTime endTime, Teacher teacher, Classroom classroom, ArrayList<Group> groups)
    {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.teacher = teacher;
        this.classroom = classroom;
        this.groups = groups;
    }

    public Lesson(LocalTime beginTime, LocalTime endTime, Teacher teacher, Classroom classroom, Group className)
    {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.teacher = teacher;
        this.classroom = classroom;
        this.groups = new ArrayList<>();
        addGroup(className);
    }

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    public String getFormatBeginTime() {
        return formatTimeString(this.beginTime);
    }

    public String getFormatEndTime() {
        return formatTimeString(this.endTime);
    }

    private String formatTimeString(LocalTime localTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        return localTime.format(dtf);
    }

    @Override
    public String toString()
    {
        return "Lesson: " + this.teacher.getSubject() + "\nBegins: " + beginTime + "\nEnds: " + endTime + "\nTeacher: "
                + teacher.getName() + "\nClassroom: " + classroom.getClassroom() + "\nGroups: " + getGroupNames(groups);
    }

    public LocalTime getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(LocalTime beginTime)
    {
        this.beginTime = beginTime;
    }

    public LocalTime getEndTime()
    {
        return endTime;
    }

    public void setEndTime(LocalTime endTime)
    {
        this.endTime = endTime;
    }

    public Teacher getTeacher()
    {
        return teacher;
    }

    public void setTeacher(Teacher teacher)
    {
        this.teacher = teacher;
    }

    public Classroom getClassroom()
    {
        return classroom;
    }

    public void setClassroom(Classroom classroom)
    {
        this.classroom = classroom;
    }

    public ArrayList<Group> getGroups()
    {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups)
    {
        this.groups = groups;
    }

    public String getGroupNames(ArrayList<Group> groups) {

        StringBuilder str = new StringBuilder();

        for (Group group : groups)
        {
            str.append(group.getGroupName()).append(", ");
        }

        String commaString = str.toString();

        if (commaString.length() > 0) {
            commaString = commaString.substring(0, commaString.length() - 2);
        }

        return commaString;
    }

}
