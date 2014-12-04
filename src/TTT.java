import java.awt.*;
import java.util.ArrayList;

public class TTT extends Minions {
    boolean left,right,down,up;


    public TTT(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers,Unit parent){
        super(x,y,30,30,kind,10,4,5,objects,lasers,parent);
        recharge=1;
        lastAction=System.nanoTime();
        alive=true;
        priority=1;
        hate=3;
        offensive=User.strategyTTT;
    }

    public void draw(Graphics2D a){
        //super.draw(g);
       // Graphics2D a = (Graphics2D)g;

        a.translate(x,y);     //i haven't added anything, check bugs
        a.rotate(-angle);

        //a.setColor(new Color(255,255,255,50));
        //g.drawOval(-w/2,-h/2,w,h);
        //g.drawOval(-w/2-200,-h/2-200,w+400,h+400);
        //g.drawOval(-w/2-20,-h/2-20,w+40,h+40);
        //g.drawOval(-w/2-40,-h/2-40,w+80,h+80);
        //g.drawOval(-w/2-60,-h/2-60,w+160,h+160);

        if(kind==1)
            a.setColor(Color.BLUE);
        else
            a.setColor(Color.RED);

        a.drawLine(-10,-10,-10,10);
        a.drawLine(-10,-10,10,0);
        a.drawLine(-10,10,10,0);



        a.rotate(angle);
        a.translate(-x,-y);


    }

    public int findTarget(int a){
        offensive=User.strategyTTT;
        return super.findTarget(a);
    }
}
