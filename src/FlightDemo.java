import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;



public class FlightDemo extends JPanel implements MouseListener,MouseWheelListener, MouseMotionListener{
    ArrayList<Unit> objects = new ArrayList<Unit>();
    ArrayList<Laser> lasers = new ArrayList<Laser>();
    static int mapsize=1500;
    double marginX = 200;
    double scaler =1.0/((double)mapsize/(Shell.DEFAULT_WINDOWSIZEX-200));
    int startx=0;
    int offsetx=0;
    int starty=0;
    int offsety=0;
    int currentx=0;
    int currenty=0;
    int selection =0;
    long lastAction=System.nanoTime();
    int level=1;
    boolean start=false;

    User user;




    public FlightDemo() {
        setLayout(null);
        setSize(Shell.DEFAULT_WINDOWSIZEX, Shell.DEFAULT_WINDOWSIZEY);
        setBackground(Color.BLACK);
        setVisible(true);
        setDoubleBuffered(true);
        addMouseListener(this);
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        objects.add(new BASE(50, mapsize / 2 - 50, 1, objects, lasers));
        //objects.add(new BASE(mapsize-50,mapsize/2-50,2,objects,lasers));
        user = new User();

    }

    public void update(){
        if((System.nanoTime()-lastAction)/1000000000.0>15&&start){
            for(int x=0;x<5*level;x++){
                objects.add(new TTT(mapsize-30,(int)(Math.random()*mapsize),2,objects,lasers));
            }
            start=false;
            level++;
        }


        for(int x=0;x<objects.size();x++){
            objects.get(x).update();
        }
        for(int x=0;x<lasers.size();x++){
            lasers.get(x).update();
        }

        int ttt=0;
        int sss=0;
        int www=0;
        int enemy=0;

        for (Iterator<Unit> iterator = objects.iterator(); iterator.hasNext(); ) {


            Unit b = iterator.next();
            if(!b.alive) {
                iterator.remove();
                if(b instanceof TTT){

                    user.moolah+=10;
                } else if(b instanceof SSS){

                    user.moolah+=15;
                } else if(b instanceof WWW){

                    user.moolah+=30;
                }

            }
            if(b.kind==1) {
                if (b instanceof TTT) {
                    ttt++;
                } else if (b instanceof SSS) {
                    sss++;
                } else if (b instanceof WWW) {
                    www++;
                }
            } else if(b.kind==2){
                enemy++;
            }
        }
        user.TTT=ttt;
        user.SSS=sss;
        user.WWW=www;

        for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext(); ) {
            Laser b = iterator.next();
            if(!b.isAlive())
                iterator.remove();
        }

