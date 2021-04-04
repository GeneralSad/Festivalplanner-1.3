package GUI;

import javafx.scene.layout.BorderPane;

/**
 * Auteurs:
 * <p>
 * Deze klasse is een abstracte klasse met de nodige methodes voor de specialisaties
 */

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
