import java.awt.*;
import java.util.ArrayList;

public class SSSSpawner extends Unit{
    public SSSSpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,50,50,kind,150,objects,lasers);
        recharge=6;
        lastAction=System.nanoTime();
        alive=true;
    }
    public void move(){

    }
    public void action(){
        objects.add(new SSS(x,y,kind,objects,lasers));
    }
    public void draw(Graphics g){
        super.draw(g);
        g.setColor(Color.lightGray);
        g.drawRect(x-25,y-25,50,50);
    }
}
