import java.awt.*;
import java.util.ArrayList;

public class Laser{
     double x;
     double y;
     double w=2;
     double h=2;
     double deltax;
     double deltay;
     static int size = 4;
     static double speed = 15;
    private boolean alive;
    int kind;
    int damage;
    int life;
    ArrayList<Point> trail = new ArrayList<Point>();


    public Laser(double x,double y,double x2,double y2,int kind, int damage, int life){
        this.x=x;
        this.y=y;
        double slope = (y-y2)/(x-x2);
        deltax=x2<x?-1.0:1.0;
        deltay=deltax*slope;
        double distance = Math.sqrt(Math.pow(deltax,2)+Math.pow(deltay,2));
        deltax=deltax/distance;
        deltay=deltay/distance;
        alive=true;
        this.kind=kind;
        this.damage=damage;
        this.life=life;
        trail.add(new Point((int)x,(int)y));
    }

    public void draw(Graphics g){
        if(kind==1)
            g.setColor(Color.CYAN);
        else
            g.setColor(Color.RED);

        g.drawLine((int)x,(int)y,(int)(x-deltax*10),(int)(y-deltay*10));


    }
    public void update(){
        move();
        life--;
        trail.add(new Point((int)x,(int)y));
        if(trail.size()>3)
            trail.remove(0);
    }

    public void move(){
        x+=deltax*speed;
        y+=deltay*speed;

        if(x>FlightDemo.mapsize||x<0||y>FlightDemo.mapsize||y<0){
            alive=false;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDeltax() {
        return deltax;
    }

    public double getDeltay() {
        return deltay;
    }

    public static int getSize() {
        return size;
    }

    public boolean isAlive(){
        return alive;
    }

    public void hit(int a){
        damage-=a;
    }

    public int getDamage(){
        return damage;
    }
    public void kill(){
        alive=false;
    }


}
