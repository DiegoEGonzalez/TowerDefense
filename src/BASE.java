import java.awt.*;
import java.util.ArrayList;

public class BASE extends Unit {
    long birthdate=0;

    public BASE(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x, y, 100,100,kind,5000,objects,lasers);
        alive=true;
        birthdate=Alpha.gametime;
    }

    public void draw(Graphics2D g){
        super.draw(g);
        if(Alpha.selected != this||Alpha.selection!=5)
            g.setColor(Color.lightGray);
        else {
            g.setColor(new Color(0,255,255,15));
            g.fillOval(getX() - User.basefield / 2, getY() - User.basefield / 2, User.basefield, User.basefield);
            g.setColor(Color.BLUE);
        }
        g.drawRect(getX()-50,getY()-50,100,100);
        if(kind==1)
            g.setColor(Color.blue);
        else
            g.setColor(Color.RED);

        g.drawRect(getX()-35,getY()-35,70,70);

        g.drawRect(getX()-20,getY()-20,40,40);
        g.drawLine(getX()-w/2,getY()-h/2,getX()+50,getY()+50);
        g.drawLine(getX()+50,getY()-h/2,getX()-w/2,getY()+50);
    }
    public void update(){
        super.update();
        if(!alive){
            for(int x=0;x<objects.size();x++){
                if(objects.get(x).kind==kind){
                    objects.get(x).alive=false;
                }
            }
            Alpha.gameover = true;
        }
    }

}
