package GUI;

import Data.Lesson;
import Data.Classroom;
import Data.Lesson;
import Data.Schedule;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import Data.Teacher;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class TabLesson extends PopUpTab
{
    private ListView listView = new ListView();
    private int spacingDistance = 10;
    private ArrayList<String> classes = new ArrayList<>();
    private HashMap<String, Lesson> data = new HashMap<>();
    private Lesson selected;
    private  TextArea lessonData = new TextArea();
    Schedule schedule;

    protected TabLesson(Schedule schedule)
    {
        super.setPopUpName("Lessen");
        this.schedule = schedule;
        classes.add("Tivt1A");
        classes.add("Tivt1B");
        classes.add("Tivt1C");

        for (int i = 0; i < this.schedule.getLessons().size(); i++)
        {
            data.put(this.schedule.getLessons().get(i).toShortString(), this.schedule.getLessons().get(i));
        }

        this.selected = null;


    }

    @Override
    protected BorderPane getPane()
    {

        BorderPane mainPane = new BorderPane();

        //left side of the menu lesson
        Label currentLesson = new Label("Bestaande Lessen");
        for (int i = 0; i < this.schedule.getLessons().size(); i++)
        {
            listView.getItems().add(this.schedule.getLessons().get(i).toShortString());
        }
        listView.setPrefWidth(500);

        listView.getSelectionModel().selectedItemProperty().addListener((ov) -> {

            try
            {
                int index = listView.getSelectionModel().getSelectedIndex();
                lessonData.setText(this.schedule.getLessons().get(index).toString());
                this.selected = this.schedule.getLessons().get(index);
            }catch (Exception e){
            }
        });




        VBox leftVbox = new VBox();
        leftVbox.getChildren().addAll(currentLesson, listView);
        leftVbox.setSpacing(spacingDistance);
        leftVbox.setPadding(new Insets(10, 10, 10, 10));


        //middle side of menu lesson
        Label selectedLesson = new Label("Geselecteerde les");
        lessonData.setMaxWidth(500);
        lessonData.setPrefHeight(400);
        lessonData.setEditable(false);
        Button deleteSelected = new Button("Verwijder les");
        deleteSelected.setOnAction(event -> {
            if (selected != null)
            {
                this.schedule.removeLesson(this.selected);
                listView.getItems().clear();
                lessonData.setText("");
                for (int i = 0; i < this.schedule.getLessons().size(); i++)
                {
                    listView.getItems().add(this.schedule.getLessons().get(i).toShortString());
                }
            }
        });

        VBox middleVbox = new VBox();
        middleVbox.getChildren().addAll(selectedLesson, lessonData, deleteSelected);
        middleVbox.setSpacing(spacingDistance);
        middleVbox.setPadding(new Insets(10, 10, 10, 10));
        middleVbox.setMinWidth(500);

        //right side of menu lesson
        Label newLesson = new Label("Nieuwe les");

        Label startLesson = new Label("Begintijd");
        ComboBox<LocalTime> startTimeSelect = new ComboBox<>();
        startTimeSelect.setMinWidth(200);

        startTimeSelect.getItems().addAll(LocalTime.of(9, 00), LocalTime.of(9, 30), LocalTime.of(10, 00), LocalTime.of(10, 30), LocalTime.of(11, 00),
                LocalTime.of(11, 30), LocalTime.of(12, 00),LocalTime.of(12, 30), LocalTime.of(13, 00), LocalTime.of(13, 30),LocalTime.of(14, 00),
                LocalTime.of(15, 00), LocalTime.of(15, 30), LocalTime.of(16, 00), LocalTime.of(16, 30), LocalTime.of(17, 00));

        Label endLesson = new Label("Eindtijd");
        ComboBox<LocalTime> endTimeSelect = new ComboBox<>();
        endTimeSelect.setMinWidth(200);

        endTimeSelect.getItems().addAll(LocalTime.of(9, 30), LocalTime.of(10, 00), LocalTime.of(10, 30), LocalTime.of(11, 00), LocalTime.of(11, 30),
                LocalTime.of(12, 00),LocalTime.of(12, 30), LocalTime.of(13, 00), LocalTime.of(13, 30),LocalTime.of(14, 00), LocalTime.of(15, 00),
                LocalTime.of(15, 30), LocalTime.of(16, 00), LocalTime.of(16, 30), LocalTime.of(17, 00), LocalTime.of(17, 30));

        Label teacherBox = new Label("Kies een docent");
        ComboBox<Teacher> teacherSelect = new ComboBox<>();
        teacherSelect.setMinWidth(200);

        teacherSelect.getItems().addAll(schedule.getTeachers());

        Label locationBox = new Label("Kies een lokaal");
        ComboBox<Classroom> locationSelect = new ComboBox<>();
        locationSelect.setMinWidth(200);

        ArrayList<Classroom> classrooms = new ArrayList<>();

        for (Lesson lesson : schedule.getLessons())
        {
            if (!classrooms.contains(lesson.getClassroom())) {
                classrooms.add(lesson.getClassroom());
            }
        }

        locationSelect.getItems().addAll(classrooms);

        Button lessonAdder = new Button("Voeg klas toe");

        lessonAdder.setOnAction(event -> {

            //schedule.addLesson(new Lesson(startTimeSelect.getValue(), endTimeSelect.getValue(), teacherSelect.getValue(), locationSelect.getValue(), ));

        });

        VBox rightVbox = new VBox();
        rightVbox.getChildren().addAll(newLesson, startLesson, startTimeSelect, endLesson, endTimeSelect, teacherBox, teacherSelect, locationBox, locationSelect, selectClass(), lessonAdder);

        rightVbox.setSpacing(spacingDistance);

        rightVbox.setPadding(new Insets(10, 10, 10, 10));
        rightVbox.setAlignment(Pos.TOP_LEFT);


        mainPane.setLeft(leftVbox);
        mainPane.setCenter(middleVbox);
        mainPane.setRight(rightVbox);
        mainPane.setPadding(new Insets(10, 500, 10, 10));


        return mainPane;
    }

    /**
     * deze methode is tijdelijk want deze moet observable worden zodat het klassen ziet als er nieuwe worden toegevoegd
     *
     * @return
     */
    public VBox selectClass()
    {
        VBox classselector = new VBox();
        classselector.setSpacing(5);
        classselector.setPrefHeight(110);
        for (String string : classes)
        {
            CheckBox box = new CheckBox(string);
            classselector.getChildren().addAll(box);
        }
        return classselector;
    }

}
