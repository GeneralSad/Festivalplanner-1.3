package GUI;

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

public class GUI extends Application
{

    public static void main(String[] args)
    {
        launch(GUI.class);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane canvasContainer = new BorderPane();
        canvasContainer.setCenter(new MainWindow(canvasContainer));
        HBox changesButtonContainer = new HBox();
        changesButtonContainer.getChildren().add(new Button("Wijzingen"));
        canvasContainer.setBottom(changesButtonContainer);
        tabPane.getTabs().add(new Tab("Rooster", canvasContainer));

        FlowPane test = new FlowPane();
        test.getChildren().add(new TextField("testing"));
        tabPane.getTabs().add(new Tab("Testing", test));

        Scene scene = new Scene(tabPane);
        stage.setScene(scene);
        stage.setTitle("School simulatie");
        stage.show();
    }
}
