import java.awt.*;
import java.util.ArrayList;

/**
 * Created by zaxtrap on 10/24/14.
 */

import java.awt.*;
import java.util.ArrayList;

public class EEESpawner extends Spawner{
    public EEESpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,kind,objects,lasers);
        recharge=25;
        maxcount=2;
        name = "EEE SPAWNER";
    }
    public void action(){

        if((Alpha.gametime-lastAction)/1000000000.0>recharge&&count<maxcount) {
            objects.add(new EEE(getX(),getY(),kind,objects,lasers,this));
            lastAction=Alpha.gametime;
            count++;
        }

    }
}


