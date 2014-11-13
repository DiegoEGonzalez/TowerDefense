import java.awt.*;
import java.util.ArrayList;

public class SSSSpawner extends Spawner{
    public SSSSpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,kind,objects,lasers);
        recharge=8;
    }
    public void action(){
        objects.add(new SSS(x,y,kind,objects,lasers));
    }
}
