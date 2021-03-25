package GUI;

import Data.Classroom;
import Data.Lesson;
import Data.Schedule;
import Data.Teacher;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Auteurs:
 *
 * Deze code zorgt ervoor dat de mainwindow goed wordt ingericht met alle delen op de goede plaats
 *
 */

public class MainWindow extends ResizableCanvas
{
    private Schedule schedule;
    private HashMap<Rectangle2D, Lesson> lessonShapes = new HashMap<>();

    private ArrayList<LocalTime> allStartingTimes;
    private ArrayList<LocalTime> allEndingTimes;

    private int amountOfClassrooms = Classroom.classRooms.values().length;

    private int timeStampVerticalSpacing = 50;
    private int timeStampHorizontalSpacing = 100;
    private int amountOfTimeStamps = 15;
    private int distanceOfTimeStampsFromTop = 100;
    private int distanceOfTimeStampsFromLeft = 50;

    private int distanceOfClassroomsFromLeft = distanceOfTimeStampsFromLeft + timeStampHorizontalSpacing;
    private int distanceOfClassroomsFromTop = 30;
    private int classroomTableWidth = 200;

    public MainWindow(Pane pane, Schedule schedule)
    {
        super(fxGraphics2D ->
        {
            // this needs to be here but isn't used
        }, pane);
        this.schedule = schedule;

        this.allStartingTimes = schedule.getAllStartingTimes();
        this.allEndingTimes = schedule.getAllEndingTimes();



        initialise();
    }

    private void initialise()
    {


        amountOfTimeStamps = allEndingTimes.size();



        super.setOnMouseClicked(event ->
        {
            for (Rectangle2D rectangle2D : this.lessonShapes.keySet()) {
                if (rectangle2D.contains(event.getX(), event.getY())) {
                    Lesson lesson = this.lessonShapes.get(rectangle2D);
                    System.out.println(lesson);

                }
            }

        });

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update();
                last = now;
            }
        }.start();

        update();
    }

    private void update()
    {
        this.lessonShapes.clear();
        for (Lesson lesson : this.schedule.getLessons())
        {
            int classroomIndex = lesson.getClassroom().getClassroom() - 1;
            int startingTimeIndex = this.allStartingTimes.indexOf(lesson.getBeginTime());
            int amountOfTimeBlocks = this.allEndingTimes.indexOf(lesson.getEndTime()) - startingTimeIndex + 1;
            if (classroomIndex != -1 && startingTimeIndex != -1)
            {
                int y = this.distanceOfTimeStampsFromTop + startingTimeIndex * this.timeStampVerticalSpacing;
                int x = this.distanceOfClassroomsFromLeft + classroomIndex * this.classroomTableWidth;
                this.lessonShapes.put(new Rectangle2D.Double(x, y, this.classroomTableWidth, (this.timeStampVerticalSpacing * amountOfTimeBlocks) - 1), lesson);
            }
            else
            {
//                System.out.println("classroomindex: " + classroomIndex + " startingTime index: " + lesson.getFormatBeginTime() + " " + startingTimeIndex);
            }
        }

        draw(new FXGraphics2D(getGraphicsContext2D()));
    }

    private void draw(FXGraphics2D graphics2D)
    {
        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, (int)getWidth(), (int)getHeight());
        graphics2D.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));

        // verticale lijnen van het tijdvak
        for (int i = 0; i < 2; i++)
        {
            int x = distanceOfTimeStampsFromLeft + i * timeStampHorizontalSpacing;
            graphics2D.drawLine(x, distanceOfTimeStampsFromTop, x, distanceOfTimeStampsFromTop + timeStampVerticalSpacing * amountOfTimeStamps);
        }

        // Alle tijden tekenen
        Font oldFont = graphics2D.getFont();
        Font newFont = new Font(oldFont.getName(), oldFont.getStyle(), oldFont.getSize() - 5);

        graphics2D.setFont(newFont);
        for (int i = 0; i < allEndingTimes.size(); i++)
        {
            graphics2D.drawString(allStartingTimes.get(i) + " -", distanceOfTimeStampsFromLeft + 10, distanceOfTimeStampsFromTop + 20 + i * timeStampVerticalSpacing);
            graphics2D.drawString(allEndingTimes.get(i) + "", distanceOfTimeStampsFromLeft + 10, distanceOfTimeStampsFromTop + 40 + i * timeStampVerticalSpacing);
        }
        graphics2D.setFont(oldFont);

        // bovenste horizontale lijn van het rooster
        graphics2D.drawLine(distanceOfClassroomsFromLeft, distanceOfClassroomsFromTop, distanceOfClassroomsFromLeft + classroomTableWidth * amountOfClassrooms, distanceOfClassroomsFromTop);


        // pauze tekenen
        graphics2D.setColor(Color.GRAY);
        graphics2D.fill(new Rectangle2D.Double(distanceOfClassroomsFromLeft, distanceOfTimeStampsFromTop + 6 * timeStampVerticalSpacing, amountOfClassrooms * classroomTableWidth, timeStampVerticalSpacing));
        graphics2D.setColor(Color.BLACK);
        // Bij elk lokaal pauze tekenen
        for (int i = 0; i < amountOfClassrooms; i++)
        {
            graphics2D.drawString("Pauze", distanceOfClassroomsFromLeft + i * classroomTableWidth, distanceOfTimeStampsFromTop + 30 + 6 * timeStampVerticalSpacing);
        }

        // horizontale lijnen van het rooster tekenen
        for (int i = 0; i < amountOfTimeStamps + 1; i++)
        {
            int y = distanceOfTimeStampsFromTop + i * timeStampVerticalSpacing;
            graphics2D.drawLine(distanceOfTimeStampsFromLeft, y, distanceOfClassroomsFromLeft + classroomTableWidth * amountOfClassrooms, y);
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




        // verticale lijnen van het rooster
        for (int i = 0; i < amountOfClassrooms + 1; i++)
        {
            int x = distanceOfClassroomsFromLeft + i * classroomTableWidth;
            graphics2D.drawLine(x, distanceOfClassroomsFromTop, x, distanceOfTimeStampsFromTop + amountOfTimeStamps * timeStampVerticalSpacing);
        }


        // alle lokalen tekenen
        for (int i = 0; i < amountOfClassrooms; i++)
        {
            graphics2D.drawString(Classroom.classRooms.values()[i].roomName, distanceOfClassroomsFromLeft + i * classroomTableWidth, distanceOfClassroomsFromTop + 20);
        }
    }
}
