import java.awt.*;
import java.util.ArrayList;

/**
 * Created by zaxtrap on 10/24/14.
 */

    import java.awt.*;
    import java.util.ArrayList;

    public class WWWSpawner extends Unit{
        public WWWSpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
            super(x,y,50,50,kind,85,objects,lasers);
            recharge=10;
            lastAction=System.nanoTime();
            alive=true;
        }
        public void move(){

        }
        public void action(){
            objects.add(new WWW(x,y,kind,objects,lasers));
        }
        public void draw(Graphics g){
            super.draw(g);
            g.setColor(Color.lightGray);
            g.drawRect(x-25,y-25,50,50);

        }
    }

}
