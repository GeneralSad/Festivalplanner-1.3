package Simulator.Time;

import java.time.LocalTime;

public class ReverseTime implements TimeType
{

    private LocalTime time;
    private TimeType timeType;

    public ReverseTime(TimeType timeType)
    {
        this.timeType = timeType;
        time = timeType.getTime();
    }


    @Override
    public LocalTime getTime()
    {
        return time;
    }

    @Override
    public void update(Long deltatime)
    {
        time = time.plusNanos(deltatime * -1);
    }
}
