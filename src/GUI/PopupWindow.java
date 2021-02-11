package GUI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class PopupWindow
{
    Stage stage = new Stage();
    PopUpLesson popUpLesson;


    public PopupWindow()
    {
        popUpLesson = new PopUpLesson(this);
    }

    public Stage popupWindowStage()
    {

        stage.setHeight(720);
        stage.setWidth(1280);

        Scene scene = new Scene(getMenubar());

        stage.setScene(scene);
        stage.setTitle("PopupWindow");

        return stage;
    }


    protected HBox getMenubar()
    {
        HBox hBox = new HBox();

        Button buttonLessons = new Button("Lessen");

        buttonLessons.setOnAction(event ->
        {
            stage.setScene(popUpLesson.getScene());
        });

        Button buttonTeachers = new Button("Docenten");
        Button buttonClasses = new Button("Klassen");

        hBox.getChildren().addAll(buttonLessons, buttonTeachers, buttonClasses);
        hBox.setPadding(new Insets(10, 7, 10, 7));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");


        return hBox;
    }


}
