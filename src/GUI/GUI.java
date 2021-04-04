package GUI;

import Data.*;
import Simulator.Maploading.TiledMap;
import Simulator.NPC.NPC;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import Simulator.CameraSystem.Camera;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.time.LocalTime;
import java.util.ArrayList;

//import java.awt.*;

/**
 * Auteurs:
 * <p>
 * Hier wordt ervoor gezorgd dat de applicatie goed wordt opgestart en hier worden ook wat test dingen in het programma gezet
 */


public class GUI extends Application {
    private Schedule schedule;
    private String filePath = "src/Data/storedSchedule";
    private DataStorage dataStorage = new DataStorage();
    private Simulator simulator;
    private Label speedFactorLabel = new Label("");
    private Canvas canvas = new Canvas(1920, 1048);
    private FXGraphics2D fxGraphics2D;

    private boolean followingNPC;
    private NPC npcBeingFollowed;

    public static void main(String[] args)
    {
        launch(GUI.class);
    }

    @Override
    public void start(Stage stage) {
        // probeer een schedule te laden
        initialise();

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane canvasContainer = new BorderPane();
        BorderPane simulatorPane = new BorderPane();
        MainWindow mainWindow = new MainWindow(canvasContainer, this.schedule);
        canvasContainer.setCenter(mainWindow);

        HBox bottomHBox = new HBox(10);
        Button wijzingen = new Button("Rooster Wijzigen");
        bottomHBox.getChildren().add(wijzingen);
        wijzingen.setOnAction(event ->
        {
            PopupController popupController = new PopupController(stage, schedule);
        });




        Button saveScheduleButton = new Button("Rooster Opslaan");
        saveScheduleButton.setOnAction(event -> dataStorage.saveSchedule(this.filePath, this.schedule));
        bottomHBox.getChildren().add(saveScheduleButton);

        Pane pane = new Pane();
        BorderPane borderPane = new BorderPane();
        Tab simulatorTab = new Tab("Simulator");
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


        Button magic2 = new Button("Kwartier terug");
        magic2.setOnAction(event ->
        {
            simulator.loadNPCs();
            updateLabel();
        });

        Button ramp = new Button("Ramp");
        ramp.setOnAction(event ->
        {
            simulator.disaster();
        });

        // update the label so text is displayed at startup
        speedFactorLabel.setText("De simulatie speelt op normale snelheid");

        vBox.getChildren().addAll(speedFactorLabel, speedSettingsBox);
        speedFactorLabel.setFont(new Font("Arial", 16));
        speedSettingsBox.getChildren().addAll(slowDownButton, speedUpButton, magic2, ramp);
        speedSettingsBox.setAlignment(Pos.BOTTOM_LEFT);
        borderPane.setBottom(vBox);

        simulatorTab.setContent(borderPane);


        tabPane.getTabs().add(new Tab("Rooster", canvasContainer));
        tabPane.getTabs().add(simulatorTab);

        Scene scene = new Scene(tabPane);

        simulator.setCamera(new Camera(canvas.getWidth(), canvas.getHeight()));

        addMouseScrolling(canvas);

        addMouseClickDrag(canvas, simulator.getCamera());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }

                graphicsContext.setImageSmoothing(false);
                fxGraphics2D = new FXGraphics2D(graphicsContext);
                fxGraphics2D.setTransform(new AffineTransform());
                fxGraphics2D.setBackground(Color.GRAY);

