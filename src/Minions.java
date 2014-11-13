import java.util.ArrayList;

public class Minions extends Unit{
    double power;
    double shotpower=10;
    double angle;
    boolean left,right,down,up;
    int target=0;
    int hate =3;
    int damage;
    boolean offensive=false;
    int bulletLife=100;


    public Minions(int x, int y,int w, int h, int kind, int health, double power,int damage,ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,w,h,kind,health,objects,lasers);
        this.power=power;
        this.damage=damage;
        if(kind==1)
            angle=0;
        else
            angle=Math.toRadians(180);
    }
    public void move(){
        if(angle<0)
            angle=Math.toRadians(360)+angle;

        angle=angle%Math.toRadians(360);
        avoidAsteroid();

        vy=(Math.sin(-angle)*power);
        vx=(Math.cos(-angle)*power);
        //checks whether velocity will put it out of the map or out of the spawnarea.
        left = ((x+vx>Alpha.mapsize)&&(x<Alpha.mapsize))||((x+vx>Alpha.mapsize+Alpha.mapsize/4+Alpha.mapsize/8)&&(x<Alpha.mapsize+Alpha.mapsize/4+Alpha.mapsize/8));
        right = ((x+vx<0)&&(x>0));
        up = ((y+vy>Alpha.mapsize/2)&&(y<Alpha.mapsize/2))||((y+vy>Alpha.mapsize/2+Alpha.mapsize/4)&&(y<Alpha.mapsize/2+Alpha.mapsize/4));
        down = ((y+vy<-Alpha.mapsize/2)&&(y>-Alpha.mapsize/2))||((y+vy<-Alpha.mapsize/2-Alpha.mapsize/4)&&(y>-Alpha.mapsize/2-Alpha.mapsize/4));

        if(clear()&&!(left||right||up||down)){
            x+=Math.round(vx);
            y+=Math.round(vy);
        }


        if(left||right||up||down)
            angle+=Math.toRadians(90);
        else {
            if(findTarget(hate)==-1) {
                if(User.targeti!=-1){
                    seek(User.targeti);
                } else if(User.targetX!=-1&&User.targetY!=-1){
                    seek(User.targetX,User.targetY);
                }else if(findTarget(1)!=-1){
                    seek(findTarget(1));
                } else {
                    //wander();
                }
            } else {
                seek(findTarget(hate));
            }
            move2();
        }

    }
    public void move2(){

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
    public void avoidAsteroid(){
        for(int b=0;b<objects.size();b++){
            if(objects.get(b).kind!=3)
                continue;

            double obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));
            if(obstacle!=0) {
                if(obstacle>0)
                    angle-=obstacle; //obstacle;//Math.toRadians(3)*power;
                else
                    angle+=obstacle; //obstacle;
            }
        }
    }
    public boolean clear(){
        for(int b=0;b<objects.size();b++){
            if(objects.get(b).kind!=3)
                continue;

            double obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));
            if(obstacle!=0) {
                return false;
            }
        }
        return true;
    }

    public void seek(int i){
        double a2 = Math.atan2((double)-(objects.get(i).y-y),(double)(objects.get(i).x-x));
        a2=(a2 %= Math.toRadians(360)) >= 0 ? a2 : (a2 + Math.toRadians(360));

        double dif = a2-angle;
        if(dif>Math.toRadians(180))
            dif-=Math.toRadians(360);
        else if(dif<Math.toRadians(-180))
            dif+=Math.toRadians(360);

        if(dif<0)
            angle-=Math.toRadians(.5)*power;
        else
            angle+=Math.toRadians(.5)*power;

    }

    public void seek(int i, int q){
        double a2 = Math.atan2((double)-(q-y),(double)(i-x));
        a2=(a2 %= Math.toRadians(360)) >= 0 ? a2 : (a2 + Math.toRadians(360));

        double dif = a2-angle;
        if(dif>Math.toRadians(180))
            dif-=Math.toRadians(360);
        else if(dif<Math.toRadians(-180))
            dif+=Math.toRadians(360);

        if(dif<0)
            angle-=Math.toRadians(.5)*power;
        else
            angle+=Math.toRadians(.5)*power;

    }

    public void wander(){
        if (Math.random() < .5) {
            angle += Math.toRadians(1 * power);
        } else {
            angle -= Math.toRadians(1 * power);
        }
    }

    public void action() {
        /**
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
         **/
    }
    public void shootClosest(){
        int indexOfClosest=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind||objects.get(q).kind==3)
                continue;
            if(collisionCircle(x,y,400,objects.get(q).x,objects.get(q).y,20)!=0&&Math.abs(collisionCircle(x,y,400,objects.get(q).x,objects.get(q).y,20))<Math.toRadians(90)) {
                int distance1 = (int)Math.sqrt(Math.pow(x-objects.get(q).x,2)+Math.pow(y-objects.get(q).y,2));
                if(indexOfClosest==-1) {
                    indexOfClosest = q;
                } else if(distance1<((int)Math.sqrt(Math.pow(x-objects.get(indexOfClosest).x,2)+Math.pow(y-objects.get(indexOfClosest).y,2)))) {
                    indexOfClosest = q;
                }

            }
        }
        if(indexOfClosest!=-1)
            lasers.add(new Laser(x, y, objects.get(indexOfClosest).x, objects.get(indexOfClosest).y, kind,damage,shotpower, bulletLife));
    }
    public void shootFarthest(){
        int indexOfClosest=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind||objects.get(q).kind==3)
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
            lasers.add(new Laser(x, y, objects.get(indexOfClosest).x, objects.get(indexOfClosest).y, kind,damage,shotpower, bulletLife));
    }
    public void shootWeakest(){
        int indexOf=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind||objects.get(q).kind==3)
                continue;
            if(collisionCircle(x,y,400,objects.get(q).x,objects.get(q).y,20)>0) {
                if(indexOf==-1)
                    indexOf=q;
                else if(objects.get(q).health<objects.get(indexOf).health)
                    indexOf=q;

            }
        }
        if(indexOf!=-1)
            lasers.add(new Laser(x, y, objects.get(indexOf).x, objects.get(indexOf).y, kind,damage,shotpower, bulletLife));
    }
    public void shootThreat(){
        int indexOfClosest=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind||objects.get(q).kind==3)
                continue;
            if(collisionCircle(x,y,400,objects.get(q).x,objects.get(q).y,20)!=0&&Math.abs(collisionCircle(x,y,400,objects.get(q).x,objects.get(q).y,20))<Math.toRadians(90)) {
                int distance1 = (int)Math.sqrt(Math.pow(objects.get(0).x-objects.get(q).x,2)+Math.pow(objects.get(0).y-objects.get(q).y,2));
                if(indexOfClosest==-1)
                    indexOfClosest=q;
                else if(distance1<((int)Math.sqrt(Math.pow(objects.get(0).x-objects.get(indexOfClosest).x,2)+Math.pow(objects.get(0).y-objects.get(indexOfClosest).y,2))))
                    indexOfClosest=q;

            }
        }
        if(indexOfClosest!=-1)
            lasers.add(new Laser(x, y, objects.get(indexOfClosest).x, objects.get(indexOfClosest).y, kind,damage,shotpower, bulletLife));
    }
    public int findTarget(int thing){
        int indexOfClosest=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this)
                continue;
            if(offensive||kind==2) {
                if (objects.get(q).kind == kind||objects.get(q).kind==3)
                    continue;
            }else {
                if (objects.get(q).kind != kind)
                    continue;
            }


            switch (thing){
                case 1:
                    if(!(objects.get(q)instanceof BASE))
                        continue;
                    break;
                case 2:
                    if(!((objects.get(q)instanceof SSSSpawner)||(objects.get(q)instanceof TTTSpawner)||(objects.get(q)instanceof WWWSpawner)))
                        continue;
                    break;
                case 3:

                    if(!((objects.get(q)instanceof SSS)||(objects.get(q)instanceof TTT)||(objects.get(q)instanceof WWW)))
                        continue;
                    break;
            }



            if(collisionCircle(x,y,Alpha.mapsize*2,objects.get(q).x,objects.get(q).y,20)>0) {
                int distance1 = (int)Math.sqrt(Math.pow(x-objects.get(q).x,2)+Math.pow(y-objects.get(q).y,2));
                if(indexOfClosest==-1)
                    indexOfClosest=q;
                else if(distance1<((int)Math.sqrt(Math.pow(x-objects.get(indexOfClosest).x,2)+Math.pow(y-objects.get(indexOfClosest).y,2))))
                    indexOfClosest=q;

            }
        }
        return indexOfClosest;

    }
}
