import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener{

    static Shell window;
    static Alpha game;
    static Timer timer;
    static boolean ready=false;
    static GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
    static GraphicsDevice comp = ge.getDefaultScreenDevice();

    public static void main(String[] args) {
        window = new Shell();//Creates a JFrame using GameEngine
        game = new Alpha();//Creates the Game
        window.add(game);//adds the Game to the JFrame
        //*********** JFRAME SPECIFICS **************

        //comp.setFullScreenWindow(window);
        //game.fullscreen();
        window.setVisible(true); //makes the JFrame visible
        Main confusing = new Main();
    }

    public Main(){
        timer=new Timer(35,this);
        timer.setInitialDelay(100);
        timer.start();
    }

    public void actionPerformed(ActionEvent e){
        game.repaint();
        game.update();

    }
}