                fxGraphics2D.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());


                fxGraphics2D.translate(simulator.getCamera().getX(), simulator.getCamera().getY());

                simulator.draw(fxGraphics2D, canvas.getWidth(), canvas.getHeight());
                long deltatime = (now - last);
                simulator.update(deltatime);


                timeLabel.setText(simulator.getFormattedTime());


                last = now;

            }
        }.start();



        Button reloadSchedule = new Button("Rooster Reset");
        reloadSchedule.setOnAction(event ->
        {
            Schedule loaded = DataStorage.loadSchedule(filePath);
            if (loaded != null)
            {
                System.out.println("Setting new schedule");
                this.schedule.setScheduleTo(loaded);
            }
            else
            {
                System.out.println("Loaded was null");
            }
        });

        Button reloadSim = new Button("Simulatie Herladen");
        reloadSchedule.setOnAction(event -> {
            Schedule loaded = DataStorage.loadSchedule(filePath);
            if (loaded != null) {
                System.out.println("Setting new schedule");

                canvas = new Canvas(1920, 1048);



                Camera camera = this.simulator.getCamera();

                this.simulator = new Simulator(this.schedule);

                canvas = new Canvas(1920, 1048);

                simulator.setCamera(camera);
                fxGraphics2D.setTransform(new AffineTransform());

            } else {
                System.out.println("Loaded was null");
            }
        });

        bottomHBox.getChildren().add(reloadSchedule);
        bottomHBox.getChildren().add(reloadSim);
        canvasContainer.setBottom(bottomHBox);

        stage.setScene(scene);
        stage.setTitle("School simulatie");
        stage.setMaximized(true);
        stage.show();
    }

    private void initialise() {

        this.schedule = DataStorage.loadSchedule(this.filePath);
        if (this.schedule == null) {
            //  Manual schedule loading:
            System.out.println("Couldn't load a schedule");
            ArrayList<Teacher> teachers = new ArrayList<>();
            teachers.add(new Teacher("Etiënne", 30, "Hardware"));
            teachers.add(new Teacher("Johan", 36, "Graphics"));
            teachers.add(new Teacher("Maurice", -1, "Programmeren"));
            ArrayList<Group> groups = new ArrayList<>();
            Group c1 = new Group("C1");
            groups.add(c1);
            groups.add(new Group("C2"));
            groups.add(new Group("C3"));
            groups.add(new Group("C4"));


            ArrayList<Classroom> classrooms = new ArrayList<>();
            classrooms.add(new Classroom(1, new ClassroomEntryPoint(550, 550)));
            classrooms.add(new Classroom(2, new ClassroomEntryPoint(1000, 550)));
            classrooms.add(new Classroom(3, new ClassroomEntryPoint(500, 820)));
            classrooms.add(new Classroom(4, new ClassroomEntryPoint(600, 820)));
            classrooms.add(new Classroom(5, new ClassroomEntryPoint(950, 820)));
            classrooms.add(new Classroom(6, new ClassroomEntryPoint(1050, 820)));
            classrooms.add(new Classroom(7, new ClassroomEntryPoint(500, 1110)));
            classrooms.add(new Classroom(8, new ClassroomEntryPoint(600, 1110)));
            ArrayList<Lesson> lessons = new ArrayList<>();
            lessons.add(new Lesson(LocalTime.of(15, 30), LocalTime.of(16, 30), teachers.get(0), classrooms.get(0), groups));
            lessons.add(new Lesson(LocalTime.of(9, 0), LocalTime.of(10, 0), teachers.get(1), classrooms.get(1), groups.get(1)));
            lessons.add(new Lesson(LocalTime.of(10, 0), LocalTime.of(11, 0), teachers.get(2), classrooms.get(2), groups.get(0)));
            lessons.add(new Lesson(LocalTime.of(9, 0), LocalTime.of(11, 0), teachers.get(0), classrooms.get(3), groups.get(3)));
            lessons.add(new Lesson(LocalTime.of(12, 30), LocalTime.of(13, 30), teachers.get(1), classrooms.get(4), groups.get(2)));
            lessons.add(new Lesson(LocalTime.of(13, 30), LocalTime.of(14, 30), teachers.get(2), classrooms.get(3), groups.get(0)));
            lessons.add(new Lesson(LocalTime.of(16, 30), LocalTime.of(17, 30), teachers.get(0), classrooms.get(2), groups));
            lessons.add(new Lesson(LocalTime.of(11, 0), LocalTime.of(12, 0), teachers.get(0), classrooms.get(7), groups));

            for (Group group : groups) {
                group.addStudent(new Student(group.getGroupName() + "test", 12, group));
            }

            this.schedule = new Schedule(lessons, teachers, groups, classrooms);


        } else {
            System.out.println("properly loaded a schedule");
        }

        simulator = new Simulator(schedule);
    }

    private void updateLabel() {
        int speedfactor = simulator.getSpeedfactor();
        if (speedfactor == 0) {
            speedFactorLabel.setText("De simulatie speelt op normale snelheid");
        } else {
            speedFactorLabel.setText("De simulatie word " + speedfactor + " keer sneller afgespeeld");
        }
    }

    public void addMouseScrolling(Node node) {
        node.setOnScroll((ScrollEvent event) ->
        {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0) {
                zoomFactor = 2.0 - zoomFactor;
            }

            if (!(node.getScaleY() * zoomFactor > 5) && !(node.getScaleY() * zoomFactor < 1)) {
                node.setScaleX(node.getScaleX() * zoomFactor);
                node.setScaleY(node.getScaleY() * zoomFactor);
            }

        });
    }

    private double lastX = -10000;
    private double lastY = -10000;

    public void addMouseClickDrag(Node node, Camera camera) {
        node.setOnMouseClicked(event ->
        {
            double x = (-camera.getX()) + event.getX();
            double y = (-camera.getY()) + event.getY();
            simulator.mouseClicked(x, y);

            if (event.getButton().equals(MouseButton.MIDDLE)) {
                NPC.collisionEnabled = !NPC.collisionEnabled;
            }
        });

        node.setOnMouseDragged((event) ->
        {
            if (!camera.getNpcFollower().isFollowing()) {
                if (lastX == -10000 && lastY == -10000) {
                    lastX = event.getX();
                    lastY = event.getY();
                }

                // if statement to limit dragging to certain areas of the map
                if ((camera.getX() + (event.getX() - lastX)) > -1200 && (camera.getY() + (event.getY() - lastY)) > -1200 && (camera.getX() + (event.getX() - lastX)) < 1500 && (camera.getY() + (event.getY() - lastY)) < 550) {

                    camera.addToPosition(event.getX() - lastX, event.getY() - lastY);

                    lastX = event.getX();
                    lastY = event.getY();
                }
            }
        });

        node.setOnMouseReleased(event ->
        {

            lastX = -10000;
            lastY = -10000;

            //            System.out.println("X: " + event.getX() + "Y: " +  event.getY());

        });

    }

    public static TiledMap getTiledmap() {
        return Simulator.getTiledmap();
    }


}