        if(enemy==0&&!start){
            start=true;
            lastAction=System.nanoTime();
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
        g.drawRect(0,0,mapsize,mapsize);

        a.scale(1/scaler,1/scaler);
        a.translate(-200 - offsetx - currentx, 0 - offsety - currenty);

        if(start) {
            if ((20 - (int) ((System.nanoTime() - lastAction) / 1000000000.0)) < 4)
                g.drawString("NEXT WAVE IN " + (20 - (int) ((System.nanoTime() - lastAction) / 1000000000.0)) + " SEC!!!", 800, 40);
            else if ((20 - (int) ((System.nanoTime() - lastAction) / 1000000000.0)) < 6)
                g.drawString("NEXT WAVE IN " + (20 - (int) ((System.nanoTime() - lastAction) / 1000000000.0)) + " SEC!", 800, 40);
            else if ((20 - (int) ((System.nanoTime() - lastAction) / 1000000000.0)) < 11)
                g.drawString("NEXT WAVE IN " + (20 - (int) ((System.nanoTime() - lastAction) / 1000000000.0)) + " SEC", 800, 40);
        }

        g.setColor(Color.BLACK);
        g.fillRect(0,0,200,Shell.DEFAULT_WINDOWSIZEY);
        g.setColor(Color.WHITE);
        g.drawRect(0,0,200,Shell.DEFAULT_WINDOWSIZEY);
        g.drawRect(0,0,200,100);
        g.drawString("*** TOWER DEFENSE GAME ***",10,30);
        g.drawString("*** (Suck it Ben!) 0.05v ***",10,60);

        g.drawString("^^^ : "+user.moolah,10,130);
        g.drawString("wave : " +(level-1),10,150);

        g.setColor(Color.WHITE);
        g.drawRect(0, 175, 200, 50);
        if(user.moolah<50)
            g.setColor(Color.DARK_GRAY);


        g.drawString("  "+(user.TTT>0?user.TTT:" ")+"    TTT",10,200);

        if(selection==1)
            g.drawRect(5, 180, 190, 40);

        g.setColor(Color.WHITE);
        g.drawRect(0,225,200,50);
        if(user.moolah<75)
            g.setColor(Color.DARK_GRAY);


        g.drawString("  "+(user.SSS>0?user.SSS:" ")+"    SSS",10,250);
        if(selection==2)
            g.drawRect(5,230,190,40);


        g.setColor(Color.WHITE);
        g.drawRect(0,275,200,50);
        if(user.moolah<150)
            g.setColor(Color.DARK_GRAY);


        g.drawString("  "+(user.WWW>0?user.WWW:" ")+"    WWW",10,300);

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
            if (collision(e.getX(), e.getY(), 0, 175, 200, 50)&&selection==1)
                selection = 0;
            else if (collision(e.getX(), e.getY(), 0, 225, 200, 50)&&selection==2)
                selection = 0;
            else if (collision(e.getX(), e.getY(), 0, 275, 200, 50)&&selection==3)
                selection = 0;

            if (collision(e.getX(), e.getY(), 0, 175, 200, 50)&&user.moolah>=50)
                selection = 1;
            else if (collision(e.getX(), e.getY(), 0, 225, 200, 50)&&user.moolah>=75)
                selection = 2;
            else if (collision(e.getX(), e.getY(), 0, 275, 200, 50)&&user.moolah>=150)
                selection = 3;
        } else if (e.getX()>225){
            switch (selection){
                case 1:
                    objects.add(new TTTSpawner((int)((e.getX()-marginX-offsetx-currentx)/scaler),(int)((e.getY()-offsety-currenty)/scaler),1,objects,lasers));
                    //user.TTT++;
                    user.moolah-=50;
                    selection=0;
                    break;
                case 2:
                    objects.add(new SSSSpawner((int)((e.getX()-marginX-offsetx-currentx)/scaler),(int)((e.getY()-offsety-currenty)/scaler),1,objects,lasers));
                    //user.SSS++;
                    user.moolah-=75;
                    selection=0;
                    break;
                case 3:
                    objects.add(new WWWSpawner((int)((e.getX()-marginX-offsetx-currentx)/scaler),(int)((e.getY()-offsety-currenty)/scaler),1,objects,lasers));
                    //user.WWW++;
                    user.moolah-=150;
                    selection=0;
                    break;
            }
        }

    }

    public void mousePressed(MouseEvent e) {
                if(selection==0) {
                    startx = e.getX();
                    starty = e.getY();
                }



    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int amount = e.getWheelRotation();
        if((((Shell.DEFAULT_WINDOWSIZEX-200)/(scaler-(amount/100.0)))<mapsize)) {
            scaler -= amount / 100.0;

        }
        System.out.println(mapsize/scaler+"");
    }

    @Override
    public void mouseDragged(MouseEvent e) {


        //// **** IMPORTANT NOTE ***** remember that shifting it to the left is negative and right is positive
        if(selection==0) {
            int tempoffsetX = e.getX() - startx;
            if ((mapsize + (200 - Shell.DEFAULT_WINDOWSIZEX + tempoffsetX + offsetx) / scaler >= 0) && (tempoffsetX + offsetx < 0))
                offsetx += tempoffsetX;

            System.out.println(offsetx + " " + currentx + " " + (e.getX() - startx));
            int tempoffsetY = e.getY() - starty;
            if ((mapsize - ((Shell.DEFAULT_WINDOWSIZEY + tempoffsetY + offsety) / scaler) >= 0) && (tempoffsetY + offsety < 0))
                offsety += e.getY() - starty;
            System.out.println(mapsize - ((Shell.DEFAULT_WINDOWSIZEY + tempoffsetY + offsety) / scaler));

            startx = e.getX();
            starty = e.getY();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
