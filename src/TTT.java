import java.awt.*;
import java.util.ArrayList;

public class TTT extends Unit {
    double power=5;
    double angle=90;
    boolean left,right,down,up;


    public TTT(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,30,30,kind,50,objects,lasers);
        recharge=1;
        lastAction=System.nanoTime();
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
        left = x+vx>FlightDemo.mapsize-50;
        right = x+vx<50;
        up = y+vy>FlightDemo.mapsize-50;
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
        if(priority==1)
            shootClosest();

    }

    public void shootClosest(){
        int indexOfClosest=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind)
                continue;
            if(collisionCircle(x,y,400,objects.get(q).x,objects.get(q).y,20)>0) {
                int distance1 = (int)Math.sqrt(Math.pow(x-objects.get(q).x,2)+Math.pow(y-objects.get(q).y,2));
                if(indexOfClosest==-1)
                    indexOfClosest=q;
                else if(distance1<((int)Math.sqrt(Math.pow(x-objects.get(indexOfClosest).x,2)+Math.pow(y-objects.get(indexOfClosest).y,2))))
                    indexOfClosest=q;

            }
        }
        if(indexOfClosest!=-1)
            lasers.add(new Laser(x, y, objects.get(indexOfClosest).x, objects.get(indexOfClosest).y, kind,10));
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


    }
}
