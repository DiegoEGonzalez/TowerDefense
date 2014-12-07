import java.util.*;
import java.awt.*;
public class Asteroid extends Unit{
    private boolean mined = false;
    private double angle=0;

    public Asteroid(int x, int y, int w, int h, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,w,h,3,500,objects,lasers);
        angle=Math.random()*Math.toRadians(360);
        alive=true;
    }

    public void move() {
        angle=Math.random()*Math.toRadians(360);
        //x+=Math.cos(angle)*.005;
        //y+=Math.sin(angle)*.005;
        angle+=Math.random()*Math.toRadians(2)-Math.toRadians(1);
    }

    public void checkLasers(){
        for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext(); ) {
            Laser b = iterator.next();

            if(collisionCircle(x,y,h/2+h/4,(int)b.getX(),(int)b.getY(),2)&&b.kind!=kind){
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
        g.setColor(new Color(51,25,0));
        g.fillOval(x-(w/2),y-(h/2),w,h);
        g.setColor(new Color(153,76,0));
        g.drawOval(x-(w/2),y-(h/2),w,h);
    }
    public boolean isMined(){return mined;}
}