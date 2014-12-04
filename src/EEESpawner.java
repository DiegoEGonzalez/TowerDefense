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
    }
    public void action(){

        if((System.nanoTime()-lastAction)/1000000000.0>recharge&&count<maxcount) {
            objects.add(new EEE(x,y,kind,objects,lasers,this));
            lastAction=System.nanoTime();
            count++;
        }

    }
}


