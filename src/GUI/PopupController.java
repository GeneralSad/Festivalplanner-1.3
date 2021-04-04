package GUI;

import Data.Schedule;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Auteurs:
 * <p>
 * Deze code zorgt ervoor dat er een popup wordt aangemaakt
 */

public class PopupController extends Stage
{
    private Stage stage = new Stage();
    private ArrayList<PopUpTab> popups = new ArrayList<>();


    protected PopupController(Stage mainstage, Schedule schedule)
    {
        stage.setHeight(720);
        stage.setWidth(1280);
        TabLesson tabLesson = new TabLesson(schedule);
        TabTeacher tabTeacher = new TabTeacher(schedule, tabLesson);
        TabClass tabClass = new TabClass(schedule, tabLesson);
        TabClassroom tabClassroom = new TabClassroom(schedule);

        popups.add(tabLesson);
        popups.add(tabTeacher);
        popups.add(tabClass);
        popups.add(tabClassroom);


        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        for (PopUpTab tab : this.popups)
        {
            tabPane.getTabs().add(new Tab(tab.getPopUpName(), tab.getPane()));
        }


        Scene scene = new Scene(tabPane, 1280, 570);

        this.setScene(scene);
        this.setTitle("Editor");
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

    //Zorgt ervoor dat een nieuwe VBox die aangemaakt wordt de goede dimensies krijgt
    public static VBox awesomeVBox(Node... elements)
    {
        int spacingDistance = 10;
        VBox vBox = new VBox();
        vBox.getChildren().addAll(elements);
        vBox.setSpacing(spacingDistance);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        return vBox;
    }


}
