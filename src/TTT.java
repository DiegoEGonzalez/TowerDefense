import java.awt.*;
import java.util.ArrayList;

public class TTT extends Unit {
    double vx=0;
    double vy=0;
    double power=10;
    double angle=90;
    boolean left,right,down,up;
    public TTT(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,30,30,objects,lasers);
        recharge=1;
        lastAction=System.nanoTime();
        maxhealth=30;
        health=maxhealth;
        this.kind=kind;
        alive=true;
    }
    public void move(){
        if(angle<0)
            angle=Math.toRadians(360)+angle;
        angle=angle%Math.toRadians(360);
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
        else {
            if (Math.random() < .5) {
                angle += Math.toRadians(1 * power);
            } else {
                angle -= Math.toRadians(1 * power);
            }
        }

    }
    public void action(){
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind)
                continue;
            if(collisionCircle(x,y,400,objects.get(q).x,objects.get(q).y,20)>0) {
                lasers.add(new Laser(x, y, objects.get(q).x, objects.get(q).y, kind));
                break;
            }
        }
    }
    public void draw(Graphics g){
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
        g.drawRect(x,y+30,30,5);
        g.fillRect(x,y+30,(int)((30.0/maxhealth)*health),5);

    }
}
