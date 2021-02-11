package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
        BorderPane mainPane = new BorderPane();
        Scene scene = new Scene(mainPane);

        PopupController popupController = new PopupController();
        stage.setScene(scene);
        stage.setTitle("Fading image");
        //stage.show();
        stage = popupController.popupWindowStage();
        stage.show();



    }
}
