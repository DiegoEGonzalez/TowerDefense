import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Spawner extends Unit{
    final static int size=50;
    //private int recharge=10;
    public Spawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,size,size,kind,150,objects,lasers);
        recharge=10;
        lastAction=System.nanoTime();
        alive=true;
    }
    public void move(){

    }
    public void action(){
        //objects.add(new TTT(x, y, kind, objects, lasers));
    }
    public void draw(Graphics g){
        super.draw(g);
        g.setColor(Color.lightGray);
        g.drawRect(x-size/2,y-size/2,size,size);

    }
}
