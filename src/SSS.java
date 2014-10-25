import java.awt.*;
import java.util.ArrayList;

public class SSS extends Unit {

    double power=10;
    double angle=90;
    boolean left,right,down,up;

    public SSS(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,30,30,kind,30,objects,lasers);
        recharge=.3;
        lastAction=System.nanoTime();
        this.kind=kind;
        alive=true;
        priority=4;
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
            avoid();
        }

    }
    public void avoid(){
        for(int b=0;b<lasers.size();b++){
            if(lasers.get(b).kind==kind)
                continue;

            double obstacle = collisionCircle(x,y,50,(int)(lasers.get(b).x+lasers.get(b).deltax),(int)(lasers.get(b).y+lasers.get(b).deltay),(int)(lasers.get(b).h/2));
            if(obstacle>0&&!(left||right||up||down)) {
                if(obstacle>0)
                    angle-=Math.toRadians(3)*power;
                else
                    angle+=Math.toRadians(3)*power;
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
            lasers.add(new Laser(x, y, objects.get(indexOfClosest).x, objects.get(indexOfClosest).y, kind,10));
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
            lasers.add(new Laser(x, y, objects.get(indexOfClosest).x, objects.get(indexOfClosest).y, kind,10));
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
            lasers.add(new Laser(x, y, objects.get(indexOf).x, objects.get(indexOf).y, kind,10));
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
            lasers.add(new Laser(x, y, objects.get(indexOfClosest).x, objects.get(indexOfClosest).y, kind,10));
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

        g.drawLine(-10,-10,-10,-4);
        g.drawLine(-10,10,-10,4);

        g.drawLine(-10,-10,10,0);
        g.drawLine(-10,10,10,0);

        g.drawLine(-10,-4,0,0);
        g.drawLine(-10,4,0,0);

        a.rotate(angle);
        a.translate(-x,-y);


    }
}
