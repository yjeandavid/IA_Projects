package main.java.net.kolipass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import net.kolipass.Log;
import net.kolipass.MasterPanel;

public class WumplusMain extends JFrame implements WindowListener, WindowFocusListener, ComponentListener {
    static private Log log = new Log();
    private MasterPanel screenP;
    private JPanel bgFraming;

    private int screenX;
    private int screenY;

    private GraphicsDevice grDev;
    private DisplayMode oldDisplay;

    /**
     * Constructor
     * Preconditions: fullscreen is a boolean value that sets Full-screen mode on or off.
     * Postconditions: The game is started in its initial state and it loads all necessary starting data.
     * If fullscreen is true, the game will begin running in Full-Screen. Else it will run in windowed mode.
     */

    public WumplusMain(boolean fullscreen) {
        super("Adventure Kitteh!!! Wumplus World!!!");
        screenX = 680;
        screenY = 576;
        this.setSize(800, 700);

        // set up resolution change mode

        this.addWindowListener(this);
        this.addWindowFocusListener(this);
        this.addComponentListener(this);

        grDev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(); // obtains your graphics device
        oldDisplay = grDev.getDisplayMode(); // retain original DisplayMode to revert back to when program finishes.

        // setup the game for full-screen if requested.
        if (fullscreen) {
            log.d("Trying to start program in Fullscreen mode.");

            if (grDev.isFullScreenSupported()) // makes sure fullscreen is supported before doing anything.
            {
                log.d("FullScreen is supported");
                this.setUndecorated(true);
                DisplayMode resChangeMode = new DisplayMode(800, 600, 32, DisplayMode.REFRESH_RATE_UNKNOWN); // create new DisplayMode with different resolution.

                try {
                    grDev.setFullScreenWindow(this); // set fullscreen mode on. Otherwise this won't work
                    grDev.setDisplayMode(resChangeMode); // change DisplayMode to our new resolution.
                    log.d("Change resolution: Success!");
                } catch (Exception e) {
                    log.d("Change resolution: FAIL! " + e);
                    fullscreen = false;
                }
            }

            this.setExtendedState(MAXIMIZED_BOTH);
        }

        // instantiate main window panel

        screenP = new MasterPanel(this);

        bgFraming = new JPanel();

        // set background panel properties

        bgFraming.setBackground(Color.black);
        this.add(bgFraming);

        bgFraming.setLayout(null);

        // set screen panel properties

        bgFraming.add(screenP);
        screenP.setSize(screenX, screenY);

        // finishing touches on Game window

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setAlwaysOnTop(false);

        this.recenterScreen();
        try {
            screenP.requestFocusInWindow();
        } catch (Exception e) {
        }

        log.d("Game Window successfully created!!!");
    }

    /**
     * recenterScreen()
     * Preconditions: none
     * Postconditions: The game's main JPanel is centered within its window.
     */

    public void recenterScreen() {
        Dimension size = this.getSize();
        int windowWidth = size.width;
        int windowHeight = size.height;

        screenP.setLocation((windowWidth - screenX) / 2, (windowHeight - screenY) / 2);
    }


    // WindowListener interface stuff

    public void windowActivated(WindowEvent e) {
        log.d("Window Activated");
    }

    public void windowClosed(WindowEvent e) // resets to old display resolution after program closes.
    {
        log.d("Adventure Kitteh program terminated. Restoring original display settings.");
        grDev.setDisplayMode(oldDisplay);
    }

    public void windowClosing(WindowEvent e) {
        log.d("Window closing");
    }

    public void windowDeactivated(WindowEvent e) {
        log.d("Window deactivated");
    }

    public void windowDeiconified(WindowEvent e) {
        log.d("Window Deiconified");
        try {
            this.recenterScreen();
            screenP.requestFocusInWindow();
        } catch (Exception ex) {
        }
    }

    public void windowIconified(WindowEvent e) {
        log.d("Window Iconified");
    }

    public void windowOpened(WindowEvent e) {
        log.d("Window opened");
    }

    public void windowGainedFocus(WindowEvent e) {
        log.d("Window gained focus");
        try {
            this.recenterScreen();
            screenP.requestFocusInWindow();
        } catch (Exception ex) {
        }
    }

    public void windowLostFocus(WindowEvent e) {
        log.d("Window lost focus");
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        this.recenterScreen();
    }

    public void componentShown(ComponentEvent e) {
    }


    public static void main(String[] args) {
        WumplusMain gui;

        for (String s : args)
            log.d(s);
        if (args.length == 0)
            gui = new WumplusMain(false);
        else if (args[0].equals("fullscreen"))
            gui = new WumplusMain(true);

    }

}