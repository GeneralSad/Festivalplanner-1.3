package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUI extends Application
{

    public static void main(String[] args) {
        launch(GUI.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        TabPane tabPane = new TabPane();
        Scene scene = new Scene(tabPane);
        tabPane.getTabs().add(new Tab("Rooster", new MainWindow()));

        stage.setScene(scene);
        stage.setTitle("School simulatie");
        stage.show();
    }
}
