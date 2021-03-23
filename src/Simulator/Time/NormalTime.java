package Simulator.Time;

import java.time.LocalTime;

public class NormalTime implements TimeType
{
    private LocalTime time;

    public NormalTime(LocalTime startingTime)
    {
        this.time = startingTime;
    }

    @Override
    public LocalTime getTime()
    {
        return time;
    }

    @Override
    public void update(Long deltatime)
    {
        time = time.plusNanos(deltatime);
    }

    @Override
    public int getSpeedFactor()
    {
        return 0;
    }
}
