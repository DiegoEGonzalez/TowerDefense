import java.awt.*;
import java.util.ArrayList;

/**
 * Created by zaxtrap on 10/24/14.
 */

import java.awt.*;
import java.util.ArrayList;

public class WWWSpawner extends Spawner{
    public WWWSpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,kind,objects,lasers);
        recharge=15;
        maxcount=3;
        name = "WWW SPAWNER";
    }
    public void action(){

        if((Alpha.gametime-lastAction)/1000000000.0>recharge&&count<maxcount) {
            objects.add(new WWW(getX(),getY(),kind,objects,lasers,this));
            lastAction=Alpha.gametime;
            count++;
        }

    }
}


