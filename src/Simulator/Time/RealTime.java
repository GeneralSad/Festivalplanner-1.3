package Simulator.Time;

import java.time.LocalTime;

public class RealTime extends NormalTime
{
    public RealTime()
    {
        super(LocalTime.now());
    }
}
