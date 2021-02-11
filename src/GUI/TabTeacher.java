package GUI;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class TabTeacher extends PopUpTab
{
    protected TabTeacher(PopupController popupController)
    {
        super.setPopUpName("Docenten");
    }


    @Override
    protected BorderPane getPane()
    {
        BorderPane mainPane = new BorderPane();
        Scene scene = new Scene(mainPane);
        return mainPane;

    }




}
