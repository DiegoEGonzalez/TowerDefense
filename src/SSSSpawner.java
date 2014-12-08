import java.awt.*;
import java.util.ArrayList;

public class SSSSpawner extends Spawner{
    public SSSSpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,kind,objects,lasers);
        recharge=5;
        name = "SSS SPAWNER";
    }
    public void action(){
        if((Alpha.gametime-lastAction)/1000000000.0>recharge&&count<maxcount) {
            objects.add(new SSS(getX(), getY(), kind, objects, lasers,this));
            lastAction=Alpha.gametime;
            count++;
        }

    }
}
