import java.awt.*;
import java.util.ArrayList;

public class TTTSpawner extends Unit{
    public TTTSpawner(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,50,50,objects,lasers);
        recharge=6;
        lastAction=System.nanoTime();
        maxhealth=200;
        health=maxhealth;
        this.kind=kind;
        alive=true;
    }
    public void move(){
        if(y+h+10<Shell.DEFAULT_WINDOWSIZEY-50)
            y+=10;
        else if(y+h<Shell.DEFAULT_WINDOWSIZEY-50)
            y=Shell.DEFAULT_WINDOWSIZEY-50-h;

        for(int q=0;q<objects.size();q++){
            if(!(objects.get(q)instanceof TTTSpawner)||objects.get(q)==this)
                continue;

        }
    }
    public void action(){
        objects.add(new TTT(x,y,kind,objects,lasers));
    }
    public void draw(Graphics g){
        g.setColor(Color.lightGray);
        g.drawRect(x,y,50,50);
        g.drawRect(x,y+70,50,10);
        g.fillRect(x,y+70,(int)((50.0/maxhealth)*health),10);
    }
}
