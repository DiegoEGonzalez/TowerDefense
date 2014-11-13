import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener{

    static Shell window;
    static Alpha game;
    static Timer timer;
    static boolean ready=false;

    public static void main(String[] args) {
        window = new Shell();//Creates a JFrame using GameEngine
        game = new Alpha();//Creates the Game
        window.add(game);//adds the Game to the JFrame
        //*********** JFRAME SPECIFICS **************
        window.setVisible(true); //makes the JFrame visible
        Main confusing = new Main();
        //while (true) {
        //game.repaint();//repaint the Game
        //java.awt.EventQueue.invokeLater(new Runnable() {
        //  public void run() {
        //game.update();
        //}
        //});


        // }
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
