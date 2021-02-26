package GUI;

import Data.Schedule;
import Data.Teacher;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class TabTeacher extends PopUpTab
{
    Schedule schedule;
    private TabLesson tabLesson;
    private Teacher selectedTeacher;


    protected TabTeacher(Schedule schedule, TabLesson tabLesson)
    {
        super.setPopUpName("Docenten");
        this.schedule = schedule;
        this.tabLesson = tabLesson;
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
        listView.setItems(FXCollections.observableArrayList(this.schedule.getTeachers()));


        VBox leftVbox = PopupController.awesomeVBox(currentTeacher, listView);
        leftVbox.setMinWidth(500);


        Label selectedTeacher = new Label("Geselecteerde Docent");

        FlowPane selectedFlowpane = new FlowPane();
        selectedFlowpane.setOrientation(Orientation.VERTICAL);
        VBox selectedNameVBox = new VBox();
        Label selectedTeacherName = new Label("Naam: ");
        TextField selectedNameField = new TextField();
        selectedNameVBox.getChildren().addAll(selectedTeacherName, selectedNameField);

        VBox selectedAgeVBox = new VBox();
        Label selectedTeacherAge = new Label("Leeftijd: ");
        TextField selectedAgeField = new TextField();
        selectedAgeVBox.getChildren().addAll(selectedTeacherAge, selectedAgeField);


        VBox selectedSubjectVBox = new VBox();
        Label selectedTeacherSubject = new Label("Vakgebied: ");
        TextField selectedSubjectField = new TextField();
        selectedSubjectVBox.getChildren().addAll(selectedTeacherSubject, selectedSubjectField);

        selectedFlowpane.getChildren().addAll(selectedNameVBox, selectedAgeVBox, selectedSubjectVBox);
        selectedFlowpane.setVgap(10);


        listView.getSelectionModel().selectedItemProperty().addListener(event ->
        {
            Teacher teacher = listView.getSelectionModel().getSelectedItem();
            if (this.selectedTeacher != teacher)
            {

                this.selectedTeacher = teacher;
                if (this.selectedTeacher != null)
                {
                    selectedNameField.setText(teacher.getName());
                    selectedAgeField.setText(teacher.getAge() + "");
                    selectedSubjectField.setText(teacher.getSubject());
                }
            }

        });


        Button editSelected = new Button("Wijzig Docent");
        editSelected.setOnAction(event ->
        {
            if (this.selectedTeacher != null)
            {
                this.selectedTeacher.setName(selectedNameField.getText());
                this.selectedTeacher.setAge(Integer.parseInt(selectedAgeField.getText()));
                this.selectedTeacher.setSubject(selectedSubjectField.getText());
                listView.getItems().clear();
                listView.setItems(FXCollections.observableArrayList(this.schedule.getTeachers()));
                tabLesson.teacherUpdater();
            }
        });


        Button deleteSelected = new Button("Verwijder Docent");
        deleteSelected.setOnAction(event ->
        {
            this.schedule.removeTeacher(listView.getSelectionModel().getSelectedItem());
            listView.getItems().clear();
            listView.setItems(FXCollections.observableArrayList(this.schedule.getTeachers()));
            tabLesson.teacherUpdater();
        });

        VBox middleVbox = new VBox();
        middleVbox.getChildren().addAll(selectedTeacher, selectedFlowpane, deleteSelected,editSelected);
        middleVbox.setSpacing(spacingDistance);
        middleVbox.setPadding(new Insets(10, 10, 10, 10));
        middleVbox.setMinWidth(500);


        Label newTeacher = new Label("Nieuwe Docent Toevoegen");


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

        addTeacherButton.setOnAction(event ->
        {

            if (Integer.parseInt(ageField.getText()) <= 0)
            {
                ErrorWindow errorWindow = new ErrorWindow("Error");
                errorWindow.ErrorStage("Voer een geldige leeftijd in.");
            }
            else
            {
                schedule.addTeacher(new Teacher(nameField.getText(), Integer.parseInt(ageField.getText()), subjectField.getText()));
                listView.getItems().clear();
                listView.setItems(FXCollections.observableArrayList(this.schedule.getTeachers()));

                tabLesson.teacherUpdater();

            }

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
