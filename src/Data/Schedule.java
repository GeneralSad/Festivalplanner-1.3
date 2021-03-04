package Data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

public class Schedule implements Serializable
{


    private ArrayList<Lesson> lessonArrayList;
    private ObservableList<Lesson> lessonObservableList;
    private ArrayList<Teacher> teacherArrayList;
    private ObservableList<Teacher> teacherObservableList;
    private ArrayList<Group> groupArrayList;
    private ObservableList<Group> groupObservableList;
    private ArrayList<Classroom> classroomArrayList;


    private ArrayList<LocalTime> allStartingTimes;
    private ArrayList<LocalTime> allEndingTimes;


    public Schedule(ArrayList<Lesson> lessons, ArrayList<Teacher> teacherArrayList, ArrayList<Group> groupArrayList, ArrayList<Classroom> classroomArrayList)
    {
        this.lessonArrayList = lessons;
        this.teacherArrayList = teacherArrayList;
        this.groupArrayList = groupArrayList;
        this.classroomArrayList = classroomArrayList;

        this.lessonObservableList = FXCollections.observableList(lessons);


        this.teacherObservableList = FXCollections.observableList(teacherArrayList);
        this.groupObservableList = FXCollections.observableList(groupArrayList);


        this.allStartingTimes = getLocalTimes();
        this.allStartingTimes.remove(allStartingTimes.size() - 1);
        this.allEndingTimes = getLocalTimes();
        this.allEndingTimes.remove(0);

    }

    private static ArrayList<LocalTime> getLocalTimes()
    {
        ArrayList<LocalTime> localTimes = new ArrayList<>();
        for (int i = 9; i < 18; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                localTimes.add(LocalTime.of(i, j * 30));
            }
        }

        return localTimes;
    }

    public void addTeacher(Teacher teacher)
    {
        teacherObservableList.add(teacher);
    }

    /**
     * Try to add a lesson, if conflicts exists with existing lessonArrayList then an exception is thrown with text as to why
     *
     * @param lesson lesson to try to add
     * @throws IllegalArgumentException exeption thrown in cases of conflicts
     */
    public void addLesson(Lesson lesson) throws IllegalArgumentException
    {
        // First check if there are no conflicts with other lessonArrayList
        LocalTime beginTime = lesson.getBeginTime();
        LocalTime endTime = lesson.getEndTime();

        for (Lesson existingLesson : this.lessonArrayList)
        {
            LocalTime tempBeginTime = existingLesson.getBeginTime();
            LocalTime tempEndTime = existingLesson.getEndTime();

            //TODO er zijn  nog edge cases waarbij hij toch een les toevoegd waarbij het niet werkt

            // Time overlap with an existing lesson
            if ((beginTime.equals(tempBeginTime) && endTime.equals(tempEndTime)) || (endTime.isBefore(tempEndTime) && endTime.isAfter(tempBeginTime)) || (beginTime.isAfter(tempBeginTime) && beginTime.isBefore(tempEndTime)))
            {

                // Check for teacher overlap
                if (lesson.getTeacher().equals(existingLesson.getTeacher()))
                {
                    throw new IllegalArgumentException("The teacher already has a lesson at that moment!");
                }

                // Check for classroom overlap
                if (lesson.getClassroom() == existingLesson.getClassroom())
                {
                    throw new IllegalArgumentException("That classroom is already preoccupied at that moment!");
                }

                // Check for group overlap
                ArrayList<Group> newLessonGroups = lesson.getGroups();
                for (Group group : existingLesson.getGroups())
                {
                    if (newLessonGroups.contains(group))
                    {
                        throw new IllegalArgumentException("The group " + group + " already has a different lesson at that moment!");
                    }
                }
            }
        }


        // if an exception was thrown then the method stops so the following statement isn't reached
        lessonObservableList.add(lesson);


    }

    public void addGroup(Group group)
    {
        groupObservableList.add(group);
    }

    public ArrayList<Lesson> getLessonArrayList()
    {
        return lessonArrayList;
    }

    public void removeTeacher(Teacher teacher)
    {
        teacherObservableList.remove(teacher);
    }

    public void removeLesson(Lesson lesson)
    {
        lessonObservableList.remove(lesson);
    }

    public void removeGroup(Group group)
    {
        groupObservableList.remove(group);
    }

    public ObservableList<Lesson> getLessons()
    {
        return lessonObservableList;
    }

    public void setLessons(ArrayList<Lesson> lessons)
    {
        this.lessonArrayList = lessons;
    }

    public ObservableList<Teacher> getTeacherObservableList()
    {
        return teacherObservableList;
    }

    public ArrayList<Teacher> getTeacherArrayList()
    {
        return teacherArrayList;
    }

    public void setTeacherArrayList(ArrayList<Teacher> teacherArrayList)
    {
        this.teacherArrayList = teacherArrayList;
    }

    public ArrayList<Group> getGroupArrayList()
    {
        return groupArrayList;
    }

    public void setGroupArrayList(ArrayList<Group> groupArrayList)
    {
        this.groupArrayList = groupArrayList;
    }

    public ObservableList<Group> getGroupObservableList()
    {
        return groupObservableList;
    }

    public ArrayList<Classroom> getClassroomArrayList()
    {
        return classroomArrayList;
    }


    public ArrayList<LocalTime> getAllStartingTimes()
    {
        return allStartingTimes;
    }

    public ArrayList<LocalTime> getAllEndingTimes()
    {
        return allEndingTimes;
    }

}
