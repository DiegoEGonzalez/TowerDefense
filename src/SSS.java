import java.awt.*;
import java.util.ArrayList;

public class SSS extends Minions {

    public SSS(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers,Unit parent){
        super(x,y,30,30,kind,40,8,2,objects,lasers,parent);
        recharge=.3;
        lastAction=Alpha.gametime;
        this.kind=kind;
        alive=true;
        priority=4;
        offensive=true;
        shotpower=20;
        bulletLife=10;
        hate=3;

        // used to target WWW to do the most damage
        enemyindex = 8;
    }

    public void move2(){
        avoid();
    }

    public void draw(Graphics2D a){
        a.translate(x,y);
        a.rotate(-angle);

        if(kind==1)
            a.setColor(Color.BLUE);
        else
            a.setColor(Color.RED);

        a.drawLine(-10,-10,-10,-4);
        a.drawLine(-10,10,-10,4);

        a.drawLine(-10,-10,10,0);
        a.drawLine(-10,10,10,0);

        a.drawLine(-10,-4,0,0);
        a.drawLine(-10,4,0,0);

        a.rotate(angle);
        a.translate(-x,-y);


    }
}
