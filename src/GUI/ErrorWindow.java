package GUI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Auteurs: Leon
 * <p>
 * Deze code zorgt ervoor dat een popupwindow aangemaakt kan worden en laten zien kan worden met een bepaalde tekst
 */

public class ErrorWindow
{

    private String stageName;

    public ErrorWindow(String stageName)
    {
        this.stageName = stageName;

    }

    public void ErrorStage(String message)
    {
        Stage stage = new Stage();
        stage.setTitle(this.stageName);

        GridPane gridPane = new GridPane();

        Label label = new Label(message);
        Button button = new Button("Ok");

        button.setOnAction(event ->
        {

            stage.close();

        });

        gridPane.add(label, 0, 0);
        gridPane.add(button, 1, 1);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
        stage.show();
    }

}
