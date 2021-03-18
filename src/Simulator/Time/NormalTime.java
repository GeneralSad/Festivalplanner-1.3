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
        time = time.plusNanos(deltaTime());
        return time;
    }

    public long deltaTime()
    {
        long deltaTime = LocalTime.now().toNanoOfDay() - lastTime;
        lastTime = LocalTime.now().toNanoOfDay();
        return deltaTime;
    }
}
