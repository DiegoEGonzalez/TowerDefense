import java.awt.*;
import java.util.ArrayList;

public class SSSSpawner extends Spawner{
    public SSSSpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,kind,objects,lasers);
        recharge=5;
    }
    public void action(){
        if((System.nanoTime()-lastAction)/1000000000.0>recharge&&count<maxcount) {
            objects.add(new SSS(x, y, kind, objects, lasers,this));
            lastAction=System.nanoTime();
            count++;
        }

    }
}
