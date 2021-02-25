package GUI;

import Data.Schedule;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class TabLesson extends PopUpTab
{
    private ObservableList timeList;
    private int spacingDistance = 10;
    private ArrayList<String> classes = new ArrayList<>();
    Schedule schedule;

    protected TabLesson(Schedule schedule)
    {
        super.setPopUpName("Lessen");
        this.schedule = schedule;
        classes.add("Tivt1A");
        classes.add("Tivt1B");
        classes.add("Tivt1C");

    }

    @Override
    protected BorderPane getPane()
    {

        BorderPane mainPane = new BorderPane();

        //left side of the menu lesson
        Label currentLesson = new Label("Bestaande Lessen");
        ListView listView = new ListView();
        listView.setPrefWidth(500);


        VBox leftVbox = new VBox();
        leftVbox.getChildren().addAll(currentLesson, listView);
        leftVbox.setSpacing(spacingDistance);
        leftVbox.setPadding(new Insets(10, 10, 10, 10));


        //middle side of menu lesson
        Label selectedLesson = new Label("Geselecteerde les");
        TextArea lessonData = new TextArea();
        lessonData.setMaxWidth(500);
        lessonData.setPrefHeight(400);
        lessonData.setEditable(false);
        Button deleteSelected = new Button("Verwijder les");

        VBox middleVbox = new VBox();
        middleVbox.getChildren().addAll(selectedLesson, lessonData, deleteSelected);
        middleVbox.setSpacing(spacingDistance);
        middleVbox.setPadding(new Insets(10, 10, 10, 10));
        middleVbox.setMinWidth(500);

        //right side of menu lesson
        Label newLesson = new Label("Nieuwe les");

        Label startLesson = new Label("Begintijd");
        ComboBox startTimeSelect = new ComboBox();
        startTimeSelect.setMinWidth(200);

        Label endLesson = new Label("Eindtijd");
        ComboBox endTimeSelect = new ComboBox();
        endTimeSelect.setMinWidth(200);

        Label teacherBox = new Label("Kies een docent");
        ComboBox teacherSelect = new ComboBox();
        teacherSelect.setMinWidth(200);

        Label locationBox = new Label("Kies een lokaal");
        ComboBox locationSelect = new ComboBox();
        locationSelect.setMinWidth(200);

        Button lessonadder = new Button("Voeg klas toe");

        VBox rightVbox = new VBox();
        rightVbox.getChildren().addAll(newLesson, startLesson, startTimeSelect, endLesson, endTimeSelect, teacherBox, teacherSelect, locationBox, locationSelect, selectClass(), lessonadder);

        rightVbox.setSpacing(spacingDistance);

        rightVbox.setPadding(new Insets(10, 10, 10, 10));
        rightVbox.setAlignment(Pos.TOP_LEFT);


        mainPane.setLeft(leftVbox);
        mainPane.setCenter(middleVbox);
        mainPane.setRight(rightVbox);
        mainPane.setPadding(new Insets(10, 500, 10, 10));


        return mainPane;
    }

    /**
     * deze methode is tijdelijk want deze moet observable worden zodat het klassen ziet als er nieuwe worden toegevoeg
     *
     * @return
     */
    public VBox selectClass()
    {
        VBox classselector = new VBox();
        classselector.setSpacing(5);
        classselector.setPrefHeight(110);
        for (String string : classes)
        {
            CheckBox box = new CheckBox(string);
            classselector.getChildren().addAll(box);
        }
        return classselector;
    }

}
