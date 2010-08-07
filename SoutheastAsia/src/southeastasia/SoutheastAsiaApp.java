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
        new SoutheastAsiaClientApp(fs).setVisible(true);
        new SoutheastAsiaClientApp(fs).setVisible(true);
        new SoutheastAsiaClientApp(fs).setVisible(true);
        new SoutheastAsiaClientApp(fs).setVisible(true);
        new SoutheastAsiaClientApp(fs).setVisible(true);
        new SoutheastAsiaClientApp(fs).setVisible(true);
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
        //SoutheastAsiaClientApp.main(null);
        
    }

    /**
     * temporary method for "parsing"
     */
    public void tempParse(String message)
    {
        window.tempAddMessage(message);
    }
}
