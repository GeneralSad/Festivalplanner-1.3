package Simulator;

import GUI.TabLesson;
import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class NPCSprites
{

    private BufferedImage sprite;
    private ArrayList<BufferedImage[]> movements;

    public NPCSprites(BufferedImage sprite)
    {
        this.movements = new ArrayList<>();
        this.sprite = sprite;


        for (int i = 0; i < 7; i++)
        {
            BufferedImage[] temp = new BufferedImage[24];
            for(int j = 0; j < 24; j++){
                temp[j] = this.sprite.getSubimage(16 * j, 32 * i, 16, 32);
            }
            this.movements.add(temp);
        }

    }

    public void draw(FXGraphics2D fxGraphics2D) {
        for (BufferedImage[] list : this.movements) {
            for (BufferedImage image : list){
                fxGraphics2D.drawImage(image, 100,100, null);
            }
        }
    }
}
