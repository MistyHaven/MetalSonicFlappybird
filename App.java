import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
       
        
        int screenWidth = 360;
        int screenHeight = 640;

        JFrame frame = new JFrame("Flappy Bird (with Metal Sonic)");

        frame.setSize(screenWidth,screenHeight);
        frame.setLocationRelativeTo(null); //center location
        frame.setResizable(false); //cannot change default size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //when press x - it exits
        
        MetalSonic metalSonic = new MetalSonic();
        frame.add(metalSonic);
        
        frame.pack(); // ignore the title bar
        metalSonic.requestFocus();//important
        frame.setVisible(true);

    
    }
}

