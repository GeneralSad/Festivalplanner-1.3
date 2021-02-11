package GUI;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TabLesson extends PopUpTab
{


    protected TabLesson(PopupController popupController)
    {
        super.setPopUpName("Lessen");
    }

    @Override
    protected BorderPane getPane()
    {

        BorderPane mainPane = new BorderPane();


        VBox vBox = new VBox();
        mainPane.setLeft(vBox);
        Label label = new Label("Bestaande Lessen");

        ListView listView = new ListView();
        vBox.getChildren().addAll(label, listView);
        vBox.setSpacing(10);


        return mainPane;


    }

}
