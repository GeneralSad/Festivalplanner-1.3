package Data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Auteurs: Leon
 *
 * Deze code zorgt ervoor dat een schedule aangemaakt kan worden en de nodige functies heeft die later nodig zijn
 *
 */

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
        ArrayList<Lesson> overLappingLesson = getOverlappingLessons(beginTime, endTime);

        checkLesson(lesson, overLappingLesson);
        // if an exception was thrown then the method stops so the following statement isn't reached
        lessonObservableList.add(lesson);


    }


    public boolean checkLesson(Lesson lesson, ArrayList<Lesson> overLappingLessons) throws IllegalArgumentException
    {
        LocalTime beginTime = lesson.getBeginTime();
        LocalTime endTime = lesson.getEndTime();

        if (beginTime.isBefore(endTime))
        {
            if (beginTime.until(endTime, ChronoUnit.MINUTES) < 60)
            {
                throw new IllegalArgumentException("Lesson moeten minimaal een uur duren");
            }

            if (isTeacherOverlap(lesson.getTeacher(), overLappingLessons))
            {
                throw new IllegalArgumentException("The teacher already has a lesson at that moment!");
            }

            if (isClassroomOverLap(lesson.getClassroom(), overLappingLessons))
            {
                throw new IllegalArgumentException("That classroom is already preoccupied at that moment!");
            }
            if (isGroupOverlap(lesson.getGroups(), overLappingLessons))
            {
                throw new IllegalArgumentException("The group " + " " + " already has a different lesson at that moment!");
            }
        }
        else
        {
            throw new IllegalArgumentException("Begin tijd is na eind tijd");
        }
        return true;
    }

    private boolean isGroupOverlap(ArrayList<Group> newLessonGroups, ArrayList<Lesson> overLappingLessons)
    {
        for (Lesson overLappingLesson : overLappingLessons)
        {
            for (Group group : overLappingLesson.getGroups())
            {
                if (newLessonGroups.contains(group))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isClassroomOverLap(Classroom classroom, ArrayList<Lesson> overLappingLessons)
    {
        for (Lesson overLappingLesson : overLappingLessons)
        {
            // Check for classroom overlap
            if (classroom == overLappingLesson.getClassroom())
            {
                return true;
            }
        }
        return false;

    }

    //returns true if overlapping
    private boolean isTeacherOverlap(Teacher teacher, ArrayList<Lesson> overLappingLessons)
    {
        for (Lesson overLappingLesson : overLappingLessons)
        {
            // Check for teacher overlap
            if (teacher.equals(overLappingLesson.getTeacher()))
            {
                return true;
            }
        }

        return false;
    }


    public ArrayList<Lesson> getOverlappingLessons(LocalTime time)
    {
        return getOverlappingLessons(time, time);
    }

    public ArrayList<Lesson> getOverlappingLessons(LocalTime beginTime, LocalTime endTime)
    {

        ArrayList<Lesson> overlappingTimeLesson = new ArrayList<>();
        for (Lesson existingLesson : this.lessonArrayList)
        {
            LocalTime tempBeginTime = existingLesson.getBeginTime();
            LocalTime tempEndTime = existingLesson.getEndTime();
            if ((beginTime.equals(tempBeginTime) && endTime.equals(tempEndTime)) || (endTime.isBefore(tempEndTime) && endTime.isAfter(tempBeginTime)) || (beginTime.isAfter(tempBeginTime) && beginTime.isBefore(tempEndTime)))
            {
                overlappingTimeLesson.add(existingLesson);
            }
        }
        return overlappingTimeLesson;
    }


    public void addGroup(Group group)
    {
        if (!groupDuplicateCheck(group))
        {
            groupObservableList.add(group);
        }
        else
        {
            throw new IllegalArgumentException("groep met die naam bestaad al");
        }

    }

    private boolean groupDuplicateCheck(Group group)
    {
        for (Group groupFromList : this.getGroupObservableList())
        {
            if (groupFromList.getGroupName().equals(group.getGroupName()))
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Lesson> getLessonArrayList()
    {
        return lessonArrayList;
    }

    public void removeTeacher(Teacher teacher)
    {
        List<Lesson> lessonList = new ArrayList<>(lessonObservableList);
        for (Lesson lesson : lessonList)
        {
            if (lesson.getTeacher() == teacher)
            {
                removeLesson(lesson);
            }
        }
        teacherObservableList.remove(teacher);
    }

    public void removeLesson(Lesson lesson)
    {
        lessonObservableList.remove(lesson);
    }

    public void removeGroup(Group group)
    {
        List<Lesson> lessonList = new ArrayList<>(lessonObservableList);
        for (Lesson lesson : lessonList)
        {
            if (lesson.getGroups().contains(group))
            {
                removeLesson(lesson);
            }
        }
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
