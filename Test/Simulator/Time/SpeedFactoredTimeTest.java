package Simulator.Time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

class SpeedFactoredTimeTest
{

    @Test
    void testSpeedFactoredTime()
    {
        //arange

        int speedFactor = 6;
        SpeedFactoredTime speedFactoredTime = new SpeedFactoredTime(LocalTime.of(9, 0, 0), speedFactor);
        long timeToAdd = 1000000000L;


        //act
        LocalTime beginTime = speedFactoredTime.getTime();
        speedFactoredTime.update(timeToAdd);
        LocalTime endTime = speedFactoredTime.getTime();
        long until = beginTime.until(endTime, ChronoUnit.NANOS);

        //assert
        Assertions.assertEquals(timeToAdd * speedFactor, until);
    }

}