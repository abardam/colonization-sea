/*
 * SoutheastAsiaApp.java
 */

package southeastasia;

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

    public static final int MAX_PLAYERS=6;
    private SoutheastAsiaView window;
 
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        window=new SoutheastAsiaView(this, new SoutheastAsiaServer());
        show(window);
        
        FakeSockets fs=new FakeSockets(this);
        SoutheastAsiaClient sac1=new SoutheastAsiaClient(fs);
        SoutheastAsiaClient sac2=new SoutheastAsiaClient(fs);
        SoutheastAsiaClient sac3=new SoutheastAsiaClient(fs);
        SoutheastAsiaClient sac4=new SoutheastAsiaClient(fs);
        SoutheastAsiaClient sac5=new SoutheastAsiaClient(fs);
        SoutheastAsiaClient sac6=new SoutheastAsiaClient(fs);

            new SoutheastAsiaClientApp(sac1).setVisible(true);
            new SoutheastAsiaClientApp(sac2).setVisible(true);
            new SoutheastAsiaClientApp(sac3).setVisible(true);
            new SoutheastAsiaClientApp(sac4).setVisible(true);
            new SoutheastAsiaClientApp(sac5).setVisible(true);
            new SoutheastAsiaClientApp(sac6).setVisible(true);

            fs.addClient(sac1);
            fs.addClient(sac2);
            fs.addClient(sac3);
            fs.addClient(sac4);
            fs.addClient(sac5);
            fs.addClient(sac6);


            window.setFakeSockets(fs); //remove this after fakesockets
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
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
     * temporary method for "parsing"
     */
    public void tempParse(String message)
    {
        window.tempAddMessage(message);
    }
}
