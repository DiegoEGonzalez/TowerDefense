import java.awt.*;
import java.util.ArrayList;

public class TTT extends Minions {
    boolean left,right,down,up;


    public TTT(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,30,30,kind,30,4,7,objects,lasers);
        recharge=1;
        lastAction=System.nanoTime();
        alive=true;
        priority=1;
        hate=3;
        offensive=true;
    }

    public void draw(Graphics g){
        //super.draw(g);
        Graphics2D a = (Graphics2D)g;
        a.translate(x,y);
        a.rotate(-angle);

        if(kind==1)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.RED);

        g.drawLine(-10,-10,-10,10);
        g.drawLine(-10,-10,10,0);
        g.drawLine(-10,10,10,0);

        a.rotate(angle);
        a.translate(-x,-y);

        /**
        double a2 = Math.atan2((double)-(objects.get(1).y-y),(double)(objects.get(1).x-x));
        a2=(a2 %= Math.toRadians(360)) >= 0 ? a2 : (a2 + Math.toRadians(360));
        a.translate(x,y);
        a.rotate(-a2);
        g.drawLine(0,0,100,0);
        a.rotate(a2);
        a.translate(-x,-y);
         **/

    }
}
