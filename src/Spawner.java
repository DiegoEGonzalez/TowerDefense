import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

public class Spawner extends Unit{
    final static int size=50;
    int maxcount=5;
    int count=0;
    double angle=0;
    int forcefield=300;
    boolean forceon = false;


    String name = "SPAWNER";

    long birthdate ;


    //private int recharge=10;
    public Spawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,size,size,kind,150,objects,lasers);
        recharge=5;
        lastAction=Alpha.gametime;
        alive=true;
        birthdate=Alpha.gametime;
    }
    public void move(){
            angle+=Math.toRadians(3);
    }
    public void action(){
        //objects.add(new TTT(x, y, kind, objects, lasers));

    }
    public void draw(Graphics2D g){
        super.draw(g);
        if(Alpha.selected != this||Alpha.selection!=5) {

            g.setColor(Color.lightGray);
        }else {
            if(forceon)
                g.setColor(new Color(0, 255, 255, 15));
            else
                g.setColor(new Color(200, 200, 200, 15));

                g.fillOval(getX() - forcefield / 2, getY() - forcefield / 2, forcefield, forcefield);

            g.setColor(Color.BLUE);
        }
        g.drawRect(getX() - size / 2, getY() - size / 2, size, size);

        g.setColor(Color.white);
        if(count>0)
        g.drawString(count+"",getX()-size/2+center(size,count+"",g),getY()+5);
        g.setColor(Color.blue);




        g.translate(x, y);     //i haven't added anything, check bugs
        g.rotate(-angle);
        g.drawRect(-15,-15,30,30);

        g.rotate(angle);
        g.translate(-x,-y);


    }
    public static void drawdefaut(int x, int y, Graphics2D g){
        g.setColor(new Color(255,255,255,50));
        g.drawRect(x - size / 2, y - size / 2, size, size);
        g.translate(x, y);     //i haven't added anything, check bugs
        g.rotate(-Math.toRadians(45));
        g.drawRect(-15,-15,30,30);
        g.rotate(Math.toRadians(45));
        g.translate(-x,-y);


    }
    public int center(int x, String a, Graphics g){
        Graphics2D text = (Graphics2D)g;
        FontMetrics fm=g.getFontMetrics();
        Rectangle2D rect=fm.getStringBounds(a,text);
        return (int)((x- rect.getWidth())/2);
    }
}
