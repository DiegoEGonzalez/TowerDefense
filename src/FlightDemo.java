import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

public class FlightDemo extends JPanel implements MouseListener{
    ArrayList<Unit> objects = new ArrayList<Unit>();
    ArrayList<Laser> lasers = new ArrayList<Laser>();


    public FlightDemo() {
        setLayout(null);
        setSize(Shell.DEFAULT_WINDOWSIZEX, Shell.DEFAULT_WINDOWSIZEY);
        setBackground(Color.BLACK);
        setVisible(true);
        setDoubleBuffered(true);
        addMouseListener(this);
        objects.add(new TTTSpawner(400, 400, 2, objects, lasers));

    }
    public void update(){
        for(int x=0;x<objects.size();x++){
            objects.get(x).update();
        }
        for(int x=0;x<lasers.size();x++){
            lasers.get(x).update();
        }

        for (Iterator<Unit> iterator = objects.iterator(); iterator.hasNext(); ) {
            Unit b = iterator.next();
            if(!b.alive)
                iterator.remove();
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int x=0;x<objects.size();x++){
            objects.get(x).draw(g);
        }
        for(int x=0;x<lasers.size();x++){
            lasers.get(x).draw(g);
        }
        g.setColor(Color.white);
        g.drawRect(0,Shell.DEFAULT_WINDOWSIZEY-50,Shell.DEFAULT_WINDOWSIZEX,Shell.DEFAULT_WINDOWSIZEY);
        g.drawString("*** TOWER DEFENSE GAME ( SUCK IT BEN KEENAN ) DEVELOPMENT VERSION 0.1 - Diego Gonzalez ***",10,30);
    }


    public void mouseClicked(MouseEvent e) {
        objects.add(new TTTSpawner(e.getX(),e.getY(),1,objects,lasers));
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
