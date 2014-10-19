
public class Main {

    static Shell window;

    public static void main(String[] args) {
        window = new Shell();//Creates a JFrame using GameEngine
        final FlightDemo game = new FlightDemo();//Creates the Game
        window.add(game);//adds the Game to the JFrame
        //*********** JFRAME SPECIFICS **************
        window.setVisible(true); //makes the JFrame visible

        Runnable r1 = new Runnable() {
            public void run() { //Sets an operation for a thread to do, currently used for the actual game application
                try {
                    while (true) {
                        game.repaint();//repaint the Game
                        java.awt.EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                game.update();
                            }
                        });

                        //game.update();//update the Game
                        Thread.sleep(35); //sleeps the thread, makes it wait, 35 milliseconds
                    }
                } catch (InterruptedException iex) {}
            }
        };
        Thread thr1 = new Thread(r1); //Creates a thread with runnable r1
        thr1.start(); //starts the first thread


    }
}
