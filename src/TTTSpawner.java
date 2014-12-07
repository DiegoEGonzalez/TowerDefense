import java.awt.*;
import java.util.ArrayList;

public class TTTSpawner extends Spawner{
    public TTTSpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,kind,objects,lasers);
        name = "TTT SPAWNER";
    }
    public void action(){
        if((System.nanoTime()-lastAction)/1000000000.0>recharge&&count<maxcount) {
            objects.add(new TTT(x, y, kind, objects, lasers,this));
            lastAction=System.nanoTime();
            count++;
        }

    }
}
