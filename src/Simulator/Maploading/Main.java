package Simulator.Maploading;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;

// for testing purposes
public class Main extends Application
{
    private ResizableCanvas resizableCanvas;
    private TiledMap tiledMap;

    public static void main(String[] args)
    {
        launch(Main.class);
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
                if (now - last > 1e8)
                {
                    draw(fxGraphics2D);
                    last = now;
                }
            }
        }.start();

        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();
    }

    public void init()
    {
        tiledMap = new TiledMap("/TiledMaps/TiledMap.json");
    }

    private void draw(FXGraphics2D graphics2D)
    {
        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, (int) this.resizableCanvas.getWidth(), (int) this.resizableCanvas.getHeight());

        graphics2D.draw(new Rectangle2D.Double(50, 50, 50, 50));

        tiledMap.draw(graphics2D);
    }
}
