package GUI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class GUI extends Application
{

    public static void main(String[] args) {
        launch(GUI.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        TabPane tabPane = new TabPane();
        FlowPane canvasContainer = new FlowPane();
        canvasContainer.getChildren().add(new MainWindow(canvasContainer));
        tabPane.getTabs().add(new Tab("Rooster", canvasContainer));

        Scene scene = new Scene(tabPane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("School simulatie");
        stage.show();
    }
}
