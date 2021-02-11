package GUI;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.Resizable;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

public class MainWindow extends ResizableCanvas
{
    private ArrayList<String> courses = new ArrayList<>();
    private ArrayList<String> courseTeachers = new ArrayList<>();
    private ArrayList<String> classStartTimes = new ArrayList<>();
    private ArrayList<String> classRooms = new ArrayList<>();

    public MainWindow(Pane pane)
    {
        super(new Resizable()
        {
            @Override
            public void draw(FXGraphics2D fxGraphics2D)
            {
                // this needs to be here but isn't used
            }
        }, pane);
        initialise();
        draw(new FXGraphics2D(getGraphicsContext2D()));
    }

    private void initialise()
    {
        this.courses.add("2dGraphics");
        this.courses.add("OOM");
        this.courses.add("OGP");
        this.courses.add("POC");
        this.courses.add("GIT");

        this.courseTeachers.add("Etiënne");
        this.courseTeachers.add("Jessica");
        this.courseTeachers.add("Edwin");
        this.courseTeachers.add("Katja");
        this.courseTeachers.add("Maurice");

        this.classStartTimes.add("10:00 -");
        this.classStartTimes.add("10:00 -");
        this.classStartTimes.add("12:30 -");
        this.classStartTimes.add("10:00 -");
        this.classStartTimes.add("15:30 -");

        this.classRooms.add("Lokaal 1");
        this.classRooms.add("Lokaal 2");
        this.classRooms.add("Lokaal 3");
        this.classRooms.add("Lokaal 4");
        this.classRooms.add("Lokaal 5");

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
    }

    public void draw(FXGraphics2D graphics2D)
    {
        graphics2D.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        ArrayList<String> allClassrooms = new ArrayList<>();
        String[] classrooms = new String[] {"Lokaal 1", "Lokaal 2", "Lokaal 3", "Lokaal 4", "Lokaal 5", "Lokaal 6", "Lokaal 7", "Lokaal 8"};
        Collections.addAll(allClassrooms, classrooms);

        ArrayList<String> allStartingTimes = new ArrayList<>();
        String[] startingTimes = new String[] {"9:00 -", "10:00 -", "11:00 -", "12:00 -", "12:30 -",
                "13:30 -", "14:30 -", "15:30 -", "16:30 -"};
        Collections.addAll(allStartingTimes, startingTimes);

        String[] endingTimes = new String[] {"10:00", "11:00", "12:00", "12:30", "13:30", "14:30",
                "15:30", "16:30", "17:30"};

        int timeStampVerticalSpacing = 90;
        int timeStampHorizontalSpacing = 100;
        int amountOfTimeStamps = 9;
        int distanceOfTimeStampsFromTop = 100;
        int distanceOfTimeStampsFromLeft = 50;

        int distanceOfClassroomsFromLeft = 200;
        int distanceOfClassroomsFromTop = 30;
        int classroomTableWidth = 200;

        // verticale lijnen van het tijdvak
        for (int i = 0; i < 2; i++) {
            int x = distanceOfTimeStampsFromLeft + i * timeStampHorizontalSpacing;
            graphics2D.drawLine(x , distanceOfTimeStampsFromTop, x, distanceOfTimeStampsFromTop + timeStampVerticalSpacing * amountOfTimeStamps);
        }
        // horizontale lijnen van het tijdvlak
        for (int i = 0; i < 10; i++) {
            int y = (distanceOfTimeStampsFromTop +  + i * timeStampVerticalSpacing);
            graphics2D.drawLine(distanceOfTimeStampsFromLeft , y, distanceOfTimeStampsFromLeft + 100, y);
        }
        // begintijden tekenen
        for (int i = 0; i < startingTimes.length; i++) {
            graphics2D.drawString(startingTimes[i],distanceOfTimeStampsFromLeft + 10, distanceOfTimeStampsFromTop + 25 + i * timeStampVerticalSpacing);
        }
        // eindtijden tekenen
        for (int i = 0; i < startingTimes.length; i++) {
            graphics2D.drawString(endingTimes[i],distanceOfTimeStampsFromLeft + 10, distanceOfTimeStampsFromTop + 50 + i * timeStampVerticalSpacing);
        }

        // Tekenen van de lessen
        for (int i = 0; i < this.courses.size(); i++) {
            int classroomIndex = allClassrooms.indexOf(this.classRooms.get(i));
            int startingTimeIndex = allStartingTimes.indexOf(this.classStartTimes.get(i));
            if (classroomIndex != -1 && startingTimeIndex != -1) {
                int y = distanceOfTimeStampsFromTop + startingTimeIndex * timeStampVerticalSpacing;
                int x = distanceOfClassroomsFromLeft + classroomIndex * classroomTableWidth;
                graphics2D.setColor(Color.GRAY);
                graphics2D.fill(new Rectangle2D.Double(x, y, classroomTableWidth, timeStampVerticalSpacing));
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(this.courses.get(i), x, y + 20);
                graphics2D.drawString(this.courseTeachers.get(i), x, y + 50);
            } else {
                System.out.println("classroomindex: " + classroomIndex + " startingTime index: " + startingTimeIndex);
            }
        }

        // pauze tekenen
        graphics2D.setColor(Color.GRAY);
        graphics2D.fill(new Rectangle2D.Double(distanceOfClassroomsFromLeft, distanceOfTimeStampsFromTop + 3 * timeStampVerticalSpacing, classrooms.length * classroomTableWidth, timeStampVerticalSpacing));
        graphics2D.setColor(Color.BLACK);
        // Bij elk lokaal pauze tekenen
        for (int i = 0; i < classrooms.length; i++) {
            graphics2D.drawString("Pauze", distanceOfClassroomsFromLeft + i * classroomTableWidth, distanceOfTimeStampsFromTop + 30 + 3 * timeStampVerticalSpacing);
        }

        // verticale lijnen van het rooster
        for (int i = 0; i < classrooms.length + 1; i++) {
            int x = distanceOfClassroomsFromLeft + i * classroomTableWidth;
            graphics2D.drawLine(x , distanceOfClassroomsFromTop, x, distanceOfTimeStampsFromTop + amountOfTimeStamps * timeStampVerticalSpacing);
        }
        // bovenste horizontale lijn van het rooster
        graphics2D.drawLine(distanceOfClassroomsFromLeft , distanceOfClassroomsFromTop, distanceOfClassroomsFromLeft + classroomTableWidth * classrooms.length, distanceOfClassroomsFromTop);
        // interne horizontale lijnen van het rooster
        for (int i = 0; i < amountOfTimeStamps + 1; i++) {
            int y = distanceOfTimeStampsFromTop + i * timeStampVerticalSpacing;
            graphics2D.drawLine(distanceOfClassroomsFromLeft , y, distanceOfClassroomsFromLeft + classroomTableWidth * classrooms.length, y);
        }

        // alle lokalen tekenen
        for (int i = 0; i < classrooms.length; i++) {
            graphics2D.drawString(classrooms[i], distanceOfClassroomsFromLeft + i * classroomTableWidth, distanceOfClassroomsFromTop + 20);
        }
    }
}
