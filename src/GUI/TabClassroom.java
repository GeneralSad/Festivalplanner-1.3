package GUI;

import Data.Classroom;
import Data.Group;
import Data.Schedule;
import Data.Teacher;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Auteurs:
 * <p>
 * Deze klasse is voor om de tab lesson goed te weergeven
 */

public class TabClassroom extends PopUpTab
{

    //components
    private ListView<Classroom> listView = new ListView<>();
    private ArrayList<Group> selectedGroups = new ArrayList<>();
    private ComboBox<Teacher> teacherSelect = new ComboBox<>();
    private Classroom selectedClassroom;
    private VBox allGroups = new VBox();
    private BorderPane mainPane = new BorderPane();

    //rooster
    private Schedule schedule;

    protected TabClassroom(Schedule schedule)
    {
        super.setPopUpName("Lokalen");
        this.schedule = schedule;
        this.selectedClassroom = null;

        this.listView.setItems(schedule.getClassroomObservableList().sorted(Comparator.comparing(Classroom::getName)));
        classUpdater();
        this.teacherSelect.setItems(schedule.getTeacherObservableList());
        schedule.getGroupObservableList().addListener(this::onClassChanged);
    }

    @Override
    protected BorderPane getPane()
    {

        //linkerkant van het lesmenu
        Label currentLesson = new Label("Bestaande Klaslokalen");
        listView.setPrefWidth(500);
        VBox leftVbox = PopupController.awesomeVBox(currentLesson, listView);


        //midden van hget lesmenu
        Label selectedLesson = new Label("Geselecteerde klaslokaal");
        FlowPane classroomData = new FlowPane();
        classroomData.setOrientation(Orientation.VERTICAL);

        Label nameClassroom = new Label("Naam klaslokaal");
        TextField classroomName = new TextField();
        classroomName.setPromptText("Naam lokaal");
        //            classroomName.getParent().requestFocus();

        classroomData.getChildren().addAll(nameClassroom, classroomName);


        Button editSelected = new Button("Wijzig lokaal naam");
        editSelected.setOnAction(event ->
        {

            if (classroomName.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen naam ingevoerd.");
                alert.showAndWait();
            }
            else if (this.selectedClassroom != null)
            {

                this.selectedClassroom.setName(classroomName.getText());
                System.out.println(classroomName.getText());

                this.listView.refresh();
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener(event ->
        {
            Classroom classroom = listView.getSelectionModel().getSelectedItem();
            if (this.selectedClassroom != classroom)
            {
                this.selectedClassroom = classroom;
                if (this.selectedClassroom != null)
                {
                    classroomName.setText(this.selectedClassroom.getName());
                }
            }
        });

        VBox middleVbox = PopupController.awesomeVBox(selectedLesson, classroomData, editSelected);
        middleVbox.setMinWidth(500);


        mainPane.setLeft(leftVbox);
        mainPane.setCenter(middleVbox);
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

}
