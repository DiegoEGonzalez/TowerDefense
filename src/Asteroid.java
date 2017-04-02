import com.sun.xml.internal.rngom.parse.host.Base;

import java.util.*;
import java.awt.*;
public class Asteroid extends Unit{
    private boolean mined = false;
    private double angle=0;


    public Asteroid(int x, int y, int w, int h, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,w,h,0,500,objects,lasers);
        angle=Math.random()*Math.toRadians(360);
        alive=true;
    }

    public void move() {
        //angle=Math.random()*Math.toRadians(360);
        double speed = .03;
        boolean canmove=true;
        double vx = Math.cos(angle) * speed*10;
        double vy = Math.sin(angle) * speed*10;
        for (int q=0;q<objects.size();q++){
            if(!(objects.get(q) instanceof BASE))
                continue;

            BASE a =(BASE)objects.get(q);

            if(collisionCircle((int)Math.round(x+vx),(int)Math.round(y+vy),h/2,a.getX(),a.getY(),User.basefield/2)) {
            canmove=false;
            }
        }
        if(canmove) {
            x += Math.cos(angle) * speed*10;
            y += Math.sin(angle) * speed*10;
        } else {
            angle=addAngles(angle);
        }
        setAngle(angle+Math.random()*Math.toRadians(2)-Math.toRadians(1));

        if(!isInBounds())
            alive = false;
    }

    public void checkLasers(){
        for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext(); ) {
            Laser b = iterator.next();

            if(collisionCircle(getX(),getY(),h/2+h/4,(int)b.getX(),(int)b.getY(),2)&&b.kind!=kind){
                health-=b.getDamage();

                for(int y=0;y<3;y++){
                    if(b.kind==2)
                        Alpha.fx.add(new Particles(new Color(255,0,0,(int)(255)),1,b.x,b.y,(Math.random()*.25),Math.random()*Math.toRadians(360),(b.speed/2*Math.random())+b.speed/2,0));
                    else if(kind==1)
                        Alpha.fx.add(new Particles(new Color(0,255,255,(int)(255)),1,b.x,b.y,(Math.random()*.25),Math.random()*Math.toRadians(360),(b.speed/2*Math.random())+b.speed/2,0));
                    else
                        Alpha.fx.add(new Particles(new Color(0,255,0,(int)(255)),1,b.x,b.y,(Math.random()*.25),Math.random()*Math.toRadians(360),(b.speed/2*Math.random())+b.speed/2,0));
                }
                b.kill();

            }

            if(b.kind==4&&health<=0)
                mined=true;

        }
    }
    public void draw(Graphics2D g){
        //if(isInBounds())
          //  g.setColor(Color.green);
        //else
            g.setColor(new Color(51,25,0));
        g.fillOval(getX()-(w/2),getY()-(h/2),w,h);
        g.setColor(new Color(153,76,0));
        g.drawOval(getX()-(w/2),getY()-(h/2),w,h);
    }
    public boolean isMined(){return mined;}

    public void setAngle(double angle){
        this.angle=angle;
        if (this.angle < 0)
            this.angle = Math.toRadians(360) + this.angle;

        this.angle = this.angle % Math.toRadians(360);

    }

    public boolean isInBounds(){

        double mapx1 = 0;
        double mapx2 = Alpha.mapsize + Alpha.mapsize /4;

        double mapy1 = -Alpha.mapsize/2 - Alpha.mapsize/4;
        double mapy2 = (Alpha.mapsize + Alpha.mapsize/2)/2;

        double asteroidx1 = x - w/2;
        double asteroidx2 = x + w/2;
        double asteroidy1 = y - w/2;
        double asteroidy2 = y + w/2;

        boolean collision = false;
        //check left wall
        if((asteroidx2<mapx2 && asteroidx2>mapx1)&&(asteroidy2<mapy2 && asteroidy1>mapy1))
            collision = true;

/**
        if(x<0)
            return false;
        if(x>(Alpha.mapsize + Alpha.mapsize / 4 + Alpha.mapsize / 8))
            return false;
        if(y<(-Alpha.mapsize / 2 - Alpha.mapsize / 4 - Alpha.mapsize / 8))
            return false;
        if(y>(Alpha.mapsize + Alpha.mapsize / 2 + Alpha.mapsize / 4)/2)
            return false;
 **/

        return collision;


    }
}