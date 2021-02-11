package GUI;

import javafx.scene.canvas.Canvas;
import org.jfree.fx.FXGraphics2D;

public class MainWindow extends Canvas {

    public MainWindow() {
        super(800, 600);
        draw(new FXGraphics2D(getGraphicsContext2D()));
    }

    public void draw(FXGraphics2D graphics2D) {
        graphics2D.drawLine(0, 0, 2000, 2000);
    }
}
