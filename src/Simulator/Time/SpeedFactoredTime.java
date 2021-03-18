package Simulator.Time;

import java.time.LocalTime;

public class SpeedFactoredTime implements TimeType
{
    private Long lastTime;
    private LocalTime time;
    private double speedFactor;


    public SpeedFactoredTime(LocalTime startingTime, double speedFactor)
    {
        this.time = startingTime;
        lastTime = LocalTime.now().toNanoOfDay();
        this.speedFactor = speedFactor;
    }

    @Override
    public LocalTime getTime()
    {

        this.time = this.time.plusNanos(deltaTime());
        return this.time;
    }


    public long deltaTime()
    {
        long deltaTime = LocalTime.now().toNanoOfDay() - lastTime;
        lastTime = LocalTime.now().toNanoOfDay();
        return (long) (deltaTime * speedFactor);
    }
}
