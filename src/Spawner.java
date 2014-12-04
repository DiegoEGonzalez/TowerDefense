import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

public class Spawner extends Unit{
    final static int size=50;
    int maxcount=5;
    int count=0;
    //private int recharge=10;
    public Spawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,size,size,kind,150,objects,lasers);
        recharge=5;
        lastAction=System.nanoTime();
        alive=true;
    }
    public void move(){

    }
    public void action(){
        //objects.add(new TTT(x, y, kind, objects, lasers));

    }
    public void draw(Graphics2D g){
        super.draw(g);
        g.setColor(Color.lightGray);
        g.drawRect(x-size/2,y-size/2,size,size);
        g.drawString(count+"",x-size/2+center(size,count+"",g),y+5);

    }
    public int center(int x, String a, Graphics g){
        Graphics2D text = (Graphics2D)g;
        FontMetrics fm=g.getFontMetrics();
        Rectangle2D rect=fm.getStringBounds(a,text);
        return (int)((x- rect.getWidth())/2);
    }
}
