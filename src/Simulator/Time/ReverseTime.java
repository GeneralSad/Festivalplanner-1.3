package Simulator.Time;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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
        LocalTime localTime = timeType.getTime();
        timeType.update(deltatime);
        LocalTime localTime2 = timeType.getTime();
        long between = ChronoUnit.NANOS.between(localTime, localTime2);
        time = time.plusNanos(between);

    }

    @Override
    public int getSpeedFactor()
    {
        return timeType.getSpeedFactor();
    }

}
