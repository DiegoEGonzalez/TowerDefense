import java.util.*;
import java.awt.*;
public class Asteroid extends Unit{
    public Asteroid(int x, int y, int w, int h, ArrayList<Unit> objects, ArrayList<Laser> lasers){
        super(x,y,w,h,3,500,objects,lasers);
        alive=true;
    }
    public void draw(Graphics g){
        g.setColor(new Color(51,25,0));
        g.fillOval(x-(w/2),y-(h/2),w,h);
        g.setColor(new Color(153,76,0));
        g.drawOval(x-(w/2),y-(h/2),w,h);
    }
}