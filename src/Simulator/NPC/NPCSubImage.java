package Simulator.NPC;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class NPCSubImage {

    private ArrayList<ArrayList<BufferedImage[]>> images = new ArrayList<>();

    public NPCSubImage() {
        subImage("/NPC/NPC1 male.png");
        subImage("/NPC/NPC2 male.png");
        subImage("/NPC/NPC3 female.png");
        subImage("/NPC/NPC4 female.png");
    }

    public void subImage(String path){
        ArrayList<BufferedImage[]> movements = new ArrayList<>();

        BufferedImage sprite = null;
        try
        {
            sprite = ImageIO.read(getClass().getResource(path));
        }
        catch (Exception e)
        {
            System.out.println("Image not found");
        }

        for (int i = 0; i < 7; i++)
        {
            BufferedImage[] temp = new BufferedImage[24];
            for (int j = 0; j < 24; j++)
            {
                temp[j] = sprite.getSubimage(16 * j, 32 * i, 16, 32);
            }
            movements.add(temp);
        }

        images.add(movements);
    }

    public ArrayList<BufferedImage[]> randomSprite(){
        return images.get((int) (Math.random() * 4));
    }
}
