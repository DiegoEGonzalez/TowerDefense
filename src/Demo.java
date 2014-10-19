import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Demo extends JPanel implements MouseListener{

    double angle = 0;

    double x=400;
    double y=400;
    double vx=0; //velocity x
    double vy=-20; //velocity y
    double ax=0; //acceleration x-axis
    double ay=0; //acceleration y-axis
    final double G=.5; // gravitational constant (little g)

    public Demo(){
        setLayout(null);
        setSize(Shell.DEFAULT_WINDOWSIZEX, Shell.DEFAULT_WINDOWSIZEY);
        setBackground(Color.BLACK);
        addMouseListener(this);
        setVisible(true);

        activateKeyboard();
    }

    public void update(){
        angle+=.05;

        vx+=ax;
        vy+=ay+G;
        x+=vx;
        if(y+vy<400)
        y+=vy;
        else if(vy<G)
            vy=0;
        else
            vy*=-.75;

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.white);
        g.drawString("Tower Defense *** DEVELOPMENT VERSION ***",100,50);
        Graphics2D a = (Graphics2D) g;

        //rotational test *WORKS! :) (try extending graphics to add this method.)
        a.translate(200,200);
        a.rotate(angle);
        a.fillRect(-20,-5,40,10);
        a.rotate(-angle);
        a.translate(-200,-200);

        //projectile test
        g.fillOval((int)Math.round(x),(int)Math.round(y),10,10);
        //g.drawLine(0,405,500,405);

        //parabolic curve test
        for(int z=0;z<30;z++)
            g.drawLine((z*10)+200,-(z*z)+400,((z+1)*10)+200,-((z+1)*(z+1))+400);

        //circle through curve test
        //top

        int r=60;
        //java way to draw a slope. (x,y,h,w,initial angle, slope angle)
        g.drawArc(200,300,r,r,0,180);
        //mathematical way to calculate slope,
        for(int q=-r;q<r;q++)
            g.drawLine(q+100,(int)Math.round(f(q,r))+300,(q+1)+100,(int)Math.round(f(q+1,r))+300);
    }

    public double f(double a, double r){
        return  Math.sqrt(r*r-a*a);
    }

    public Font getFont(String name, int size){
        Font font = null;
        try{
            InputStream is = getClass().getResourceAsStream(name);
            font = Font.createFont(Font.TRUETYPE_FONT,is);
            font = font.deriveFont(Font.PLAIN,size);
        } catch (FontFormatException e){
            System.out.println("Font format exception");
        } catch (IOException e){
            System.out.println("IO Exception from font");
        }
        return font;
    }

    public int center(String a, Graphics g){
        Graphics2D text = (Graphics2D)g;
        FontMetrics fm=g.getFontMetrics();
        Rectangle2D rect=fm.getStringBounds(a,text);
        return (int)((Shell.DEFAULT_WINDOWSIZEX- rect.getWidth())/2);
    }

    public int after(String a, Graphics g,Font b,Font c){
        Graphics2D text = (Graphics2D)g;
        text.setFont(b);
        FontMetrics fm=g.getFontMetrics();
        Rectangle2D rect=fm.getStringBounds(a,text);
        text.setFont(c);
        return (int)((Shell.DEFAULT_WINDOWSIZEX- rect.getWidth())/2+rect.getWidth());
    }

    public void activateKeyboard(){
        Action enter = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

            }
        };
        getInputMap().put(KeyStroke.getKeyStroke("ENTER"),
                "enter");
        getActionMap().put("enter",
                enter);
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

    //////////////////////////////////////         ||
    //                                  //         ||
    //          DOES NOTHING!!          //       \ || /
    //                                  //        \  /
    //////////////////////////////////////         \/

    public void mouseEntered(MouseEvent e){

    }
    public void mouseExited(MouseEvent e){

    }
    public void mouseClicked(MouseEvent e){

    }
    public void mouseReleased(MouseEvent e){

    }

    ////////////////////////////////////////////////   _    _
    //                                            //   O    O
    //   the one that actually does something...  //   ______
    //                                            //
    ////////////////////////////////////////////////

    public void mousePressed(MouseEvent e){

    }


}
