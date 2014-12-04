import java.awt.*;
import java.util.ArrayList;

/**
 * Created by DiegoGonzalez on 12/3/14.
 */
public class EEE extends Minions{
public EEE(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers,Unit parent){
super(x,y,30,30,kind,40,1,5,objects,lasers,parent);
    alive=true;
    recharge=.05;
    priority=5;
    hate=1;
    bulletLife=.3;
    shotpower=3;
    offensive=true;
}
        public void shoot(int index){
            lasers.add(new Laser(x, y, objects.get(index).x, objects.get(index).y, 4,damage,shotpower, bulletLife));
        }

    public void draw(Graphics2D a){
        //super.draw(g);
        //Graphics2D a = (Graphics2D)g;
        a.translate(x,y);
        a.rotate(-angle);

        if(kind==1)
            a.setColor(Color.BLUE);
        else
            a.setColor(Color.RED);

        a.drawLine(-15, -15, -15, 15);

        a.drawLine(15, -15, -15, -15);
        a.drawLine(-15,15,15,15);

        a.drawLine(15,-15,15,-5);
        a.drawLine(15,15,15,5);

        a.drawLine(5,-5,15,-5);
        a.drawLine(5,5,15,5);

        a.drawLine(5,5,5,-5);

        a.rotate(angle);
        a.translate(-x,-y);
    }

    public boolean avoidAsteroid(){
        boolean hit=false;
        for(int b=0;b<objects.size();b++){
            if(objects.get(b).kind!=3)
                continue;

            boolean obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));
            if(obstacle) {
                hit=true;
            }
        }
        return hit;
    }

    public void dodgeAsteroid(){
        //tries to avoid an asteroid before hitting it
        for(int b=0;b<objects.size();b++){
            if(objects.get(b).kind!=3)
                continue;
            if(!objects.get(b).isInMap())
                continue;

            boolean obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2+20,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));
            if(obstacle) {
                double angle2=angle-angleBetween(b);


                if(compareAngle(angle,angle2))
                    setAngle(angle+Math.toRadians(3)*power);
                else
                    setAngle(angle-Math.toRadians(3)*power);
                //angle+=angleBetween(b); //obstacle;//Math.toRadians(3)*power;

            } else {
                obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2+40,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));
                if(obstacle) {
                    double angle2=angle-angleBetween(b);


                    if(compareAngle(angle,angle2))
                        setAngle(angle+Math.toRadians(2)*power);
                    else
                        setAngle(angle-Math.toRadians(2)*power);
                    //angle+=angleBetween(b); //obstacle;//Math.toRadians(3)*power;

                } else{
                    obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2+80,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));
                    if(obstacle) {
                        double angle2=angle-angleBetween(b);


                        if(compareAngle(angle,angle2))
                            setAngle(angle+Math.toRadians(1)*power);
                        else
                            setAngle(angle-Math.toRadians(1)*power);
                        //angle+=angleBetween(b); //obstacle;//Math.toRadians(3)*power;

                    }
                }


            }
        }
    }

}
