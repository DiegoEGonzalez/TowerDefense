import java.awt.*;
import java.util.ArrayList;

/**
 * Created by DiegoGonzalez on 10/27/14.
 */
public class NN extends Unit {
    public NN(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers) {
        super(x, y, 20, 20, kind, 250, objects, lasers);
        alive=true;
    }
    public void draw(Graphics g){
        g.setColor(Color.lightGray);
        g.drawRect(x-10,y-10,20,20);
        g.setColor(Color.CYAN);
        for(int q=0;q<objects.size();q++) {
            if (objects.get(q) == this)
                continue;
                    if((objects.get(q)instanceof NN)) {
                        if(collisionCircle(x,y,150,objects.get(q).x,objects.get(q).y,10)>0) {
                            g.drawLine(x,y,objects.get(q).x,objects.get(q).y);
                        }
                    }

            }


    }
}
