package Simulator.Time;


import java.time.LocalTime;

public interface TimeType
{
    LocalTime getTime();

    void update(Long deltatime);

    int getSpeedFactor();
}
