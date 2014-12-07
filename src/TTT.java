import java.awt.*;
import java.util.ArrayList;

public class TTT extends Minions {
    boolean left,right,down,up;

    /*

    /// VALUES FOR UNITS

     0- Asteroid

     1- Base

     Spawnera

     2- TTTSpawner
     3- SSSSpawner
     4- WWWSpawner
     5- EEESpawner

     Spips

     6-TTT
     7-SSS
     8-WWW
     9-EEE

     */



    public TTT(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers,Unit parent){
        super(x,y,30,30,kind,10,4,5,objects,lasers,parent);
        recharge=1;
        lastAction=System.nanoTime();
        alive=true;
        priority=1;
        hate=3;
        offensive=User.strategyTTT;
        horde = true;
        enemyindex = 7; //used to target SSS to do the most damage
        protectindex = 6;
    }

    public void draw(Graphics2D a){

        a.translate(x,y);
        a.rotate(-angle);

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






}
