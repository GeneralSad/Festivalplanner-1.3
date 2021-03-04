package GUI;

import Data.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.util.ArrayList;

public class TabLesson extends PopUpTab
{
    //distance
    private int spacingDistance = 10;

    //components
    private ListView<Lesson> listView = new ListView<Lesson>();
    private ArrayList<Group> selectedGroups = new ArrayList<>();
    private ComboBox<Teacher> teacherSelect = new ComboBox<>();
    private Lesson selectedLesson;
    private VBox allGroups = new VBox();
    private BorderPane mainPane = new BorderPane();

    //rooster
    private Schedule schedule;

    protected TabLesson(Schedule schedule)
    {
        super.setPopUpName("Lessen");
        this.schedule = schedule;
        this.selectedLesson = null;
        this.listView.setItems(FXCollections.observableArrayList(this.schedule.getLessons()));
        classUpdater();
        teacherUpdater();
    }

    @Override
    protected BorderPane getPane()
    {

        //left side of the menu lesson
        Label currentLesson = new Label("Bestaande Lessen");
        listView.setPrefWidth(500);


        VBox leftVbox = PopupController.awesomeVBox(currentLesson, listView);


        //middle side of menu lesson
        Label selectedLesson = new Label("Geselecteerde les");
        FlowPane lessonData = new FlowPane();
        lessonData.setOrientation(Orientation.VERTICAL);

        Label selectedStartLesson = new Label("Begintijd");
        ComboBox<LocalTime> selectedStartTimeSelect = timeComboBox(true);

        Label selectedEndLesson = new Label("Eindtijd");
        ComboBox<LocalTime> selectedEndTimeSelect = timeComboBox(false);

        Label selectedTeacherBox = new Label("Docent");
        ComboBox selectedTeachterComboBox = new ComboBox();
        selectedTeachterComboBox.setItems(FXCollections.observableArrayList(schedule.getTeachers()));

        Label selectedTocationBox = new Label("Lokaal");
        ComboBox<Classroom> selectedLocationSelect = new ComboBox<>();
        selectedLocationSelect.setItems(FXCollections.observableArrayList(schedule.getClassrooms()));
        selectedLocationSelect.setMinWidth(200);

        lessonData.getChildren().addAll(selectedStartLesson, selectedStartTimeSelect, selectedEndLesson, selectedEndTimeSelect, selectedTeacherBox, selectedTeachterComboBox, selectedTocationBox, selectedLocationSelect);


        Button deleteSelected = new Button("Verwijder les");
        Button editSelected = new Button("Wijzig les");
        editSelected.setOnAction(event ->
        {
            if (this.selectedLesson != null)
            {
                this.selectedLesson.setBeginTime(selectedStartTimeSelect.getSelectionModel().getSelectedItem());
                this.selectedLesson.setEndTime(selectedEndTimeSelect.getSelectionModel().getSelectedItem());
                this.selectedLesson.setClassroom(selectedLocationSelect.getSelectionModel().getSelectedItem());
                this.selectedLesson.setTeacher((Teacher) selectedTeachterComboBox.getSelectionModel().getSelectedItem());
                listView.getItems().clear();
                this.listView.setItems(FXCollections.observableArrayList(this.schedule.getLessons()));
            }
        });


        deleteSelected.setOnAction(event ->
        {
            schedule.removeLesson(this.selectedLesson);
            listView.getItems().clear();
            this.listView.setItems(FXCollections.observableArrayList(this.schedule.getLessons()));
        });

        //listens to your selectoin on the menu
        listView.getSelectionModel().selectedItemProperty().addListener(event ->
        {
            Lesson lesson = listView.getSelectionModel().getSelectedItem();
            if (this.selectedLesson != lesson)
            {
                this.selectedLesson = lesson;
                if (this.selectedLesson != null)
                {
                    selectedStartTimeSelect.getSelectionModel().select(this.selectedLesson.getBeginTime());
                    selectedEndTimeSelect.getSelectionModel().select(this.selectedLesson.getEndTime());
                    selectedTeachterComboBox.getSelectionModel().select(this.selectedLesson.getTeacher());
                    selectedLocationSelect.getSelectionModel().select(this.selectedLesson.getClassroom());
                }
            }
        });


        VBox middleVbox = PopupController.awesomeVBox(selectedLesson, lessonData, deleteSelected, editSelected);
        middleVbox.setMinWidth(500);

        //right side of menu lesson
        Label newLesson = new Label("Nieuwe les");

        Label startLesson = new Label("Begintijd");
        ComboBox<LocalTime> startTimeSelect = timeComboBox(true);

        Label endLesson = new Label("Eindtijd");
        ComboBox<LocalTime> endTimeSelect = timeComboBox(false);


        Label teacherBox = new Label("Kies een docent");
        teacherSelect.setMinWidth(200);


        Label locationBox = new Label("Kies een lokaal");
        ComboBox<Classroom> locationSelect = new ComboBox<>();
        locationSelect.setMinWidth(200);


        locationSelect.setItems(FXCollections.observableArrayList(schedule.getClassrooms()));

        Button lessonAdder = new Button("Voeg klas toe");

        lessonAdder.setOnAction(event ->
        {
            //todo makes this do something
            try
            {
                this.schedule.addLesson(new Lesson(startTimeSelect.getValue(), endTimeSelect.getValue(), teacherSelect.getValue(), locationSelect.getValue(), this.selectedGroups));
                listView.setItems(FXCollections.observableArrayList(this.schedule.getLessons()));
            }
            catch (Exception e)
            {

            }

        });


        VBox rightVbox = PopupController.awesomeVBox(newLesson, startLesson, startTimeSelect, endLesson, endTimeSelect, teacherBox, teacherSelect, locationBox, locationSelect, allGroups, lessonAdder);


        rightVbox.setAlignment(Pos.TOP_LEFT);

        mainPane.setLeft(leftVbox);
        mainPane.setCenter(middleVbox);
        mainPane.setRight(rightVbox);
        mainPane.setPadding(new Insets(10, 500, 10, 10));


        return mainPane;
    }


    public void teacherUpdater()
    {
        teacherSelect.getItems().clear();
        teacherSelect.getItems().addAll(schedule.getTeachers());
        listView.setItems(FXCollections.observableArrayList(this.schedule.getLessons()));
    }

    public void classUpdater()
    {
        allGroups.getChildren().clear();
        allGroups.setSpacing(5);
        allGroups.setPrefHeight(110);
        for (int i = 0; i < this.schedule.getGroups().size(); i++)
        {
            CheckBox checkBox = new CheckBox(this.schedule.getGroups().get(i).getGroupName());
            int index = i;
            checkBox.setOnAction(event ->
            {
                if (checkBox.isSelected())
                {
                    selectedGroups.add(this.schedule.getGroups().get(index));
                }
                else
                {
                    selectedGroups.remove(this.schedule.getGroups().get(index));
                }
            });
            allGroups.getChildren().add(checkBox);
        }
    }


    private ComboBox timeComboBox(boolean isStartTime)
    {
        ComboBox comboBox = new ComboBox();
        comboBox.setMinWidth(200);

        if (isStartTime)
        {
            comboBox.setItems(FXCollections.observableArrayList(schedule.getAllStartingTimes()));
        }
        else
        {
            comboBox.setItems(FXCollections.observableArrayList(schedule.getAllEndingTimes()));
        }

        return comboBox;

    }

    public void refresh()
    {
        mainPane.getChildren().clear();
        mainPane = getPane();
    }


}
