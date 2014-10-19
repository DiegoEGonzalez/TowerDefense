import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

public class FlightDemo extends JPanel implements MouseListener{
    ArrayList<Plane> stuff = new ArrayList<Plane>();
    ArrayList<Spawner> lifes = new ArrayList<Spawner>();
    ArrayList<Laser> lasers = new ArrayList<Laser>();


    public FlightDemo() {
        setLayout(null);
        setSize(Shell.DEFAULT_WINDOWSIZEX, Shell.DEFAULT_WINDOWSIZEY);
        setBackground(Color.BLACK);
        setVisible(true);
        setDoubleBuffered(true);
        addMouseListener(this);
        stuff.add(new Plane(100,100,10,0,2,lasers));
        lifes.add(new Spawner(400,400,3,stuff,lasers,2));

    }
    public void update(){
        for(int x=0;x<lifes.size();x++){
            lifes.get(x).update();
        }

        for(int x=0;x<stuff.size();x++){
            stuff.get(x).ai(stuff);
            stuff.get(x).update();

        }

        for (Iterator<Plane> iterator = stuff.iterator(); iterator.hasNext(); ) {
            Plane b = iterator.next();
            if(!b.alive)
                iterator.remove();
        }
        for (Iterator<Spawner> iterator = lifes.iterator(); iterator.hasNext(); ) {
            Spawner b = iterator.next();
            if(!b.alive)
                iterator.remove();
        }



    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int x=0;x<lifes.size();x++){
            lifes.get(x).draw(g);
        }
        for(int x=0;x<stuff.size();x++){
            stuff.get(x).draw(g);
        }
        for(int x=0;x<lasers.size();x++){
            lasers.get(x).draw(g);
        }
        g.setColor(Color.white);
        g.drawRect(0,Shell.DEFAULT_WINDOWSIZEY-50,Shell.DEFAULT_WINDOWSIZEX,Shell.DEFAULT_WINDOWSIZEY);
        g.drawString("*** TOWER DEFENSE GAME ( SUCK IT BEN KEENAN ) DEVELOPMENT VERSION 0.1 - Diego Gonzalez ***",10,30);
    }


    public void mouseClicked(MouseEvent e) {
        lifes.add(new Spawner(e.getX(),e.getY(),5,stuff,lasers,1));
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
