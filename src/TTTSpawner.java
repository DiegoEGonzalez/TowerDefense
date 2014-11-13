import java.awt.*;
import java.util.ArrayList;

public class TTTSpawner extends Spawner{
    public TTTSpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,kind,objects,lasers);
    }
    public void action(){
        objects.add(new TTT(x, y, kind, objects, lasers));
    }
}
