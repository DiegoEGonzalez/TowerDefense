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
    double wanderangle=Math.toRadians(.25);
    boolean horde=false;

    int protectindex=0;
    int enemyindex=0;

    boolean directed=false;


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

        if(kind==1&&Alpha.beacon&&User.beaconing&&collisionCircle(getX(),getY(),h/2,User.userx,User.userY,150))
            directed=true;
        else
        directed=false;


        if (left || right || up || down){
            setAngle(angle+Math.toRadians(180));
        }else if(clear()||directed){

            if(findTarget(hate)!=-1||directed) {
                seek(findTarget(hate));
            } else {
                wander();
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

            boolean obstacle = collisionCircle(getX(),getY(),75,(int)(lasers.get(b).x+lasers.get(b).deltax),(int)(lasers.get(b).y+lasers.get(b).deltay),(int)(lasers.get(b).h/2));

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
            if(objects.get(b).kind!=0)
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
            if(objects.get(b).kind!=0)
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
            if(objects.get(b).kind!=0) //if the object is not a meteor, skip
                continue;

            boolean obstacle = collisionCircle((int)(x+vx),(int)(y+vy),h/2,(int)(objects.get(b).x),(int)(objects.get(b).y),(int)(objects.get(b).h/2));

            if(obstacle) {
                return false;
            }
        }
        return true;
    }

    public void seek(int i){
        double a2=0;
        if(!directed) {
            a2 = Math.atan2((double) -(objects.get(i).y - y), (double) (objects.get(i).x - x));
        } else {
            a2 = Math.atan2((double) -(User.userY - y), (double) (User.userx - x));
        }
        a2=(a2 %= Math.toRadians(360)) >= 0 ? a2 : (a2 + Math.toRadians(360));

        double dif = a2-angle;
        if(dif>Math.toRadians(180))
            dif-=Math.toRadians(360);
        else if(dif<Math.toRadians(-180))
            dif+=Math.toRadians(360);

        if((compareAngle(angle,a2))) {
            if(compareAngle(angle+ Math.toRadians(2) * power,a2)==compareAngle(angle,a2))
            setAngle(angle + Math.toRadians(2) * power);
            else
                setAngle(a2);
        }else {
            if(compareAngle(angle- Math.toRadians(2) * power,a2)==compareAngle(angle,a2))
            setAngle(angle - Math.toRadians(2) * power);
            else
            setAngle(a2);
        }

    }

    public void wander(){
        if(Math.random()<.0005)
            wanderangle*=-1;
        setAngle(angle+wanderangle);
    }

    public void action() {
        int index = -1;

        index=tactic(false);

        if((Alpha.gametime-lastAction)/1000000000.0>recharge&&index>-1) {
            shoot(index);
            lastAction=Alpha.gametime;
        }


    }

    public int findClosest(){
        int index=-1;
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this||objects.get(q).kind==kind||(objects.get(q).kind==0))
                continue;
            int searchRadius = 400; //radius of whether the object will be findable
            boolean search = collisionCircle(getX(),getY(),searchRadius,objects.get(q).getX(),objects.get(q).getY(),20); // whether the object is close enough to be considered
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
            if (objects.get(q) == this||objects.get(q).kind==kind||objects.get(q).kind==0)
                continue;
            int searchRadius = 400; //radius of whether the object will be findable
            boolean search = collisionCircle(getX(),getY(),searchRadius,objects.get(q).getX(),objects.get(q).getY(),20); // whether the object is close enough to be considered
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
            if (objects.get(q) == this||objects.get(q).kind==kind||objects.get(q).kind==0)
                continue;
            int searchRadius = 400; //radius of whether the object will be findable
            boolean search = collisionCircle(getX(),getY(),searchRadius,objects.get(q).getX(),objects.get(q).getY(),20); // whether the object is close enough to be considered
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
            if (objects.get(q) == this||objects.get(q).kind==kind||objects.get(q).kind==0)
                continue;
            int searchRadius = 400; //radius of whether the object will be findable
            boolean search = collisionCircle(getX(),getY(),searchRadius,objects.get(q).getX(),objects.get(q).getY(),20); // whether the object is close enough to be considered
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
            if ((objects.get(q).kind!=0))
                continue;
            int searchRadius = h/2+10; //radius of whether the object will be findable
            boolean search = collisionCircle(getX(),getY(),searchRadius,objects.get(q).getX(),objects.get(q).getY(),objects.get(q).h/2); // whether the object is close enough to be considered
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


    public int findTarget(int thing){
        int index = tacticAlgorithm(false,enemyindex);
        if(index==-1){
            index=tactic(false);
        }

        if(index!=-1&&horde) {
            boolean enemynearby = collisionCircle(getX(), getY(), h / 2 + 250, objects.get(index).getX(), objects.get(index).getY(), objects.get(index).h / 2);
            int index2 = tacticAlgorithm(true, protectindex);
            if (index2 == -1)
                index2 = tactic(true);
            if(index2!=-1&&objects.get(index2).isInMap()&&isInMap()) {
                boolean friendnearby = collisionCircle(getX(), getY(), h / 2 + 250, objects.get(index2).getX(), objects.get(index2).getY(), objects.get(index2).h / 2);
                boolean friendReallyClose = collisionCircle(getX(), getY(), h / 2 + 50, objects.get(index2).getX(), objects.get(index2).getY(), objects.get(index2).h / 2);
                if (!enemynearby && friendnearby&&!friendReallyClose)
                    index = index2;
            }
        } else if(horde){
            int index2 = tacticAlgorithm(true, protectindex);
            if (index2 == -1)
                index2 = tactic(true);
            if(index2!=-1&&objects.get(index2).isInMap()&&isInMap()) {
                boolean friendnearby = collisionCircle(getX(), getY(), h / 2 + 250, objects.get(index2).getX(), objects.get(index2).getY(), objects.get(index2).h / 2);
                boolean friendReallyClose = collisionCircle(getX(),getY(), h / 2 + 50, objects.get(index2).getX(), objects.get(index2).getY(), objects.get(index2).h / 2);
                if (friendnearby && !friendReallyClose)
                    index = index2;
            }
        }
        return index;
    }

    public int tactic(boolean defense){
        int index=-1;
        for(int q=0;q<objects.size();q++) {

            if (objects.get(q) == this)
                continue;

            if(!objects.get(q).isInMap()&&isInMap())
                continue;

            if(objects.get(q) instanceof Asteroid&&!(this instanceof EEE))
                continue;

            if(objects.get(q).kind==kind&&!defense)
                continue;
            else if(objects.get(q).kind!=kind&&defense)
                continue;


            boolean searchable = collisionCircle(getX(),getY(),h/2+400,objects.get(q).getX(),objects.get(q).getY(),objects.get(q).h/2);

            if(searchable) {
                int distance1 = (int) Math.sqrt(Math.pow(x - objects.get(q).x, 2) + Math.pow(y - objects.get(q).y, 2));

                if (index == -1)
                    index = q;
                else if (distance1 < ((int) Math.sqrt(Math.pow(x - objects.get(index).x, 2) + Math.pow(y - objects.get(index).y, 2))))
                    index = q;
            }

        }

        return index;
    }

    public int tacticAlgorithm(boolean defense, int value){
        int index=-1;
        for(int q=0;q<objects.size();q++) {

            if (objects.get(q) == this)
                continue;

            if(!objects.get(q).isInMap()&&isInMap())
                continue;

            if(objects.get(q) instanceof Asteroid&&!(this instanceof EEE))
                continue;

            if(objects.get(q).kind==kind&&!defense)
                continue;
            else if(objects.get(q).kind!=kind&&defense)
                continue;


            switch (value){
                case 0:
                    if(!(objects.get(q) instanceof Asteroid))
                        continue;
                    break;
                case 1:
                    if(!(objects.get(q) instanceof BASE))
                        continue;
                    break;
                case 2:
                    if(!(objects.get(q) instanceof TTTSpawner))
                        continue;
                    break;
                case 3:
                    if(!(objects.get(q) instanceof SSSSpawner))
                        continue;
                    break;
                case 4:
                    if(!(objects.get(q) instanceof WWWSpawner))
                        continue;
                    break;
                case 5:
                    if(!(objects.get(q) instanceof EEESpawner))
                        continue;
                    break;
                case 6:
                    if(!(objects.get(q) instanceof TTT))
                        continue;
                    break;
                case 7:
                    if(!(objects.get(q) instanceof SSS))
                        continue;
                    break;
                case 8:
                    if(!(objects.get(q) instanceof WWW))
                        continue;
                    break;
                case 9:
                    if(!(objects.get(q) instanceof EEE))
                        continue;
                    break;
            }

            boolean searchable = collisionCircle(getX(),getY(),h/2+400,objects.get(q).getX(),objects.get(q).getY(),objects.get(q).h/2);

            if(searchable) {
                int distance1 = (int) Math.sqrt(Math.pow(x - objects.get(q).x, 2) + Math.pow(y - objects.get(q).y, 2));
                if (index == -1)
                    index = q;
                else if (distance1 < ((int) Math.sqrt(Math.pow(x - objects.get(index).x, 2) + Math.pow(y - objects.get(index).y, 2))))
                    index = q;
            }
        }
        return index;
    }


    public void shoot(int index){
        lasers.add(new Laser(x, y, objects.get(index).getX(), objects.get(index).getY(), kind,damage,shotpower, bulletLife));
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
