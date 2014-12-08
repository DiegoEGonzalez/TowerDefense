import java.awt.*;
import java.util.ArrayList;

public class TTTSpawner extends Spawner{
    public TTTSpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,kind,objects,lasers);
        name = "TTT SPAWNER";
    }
    public void action(){
        if((Alpha.gametime-lastAction)/1000000000.0>recharge&&count<maxcount) {
            objects.add(new TTT(getX(), getY(), kind, objects, lasers,this));
            lastAction=Alpha.gametime;
            count++;
        }

    }
}
