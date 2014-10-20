import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Unit{
    int x,y;
    int h,w;
    int maxhealth,health;
    int kind;
    boolean alive;
    ArrayList<Unit> objects;
    ArrayList<Laser> lasers;
    double recharge; //how much time in between action ( in seconds )
    long lastAction; //when the last action was triggered

    public Unit(int x, int y, int w, int h, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.objects=objects;
        this.lasers=lasers;
    }
    public void update(){
        move();
        if((System.nanoTime()-lastAction)/1000000000.0>recharge) {
            action();
            lastAction=System.nanoTime();
        }
        for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext(); ) {
            Laser b = iterator.next();
            if(collisionCircle(x,y,h/2,(int)b.getX(),(int)b.getY(),2)>0&&b.kind!=kind){
                health-=b.damage;
                iterator.remove();
            }

        }
        if(health<=0)
            alive=false;

    }
    public void action(){

    }
    public void move(){

    }
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.drawRect(x-(w/2),y-(y/2),w,h);

    }
    public double collisionCircle(int x1, int y1, int r1, int x2, int y2, int r2){
        double xDif = x1 - x2;
        double yDif = y1 - y2;
        double distanceSquared = xDif * xDif + yDif * yDif;
        double angleinbetween = Math.atan2((double)(y2-y1),(double) (x1-x2));
        angleinbetween+=Math.toRadians(180);


        if(!(distanceSquared < (r1 + r2) * (r1 + r2)))
            return 0;
        else
            return angleinbetween;

    }
}
