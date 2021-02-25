package GUI;

import Data.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.util.ArrayList;

public class TabLesson extends PopUpTab
{
    //distance
    private int spacingDistance = 10;

    //components
    private ListView listView = new ListView();
    private ArrayList<Group> selectedGroups = new ArrayList<>();
    private ComboBox<Teacher> teacherSelect = new ComboBox<>();
    private Lesson selected;
    private TextArea lessonData = new TextArea();
    private VBox allGroups = new VBox();

    //rooster
    Schedule schedule;

    protected TabLesson(Schedule schedule)
    {
        super.setPopUpName("Lessen");
        this.schedule = schedule;
        this.selected = null;
        this.listView.setItems(FXCollections.observableArrayList(this.schedule.getLessons()));
        classUpdater();
    }

    @Override
    protected BorderPane getPane()
    {
        //mainpane
        BorderPane mainPane = new BorderPane();

        //left side of the menu lesson
        Label currentLesson = new Label("Bestaande Lessen");
        listView.setPrefWidth(500);

        //listens to your selectoin on the menu
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
                lessonData.setText("");
                listView.getItems().remove(this.selected);
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
            try
            {
                this.schedule.addLesson(new Lesson(startTimeSelect.getValue(), endTimeSelect.getValue(), teacherSelect.getValue(), locationSelect.getValue(), this.selectedGroups));
                listView.setItems(FXCollections.observableArrayList(this.schedule.getLessons()));
            } catch (Exception e){

            }
        });


        VBox rightVbox = new VBox();
        rightVbox.getChildren().addAll(newLesson, startLesson, startTimeSelect, endLesson, endTimeSelect, teacherBox, teacherSelect, locationBox, locationSelect, allGroups, lessonAdder);

        rightVbox.setSpacing(spacingDistance);

        rightVbox.setPadding(new Insets(10, 10, 10, 10));
        rightVbox.setAlignment(Pos.TOP_LEFT);


        mainPane.setLeft(leftVbox);
        mainPane.setCenter(middleVbox);
        mainPane.setRight(rightVbox);
        mainPane.setPadding(new Insets(10, 500, 10, 10));


        return mainPane;
    }


    //TODO update methodes maken zodat de data overeen komt met opgevendata in de andere tabs

    public void teacherUpdater(){
        teacherSelect.getItems().clear();
        teacherSelect.getItems().addAll(schedule.getTeachers());
    }

    public void classUpdater(){
        allGroups.getChildren().clear();
        allGroups.setSpacing(5);
        allGroups.setPrefHeight(110);
        for (int i = 0; i < this.schedule.getGroups().size(); i++)
        {
            CheckBox checkBox = new CheckBox(this.schedule.getGroups().get(i).getGroupName());
            int index = i;
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()){
                    selectedGroups.add(this.schedule.getGroups().get(index));
                } else {
                    selectedGroups.remove(this.schedule.getGroups().get(index));
                }
            });
            allGroups.getChildren().add(checkBox);
        }
    }

}
