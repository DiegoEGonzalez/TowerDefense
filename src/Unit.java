import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Unit{
    double power=0;
    double x,y=0;
    int h,w;
    double vx,vy=0;
    int maxhealth,health;
    int kind;
    boolean alive;
    ArrayList<Unit> objects;
    ArrayList<Laser> lasers;
    double recharge; //how much time in between action ( in seconds )
    long lastAction; //when the last action was triggered
    int priority=1;

    Spawner parent;

    public Unit(int x, int y, int w, int h, int kind, int health, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.kind=kind;
        maxhealth=health;
        this.health=health;
        this.objects=objects;
        this.lasers=lasers;
    }

    public void update(){
        move();
        action();
        checkLasers();

        if(health<=0) {
            alive = false;
            if (this instanceof Minions) {
                for(int q=0;q<objects.size();q++){
                    if(objects.get(q)==parent&&kind==1){
                        parent.count--;
                    }
                }
            }
        } else if (health>maxhealth){
            health=maxhealth;
        }

    }

    public void action(){

    }

    public void move(){

    }

    public void draw(Graphics2D g){
        g.setColor(Color.WHITE);
        g.drawRect(getX()-w/2,getY()+h/2+10,w,5);
        g.fillRect(getX()-w/2,getY()+h/2+10,(int)(((double)w/maxhealth)*health),5);

    }

    public void checkLasers(){
        for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext(); ) {
            Laser b = iterator.next();

            if((kind!=0)&&(b.kind==4))
                continue;
            if(!b.isAlive()){
                continue;
            }

            int health2=health;

            if(collisionCircle(getX(),getY(),h/2,(int)b.getX(),(int)b.getY(),2)&&b.kind!=kind){
                health-=b.getDamage();

                switch (b.kind){
                    case 1:
                        for(int y=0;y<3;y++)
                        Alpha.fx.add(new Particles(new Color(0,255,255,(int)(255)),1,b.x,b.y,(Math.random()*.25),Math.random()*Math.toRadians(360),(b.speed/2*Math.random())+b.speed/2,0));
                        break;
                    case 2:
                        for(int y=0;y<3;y++)
                        Alpha.fx.add(new Particles(new Color(255,0,0,(int)(255)),1,b.x,b.y,(Math.random()*.25),Math.random()*Math.toRadians(360),(b.speed/2*Math.random())+b.speed/2,0));
                        break;
                    case 3:
                        for(int y=0;y<6;y++)
                        Alpha.fx.add(new Particles(new Color(0,255,0,(int)(255)),(int)(Math.random()*2)+1,b.x,b.y,(Math.random()*.5),Math.random()*Math.toRadians(360),Math.random()*8+3,0));
                        break;
                }


                b.damage-=health2;
                if(b.damage<=0)
                b.kill();

            }

        }
    }

    public boolean collisionCircle(int x1, int y1, int r1, int x2, int y2, int r2){
        double xDif = x1 - x2;
        double yDif = y1 - y2;
        double distanceSquared = xDif * xDif + yDif * yDif;

        return (distanceSquared < (r1 + r2) * (r1 + r2));
    }

    public boolean isInMap(){
        if(x<0)
            return false;
        if(x>Alpha.mapsize)
            return false;
        if(y<-Alpha.mapsize/2)
            return false;
        if(y>Alpha.mapsize/2)
            return false;

        return true;
    }

    public static boolean compareAngle(double a, double b){

        if(Math.abs(a-b)>Math.toRadians(180)) {
            if(a<b)
            return (a < (b - Math.toRadians(360)));
            else
                return (b < (a - Math.toRadians(360)));

        }else
            return (a<b);
    }

    public int getX(){
        return (int)Math.round(x);
    }
    public int getY(){
        return (int)Math.round(y);
    }

}
