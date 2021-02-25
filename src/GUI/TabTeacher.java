package GUI;

import Data.Schedule;
import Data.Teacher;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TabTeacher extends PopUpTab
{
    Schedule schedule;


    protected TabTeacher(Schedule schedule)
    {
        super.setPopUpName("Docenten");
        this.schedule = schedule;
    }

    private int spacingDistance = 10;
    int i = 0;

    @Override
    protected BorderPane getPane()
    {
        BorderPane mainPane = new BorderPane();


        //left side of the menu teacher
        Label currentTeacher = new Label("Bestaande Docenten");
        ListView<Teacher> listView = new ListView<>();


        VBox leftVbox = new VBox();
        leftVbox.getChildren().addAll(currentTeacher, listView);
        leftVbox.setSpacing(spacingDistance);
        leftVbox.setPadding(new Insets(10, 10, 10, 10));
        leftVbox.setMinWidth(500);


        Label selectedTeacher = new Label("Geselecteerde Docent");
        Label teacherData = new Label();



        Button deleteSelected = new Button("Verwijder Docent");

        deleteSelected.setOnAction(event -> {

            schedule.removeTeacher(listView.getSelectionModel().getSelectedItem());

        });

        VBox middleVbox = new VBox();
        middleVbox.getChildren().addAll(selectedTeacher, teacherData, deleteSelected);
        middleVbox.setSpacing(spacingDistance);
        middleVbox.setPadding(new Insets(10, 10, 10, 10));
        middleVbox.setMinWidth(500);


        Label newTeacher = new Label("Nieuwe Docent");


        VBox nameVBox = new VBox();
        Label teacherName = new Label("Naam: ");
        TextField nameField = new TextField();
        nameVBox.getChildren().addAll(teacherName, nameField);

        VBox ageVBox = new VBox();
        Label teacherAge = new Label("Leeftijd: ");
        TextField ageField = new TextField();
        ageVBox.getChildren().addAll(teacherAge, ageField);


        VBox subjectVBox = new VBox();
        Label teacherSubject = new Label("Vakgebied: ");
        TextField subjectField = new TextField();
        subjectVBox.getChildren().addAll(teacherSubject, subjectField);


        Button addTeacherButton = new Button("Voeg docent toe");

        addTeacherButton.setOnAction(event -> {

            schedule.addTeacher(new Teacher(teacherName.getText(), Integer.parseInt(teacherAge.getText()), teacherSubject.getText()));

        });


        VBox rightVbox = new VBox();
        rightVbox.getChildren().addAll(newTeacher, nameVBox, ageVBox, subjectVBox, addTeacherButton);
        rightVbox.setSpacing(spacingDistance);
        rightVbox.setPadding(new Insets(10, 10, 10, 10));
        rightVbox.setMinWidth(250);


        mainPane.setLeft(leftVbox);
        mainPane.setCenter(middleVbox);
        mainPane.setRight(rightVbox);


        mainPane.setPadding(new Insets(10, 500, 10, 10));


        Scene scene = new Scene(mainPane);
        return mainPane;

    }





}
