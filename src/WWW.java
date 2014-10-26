import java.awt.*;
import java.util.ArrayList;

/**
 * Created by DiegoGonzalez on 10/23/14.
 */
public class WWW extends Minions {
    boolean left,right,down,up;

    public WWW(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,30,30,kind,150,2,50,objects,lasers);
        alive=true;
        recharge=.75;
        priority=1;
        hate=1;
    }


    public void draw(Graphics g){
        super.draw(g);
        Graphics2D a = (Graphics2D)g;
        a.translate(x,y);
        a.rotate(-angle);

        if(kind==1)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.RED);

        g.drawLine(-15,-15,-15,15);

        g.drawLine(-15,-15,0,-15);
        g.drawLine(-15,15,0,15);

        g.drawLine(0,-15,0,-10);
        g.drawLine(0,15,0,10);

        g.drawLine(0,10,15,0);
        g.drawLine(0,-10,15,0);

        a.rotate(angle);
        a.translate(-x,-y);
    }
}
