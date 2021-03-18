package Simulator.Time;

import Data.Lesson;
import Data.Schedule;

import java.time.LocalTime;
import java.util.ArrayList;

public class TimeManager
{

    private TimeType timeType;
    private Schedule schedule;

    public TimeManager(Schedule schedule, TimeType timeType)
    {
        this.timeType = timeType;
        this.schedule = schedule;
    }


    public ArrayList<Lesson> getCurrentLessons()
    {
        return schedule.getOverlappingLessons(getTime());
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
