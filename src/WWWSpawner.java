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
        recharge=12;
    }
    public void action(){
        objects.add(new WWW(x,y,kind,objects,lasers));
    }
}


