package GUI;

import javafx.scene.layout.BorderPane;

public class TabClass extends PopUpTab
{
    public TabClass()
    {
        super.setPopUpName("Klassen");
    }

    @Override
    protected BorderPane getPane()
    {
        BorderPane mainPane = new BorderPane();
        return mainPane;

    }
}
