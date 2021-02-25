package GUI;

import Data.Group;
import Data.Schedule;
import Data.Student;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class TabClass extends PopUpTab
{
    private int spacingDistance = 10;
    private ArrayList<String> classes = new ArrayList<>();
    Schedule schedule;

    public TabClass(Schedule schedule)
    {
        super.setPopUpName("Klassen");
        this.schedule = schedule;
    }


    
    @Override
    protected BorderPane getPane()
    {
        BorderPane mainPane = new BorderPane();

        //linker kant
        //klassen listview
        HBox classInfo = new HBox();

        Label currentLesson = new Label("Bestaande klassen");
        ListView<Group> listViewClass = new ListView<>();
        listViewClass.setItems(FXCollections.observableArrayList(schedule.getGroups()));
        listViewClass.setPrefWidth(250);


        VBox leftVboxClasses = new VBox();
        leftVboxClasses.getChildren().addAll(currentLesson, listViewClass);
        leftVboxClasses.setSpacing(spacingDistance);
        leftVboxClasses.setPadding(new Insets(10, 10, 10, 10));


        //Geselecteerde klas listview
        Label selectedClass = new Label("Geselecteerde klas");
        ListView<Student> listViewStudent = new ListView<>();
        listViewStudent.setPrefWidth(250);

        Button deleteClass = new Button("Verwijder klas");

        deleteClass.setOnAction(event -> {

            schedule.removeGroup(listViewClass.getSelectionModel().getSelectedItem());
            listViewClass.getItems().clear();
            listViewClass.setItems(FXCollections.observableArrayList(this.schedule.getGroups()));

        });

        VBox leftVboxStudent = new VBox();
        leftVboxStudent.getChildren().addAll(selectedClass, listViewStudent,deleteClass);
        leftVboxStudent.setSpacing(spacingDistance);
        leftVboxStudent.setPadding(new Insets(10, 10, 10, 10));

        classInfo.getChildren().addAll(leftVboxClasses, leftVboxStudent);
        mainPane.setLeft(classInfo);


        //midden kant
        HBox studentInfo = new HBox();

        //student information
        Label selectedStudent = new Label("Geselecteerde student");
        TextArea studentData = new TextArea();
        studentData.setPrefSize(250, 400);
        studentData.setEditable(false);
        listViewStudent.setPrefWidth(250);

        Button deleteStudent = new Button("Verwijder student");

        deleteStudent.setOnAction(event -> {

            schedule.getGroups().get(listViewClass.getSelectionModel().getSelectedIndex()).removeStudent(listViewStudent.getSelectionModel().getSelectedItem());

        });



        VBox middleVboxStudent = new VBox();
        middleVboxStudent.getChildren().addAll(selectedStudent, studentData, deleteStudent);
        middleVboxStudent.setSpacing(spacingDistance);
        middleVboxStudent.setPadding(new Insets(10, 10, 10, 10));


        //student toevoegen
        Label newStudent = new Label("Nieuwe student");


        Label newName = new Label("Naam");
        TextField inputName = new TextField();
        inputName.setMaxWidth(200);

        Label newAge = new Label("Leeftijd");
        TextField inputAge = new TextField();
        inputAge.setMaxWidth(200);

        Button submitStudent = new Button("Voeg student toe");

        submitStudent.setOnAction(event -> {

            schedule.getGroups().get(listViewClass.getSelectionModel().getSelectedIndex()).addStudent(new Student(inputName.getText(), Integer.parseInt(inputAge.getText()), listViewClass.getSelectionModel().getSelectedItem()));

        });

        VBox addersStudenten = new VBox();
        addersStudenten.getChildren().addAll(newName, inputName, newAge, inputAge);
        addersStudenten.setSpacing(spacingDistance);
        addersStudenten.setPadding(new Insets(0,0, 265, 0));

        VBox addStudent = new VBox();
        addStudent.getChildren().addAll(newStudent, addersStudenten, submitStudent);
        addStudent.setSpacing(spacingDistance);
        addStudent.setPadding(new Insets(10, 10, 10, 10));
        addStudent.setMinWidth(200);


        //rechter kant
        //toevoegen klas
        //student toevoegen
        Label newClas = new Label("Nieuwe klas");

        Label newClass = new Label("Naam");
        TextField inputClass = new TextField();
        inputName.setMaxWidth(200);



        Button submitClass = new Button("Voeg klas toe");

        submitClass.setOnAction(event -> {

            schedule.addGroup(new Group(inputClass.getText()));

        });

        VBox addersClass = new VBox();
        addersClass.getChildren().addAll(newClass, inputClass);
        addersClass.setSpacing(spacingDistance);
        addersClass.setPadding(new Insets(0,0, 300, 0));

        VBox addClass = new VBox();
        addClass.getChildren().addAll(newClas, addersClass, submitClass);
        addClass.setSpacing(spacingDistance);
        addClass.setPadding(new Insets(10, 10, 10, 10));
        addClass.setMinWidth(200);


        studentInfo.getChildren().addAll(middleVboxStudent, addStudent, addClass);
        mainPane.setCenter(studentInfo);

        return mainPane;

    }
}
