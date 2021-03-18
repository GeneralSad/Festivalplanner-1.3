package GUI;

import Data.*;
import Simulator.Maploading.TiledMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Auteurs:
 *
 * Hier wordt ervoor gezorgd dat de applicatie goed wordt opgestart en hier worden ook wat test dingen in het programma gezet
 *
 */

public class GUI extends Application
{
    private Schedule schedule;
    private String filePath = "src/Data/storedSchedule";
    private DataStorage dataStorage = new DataStorage();

    public static void main(String[] args)
    {
        launch(GUI.class);
    }

    @Override
    public void start(Stage stage)
    {
        // probeer een schedule te laden
        initialise();

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane canvasContainer = new BorderPane();
        BorderPane simulator = new BorderPane();
        MainWindow mainWindow = new MainWindow(canvasContainer, this.schedule);
        canvasContainer.setCenter(mainWindow);

        HBox bottomHBox = new HBox();
        Button wijzingen = new Button("Wijzigen");
        bottomHBox.getChildren().add(wijzingen);
        wijzingen.setOnAction(event ->
        {
            PopupController popupController = new PopupController(stage, schedule);
        });


        Button saveScheduleButton = new Button("Opslaan");
        saveScheduleButton.setOnAction(event -> dataStorage.saveSchedule(this.filePath, this.schedule));


        Button reloadSchedule = new Button("Herladen");

        bottomHBox.getChildren().add(saveScheduleButton);
        bottomHBox.getChildren().add(reloadSchedule);
        canvasContainer.setBottom(bottomHBox);

        BorderPane borderPane = new BorderPane();

        Tab simulatorTab = new Tab("Simulator");
        Canvas canvas = new Canvas(2048, 2048);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        borderPane.setCenter(canvas);
        simulatorTab.setContent(borderPane);

        tabPane.getTabs().add(new Tab("Rooster", canvasContainer));
        tabPane.getTabs().add(simulatorTab);

        Scene scene = new Scene(tabPane);
        TiledMap tiledmap = new TiledMap("/TiledMaps/MapFinal.json");

        new AnimationTimer()
        {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                {
                    last = now;
                }
                if (now - last > 1e9)
                {
                    graphicsContext.setImageSmoothing(false);
                    FXGraphics2D fxGraphics2D =  new FXGraphics2D(graphicsContext);
                    fxGraphics2D.setBackground(Color.WHITE);
                    fxGraphics2D.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
                    double heck = 0;
                    //fxGraphics2D.translate(heck += 0.1, heck += 0.1);
                    tiledmap.draw(fxGraphics2D);
                    addMouseScrolling(canvas);
                    last = now;
                }
            }
        }.start();

        stage.setScene(scene);
        stage.setTitle("School simulatie");
        stage.setMaximized(true);
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
            classrooms.add(new Classroom(6));
            classrooms.add(new Classroom(7));
            classrooms.add(new Classroom(8));
            ArrayList<Lesson> lessons = new ArrayList<>();
            lessons.add(new Lesson(LocalTime.of(15, 30), LocalTime.of(16, 30), teachers.get(0), classrooms.get(0), groups));
            lessons.add(new Lesson(LocalTime.of(9, 0), LocalTime.of(10, 0), teachers.get(1), classrooms.get(1), groups.get(1)));
            lessons.add(new Lesson(LocalTime.of(10, 0), LocalTime.of(11, 0), teachers.get(2), classrooms.get(2), groups.get(0)));
            lessons.add(new Lesson(LocalTime.of(9, 0), LocalTime.of(11, 0), teachers.get(0), classrooms.get(3), groups.get(3)));
            lessons.add(new Lesson(LocalTime.of(12, 30), LocalTime.of(13, 30), teachers.get(1), classrooms.get(4), groups.get(2)));
            lessons.add(new Lesson(LocalTime.of(13, 30), LocalTime.of(14, 30), teachers.get(2), classrooms.get(3), groups.get(0)));
            lessons.add(new Lesson(LocalTime.of(16, 30), LocalTime.of(17, 30), teachers.get(0), classrooms.get(2), groups));
            lessons.add(new Lesson(LocalTime.of(11, 0), LocalTime.of(12, 0), teachers.get(0), classrooms.get(7), groups));
            this.schedule = new Schedule(lessons, teachers, groups, classrooms);
        }
        else
        {
            System.out.println("properly loaded a schedule");
        }
    }

    public void addMouseScrolling(Node node) {
        node.setOnScroll((ScrollEvent event) -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0){
                zoomFactor = 2.0 - zoomFactor;
            }

            if (!(node.getScaleY() * zoomFactor > 5) && !(node.getScaleY() * zoomFactor < 0.9)) {
                //System.out.println("X: " + node.getScaleX());
                //System.out.println("Y: " + node.getScaleY());


                node.setScaleX(node.getScaleX() * zoomFactor);
                node.setScaleY(node.getScaleY() * zoomFactor);
            }

        });
    }

}
