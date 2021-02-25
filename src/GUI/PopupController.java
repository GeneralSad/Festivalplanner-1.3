package GUI;

import Data.Schedule;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;


public class PopupController extends Stage
{
    private Stage stage = new Stage();
    private ArrayList<PopUpTab> popups = new ArrayList<>();



    protected PopupController(Stage mainstage, Schedule schedule)
    {
        stage.setHeight(720);
        stage.setWidth(1280);
        TabLesson tabLesson = new TabLesson(schedule);
        TabLesson popUpLesson = tabLesson;
        TabTeacher tabTeacher = new TabTeacher(schedule, tabLesson);
        TabClass tabClass = new TabClass(schedule, tabLesson);

        popups.add(popUpLesson);
        popups.add(tabTeacher);
        popups.add(tabClass);



        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        for (PopUpTab tab : this.popups)
        {
            tabPane.getTabs().add(new Tab(tab.getPopUpName(), tab.getPane()));
        }


        Scene scene = new Scene(tabPane, 1280, 570);

        this.setScene(scene);
        this.setTitle("PopupController");
        this.initModality(Modality.APPLICATION_MODAL);
        this.initOwner(mainstage);
        this.showAndWait();


    }


    public static void textEditor(Label label, int line, String string)
    {

        if (line < 0)
        {
            throw new IllegalArgumentException("line cant be below zero");
        }


        String text = label.getText();
        String[] lines = text.split("\n");

        StringBuilder newText = new StringBuilder();


        for (int j = 0; j < line + 1; j++)
        {
            if (j == line)
            {
                newText.append(string).append("\n");
            }
            else if (j > lines.length - 1)
            {
                newText.append("\n");
            }
            else
            {
                newText.append(lines[j]).append("\n");
            }

        }

        label.setText(String.valueOf(newText));

    }


}
