import java.awt.*;
import java.util.ArrayList;

/**
 * Created by DiegoGonzalez on 10/23/14.
 */
public class WWW extends Minions {
    boolean left,right,down,up;

    public WWW(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers,Unit parent){
        super(x,y,30,30,kind,100,2,50,objects,lasers,parent);
        alive=true;
        recharge=2;
        priority=2;
        hate=1;
        bulletLife=30;
        shotpower=100;
        if(kind==1)
            offensive=User.strategyWWW;
    }


    public void draw(Graphics2D a){
        //super.draw(g);
        //Graphics2D a = (Graphics2D)g;
        a.translate(x,y);
        a.rotate(-angle);

        if(kind==1)
            a.setColor(Color.BLUE);
        else
            a.setColor(Color.RED);

        a.drawLine(-15, -15, -15, 15);

        a.drawLine(-15, -15, 0, -15);
        a.drawLine(-15, 15, 0, 15);

        a.drawLine(0, -15, 0, -10);
        a.drawLine(0, 15, 0, 10);

        a.drawLine(0, 10, 15, 0);
        a.drawLine(0, -10, 15, 0);

        a.rotate(angle);
        a.translate(-x,-y);
    }
}
