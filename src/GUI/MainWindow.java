package GUI;

import Data.Lesson;
import Data.Schedule;
import Data.Teacher;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.Resizable;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainWindow extends ResizableCanvas
{
    private Schedule schedule;
    private HashMap<Rectangle2D, Lesson> lessonShapes = new HashMap<>();

    private String[] classrooms = new String[]{"Lokaal 1", "Lokaal 2", "Lokaal 3", "Lokaal 4", "Lokaal 5", "Lokaal 6", "Lokaal 7", "Lokaal 8"};
    private ArrayList<String> allStartingTimes = new ArrayList<>();
    private ArrayList<String> allEndingTimes = new ArrayList<>();

    private int timeStampVerticalSpacing = 90;
    private int timeStampHorizontalSpacing = 100;
    private int amountOfTimeStamps = 9;
    private int distanceOfTimeStampsFromTop = 100;
    private int distanceOfTimeStampsFromLeft = 50;

    private int distanceOfClassroomsFromLeft = distanceOfTimeStampsFromLeft + timeStampHorizontalSpacing;
    private int distanceOfClassroomsFromTop = 30;
    private int classroomTableWidth = 200;

    public MainWindow(Pane pane, Schedule schedule)
    {
        super(new Resizable()
        {
            @Override
            public void draw(FXGraphics2D fxGraphics2D)
            {
                // this needs to be here but isn't used
            }
        }, pane);
        this.schedule = schedule;
        initialise();
        draw(new FXGraphics2D(getGraphicsContext2D()));

        super.setOnMouseClicked(event ->
        {
            for (Rectangle2D rectangle2D : this.lessonShapes.keySet()) {
                if (rectangle2D.contains(event.getX(), event.getY())) {
                    System.out.println(this.lessonShapes.get(rectangle2D));
                }
            }
        });
    }

    private void initialise()
    {
        new AnimationTimer()
        {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                {
                    last = now;
                }

                if (now - last >= 100000000.0)
                {
                    draw(new FXGraphics2D(getGraphicsContext2D()));
                    last = now;
                }
            }
        }.start();

        Collections.addAll(allStartingTimes, "09:00", "10:00", "11:00", "12:00", "12:30", "13:30", "14:30", "15:30", "16:30");
        Collections.addAll(allEndingTimes, "10:00", "11:00", "12:00", "12:30", "13:30", "14:30", "15:30", "16:30", "17:30");

        for (Lesson lesson : this.schedule.getLessons())
        {
            int classroomIndex = lesson.getClassroom().getClassroom();
            int startingTimeIndex = this.allStartingTimes.indexOf(lesson.getFormatBeginTime());
            int amountOfTimeBlocks = this.allEndingTimes.indexOf(lesson.getFormatEndTime()) - startingTimeIndex + 1;
            if (classroomIndex != -1 && startingTimeIndex != -1)
            {
                int y = this.distanceOfTimeStampsFromTop + startingTimeIndex * this.timeStampVerticalSpacing;
                int x = this.distanceOfClassroomsFromLeft + classroomIndex * this.classroomTableWidth;
                this.lessonShapes.put(new Rectangle2D.Double(x, y, this.classroomTableWidth, this.timeStampVerticalSpacing * amountOfTimeBlocks), lesson);
            }
            else
            {
                System.out.println("classroomindex: " + classroomIndex + " startingTime index: " + lesson.getFormatBeginTime() + " " + startingTimeIndex);
            }
        }
    }

    public void draw(FXGraphics2D graphics2D)
    {
        graphics2D.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));



        // verticale lijnen van het tijdvak
        for (int i = 0; i < 2; i++)
        {
            int x = distanceOfTimeStampsFromLeft + i * timeStampHorizontalSpacing;
            graphics2D.drawLine(x, distanceOfTimeStampsFromTop, x, distanceOfTimeStampsFromTop + timeStampVerticalSpacing * amountOfTimeStamps);
        }
        // horizontale lijnen van het tijdvlak
        for (int i = 0; i < 10; i++)
        {
            int y = (distanceOfTimeStampsFromTop + +i * timeStampVerticalSpacing);
            graphics2D.drawLine(distanceOfTimeStampsFromLeft, y, distanceOfTimeStampsFromLeft + 100, y);
        }
        // begintijden tekenen
        for (int i = 0; i < allStartingTimes.size(); i++)
        {
            graphics2D.drawString(allStartingTimes.get(i) + " -", distanceOfTimeStampsFromLeft + 10, distanceOfTimeStampsFromTop + 25 + i * timeStampVerticalSpacing);
        }
        // eindtijden tekenen
        for (int i = 0; i < allStartingTimes.size(); i++)
        {
            graphics2D.drawString(allEndingTimes.get(i), distanceOfTimeStampsFromLeft + 10, distanceOfTimeStampsFromTop + 50 + i * timeStampVerticalSpacing);
        }

        // bovenste horizontale lijn van het rooster
        graphics2D.drawLine(distanceOfClassroomsFromLeft, distanceOfClassroomsFromTop, distanceOfClassroomsFromLeft + classroomTableWidth * classrooms.length, distanceOfClassroomsFromTop);
        // interne horizontale lijnen van het rooster
        for (int i = 0; i < amountOfTimeStamps + 1; i++)
        {
            int y = distanceOfTimeStampsFromTop + i * timeStampVerticalSpacing;
            graphics2D.drawLine(distanceOfClassroomsFromLeft, y, distanceOfClassroomsFromLeft + classroomTableWidth * classrooms.length, y);
        }

        // Tekenen van de lessen
        for (Rectangle2D rectangle2D : this.lessonShapes.keySet()) {
            graphics2D.setColor(Color.GRAY);
            graphics2D.fill(rectangle2D);
            graphics2D.setColor(Color.BLACK);
            Teacher teacher = this.lessonShapes.get(rectangle2D).getTeacher();
            graphics2D.drawString(teacher.getSubject(), (int)rectangle2D.getX(), (int)(rectangle2D.getY() + 20));
            graphics2D.drawString(teacher.getName(), (int)rectangle2D.getX(), (int)(rectangle2D.getY() + 50));
            graphics2D.drawString(this.lessonShapes.get(rectangle2D).getGroupNames(this.lessonShapes.get(rectangle2D).getGroups()), (int)rectangle2D.getX(), (int)(rectangle2D.getY() + 80));
        }

        // pauze tekenen
        graphics2D.setColor(Color.GRAY);
        graphics2D.fill(new Rectangle2D.Double(distanceOfClassroomsFromLeft, distanceOfTimeStampsFromTop + 3 * timeStampVerticalSpacing, classrooms.length * classroomTableWidth, timeStampVerticalSpacing));
        graphics2D.setColor(Color.BLACK);
        // Bij elk lokaal pauze tekenen
        for (int i = 0; i < classrooms.length; i++)
        {
            graphics2D.drawString("Pauze", distanceOfClassroomsFromLeft + i * classroomTableWidth, distanceOfTimeStampsFromTop + 30 + 3 * timeStampVerticalSpacing);
        }

        // lijn boven de pauzes opnieuw tekenen om te scheiden van de lessen.
        for (int i = 0; i < 2; i++)
        {
            int y = distanceOfTimeStampsFromTop + 3 * timeStampVerticalSpacing + i * timeStampVerticalSpacing;
            graphics2D.drawLine(distanceOfClassroomsFromLeft, y, distanceOfClassroomsFromLeft + classroomTableWidth * classrooms.length, y);
        }

        // verticale lijnen van het rooster
        for (int i = 0; i < classrooms.length + 1; i++)
        {
            int x = distanceOfClassroomsFromLeft + i * classroomTableWidth;
            graphics2D.drawLine(x, distanceOfClassroomsFromTop, x, distanceOfTimeStampsFromTop + amountOfTimeStamps * timeStampVerticalSpacing);
        }

        // alle lokalen tekenen
        for (int i = 0; i < classrooms.length; i++)
        {
            graphics2D.drawString(classrooms[i], distanceOfClassroomsFromLeft + i * classroomTableWidth, distanceOfClassroomsFromTop + 20);
        }
    }
}