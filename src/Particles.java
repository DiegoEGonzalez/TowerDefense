import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Particles {
    private double x;
    private double y;
    private double x2;
    private double y2;
    private double size;
    private double velocity=.5;
    private Color color;
    private long life;
    private double lifetime;
    boolean alive;
    double angle;
    double speed = 10;
    int shape=0;

    public Particles(Color color, double size, double x, double y, double lifetime, double angle, double speed,int shape){
        this.color=color;
        this.x=x;
        this.y=y;
        this.size=size;
        life=System.nanoTime();
        this.lifetime=lifetime;
        this.angle=angle;
        this.speed=speed;
        this.shape=shape;
        alive=true;
    }

    public void draw(Graphics g){
        g.setColor(color);
        if(shape==0)
        g.fillRect((int)Math.round(x),(int)Math.round(y),(int)Math.round(size),(int)Math.round(size));
        else
            g.fillOval((int)Math.round(x),(int)Math.round(y),(int)Math.round(size),(int)Math.round(size));

    }

    public void update(){
        x+=Math.cos(angle)*speed;
        y+=Math.sin(angle)*speed;
        if((System.nanoTime()-life)/1000000000.0>lifetime) {
            alive=false;
        }
    }

    public boolean isAlive(){
        return alive;
    }

}
