import java.awt.*;
import java.util.ArrayList;

public class BASE extends Unit {

    public BASE(int x, int y, int kind, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x, y, 100,100,kind,5000,objects,lasers);
        alive=true;
    }

    public void draw(Graphics2D g){
        super.draw(g);
        g.setColor(Color.WHITE);
        g.drawRect(x-50,y-50,100,100);
        if(kind==1)
            g.setColor(Color.blue);
        else
            g.setColor(Color.RED);

        g.drawRect(x-35,y-35,70,70);

        g.drawRect(x-20,y-20,40,40);
        g.drawLine(x-w/2,y-h/2,x+50,y+50);
        g.drawLine(x+50,y-h/2,x-w/2,y+50);
    }
    public void update(){
        super.update();
        if(!alive){
            for(int x=0;x<objects.size();x++){
                if(objects.get(x).kind==kind){
                    objects.get(x).alive=false;
                }
            }
        }
    }

}
