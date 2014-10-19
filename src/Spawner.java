import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Spawner {

    int x;
    int y;
    int speed;
    long lastupdate;
    ArrayList<Plane> stuff = new ArrayList<Plane>();
    ArrayList<Laser> lasers = new ArrayList<Laser>();
    int kind;
    int maxhealth =200;
    int health = 200;
    boolean alive=true;

    public Spawner(int x, int y, int speed, ArrayList<Plane> a, ArrayList<Laser> lasers,int kind){
        this.x=x;
        this.y=y;
        this.speed=speed;
        stuff=a;
        lastupdate=System.nanoTime();
        this.lasers=lasers;
        this.kind=kind;

    }
    public void update(){
        if((System.nanoTime()-lastupdate)/1000000000>speed) {
            stuff.add(new Plane(x + 25, y + 25, 10, 90, kind,lasers));
            lastupdate=System.nanoTime();
        }
        for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext(); ) {
            Laser b = iterator.next();
            if(collisionCircle(x,y,25,(int)b.getX(),(int)b.getY(),2)>0&&b.kind!=kind){
                //alive=false;
                health-=b.damage;
                iterator.remove();
            }

        }
        if(health<=0)
            alive=false;
    }
    public void draw(Graphics g){
        g.setColor(Color.lightGray);
        g.drawRect(x,y,50,50);
        g.drawRect(x,y+70,50,10);
        g.fillRect(x,y+70,(int)((50.0/maxhealth)*health),10);
    }

    public int collisionCircle(int x1, int y1, int r1, int x2, int y2, int r2){
        double xDif = x1 - x2;
        double yDif = y1 - y2;
        double distanceSquared = xDif * xDif + yDif * yDif;
       // double angleinbetween = Math.atan2((double)(y2-y1),(double) (x1-x2));
        //angleinbetween+=Math.toRadians(180);


        if(!(distanceSquared < (r1 + r2) * (r1 + r2)))
            return 0;
        else
            return 1;

    }


}
