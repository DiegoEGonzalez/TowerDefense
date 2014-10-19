import javax.swing.*;

public class Shell extends JFrame {
    public static int DEFAULT_WINDOWSIZEY=800; //width of the game *** DO NOT CHANGE *** NO! NO! NO! ( I WILL FREAK OUT SO F**CKING BAD! )
    public static int DEFAULT_WINDOWSIZEX=1400; // height of the game *** DO NOT CHANGE or read above ***

    public Shell(){
        //********* JFRAME SPECIFICS ******************
        setTitle("");//adds a title to the top of the JFRAME
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//determines that if the user presses the red x, the program will end.
        setSize(Shell.DEFAULT_WINDOWSIZEX, Shell.DEFAULT_WINDOWSIZEY);//sets the size of the JFRAME
        //setResizable(false); //prevents user from resizing window
        setLocationRelativeTo(null);//centers the JFRAME to the middle of the screen

    }

}
