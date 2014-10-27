import javafx.scene.shape.Line;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

public class Plane {
    int x;
    int y;
    int w=20;
    int h=20;
    double vx=0;
    double vy=0;
    double ax=0;
    double ay=0;
    double power;
    double angle=Math.toRadians(0);
    double deltaangle=0;
    boolean viewbounds = false;
    boolean left,right,down,up;
    boolean oh = false;
    ArrayList<Point> trail = new ArrayList<Point>();
    ArrayList<Laser> lasers = new ArrayList<Laser>();
    int kind;
    double ang=0;
    int secx=0;
    int secy=0;
    int ammo=10;
    int maxhealth=30;
    int health=30;

    double desiredAngle = 0;

    double rateOfFire = .5;
    long lastShotFire=0;

    boolean alive = true;

    public Plane(int x, int y, double power, double angle, int kind, ArrayList<Laser> lasers){
        this.x=x;
        this.y=y;
        this.power=power;
        this.angle=angle;
        this.kind=kind;
        this.lasers=lasers;
        lastShotFire=System.nanoTime();

    }
    public void move(){
        if(angle<0)
            angle=Math.toRadians(360)+angle;
        angle=angle%Math.toRadians(360)+deltaangle;
        vx+=ax;
        vy+=ay;
        vy=(Math.sin(-angle)*power);
        vx=(Math.cos(-angle)*power);
        x+=Math.round(vx);
        y+=Math.round(vy);
         left = x+vx>Shell.DEFAULT_WINDOWSIZEX-50;
        right = x+vx<50;
         up = y+vy>Shell.DEFAULT_WINDOWSIZEY-50;
        down = y+vy<50;
        if(left||right||up||down)
            angle+=Math.toRadians(2)*power;

    }
    public void update() {
        move();
        if(health<=0)
            alive=false;



    }
    public void ai(ArrayList<Plane> a){

        // wander is still figidy
        wander();
        //avoid(a);
        attack(a);
/**
        for(int q=0;q<lasers.size();q++) {
            if(collisionCircle(x,y,20,a.get(q).getX(),a.get(q).getY(),2)>0&&a.get(q).kind!=kind&&(System.nanoTime()-lastShotFire)/1000000000.0>rateOfFire){
                alive=false;
            }
        }
 **/
        for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext(); ) {
            Laser b = iterator.next();
            if(collisionCircle(x,y,20,(int)b.getX(),(int)b.getY(),2)>0&&b.kind!=kind){
                //alive=false;
                health-=b.damage;
                iterator.remove();
            }

        }

        /**
         ang = Math.atan2((double)(a.get(q).getY()-y),(double) (x-a.get(q).getX()) );
         ang+=Math.toRadians(180);

         secx=a.get(q).x;
         secy=a.get(q).y;
         **/

    }

    public void attack(ArrayList<Plane> a){
        for(int q=0;q<a.size();q++) {
            if (a.get(q) == this)
                continue;
            if(collisionCircle(x,y,400,a.get(q).getX(),a.get(q).getY(),20)>0&&a.get(q).kind!=kind&&(System.nanoTime()-lastShotFire)/1000000000.0>rateOfFire){
                lasers.add(new Laser(x,y,a.get(q).getX(),a.get(q).getY(),kind,1,1000));
                lastShotFire=System.nanoTime();
            }
        }
    }

    public void wander(){
        //wander
        if(!(left||right||up||down)){
            if(Math.random()<.5){
                angle+=Math.toRadians(1*power);
            } else {
                angle-=Math.toRadians(1*power);
            }
        }
    }

    public void avoid(ArrayList<Plane> a){
        for(int b=0;b<a.size();b++){
            if(a.get(b)==this)
                continue;

            int obstacle = collisionCircle(x+(int)vx,y+(int)vy,50,a.get(b).getX()+a.get(b).getW()/2,a.get(b).getY()+a.get(b).getH()/2,a.get(b).getH()/2);
            if(obstacle>0&&!(left||right||up||down)) {
                if(obstacle==1)
                    angle-=Math.toRadians(2)*power;
                else
                    angle+=Math.toRadians(2)*power;
            }
        }
    }

    public void draw(Graphics g){
        //triangle
        Graphics2D a = (Graphics2D)g;
        a.translate(x,y);
        a.rotate(-angle);
        if(kind==1)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.RED);
        g.drawLine(-10,-10,-10,10);
        g.drawLine(-10,-10,10,0);
        g.drawLine(-10,10,10,0);
        a.rotate(angle);
        a.translate(-x,-y);

        g.setColor(Color.WHITE);
        if(viewbounds) {
            g.drawOval(-50, -50, 100, 100);
            if(kind==1)
                g.setColor(Color.green);
            g.drawOval(-200, -200, 400, 400);
        }



        /**
         *
         *
        trail.add(new Point(x,y));
        if(trail.size()>1)
            for(int z=0;z<trail.size()-1;z++)
                g.drawLine((int)trail.get(z).getX(),(int)trail.get(z).getY(),(int)trail.get(z+1).getX(),(int)trail.get(z+1).getY());
        **/
        //g.drawString(""+health,x,y+30);
        g.drawRect(x,y+30,30,5);
        g.fillRect(x,y+30,(int)((30.0/maxhealth)*health),5);

    }

    public int collisionCircle(int x1, int y1, int r1, int x2, int y2, int r2){
        double xDif = x1 - x2;
        double yDif = y1 - y2;
        double distanceSquared = xDif * xDif + yDif * yDif;
        double angleinbetween = Math.atan2((double)(y2-y1),(double) (x1-x2));
        angleinbetween+=Math.toRadians(180);


        if(!(distanceSquared < (r1 + r2) * (r1 + r2)))
            return 0;
        else if(angle-angleinbetween<0)
            return 1;
        else
            return 2;

    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getW() {
        return w;
    }
    public void setW(int w) {
        this.w = w;
    }
    public int getH() {
        return h;
    }
    public void setH(int h) {
        this.h = h;
    }
}
