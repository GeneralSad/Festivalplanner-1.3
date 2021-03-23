package Simulator.Time;

import java.time.LocalTime;

public class NormalTime implements TimeType
{
    private LocalTime time;
    private Long lastTime;

    public NormalTime(LocalTime startingTime)
    {
        this.time = startingTime;
        lastTime = LocalTime.now().toNanoOfDay();
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
}
