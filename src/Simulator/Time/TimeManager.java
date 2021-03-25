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
        nextChange = schedule.nextLesson(timeType.getTime());
        nextChange = nextChange.plusMinutes(1);
        lessons = schedule.getOverlappingLessons(nextChange);
        return lessons;

    }


    public int getSpeedFactor()
    {
        return timeType.getSpeedFactor();
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

    public void setSpeedFactor(int speedFactor)
    {
        LocalTime time = timeType.getTime();
        if (speedFactor > 2)
        {
            timeType = new SpeedFactoredTime(time, speedFactor);
        }
        else if (speedFactor < -2)
        {
            timeType = new ReverseTime(new SpeedFactoredTime(time, speedFactor));
        }
        else
        {
            timeType = new NormalTime(time);
        }
    }


}
