package GUI;

import Data.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.ArrayList;

public class GUI extends Application
{
    private Schedule schedule;
    private String filePath = "src/Data/storedSchedule";


    public static void main(String[] args)
    {
        launch(GUI.class);
    }

    @Override
    public void start(Stage stage) throws Exception
    {

        // probeer een schedule te laden
        initialise();

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane canvasContainer = new BorderPane();
        canvasContainer.setCenter(new MainWindow(canvasContainer, this.schedule));


        HBox bottomHBox = new HBox();
        Button wijzingen = new Button("Wijzigen");
        bottomHBox.getChildren().add(wijzingen);
        wijzingen.setOnAction(event ->
        {
            PopupController popupController = new PopupController(stage, schedule);
        });


        Button saveScheduleButton = new Button("Opslaan");
        saveScheduleButton.setOnAction(event ->
        {
            DataStorage.saveSchedule(this.filePath, this.schedule);
        });
        bottomHBox.getChildren().add(saveScheduleButton);
        canvasContainer.setBottom(bottomHBox);

        tabPane.getTabs().add(new Tab("Rooster", canvasContainer));

        FlowPane test = new FlowPane();
        test.getChildren().add(new TextField("testing"));
        tabPane.getTabs().add(new Tab("Testing", test));

        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.setTitle("School simulatie");
        stage.show();


    }

    private void initialise()
    {
        this.schedule = DataStorage.loadSchedule(this.filePath);
        if (this.schedule == null)
        {
            System.out.println("Couldn't load a schedule");
            ArrayList<Teacher> teachers = new ArrayList<>();
            teachers.add(new Teacher("Etiënne", 30, "Hardware"));
            teachers.add(new Teacher("Johan", 36, "Graphics"));
            teachers.add(new Teacher("Maurice", -1, "Programmeren"));
            ArrayList<Group> groups = new ArrayList<>();
            groups.add(new Group("C1"));
            groups.add(new Group("C2"));
            groups.add(new Group("C3"));
            groups.add(new Group("C4"));
            ArrayList<Classroom> classrooms = new ArrayList<>();
            classrooms.add(new Classroom(1));
            classrooms.add(new Classroom(2));
            classrooms.add(new Classroom(3));
            classrooms.add(new Classroom(4));
            classrooms.add(new Classroom(5));
            ArrayList<Lesson> lessons = new ArrayList<>();
            lessons.add(new Lesson(LocalTime.of(15, 30), LocalTime.of(16, 30), teachers.get(0), classrooms.get(0), groups));
            lessons.add(new Lesson(LocalTime.of(9, 00), LocalTime.of(10, 0), teachers.get(1), classrooms.get(1), groups.get(1)));
            lessons.add(new Lesson(LocalTime.of(10, 00), LocalTime.of(11, 0), teachers.get(2), classrooms.get(2), groups.get(0)));
            lessons.add(new Lesson(LocalTime.of(10, 00), LocalTime.of(12, 0), teachers.get(0), classrooms.get(3), groups.get(3)));
            lessons.add(new Lesson(LocalTime.of(12, 30), LocalTime.of(13, 30), teachers.get(1), classrooms.get(4), groups.get(2)));
            lessons.add(new Lesson(LocalTime.of(13, 30), LocalTime.of(14, 30), teachers.get(2), classrooms.get(3), groups.get(0)));
            lessons.add(new Lesson(LocalTime.of(16, 30), LocalTime.of(17, 30), teachers.get(0), classrooms.get(2), groups));
            lessons.add(new Lesson(LocalTime.of(11, 00), LocalTime.of(12, 00), teachers.get(0), classrooms.get(0), groups));
            this.schedule = new Schedule(lessons, teachers, groups);
        }
        else
        {
            System.out.println("properly loaded a schedule");
        }
    }
}
