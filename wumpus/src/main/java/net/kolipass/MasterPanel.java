package main.java.net.kolipass;

import net.kolipass.gameEngine.ImageLoader;
import net.kolipass.gameEngine.Keyboard;
import net.kolipass.gameEngine.Level;
import net.kolipass.gameEngine.MidiPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MasterPanel extends JPanel {
    public static final boolean FPS = false;
    // essential members
    public Frame parentFrame;
    //	public GUIManager guiManager;
    private Log log = new Log();
    private Timer timer;
    public double prevFPS;
    private Keyboard keyboard;
    private ConfigWumpus config;
    private MidiPlayer midiPlayer;

    public char gameMode; // A ai H human
    public String loadFile;
    private Level currentLevel;
    private boolean changingLevel;
    public ImageLoader imageLoader;

    // non-essential/temporary members

    boolean timerReady;

    public MasterPanel(JFrame parent)//GUIManager parent)
    {
        super(true);
        parentFrame = parent;
        //parentFrame = parent.frame;

        startGame();

    }

    private void init() {

        this.setBackground(Color.black);

        this.setFocusable(true);

        // set up keyboard input

        keyboard = new Keyboard();
        this.addKeyListener(keyboard);

        // set up config file

        config = new ConfigWumpus().load();

        // load images/sounds/etc.
        Toolkit.getDefaultToolkit().sync();
        imageLoader = new ImageLoader(this);
        midiPlayer = new MidiPlayer(config);

        // set up the timer

        TimerListener timerListener = new TimerListener();

        prevFPS = 0;
        timerReady = true;
        timer = new Timer(0, timerListener);
        this.setFPS(60);

    }

    /**
     * startGame()
     * Initializes any global game variables and loads the first level.
     */

    public void startGame() {
        init();
        changeCurrentLevel("Menu");
    }

    /**
     * changeCurrentLevel(String levelName)
     * Unloads current level and loads a new level.
     * Preconditions: levelName is the key String for the level we are loading.
     * Postconditions: The current level is unloaded. If levelName is a valid key String,
     * then its level is loaded. Otherwise the game crashes spectacularly.
     */

    public void changeCurrentLevel(String levelName) {
        timer.stop();

        //imageLoader.reset();

        if (currentLevel != null)
            currentLevel.clean();

        changingLevel = true;

        if (levelName.equals("Menu"))
            currentLevel = new MenuLevel(this);
        else if (levelName.equals("MainLevel"))
            currentLevel = new MainLevel(this);
        else if (levelName.equals("HowToPlay"))
            currentLevel = new HowToPlayLevel(this);
        else {
            log.d("Change Level Failed. Bad level name.");
            return;
        }

        currentLevel.loadData();

        timer.start();
    }

    /**
     * endGame()
     * exits the game and closes its window.
     */

    public void endGame() {
        timer.stop();
        changingLevel = true;

        if (currentLevel != null)
            currentLevel.clean();

        //	parentFrame.setVisible(false);
        //	parentFrame.dispose();
        System.exit(0);
    }


    /**
     * setFPS()
     * Preconditions: fps is a quantity of frames per second
     * Postconditions: Sets the timer's refresh rate so that it fires fps times per second.
     */

    public void setFPS(int fps) {
        int mspf = (int) (1000.0 / fps + 0.5);
        timer.setDelay(mspf);
    }


    /**
     * getKeyboard()
     * Allows easy access for the game's levels to access the global keyboard.
     */

    public Keyboard getKeyboard() {
        return keyboard;
    }

    /**
     * getConfig()
     * Allows easy access for the game's levels to access the global configurations.
     */

    public ConfigWumpus getConfig() {
        return config;
    }

    /**
     * getMidiPlayer()
     * Allows easy access for the game's levels to access the global midi player.
     */

    public MidiPlayer getMidiPlayer() {
        return midiPlayer;
    }


    // Event listener for the timer objects

    private class TimerListener implements ActionListener {
        long startTime = System.currentTimeMillis();
        long lastTime = this.startTime;
        int ticks = 0;

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == timer) {
                // perform a loop through the game's logic
                synchronized (this) {
                    if (timerReady) {
                        timerReady = false;
                        if (midiPlayer == null)
                            log.d("MidiPlayer is null");
                        currentLevel.timerLoop();//timerLoop();
                        repaint();
                        timerReady = true;
                    }

                }

                // Logic for Frames per Second counter

                this.ticks++;

                long currentTime = System.currentTimeMillis();

                if (currentTime - startTime >= 500) {
                    prevFPS = 1000.0 * ticks / (1.0 * currentTime - startTime);
                    if (FPS) {
                        log.d(String.valueOf(prevFPS));
                        //todo
                    }
                    startTime = currentTime;
                    ticks = 0;
                }

                lastTime = currentTime;
            }
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setFont(new Font("Arial", Font.PLAIN, 8));
        g2D.scale(2, 2);

        if (currentLevel != null && !changingLevel)
            currentLevel.render(g2D);

        changingLevel = false;

        g.dispose();

    }


}
