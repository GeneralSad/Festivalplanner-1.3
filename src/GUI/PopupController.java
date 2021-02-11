package GUI;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.ArrayList;


public class PopupController
{
    private Stage stage = new Stage();
    private ArrayList<PopUpTab> popups = new ArrayList<>();


    protected PopupController()
    {
        TabLesson popUpLesson = new TabLesson();
        TabTeacher tabTeacher = new TabTeacher();
        TabClass tabClass = new TabClass();

        popups.add(popUpLesson);
        popups.add(tabTeacher);
        popups.add(tabClass);
    }

    protected Stage popupWindowStage()
    {

        stage.setHeight(720);
        stage.setWidth(1280);
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        for (PopUpTab tab : this.popups)
        {
            tabPane.getTabs().add(new Tab(tab.getPopUpName(), tab.getPane()));
        }


        Scene scene = new Scene(tabPane);

        stage.setScene(scene);
        stage.setTitle("PopupController");

        return stage;
    }


}
