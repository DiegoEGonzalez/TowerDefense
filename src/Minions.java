import java.util.ArrayList;

public class Minions extends Unit{

    double shotpower=10;
    double angle;
    boolean left,right,down,up;
    int target=0;
    int hate =3;
    int damage;
    boolean offensive=false;
    double bulletLife=100;
    boolean smart=false;
    boolean attack=true;

    ArrayList<Integer>strategy = new ArrayList<Integer>();
    ArrayList<Integer>strategy2 = new ArrayList<Integer>();




    public Minions(int x, int y,int w, int h, int kind, int health, double power,int damage,ArrayList<Unit> objects, ArrayList<Laser> lasers,Unit parent){
        super(x,y,w,h,kind,health,objects,lasers);
        this.power=power;
        this.damage=damage;
        if(kind==1)
            setAngle(0);
        else
            setAngle(Math.toRadians(180));

        this.parent=(Spawner)parent;
    }

    public void move() {

        setAngle(angle); //resets angle to make sure it's within 0-359 degrees

        vy = (Math.sin(-angle) * power);
        vx = (Math.cos(-angle) * power);
        if(!avoidAsteroid()){
            dodgeAsteroid();
        } else if (!(this instanceof EEE)) {
            health-=power;
        }


        vy = (Math.sin(-angle) * power);
        vx = (Math.cos(-angle) * power);

        //checks whether velocity will put it out of the map or out of the spawnarea.
        boolean outOfMap = ((x + vx > Alpha.mapsize) && (x <= Alpha.mapsize));
        boolean outOfSpawn = (x + vx > (Alpha.mapsize + Alpha.mapsize / 4 + Alpha.mapsize / 8)) && (x <= Alpha.mapsize + Alpha.mapsize / 4 + Alpha.mapsize / 8);
        left = outOfMap || outOfSpawn;

        right = ((x + vx < 0) && (x >= 0));

        outOfMap = ((y + vy > Alpha.mapsize / 2) && (y <= Alpha.mapsize / 2));
        outOfSpawn = ((y + vy > Alpha.mapsize / 2 + Alpha.mapsize / 4) && (y <= Alpha.mapsize / 2 + Alpha.mapsize / 4));
        up = outOfMap||outOfSpawn;

        outOfMap=((y + vy < -Alpha.mapsize / 2) && (y > -Alpha.mapsize / 2));
        outOfSpawn = ((y + vy < -Alpha.mapsize / 2 - Alpha.mapsize / 4) && (y >= -Alpha.mapsize / 2 - Alpha.mapsize / 4));
        down = outOfMap ||outOfSpawn;


        if (clear() && !left && !right && !up && !down) {
            x += Math.round(vx);
            y += Math.round(vy);
        }


        if (left || right || up || down){
            setAngle(angle+Math.toRadians(180));
        }else if(clear()){

            if(findTarget(hate)==-1) {
                for (int b=0;b<5;b++){
                    if(hate+b>3)
                        break;
                    if(findTarget(hate+b)!=-1) {
                        seek(hate + b);
                        break;
                    }
                }
                //wander();
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

            boolean obstacle = collisionCircle(x,y,75,(int)(lasers.get(b).x+lasers.get(b).deltax),(int)(lasers.get(b).y+lasers.get(b).deltay),(int)(lasers.get(b).h/2));

            if(obstacle&&(!left||!right||!up||!down)&&!isFacing2(b, 90)) {
                if(obstacle)
                    setAngle(angle-Math.toRadians(3)*power);
                else
                    setAngle(angle+Math.toRadians(3)*power);
            }
        }
    }

    public boolean avoidAsteroid(){
        boolean hit=false;
        for(int b=0;b<objects.size();b++){
            if(objects.get(b).kind!=3)
                continue;

            boolean obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));
            if(obstacle) {
                hit=true;
                double angle2=angle-angleBetween(b);


                if(compareAngle(angle,angle2))
                    setAngle(angle-Math.toRadians(2)*power);
                else
                    setAngle(angle+Math.toRadians(2)*power);

                objects.get(b).health-=power;

                //setAngle(angle+angleBetween(b));


                //angle+=angleBetween(b); //obstacle;//Math.toRadians(3)*power;

            }
        }
        return hit;
    }

    public void dodgeAsteroid(){
        //tries to avoid an asteroid before hitting it
        for(int b=0;b<objects.size();b++){
            if(objects.get(b).kind!=3)
                continue;

            boolean obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2+20,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));
            if(obstacle) {
                double angle2=angle-angleBetween(b);


                if(compareAngle(angle,angle2))
                    setAngle(angle-Math.toRadians(3)*power);
                else
                    setAngle(angle+Math.toRadians(3)*power);
                //angle+=angleBetween(b); //obstacle;//Math.toRadians(3)*power;

            } else {
                obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2+40,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));
                if(obstacle) {
                    double angle2=angle-angleBetween(b);


                    if(compareAngle(angle,angle2))
                        setAngle(angle-Math.toRadians(2)*power);
                    else
                        setAngle(angle+Math.toRadians(2)*power);
                    //angle+=angleBetween(b); //obstacle;//Math.toRadians(3)*power;

                } else{
                    obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2+80,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));
                    if(obstacle) {
                        double angle2=angle-angleBetween(b);


                        if(compareAngle(angle,angle2))
                            setAngle(angle-Math.toRadians(1)*power);
                        else
                            setAngle(angle+Math.toRadians(1)*power);
                        //angle+=angleBetween(b); //obstacle;//Math.toRadians(3)*power;

                    }
                }


            }
        }
    }

    public boolean clear(){
        //checks to see if it overlaps anything
        for(int b=0;b<objects.size();b++){
            if(objects.get(b).kind!=3) //if the object is not a meteor, skip
                continue;

            boolean obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));

            if(obstacle) {
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

        if(attack) {
            if (dif < 0)
                setAngle(angle - Math.toRadians(.5) * power);
            else
                setAngle(angle + Math.toRadians(.5) * power);
        }else {
            if (dif < 0)
                setAngle(angle + Math.toRadians(.5) * power);
            else
                setAngle(angle - Math.toRadians(.5) * power);
        }

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
            setAngle(angle +Math.toRadians(1 * power));
        } else {
            setAngle(angle - Math.toRadians(1 * power));
        }
    }

    public void action() {
        int index = -1;

        switch (priority){
            case 1:
                index=findClosest();
                break;
            case 2:
                index=findFarthest();
                break;
            case 3:
                index=findWeakest();
                break;
            case 4:
                index=findThreat();
                break;
            case 5:
                index=findMOOLAH();
                break;
        }

        if((System.nanoTime()-lastAction)/1000000000.0>recharge&&index>-1) {
            shoot(index);
            lastAction=System.nanoTime();
        }


    }

    public int findClosest(){
        int index=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind||(objects.get(q).kind==3))
                continue;
            int searchRadius = 400; //radius of whether the object will be findable
            boolean search = collisionCircle(x,y,searchRadius,objects.get(q).x,objects.get(q).y,20); // whether the object is close enough to be considered
            boolean facing = isFacing(q,45); //whether it's facing the object
            if(search&&facing) {
                if(index==-1) {
                    index = q;
                }

                int distance1 = (int)Math.sqrt(Math.pow(x-objects.get(q).x,2)+Math.pow(y-objects.get(q).y,2)); //distance possible object
                int distance2 = ((int)Math.sqrt(Math.pow(x-objects.get(index).x,2)+Math.pow(y-objects.get(index).y,2))); // distance for current closest object
                if (distance1<distance2) {
                    index = q;
                }

            }
        }
        return index;
    }

    public int findFarthest(){
        int index=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind||objects.get(q).kind==3)
                continue;
            int searchRadius = 400; //radius of whether the object will be findable
            boolean search = collisionCircle(x,y,searchRadius,objects.get(q).x,objects.get(q).y,20); // whether the object is close enough to be considered
            boolean facing = isFacing(q,45); //whether it's facing the object
            if(search&&facing) {
                if(index==-1)
                    index=q;
                int distance1 = (int)Math.sqrt(Math.pow(x-objects.get(q).x,2)+Math.pow(y-objects.get(q).y,2));//challenger for farthest distance distance...
                int distance2 = ((int)Math.sqrt(Math.pow(x-objects.get(index).x,2)+Math.pow(y-objects.get(index).y,2))); //current farthest distance

                if(distance1>distance2)
                    index=q;

            }
        }
        return index;
    }

    public int findWeakest(){
        int index=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind||objects.get(q).kind==3)
                continue;
            int searchRadius = 400; //radius of whether the object will be findable
            boolean search = collisionCircle(x,y,searchRadius,objects.get(q).x,objects.get(q).y,20); // whether the object is close enough to be considered
            boolean facing = isFacing(q,45); //whether it's facing the object
            if(search&&facing) {
                if(index==-1)
                    index=q;
                else if(objects.get(q).health<objects.get(index).health)
                    index=q;

            }
        }
        return index;
    }

    public int findThreat(){
        int index=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind||objects.get(q).kind==3)
                continue;
            int searchRadius = 400; //radius of whether the object will be findable
            boolean search = collisionCircle(x,y,searchRadius,objects.get(q).x,objects.get(q).y,20); // whether the object is close enough to be considered
            boolean facing = isFacing(q,45); //whether it's facing the object
            if(search&&facing) {
                int distance1 = (int)Math.sqrt(Math.pow(objects.get(0).x-objects.get(q).x,2)+Math.pow(objects.get(0).y-objects.get(q).y,2));
                if(index==-1)
                    index=q;
                else if(distance1<((int)Math.sqrt(Math.pow(objects.get(0).x-objects.get(index).x,2)+Math.pow(objects.get(0).y-objects.get(index).y,2))))
                    index=q;

            }
        }
        return index;
    }

    public int findMOOLAH(){
        int index=-1;
        for(int q=0;q<objects.size();q++) {
            if ((objects.get(q).kind!=3))
                continue;
            int searchRadius = h/2+100; //radius of whether the object will be findable
            boolean search = collisionCircle(x,y,searchRadius,objects.get(q).x,objects.get(q).y,objects.get(q).h/2); // whether the object is close enough to be considered
            boolean facing = isFacing(q,45); //whether it's facing the object
            if(search&&facing) {
                if(index==-1) {
                    index = q;
                }

                int distance1 = (int)Math.sqrt(Math.pow(x-objects.get(q).x,2)+Math.pow(y-objects.get(q).y,2)); //distance possible object
                int distance2 = ((int)Math.sqrt(Math.pow(x-objects.get(index).x,2)+Math.pow(y-objects.get(index).y,2))); // distance for current closest object
                if (distance1<distance2) {
                    index = q;
                }

            }
        }
        return index;
    }

    public int stayTogether(){
        int index=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind!=kind)
                continue;
            int searchRadius = 100; //radius of whether the object will be findable
            boolean search = collisionCircle(x,y,searchRadius,objects.get(q).x,objects.get(q).y,20); // whether the object is close enough to be considered
            boolean facing = isFacing(q,20); //whether it's facing the object
            if(search&&facing) {
                if(index==-1) {
                    index = q;
                }

                int distance1 = (int)Math.sqrt(Math.pow(x-objects.get(q).x,2)+Math.pow(y-objects.get(q).y,2)); //distance possible object
                int distance2 = ((int)Math.sqrt(Math.pow(x-objects.get(index).x,2)+Math.pow(y-objects.get(index).y,2))); // distance for current closest object
                if (distance1<distance2) {
                    index = q;
                }

            }
        }

        if(index!=-1) {
            return findClosest();
        } else {
            index=-1;
            for(int q=0;q<objects.size();q++) {
                if (objects.get(q) == this||objects.get(q).kind!=kind)
                    continue;
                int searchRadius = 400; //radius of whether the object will be findable
                boolean search = collisionCircle(x,y,searchRadius,objects.get(q).x,objects.get(q).y,20); // whether the object is close enough to be considered
                boolean facing = isFacing(q,20); //whether it's facing the object
                if(search&&facing) {
                    if(index==-1) {
                        index = q;
                    }

                    int distance1 = (int)Math.sqrt(Math.pow(x-objects.get(q).x,2)+Math.pow(y-objects.get(q).y,2)); //distance possible object
                    int distance2 = ((int)Math.sqrt(Math.pow(x-objects.get(index).x,2)+Math.pow(y-objects.get(index).y,2))); // distance for current closest object
                    if (distance1<distance2) {
                        index = q;
                    }

                }
            }
            if(index!=-1)
                return index;
            else
                return findClosest();
        }




    }

    public int findTarget(int thing){

        int indexOfClosest=-1;
        for(int q=0;q<objects.size();q++) {

            if (objects.get(q) == this)
                continue;

            if(offensive) {
                if (objects.get(q).kind == kind||objects.get(q).kind==3)
                    continue;
            } else {
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


            if(!objects.get(q).isInMap())
                continue;


            //int searchradius = (kind==1)?300:1000;

            //if(collisionCircle(x,y,Alpha.mapsize*2,objects.get(q).x,objects.get(q).y,20)) {
            int distance1 = (int)Math.sqrt(Math.pow(x-objects.get(q).x,2)+Math.pow(y-objects.get(q).y,2));
            //if(distance1<searchradius) {
            if (indexOfClosest == -1)
                indexOfClosest = q;
            else if (distance1 < ((int) Math.sqrt(Math.pow(x - objects.get(indexOfClosest).x, 2) + Math.pow(y - objects.get(indexOfClosest).y, 2))))
                indexOfClosest = q;

            //}
            // }
        }
        return indexOfClosest;

    }
    /**
     public void determineThreat(int index){

     boolean insideMap = (x < Alpha.mapsize && x > 0 && y < Alpha.mapsize / 2 && y > -Alpha.mapsize / 2);
     boolean goingInside = (objects.get(index).x<Alpha.mapsize&&objects.get(index).x>0&&objects.get(index).y<Alpha.mapsize/2&&objects.get(index).y>-Alpha.mapsize/2);
     if (insideMap&&goingInside) {
     int threat = Alpha.threat[(int) ((double) x / Alpha.mapsize * 10)][(int) (((double) y + Alpha.mapsize / 2) / Alpha.mapsize * 10)]++;
     int threat2 = Alpha.threat2[(int) ((double) objects.get(index).x / Alpha.mapsize * 10)][(int) (((double) objects.get(index).y + Alpha.mapsize / 2) / Alpha.mapsize * 10)]++;

     if(threat2>threat){
     offensive=false;
     } else {
     offensive=true;
     }


     } else {
     offensive=true;
     }


     }
     **/


    public void shoot(int index){
        lasers.add(new Laser(x, y, objects.get(index).x, objects.get(index).y, kind,damage,shotpower, bulletLife));
    }

    public boolean isFacing(int index, double degree){
        double a2 = Math.atan2((double)-(objects.get(index).y-y),objects.get(index).x-x); //correct location angle algorithmdouble a2 = Math.atan2((double)-(objects.get(index).y-y),objects.get(index).x-x); //correct location angle algorithm
        a2=(a2 %= Math.toRadians(360)) >= 0 ? a2 : (a2 + Math.toRadians(360));

        double angledifference = (angle-a2); //determines the diffference between the two angles
        double angledifference2 = (angledifference)+Math.toRadians(360); //backup angle taking into consideration that angles only go from 0-359
        boolean facing = (angledifference>Math.toRadians(180))?(Math.abs(angledifference2)>Math.toRadians(degree)):(Math.abs(angledifference)>Math.toRadians(degree));
        // determines if angle is within 45 degrees of the angle2, does this by determining if the angle difference is greater than 180, if so take the backup angle
        // to determine the actual angle difference.
        return !facing;
    }

    public boolean isFacing2(int index, double degree){
        double a2 = Math.atan2((double)-(lasers.get(index).y-y),lasers.get(index).x-x); //correct location angle algorithmdouble a2 = Math.atan2((double)-(objects.get(index).y-y),objects.get(index).x-x); //correct location angle algorithm
        a2=(a2 %= Math.toRadians(360)) >= 0 ? a2 : (a2 + Math.toRadians(360));

        double angledifference = (angle-a2); //determines the diffference between the two angles
        double angledifference2 = (angledifference)+Math.toRadians(360); //backup angle taking into consideration that angles only go from 0-359
        boolean facing = (angledifference>Math.toRadians(180))?(Math.abs(angledifference2)>Math.toRadians(degree)):(Math.abs(angledifference)>Math.toRadians(degree));
        // determines if angle is within 45 degrees of the angle2, does this by determining if the angle difference is greater than 180, if so take the backup angle
        // to determine the actual angle difference.
        return !facing;
    }

    public double angleBetween(int index){
        double a2 = Math.atan2((double)-(objects.get(index).y-y),objects.get(index).x-x); //correct location angle algorithmdouble a2 = Math.atan2((double)-(objects.get(index).y-y),objects.get(index).x-x); //correct location angle algorithm
        a2=(a2 %= Math.toRadians(360)) >= 0 ? a2 : (a2 + Math.toRadians(360));

        double angledifference = (angle-a2); //determines the diffference between the two angles
        double angledifference2 = (angledifference)+Math.toRadians(360); //backup angle taking into consideration that angles only go from 0-359
        return (angledifference>Math.toRadians(180))?angledifference2:angledifference;
    }



    public void setAngle(double angle){
        this.angle=angle;
        if (this.angle < 0)
            this.angle = Math.toRadians(360) + this.angle;

        this.angle = this.angle % Math.toRadians(360);

    }







}
