package GUI;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.Resizable;
import org.jfree.fx.ResizableCanvas;

import javax.naming.ldap.Control;

public class MainWindow extends ResizableCanvas {

    public MainWindow(Pane pane) {
        super(new Resizable() {
            @Override
            public void draw(FXGraphics2D fxGraphics2D) {
                // this needs to be here but isn't used
            }
        }, pane);
        initialise();
        draw(new FXGraphics2D(getGraphicsContext2D()));
    }

    private void initialise() {
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }

                if (now - last >= 10000000.0) {
                    draw(new FXGraphics2D(getGraphicsContext2D()));
                    last = now;
                }
            }
        }.start();
    }

    public void draw(FXGraphics2D graphics2D) {
        graphics2D.drawLine(0, 0, 2000, 2000);
    }
}
