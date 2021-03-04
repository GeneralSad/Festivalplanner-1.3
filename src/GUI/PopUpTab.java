package GUI;

import javafx.scene.layout.BorderPane;

public abstract class PopUpTab
{
    private String popUpName;

    protected abstract BorderPane getPane();


    protected String getPopUpName()
    {
        return popUpName;
    }

    protected void setPopUpName(String popUpName)
    {
        this.popUpName = popUpName;
    }


}
