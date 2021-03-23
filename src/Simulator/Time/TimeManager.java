package Simulator.Time;

import Data.Lesson;
import Data.Schedule;

import java.time.LocalTime;
import java.util.ArrayList;

public class TimeManager
{

    private TimeType timeType;
    private Schedule schedule;
    private LocalTime nextChange;
    private ArrayList<Lesson> lessons;

    public TimeManager(Schedule schedule, TimeType timeType)
    {
        this.timeType = timeType;
        this.schedule = schedule;
        this.nextChange = timeType.getTime();
    }

    public void update(Long deltatime)
    {
        timeType.update(deltatime);
    }

    public ArrayList<Lesson> getCurrentLessons()
    {

        lessons = schedule.getOverlappingLessons(getTime());
        nextChange = null;
        for (Lesson lesson : lessons)
        {
            LocalTime endTime = lesson.getEndTime();
            if (nextChange == null || endTime.isBefore(nextChange))
            {
                nextChange = endTime;
            }
        }

        if (nextChange == null)
        {
            nextChange = schedule.nextLesson(timeType.getTime());
        }


        return lessons;


    }

    public boolean isChanged()
    {
        return timeType.getTime().isAfter(nextChange);
    }

    public LocalTime getTime()
    {
        return timeType.getTime();
    }

    public TimeType getTimeType()
    {
        return timeType;
    }

    public void setTimeType(TimeType timeType)
    {
        this.timeType = timeType;
    }

}
