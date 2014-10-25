import java.awt.*;
import java.util.ArrayList;

/**
 * Created by DiegoGonzalez on 10/23/14.
 */
public class WWW extends Unit {
    double power=2;
    double angle=90;
    boolean left,right,down,up;

    public WWW(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,30,30,kind,150,objects,lasers);
        alive=true;
        recharge=.75;
        priority=1;
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

    public void action() {
        switch (priority){
            case 1:
                shootClosest();
                break;
            case 2:
                shootFarthest();
                break;
            case 3:
                shootWeakest();
                break;
            case 4:
                shootThreat();
        }
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
            lasers.add(new Laser(x, y, objects.get(indexOfClosest).x, objects.get(indexOfClosest).y, kind,50));
    }
    public void shootFarthest(){
        int indexOfClosest=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind)
                continue;
            if(collisionCircle(x,y,400,objects.get(q).x,objects.get(q).y,20)>0) {
                int distance1 = (int)Math.sqrt(Math.pow(x-objects.get(q).x,2)+Math.pow(y-objects.get(q).y,2));
                if(indexOfClosest==-1)
                    indexOfClosest=q;
                else if(distance1>((int)Math.sqrt(Math.pow(x-objects.get(indexOfClosest).x,2)+Math.pow(y-objects.get(indexOfClosest).y,2))))
                    indexOfClosest=q;

            }
        }
        if(indexOfClosest!=-1)
            lasers.add(new Laser(x, y, objects.get(indexOfClosest).x, objects.get(indexOfClosest).y, kind,50));
    }
    public void shootWeakest(){
        int indexOf=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind)
                continue;
            if(collisionCircle(x,y,400,objects.get(q).x,objects.get(q).y,20)>0) {
                if(indexOf==-1)
                    indexOf=q;
                else if(objects.get(q).health<objects.get(indexOf).health)
                    indexOf=q;

            }
        }
        if(indexOf!=-1)
            lasers.add(new Laser(x, y, objects.get(indexOf).x, objects.get(indexOf).y, kind,50));
    }
    public void shootThreat(){
        int indexOfClosest=-1;
        for(int q=1;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind)
                continue;
            if(collisionCircle(x,y,400,objects.get(q).x,objects.get(q).y,20)>0) {
                int distance1 = (int)Math.sqrt(Math.pow(objects.get(0).x-objects.get(q).x,2)+Math.pow(objects.get(0).y-objects.get(q).y,2));
                if(indexOfClosest==-1)
                    indexOfClosest=q;
                else if(distance1<((int)Math.sqrt(Math.pow(objects.get(0).x-objects.get(indexOfClosest).x,2)+Math.pow(objects.get(0).y-objects.get(indexOfClosest).y,2))))
                    indexOfClosest=q;

            }
        }
        if(indexOfClosest!=-1)
            lasers.add(new Laser(x, y, objects.get(indexOfClosest).x, objects.get(indexOfClosest).y, kind,50));
    }

    public void draw(Graphics g){
        super.draw(g);
        Graphics2D a = (Graphics2D)g;
        a.translate(x,y);
        a.rotate(-angle);

        if(kind==1)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.RED);

        g.drawLine(-15,-15,-15,15);

        g.drawLine(-15,-15,0,-15);
        g.drawLine(-15,15,0,15);

        g.drawLine(0,-15,0,-10);
        g.drawLine(0,15,0,10);

        g.drawLine(0,10,15,0);
        g.drawLine(0,-10,15,0);

        a.rotate(angle);
        a.translate(-x,-y);
    }
}
