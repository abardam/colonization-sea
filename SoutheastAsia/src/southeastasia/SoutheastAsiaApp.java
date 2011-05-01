/*
 * SoutheastAsiaApp.java
 */
package southeastasia;

import javax.swing.JOptionPane;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 *
 *
 * HEY Y'ALL THIS IS THE SERVER
 * 
 */
public class SoutheastAsiaApp extends SingleFrameApplication {

    public static final int MAX_PLAYERS = 6;
    private SoutheastAsiaView window;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        

        //FakeSockets fs=new FakeSockets(this);
        Object[] options={"Student", "Teacher"};//, "Debug"};
        int optionPicked=JOptionPane.showOptionDialog(null, "You are what kind of player?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);


        if(optionPicked==JOptionPane.NO_OPTION)
        {
            window = new SoutheastAsiaView(this);
            show(window);
        }
        else if(optionPicked==JOptionPane.YES_OPTION)
        {
            String ip=JOptionPane.showInputDialog("Enter IP Address", "0");
            String name = JOptionPane.showInputDialog("Welcome, player! \n\nPlease enter your name.", "");

            SoutheastAsiaClientApp sac;

            for (int i = 0; i < 1; i++) {
                sac = new SoutheastAsiaClientApp(ip, name);
                sac.setVisible(true);
                //sac.setFakeSocket(fs); //change this
                //fs.addClient(sac);
            }
        }
        else if(optionPicked==JOptionPane.CANCEL_OPTION)
        {/*
            SoutheastAsiaClientApp sac=new SoutheastAsiaClientApp("0");
            sac.setVisible(true);
            sac.startGameScreen();
            sac.setOutfile("map.txt");*/
        }
        else{
        }

        //window.setFakeSockets(fs); //remove this after fakesockets
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of SoutheastAsiaApp
     */
    public static SoutheastAsiaApp getApplication() {
        return Application.getInstance(SoutheastAsiaApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(SoutheastAsiaApp.class, args);

    }

    /**
     * Previously known as tempParse() >:)
     * Refer to Interpreter class for details.
     */
    public void receive(int source, String message) {
        southeastasia.server.Interpreter.interpret(window, source, message);
    }

    public void sendBigUpdate(int oldNum) {
        window.bigPlayerUpdate();
    }
}
