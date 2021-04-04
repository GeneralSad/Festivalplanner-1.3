package Data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;

class ScheduleTest
{
    Schedule schedule = null;

    @BeforeEach
    void setUp()
    {
        String filePath = "src/Data/storedSchedule";
        this.schedule = DataStorage.loadSchedule(filePath);
    }

    @Test
    void nextLessonTest()
    {
        //arange
        LocalTime searchAfter = LocalTime.of(12, 30, 0);

        //act
        LocalTime startTimeFound = schedule.nextLesson(searchAfter);

        //assert
        Assertions.assertTrue(startTimeFound.isAfter(searchAfter), "De gevonden tijd was: " + startTimeFound.toString() + "\n terwijl de de tijd na: " + searchAfter.toString() + " Moest zijn");
    }


    @Test
    void getOverlappingLessonsTest()
    {
        //arange
        LocalTime localTime = LocalTime.of(10, 30, 0);

        //act
        ArrayList<Lesson> overlappingLessons = schedule.getOverlappingLessons(localTime);

        //assert
        for (Lesson overlappingLesson : overlappingLessons)
        {
            if (overlappingLesson.getBeginTime().isAfter(localTime))
            {
                Assertions.fail("Les overlap met les van: " + overlappingLesson.getBeginTime() + " Terwijl deze les begint na de gezochte les");
            }
        }
    }
}