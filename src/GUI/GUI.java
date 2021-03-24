package GUI;

import Data.*;
import Simulator.Maploading.TiledMap;
import Simulator.Simulator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.time.LocalTime;
import java.util.ArrayList;

//import java.awt.*;

/**
 * Auteurs:
 * <p>
 * Hier wordt ervoor gezorgd dat de applicatie goed wordt opgestart en hier worden ook wat test dingen in het programma gezet
 */


public class GUI extends Application
{
    private Schedule schedule;
    private String filePath = "src/Data/storedSchedule";
    private DataStorage dataStorage = new DataStorage();
    private Simulator simulator;
    Label speedFactorLabel = new Label("");


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
        BorderPane simulatorPane = new BorderPane();
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
        reloadSchedule.setOnAction(event -> {
            Schedule loaded = DataStorage.loadSchedule(filePath);
            if (loaded != null) {
                System.out.println("Setting new schedule");
                this.schedule.setScheduleTo(loaded);
            } else {
                System.out.println("Loaded was null");
            }
        });

        bottomHBox.getChildren().add(saveScheduleButton);
        bottomHBox.getChildren().add(reloadSchedule);
        canvasContainer.setBottom(bottomHBox);

        Pane pane = new Pane();
        BorderPane borderPane = new BorderPane();
        Tab simulatorTab = new Tab("Simulator");
        Canvas canvas = new Canvas(2048, 2048);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        pane.getChildren().add(canvas);
        pane.setPrefSize(1280, 720);
        borderPane.setCenter(pane);


        FlowPane topBar = new FlowPane();
        topBar.setAlignment(Pos.CENTER);
        Label timeLabel = new Label("(9:00");
        Font font = new Font("Courier", 48);
        timeLabel.setFont(font);
        topBar.getChildren().add(timeLabel);
        borderPane.setTop(topBar);
        Background background = new Background(new BackgroundFill(javafx.scene.paint.Color.gray(0.5), CornerRadii.EMPTY, Insets.EMPTY));
        borderPane.setBackground(background);

        VBox vBox = new VBox();


        HBox speedSettingsBox = new HBox();
        Button speedUpButton = new Button("Versnel");
        speedUpButton.setOnAction(event ->
        {
            int speedfactor = simulator.getSpeedfactor();
            simulator.setSpeedfactor(speedfactor + 10);
            updateLabel();
        });


        Button slowDownButton = new Button("Vertraag");
        slowDownButton.setOnAction(event ->
        {
            int speedfactor = simulator.getSpeedfactor();
            simulator.setSpeedfactor(speedfactor - 10);
            updateLabel();
        });

        // update the label so text is displayed at startup
        speedFactorLabel.setText("De simulatie speelt op normale snelheid");

        vBox.getChildren().addAll(speedFactorLabel, speedSettingsBox);
        speedFactorLabel.setFont(new Font("Arial", 16));
        speedSettingsBox.getChildren().addAll(slowDownButton, speedUpButton);
        speedSettingsBox.setAlignment(Pos.BOTTOM_LEFT);
        borderPane.setBottom(vBox);

        simulatorTab.setContent(borderPane);


        tabPane.getTabs().add(new Tab("Rooster", canvasContainer));
        tabPane.getTabs().add(simulatorTab);

        Scene scene = new Scene(tabPane);


        addMouseScrolling(canvas);

        simulator = new Simulator(schedule);
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

                graphicsContext.setImageSmoothing(false);
                FXGraphics2D fxGraphics2D = new FXGraphics2D(graphicsContext);
                fxGraphics2D.setBackground(Color.GRAY);

                fxGraphics2D.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

                simulator.draw(fxGraphics2D);
                long deltatime = (now - last);
                simulator.update(deltatime);

