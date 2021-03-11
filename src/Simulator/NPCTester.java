package Simulator;

import Data.Person;
import Simulator.Maploading.TiledMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;

// for testing purposes
public class NPCTester extends Application
{
    private ResizableCanvas resizableCanvas;
//    private TiledMap tiledMap;
    private NPCManager npcManager;

    public static void main(String[] args)
    {
        launch(NPCTester.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        resizableCanvas = new ResizableCanvas(fxGraphics2D -> draw(fxGraphics2D), mainPane);

        mainPane.setCenter(resizableCanvas);
        FXGraphics2D fxGraphics2D = new FXGraphics2D(resizableCanvas.getGraphicsContext2D());

        new AnimationTimer()
        {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                {
                    last = now;
                }
//                if (now - last > 1e9)
//                {
                    update((now - last) / 1e9);
                    draw(fxGraphics2D);
                    last = now;
//                }
            }
        }.start();

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();
    }

    public void init()
    {
        npcManager = new NPCManager();
        npcManager.addNPC(new Person("Test", 0), 100, 100, 10, -6, 10, 10, 0);
        npcManager.addNPC(new Person("Test", 0), 200, 100, -10, 7, 10, 10, 0);
    }

    private void update(double deltaTime) {
        npcManager.update(deltaTime);
    }

    private void draw(FXGraphics2D graphics2D)
    {
        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, (int) this.resizableCanvas.getWidth(), (int) this.resizableCanvas.getHeight());

        npcManager.draw(graphics2D);
    }
}
