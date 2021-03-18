package Simulator.NPC;

import Data.Person;
import Simulator.Pathfinding.Pathfinding;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

// for testing purposes
public class NPCTester extends Application
{

    private BufferedImage bufferedImage;
    private ResizableCanvas resizableCanvas;
    private NPCManager npcManager;
    private NPCSprites npcSprites;
    private Pathfinding pathfinding;

    private boolean debug = true;

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
                update((now - last) / 1e9);
                draw(fxGraphics2D);
                last = now;
            }
        }.start();

        resizableCanvas.setOnMouseClicked(event -> {
            pathfinding.setDestination((int)event.getX(), (int)event.getY());
        });

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();
    }

    public void init()
    {
        npcManager = new NPCManager();

        pathfinding = new Pathfinding(16, 16, 128, 128);

        for (int i = 0; i < 20; i++)
        {
            pathfinding.getTile(i, 10).setWalkable(false);
        }

        for (int i = 0; i < 1; i++)
        {
            NPC npc = new NPC(new Person("Test", 0), 200, 100 - i * 100, 10, 0, 10, 10, "/NPC/NPC1 male.png");
            npcManager.addNPC(npc);
            //        npc.setTargetRotation(Math.PI * 1.5);
//            npc.goToDestination(200, 200);
            npc.setPathfinding(pathfinding);
        }
    }

    private void update(double deltaTime)
    {
        npcManager.update(deltaTime);
    }



    private void draw(FXGraphics2D graphics2D)
    {
        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, (int) this.resizableCanvas.getWidth(), (int) this.resizableCanvas.getHeight());

        npcManager.draw(graphics2D, debug);

        pathfinding.draw(graphics2D, debug);
    }
}