                timeLabel.setText(simulator.getFormattedTime());
                addMouseClickDrag(canvas, fxGraphics2D);
                last = now;

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
//            System.out.println("Couldn't load a schedule");
//            ArrayList<Teacher> teachers = new ArrayList<>();
//            teachers.add(new Teacher("Etiënne", 30, "Hardware"));
//            teachers.add(new Teacher("Johan", 36, "Graphics"));
//            teachers.add(new Teacher("Maurice", -1, "Programmeren"));
//            ArrayList<Group> groups = new ArrayList<>();
//            Group c1 = new Group("C1");
//            groups.add(c1);
//            groups.add(new Group("C2"));
//            groups.add(new Group("C3"));
//            groups.add(new Group("C4"));
//
//
//            ArrayList<Classroom> classrooms = new ArrayList<>();
//            classrooms.add(new Classroom(1, new ClassroomEntryPoint(550, 550)));
//            classrooms.add(new Classroom(2, new ClassroomEntryPoint(1000, 550)));
//            classrooms.add(new Classroom(3, new ClassroomEntryPoint(500, 820)));
//            classrooms.add(new Classroom(4, new ClassroomEntryPoint(600, 820)));
//            classrooms.add(new Classroom(5, new ClassroomEntryPoint(950, 820)));
//            classrooms.add(new Classroom(6, new ClassroomEntryPoint(1050, 820)));
//            classrooms.add(new Classroom(7, new ClassroomEntryPoint(500, 1120)));
//            classrooms.add(new Classroom(8, new ClassroomEntryPoint(600, 1120)));
//            ArrayList<Lesson> lessons = new ArrayList<>();
//            lessons.add(new Lesson(LocalTime.of(15, 30), LocalTime.of(16, 30), teachers.get(0), classrooms.get(0), groups));
//            lessons.add(new Lesson(LocalTime.of(9, 0), LocalTime.of(10, 0), teachers.get(1), classrooms.get(1), groups.get(1)));
//            lessons.add(new Lesson(LocalTime.of(10, 0), LocalTime.of(11, 0), teachers.get(2), classrooms.get(2), groups.get(0)));
//            lessons.add(new Lesson(LocalTime.of(9, 0), LocalTime.of(11, 0), teachers.get(0), classrooms.get(3), groups.get(3)));
//            lessons.add(new Lesson(LocalTime.of(12, 30), LocalTime.of(13, 30), teachers.get(1), classrooms.get(4), groups.get(2)));
//            lessons.add(new Lesson(LocalTime.of(13, 30), LocalTime.of(14, 30), teachers.get(2), classrooms.get(3), groups.get(0)));
//            lessons.add(new Lesson(LocalTime.of(16, 30), LocalTime.of(17, 30), teachers.get(0), classrooms.get(2), groups));
//            lessons.add(new Lesson(LocalTime.of(11, 0), LocalTime.of(12, 0), teachers.get(0), classrooms.get(7), groups));
//
//            for (Group group : groups)
//            {
//                group.addStudent(new Student(group.getGroupName() + "test", 12, group));
//            }
//
//            this.schedule = new Schedule(lessons, teachers, groups, classrooms);


        }
        else
        {
            System.out.println("properly loaded a schedule");
        }
    }

    private void updateLabel()
    {
        int speedfactor = simulator.getSpeedfactor();
        if (speedfactor == 0)
        {
            speedFactorLabel.setText("De simulatie speelt op normale snelheid");
        }
        else
        {
            speedFactorLabel.setText("De simulatie word " + speedfactor + " keer sneller afgespeeld");
        }
    }

    public void addMouseScrolling(Node node)
    {
        node.setOnScroll((ScrollEvent event) ->
        {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0)
            {
                zoomFactor = 2.0 - zoomFactor;
            }

            if (!(node.getScaleY() * zoomFactor > 5) && !(node.getScaleY() * zoomFactor < 1))
            {
                node.setScaleX(node.getScaleX() * zoomFactor);
                node.setScaleY(node.getScaleY() * zoomFactor);
            }

        });
    }

    private double lastX = -10000;
    private double lastY = -10000;
    private double x = 0;
    private double y = 0;

    public void addMouseClickDrag(Node node, FXGraphics2D fxGraphics2D)
    {
        node.setOnMouseDragged((event) ->
        {

            if (lastX == -10000 && lastY == -10000)
            {
                lastX = event.getX();
                lastY = event.getY();
            }

            if ((x + (event.getX() - lastX)) > -1200 && (y + (event.getY() - lastY)) > -1200 && (x + (event.getX() - lastX)) < 1500 && (y + (event.getY() - lastY)) < 550)
            {

                fxGraphics2D.translate(event.getX() - lastX, event.getY() - lastY);

                this.x += (event.getX() - lastX);
                this.y += (event.getY() - lastY);

                //System.out.println("X: " + (x + (event.getX() - lastX) + ", Y: " + (y + (event.getY() - lastY))));

                lastX = event.getX();
                lastY = event.getY();

            }

        });

        node.setOnMouseReleased(event ->
        {

            lastX = -10000;
            lastY = -10000;

            System.out.println("X: " + event.getX() + "Y: " +  event.getY());

        });

    }

    public static TiledMap getTiledmap()
    {
        return Simulator.getTiledmap();
    }

    public static TiledMap getWalkablemap()
    {
        return new TiledMap("/TiledMaps/MapFinal.json", true);
    }

}
