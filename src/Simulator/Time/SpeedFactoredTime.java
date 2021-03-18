package Simulator.Time;

import java.time.LocalTime;

public class SpeedFactoredTime implements TimeType
{
    private Long lastTime;
    private LocalTime time;
    private int speedFactor;


    public SpeedFactoredTime(LocalTime startingTime, int speedFactor)
    {
        this.time = startingTime;
        lastTime = LocalTime.now().toNanoOfDay();
        this.speedFactor = speedFactor;
    }


    @Override
    public LocalTime getTime()
    {
        return time;
    }


    @Override
    public void update(Long deltatime)
    {
        time = time.plusNanos(deltatime * speedFactor);
    }
}
