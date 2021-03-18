package Simulator.Time;


import java.time.LocalTime;

public interface TimeType
{
    LocalTime getTime();

    long deltaTime();
}
