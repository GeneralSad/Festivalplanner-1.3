package Simulator.NPC;

import org.jfree.fx.FXGraphics2D;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class NPCSprites
{
    //sprites loading
    private BufferedImage sprite;
    private ArrayList<BufferedImage[]> movements;

    //cache
    private double previousX;
    private double previousY;
    private double previousROT;

    //refreshable
    private int dir = 0;
    private double refresh = 2;

    //state
    private int frame = (int)(Math.random()*5);
    private boolean onPhone = false;


    public NPCSprites(String image)
    {
        this.movements = new ArrayList<>();

        try
        {
            this.sprite = ImageIO.read(getClass().getResource(image));
        }catch (Exception e){
            System.out.println("Image not found");
        }

        for (int i = 0; i < 7; i++)
        {
            BufferedImage[] temp = new BufferedImage[24];
            for(int j = 0; j < 24; j++){
                temp[j] = this.sprite.getSubimage(16 * j, 32 * i, 16, 32);
            }
            this.movements.add(temp);
        }

    }

    public BufferedImage[] getStanding(){

        return movements.get(1);
    }

    public BufferedImage[] getRunning(){
        return movements.get(2);
    }

    public BufferedImage[] getSitting(){
        return movements.get(5);
    }

    public BufferedImage[] getPhonening(){
        return movements.get(6);
    }

    public void draw(FXGraphics2D graphics2D, boolean atDestination, double x, double y, double rotation){
        AffineTransform af = new AffineTransform();
        af.translate(x  - 8, y - 16);

        if (atDestination){
//            if (dir == 18){
//                onPhone = !onPhone;
//            }

            if (onPhone){
                graphics2D.drawImage(getPhonening()[frame + dir], af , null);
            } else
            {
                graphics2D.drawImage(getStanding()[frame + dir], af, null);
            }

        } else {
            graphics2D.drawImage(getRunning()[frame + dir], af, null);
        }
    }


    public void frameUpdater(double x, double y){
        double xDif = x - this.previousX;
        double yDif = y - this.previousY;

        double distance = Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));


        if (distance >= refresh){

            if (frame < 5){
                frame++;
            } else {
                frame = 0;
            }

            previousX = x;
            previousY = y;

        }
    }

    public void directionUpdater(double rotation){
        double angleDegrees = Math.toDegrees(rotation);
        if ((angleDegrees<= 45 && angleDegrees>= 0) || (angleDegrees >= 315 && angleDegrees <= 180)){
            dir = 0;
        } else if (angleDegrees >45  && angleDegrees < 135){
            dir = 6;
        } else if (angleDegrees >= 135  && angleDegrees <= 225){
            dir = 12;
        } else if (angleDegrees > 225  && angleDegrees < 315){
            dir = 18;
        }

        if (Math.abs(previousROT - rotation) >= 10)
        {

            if (frame < 5)
            {
                frame++;
            }
            else
            {
                frame = 0;
            }

            previousROT = rotation;
        }


    }
}
