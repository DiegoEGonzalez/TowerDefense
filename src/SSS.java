import java.awt.*;
import java.util.ArrayList;

public class SSS extends Minions {



    public SSS(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,30,30,kind,40,8,5,objects,lasers);
        recharge=.3;
        lastAction=System.nanoTime();
        this.kind=kind;
        alive=true;
        priority=4;
        offensive=true;
        shotpower=15;
    }

    public void move2(){
        avoid();
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

        g.drawLine(-10,-10,-10,-4);
        g.drawLine(-10,10,-10,4);

        g.drawLine(-10,-10,10,0);
        g.drawLine(-10,10,10,0);

        g.drawLine(-10,-4,0,0);
        g.drawLine(-10,4,0,0);

        a.rotate(angle);
        a.translate(-x,-y);


    }
}
