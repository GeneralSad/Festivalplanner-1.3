package GUI;

import Data.Schedule;
import Data.Teacher;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * Auteurs:
 * <p>
 * Deze klasse is voor om de tab teacher goed te weergeven
 */

public class TabTeacher extends PopUpTab
{
    private Schedule schedule;
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
        listView.setItems(schedule.getTeacherObservableList());


        VBox leftVbox = PopupController.awesomeVBox(currentTeacher, listView);
        leftVbox.setMinWidth(500);


        Label selectedTeacher = new Label("Geselecteerde Docent");

        FlowPane selectedFlowpane = new FlowPane();
        selectedFlowpane.setOrientation(Orientation.VERTICAL);
        VBox selectedNameVBox = new VBox();
        Label selectedTeacherName = new Label("Naam: ");
        TextField selectedNameField = new TextField();
        selectedNameField.setPromptText("Naam");
        selectedNameVBox.getChildren().addAll(selectedTeacherName, selectedNameField);

        VBox selectedAgeVBox = new VBox();
        Label selectedTeacherAge = new Label("Leeftijd: ");
        TextField selectedAgeField = new TextField();
        selectedAgeField.setPromptText("Leeftijd");
        selectedAgeVBox.getChildren().addAll(selectedTeacherAge, selectedAgeField);


        VBox selectedSubjectVBox = new VBox();
        Label selectedTeacherSubject = new Label("Vakgebied: ");
        TextField selectedSubjectField = new TextField();
        selectedSubjectField.setPromptText("Vakgebied");
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
            if (selectedNameField.getText().isEmpty() && selectedSubjectField.getText().isEmpty() && selectedAgeField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er zijn geen velden ingevoerd.");
                alert.showAndWait();
            }
            else if (selectedNameField.getText().isEmpty() && selectedSubjectField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen naam en vakgebied ingevoerd.");
                alert.showAndWait();
            }
            else if (selectedNameField.getText().isEmpty() && selectedAgeField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen naam en leeftijd ingevoerd.");
                alert.showAndWait();
            }
            else if (selectedSubjectField.getText().isEmpty() && selectedAgeField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen vakgebied en leeftijd ingevoerd.");
                alert.showAndWait();
            }
            else if (selectedSubjectField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen vakgebied ingevoerd.");
                alert.showAndWait();
            }
            else if (selectedNameField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen naam ingevoerd.");
                alert.showAndWait();
            }
            else if (selectedAgeField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen leeftijd ingevoerd.");
                alert.showAndWait();
            }
            else if (Integer.parseInt(selectedAgeField.getText()) <= 0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Voer een geldige leeftijd in");
                alert.showAndWait();
            }
            else if (this.selectedTeacher != null)
            {
                this.selectedTeacher.setName(selectedNameField.getText());
                this.selectedTeacher.setAge(Integer.parseInt(selectedAgeField.getText()));
                this.selectedTeacher.setSubject(selectedSubjectField.getText());
            }
        });


        Button deleteSelected = new Button("Verwijder Docent");
        deleteSelected.setOnAction(event -> this.schedule.removeTeacher(listView.getSelectionModel().getSelectedItem()));

        VBox middleVbox = new VBox();
        middleVbox.getChildren().addAll(selectedTeacher, selectedFlowpane, deleteSelected, editSelected);
        middleVbox.setSpacing(spacingDistance);
        middleVbox.setPadding(new Insets(10, 10, 10, 10));
        middleVbox.setMinWidth(500);


        Label newTeacher = new Label("Nieuwe Docent Toevoegen");


        VBox nameVBox = new VBox();
        Label teacherName = new Label("Naam: ");
        TextField nameField = new TextField();
        nameField.setPromptText("Naam");
        nameVBox.getChildren().addAll(teacherName, nameField);

        VBox ageVBox = new VBox();
        Label teacherAge = new Label("Leeftijd: ");
        TextField ageField = new TextField();
        ageField.setPromptText("Leeftijd");
        ageVBox.getChildren().addAll(teacherAge, ageField);


        VBox subjectVBox = new VBox();
        Label teacherSubject = new Label("Vakgebied: ");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Vakgebied");
        subjectVBox.getChildren().addAll(teacherSubject, subjectField);


        Button addTeacherButton = new Button("Voeg docent toe");

        addTeacherButton.setOnAction(event ->
        {

            if (nameField.getText().isEmpty() && subjectField.getText().isEmpty() && ageField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er zijn geen velden ingevoerd.");
                alert.showAndWait();
            }
            else if (nameField.getText().isEmpty() && subjectField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen naam en vakgebied ingevoerd.");
                alert.showAndWait();
            }
            else if (nameField.getText().isEmpty() && ageField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen naam en leeftijd ingevoerd.");
                alert.showAndWait();
            }
            else if (subjectField.getText().isEmpty() && ageField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen vakgebied en leeftijd ingevoerd.");
                alert.showAndWait();
            }
            else if (subjectField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen vakgebied ingevoerd.");
                alert.showAndWait();
            }
            else if (nameField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen naam ingevoerd.");
                alert.showAndWait();
            }
            else if (ageField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Er is geen leeftijd ingevoerd.");
                alert.showAndWait();
            }
            else if (Integer.parseInt(ageField.getText()) <= 0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Er is iets fout gegaan");
                alert.setContentText("Voer een geldige leeftijd in");
                alert.showAndWait();
            }
            else
            {
                schedule.addTeacher(new Teacher(nameField.getText(), Integer.parseInt(ageField.getText()), subjectField.getText()));
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
