package Simulator.LocationSystem;

import Data.Classroom;
import Data.Lesson;
import Data.Schedule;
import Simulator.Maploading.TiledMap;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class LocationLabels {


    private LocationDatabase locationDatabase = new LocationDatabase();
    private ArrayList<ClassRoomBehavior> classRoomBehaviors = locationDatabase.ClassRoomData();



    public void draw(FXGraphics2D graphics2D, Schedule schedule, ArrayList<Lesson> lessons){

        float alpha = 0.75f;
        Color colorField = new Color(0, 0, 0, alpha);
        graphics2D.setColor(colorField);


        for (int i = 0; i < classRoomBehaviors.size(); i++) {
            graphics2D.fill(new RoundRectangle2D.Double(classRoomBehaviors.get(i).getDraw().getX() + 10, classRoomBehaviors.get(i).getDraw().getY(), 120, 65, 20, 20));
        }

        for (ClassRoomBehavior clas: classRoomBehaviors) {
            for (Classroom classRoom : schedule.getClassroomArrayList()){
                if (classRoom.getEntry().distance(clas.getEntry()) < 10){

                    Font big = new Font(Font.MONOSPACED, Font.PLAIN, 15);
                    graphics2D.setFont(big);
                    graphics2D.setColor(Color.white);
                    graphics2D.drawString("Lokaal: " +  classRoom.getName(),(int) clas.getDraw().getX() + 15, (int)clas.getDraw().getY()+15);



                    for (int i = 0; i < lessons.size(); i++) {
                        Font tiny = new Font(Font.MONOSPACED, Font.PLAIN, 10);
                        graphics2D.setFont(tiny);
                        graphics2D.setColor(Color.white);
                        if (classRoom == lessons.get(i).getClassroom()){

                            String groups = "";
                            for (int j = 0; j < lessons.get(i).getGroups().size(); j++) {
                                if (j == lessons.get(i).getGroups().size()-1){
                                    groups += lessons.get(i).getGroups().get(j).getGroupName();
                                } else {
                                    groups += lessons.get(i).getGroups().get(j).getGroupName() + ", ";
                                }
                            }

                            graphics2D.drawString("Les: " +  lessons.get(i).getTeacher().getSubject() + "\n"
                                    + "Tijd: " + lessons.get(i).getBeginTime().toString() + " - "  + lessons.get(i).getEndTime() + "\n" +
                                    "Groepen: " + groups + "\nDocent: " + lessons.get(i).getTeacher().getName()
                                    ,(int) clas.getDraw().getX() + 15, (int)clas.getDraw().getY()+25);
                        }
                    }

                }
            }
        }
    }
}
