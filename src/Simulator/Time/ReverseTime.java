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
        time = time.plusNanos(deltaTime());
        return time;
    }

    @Override
    public long deltaTime()
    {
        return timeType.deltaTime() * -1;
    }
}
