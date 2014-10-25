import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;



public class FlightDemo extends JPanel implements MouseListener,MouseWheelListener, MouseMotionListener{
    ArrayList<Unit> objects = new ArrayList<Unit>();
    ArrayList<Laser> lasers = new ArrayList<Laser>();
    static int mapsize=3000;
    static int mapsizey=1800;
    double marginX = 200;
    double scaler =1.0/((double)mapsize/(Shell.DEFAULT_WINDOWSIZEX-200));
    int startx=0;
    int offsetx=0;
    int starty=0;
    int offsety=0;
    int currentx=0;
    int currenty=0;
    int selection =0;




    public FlightDemo() {
        setLayout(null);
        setSize(Shell.DEFAULT_WINDOWSIZEX, Shell.DEFAULT_WINDOWSIZEY);
        setBackground(Color.BLACK);
        setVisible(true);
        setDoubleBuffered(true);
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        objects.add(new BASE(50, mapsizey / 2 - 50, 1, objects, lasers));
        objects.add(new TTTSpawner(2000, 900, 2, objects, lasers));
        objects.add(new TTTSpawner(2000, 800, 2, objects, lasers));
        objects.add(new SSSSpawner(2000, 400, 2, objects, lasers));
        objects.add(new SSSSpawner(2000, 1100, 2, objects, lasers));
        objects.add(new TTTSpawner(2000, 500, 2, objects, lasers));


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
        Graphics2D a = (Graphics2D)g;
        a.translate(200+offsetx+currentx,0+offsety+currenty);
        a.scale(scaler,scaler);


        for(int x=0;x<objects.size();x++){
            objects.get(x).draw(g);
        }
        for(int x=0;x<lasers.size();x++){
            lasers.get(x).draw(g);
        }
        g.setColor(Color.white);
        g.drawRect(0,Shell.DEFAULT_WINDOWSIZEY-50,Shell.DEFAULT_WINDOWSIZEX,Shell.DEFAULT_WINDOWSIZEY);
        g.drawRect(0,0,mapsize,mapsize);

        a.scale(1/scaler,1/scaler);
        a.translate(-200 - offsetx - currentx, 0 - offsety - currenty);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,200,Shell.DEFAULT_WINDOWSIZEY);
        g.setColor(Color.WHITE);
        g.drawRect(0,0,200,Shell.DEFAULT_WINDOWSIZEY);
        g.drawRect(0,0,200,100);
        g.drawString("*** TOWER DEFENSE GAME ***",10,30);
        g.drawString("*** (Suck it Ben!) 0.01v ***",10,60);

        g.drawString("* TTT ",10,200);
        g.drawRect(0,175,200,50);
        if(selection==1)
            g.drawRect(5,180,190,40);
        g.drawString("* SSS ",10,250);
        if(selection==2)
            g.drawRect(5,230,190,40);
        g.drawRect(0,225,200,50);
        g.drawString("* WWW ",10,300);
        g.drawRect(0,275,200,50);
        if(selection==3)
            g.drawRect(5,280,190,40);


    }

    public boolean collision(int x, int y, int a, int b, int w, int h){
        boolean up=(y-b)<h;
        boolean down=(y-b)>0;
        boolean left=(x-a)<w;
        boolean right=(x-a)>0;

        if(left&&right&&up&&down){
            return true;
        }
        return false;
    }


    public void mouseClicked(MouseEvent e) {
        if(selection==0 || (selection>0 && e.getX()<200)) {
            if (collision(e.getX(), e.getY(), 0, 175, 200, 50))
                selection = 1;
            else if (collision(e.getX(), e.getY(), 0, 225, 200, 50))
                selection = 2;
            else if (collision(e.getX(), e.getY(), 0, 275, 200, 50))
                selection = 3;
        } else if (e.getX()>225){
            switch (selection){
                case 1:
                    objects.add(new TTTSpawner((int)((e.getX()-marginX-offsetx-currentx)/scaler),(int)((e.getY()-offsety-currenty)/scaler),1,objects,lasers));
                    selection=0;
                    break;
                case 2:
                    objects.add(new SSSSpawner((int)((e.getX()-marginX-offsetx-currentx)/scaler),(int)((e.getY()-offsety-currenty)/scaler),1,objects,lasers));
                    selection=0;
                    break;
                case 3:
                    objects.add(new WWWSpawner((int)((e.getX()-marginX-offsetx-currentx)/scaler),(int)((e.getY()-offsety-currenty)/scaler),1,objects,lasers));
                    selection=0;
                    break;
            }
        }



        //objects.add(new SSSSpawner(e.getX()-(int)marginX,e.getY(),1,objects,lasers));
    }

    public void mousePressed(MouseEvent e) {

                startx=e.getX();
                starty=e.getY();



    }

    public void mouseReleased(MouseEvent e) {
              //  currentx+=offsetx;
            //    currenty+=offsety;
          //      offsety=0;
        //offsetx=0;
        //startx=0;
        //starty=0;
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int amount = e.getWheelRotation();
        if((((Shell.DEFAULT_WINDOWSIZEX-200)/(scaler-(amount/100.0)))<mapsize))
        scaler -= amount / 100.0;
        System.out.println(mapsize/scaler+"");
    }

    @Override
    public void mouseDragged(MouseEvent e) {


        //// **** IMPORTANT NOTE ***** remember that shifting it to the left is negative and right is positive

        int tempoffsetX=e.getX()-startx;
        if((mapsize+(200-Shell.DEFAULT_WINDOWSIZEX+tempoffsetX+offsetx)/scaler>=0)&&(tempoffsetX+offsetx<0))
        offsetx+=tempoffsetX;

        System.out.println(offsetx+" "+currentx+" "+(e.getX()-startx));
        int tempoffsetY=e.getY()-starty;
        if((mapsize-((Shell.DEFAULT_WINDOWSIZEY+tempoffsetY+offsety)/scaler)>=0)&&(tempoffsetY+offsety<0))
        offsety+=e.getY()-starty;
        System.out.println(mapsize-((Shell.DEFAULT_WINDOWSIZEY+tempoffsetY+offsety)/scaler));

        startx=e.getX();
        starty=e.getY();

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
