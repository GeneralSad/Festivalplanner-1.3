package GUI;

import Data.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

public class TabLesson extends PopUpTab
{
    //distance
    private int spacingDistance = 10;

    //components
    private ListView<Lesson> listView = new ListView<>();
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

        this.listView.setItems(schedule.getLessons().sorted(Comparator.comparing(Lesson::getBeginTime)));
        classUpdater();
        this.teacherSelect.setItems(schedule.getTeacherObservableList());
        schedule.getGroupObservableList().addListener(this::onClassChanged);
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
        ComboBox<Teacher> selectedTeachterComboBox = new ComboBox<>();
        selectedTeachterComboBox.setItems(schedule.getTeacherObservableList());

        Label selectedTocationBox = new Label("Lokaal");
        ComboBox<Classroom> selectedLocationSelect = new ComboBox<>();
        selectedLocationSelect.setItems(FXCollections.observableArrayList(schedule.getClassroomArrayList()));
        selectedLocationSelect.setMinWidth(200);

        lessonData.getChildren().addAll(selectedStartLesson, selectedStartTimeSelect, selectedEndLesson, selectedEndTimeSelect, selectedTeacherBox, selectedTeachterComboBox, selectedTocationBox, selectedLocationSelect);


        Button deleteSelected = new Button("Verwijder les");
        Button editSelected = new Button("Wijzig les");
        editSelected.setOnAction(event ->
        {
            if (this.selectedLesson != null)
            {
                LocalTime beginTime = selectedStartTimeSelect.getSelectionModel().getSelectedItem();
                LocalTime endTime = selectedEndTimeSelect.getSelectionModel().getSelectedItem();

                Lesson lesson = this.selectedLesson.clone();

                ArrayList<Lesson> overlappingLessons = this.schedule.getOverlappingTime(beginTime, endTime);
                overlappingLessons.remove(this.selectedLesson);

                lesson.setBeginTime(beginTime);
                lesson.setEndTime(endTime);
                lesson.setClassroom(selectedLocationSelect.getSelectionModel().getSelectedItem());
                lesson.setTeacher(selectedTeachterComboBox.getSelectionModel().getSelectedItem());


                try
                {
                    if (this.schedule.checkLesson(lesson, overlappingLessons))
                    {
                        schedule.removeLesson(this.selectedLesson);
                        schedule.addLesson(lesson);
                    }
                }
                catch (IllegalArgumentException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Er is iets fout gegaan");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }


                this.listView.refresh();
            }
        });


        deleteSelected.setOnAction(event -> schedule.removeLesson(this.selectedLesson));

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


        locationSelect.setItems(FXCollections.observableArrayList(schedule.getClassroomArrayList()));

        Button lessonAdder = new Button("Voeg klas toe");

        lessonAdder.setOnAction(event ->
        {
            try
            {
                this.schedule.addLesson(new Lesson(startTimeSelect.getValue(), endTimeSelect.getValue(), teacherSelect.getValue(), locationSelect.getValue(), this.selectedGroups));

            }
            catch (NullPointerException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Niet alles is ingevuld");

                alert.showAndWait();
            }
            catch (IllegalArgumentException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText(e.getMessage());

                alert.showAndWait();
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


    private void classUpdater()
    {
        allGroups.getChildren().clear();
        allGroups.setSpacing(5);
        allGroups.setPrefHeight(110);
        for (int i = 0; i < this.schedule.getGroupArrayList().size(); i++)
        {
            CheckBox checkBox = new CheckBox(this.schedule.getGroupArrayList().get(i).getGroupName());
            int index = i;
            checkBox.setOnAction(event ->
            {
                if (checkBox.isSelected())
                {
                    selectedGroups.add(this.schedule.getGroupArrayList().get(index));
                }
                else
                {
                    selectedGroups.remove(this.schedule.getGroupArrayList().get(index));
                }
            });
            allGroups.getChildren().add(checkBox);
        }
    }


    private void onClassChanged(ListChangeListener.Change c)
    {

        if (c.next())
        {
            classUpdater();
        }
    }


    private ComboBox<LocalTime> timeComboBox(boolean isStartTime)
    {
        ComboBox<LocalTime> comboBox = new ComboBox<>();
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


}
