package GUI;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PopUpLesson
{
    PopupWindow popupWindow;


    public PopUpLesson(PopupWindow popupWindow)
    {
        this.popupWindow = popupWindow;
    }

    protected Scene getScene()
    {

        BorderPane mainPane = new BorderPane();
        mainPane.setTop(this.popupWindow.getMenubar());


        VBox vBox = new VBox();
        mainPane.setLeft(vBox);
        Label label = new Label("Bestaande Lessen");

        ListView listView = new ListView();
        vBox.getChildren().addAll(label, listView);
        vBox.setSpacing(10);

        Scene scene = new Scene(mainPane);

        return  scene;


    }

}
