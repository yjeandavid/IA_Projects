package main.java.net.kolipass;

import net.kolipass.gameEngine.Keyboard;
import net.kolipass.gameEngine.Level;
import net.kolipass.gameEngine.MidiPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class MenuLevel extends Level {
    private MenuBackgroundsSprite menuBg;
    private MenuSprite title;
    private MenuSprite aiGame;
    private MenuSprite normalGame;
    private MenuSprite howToPlay;
    private MenuSprite quit;
    private MenuSprite loadMap;
    private MenuSprite randomMap;
    private Log log = new Log();
    private int menuChoice;
    private int menuNum;

    // obtained from parent

    private Keyboard keyboard;
    private ConfigWumpus config;
    private MidiPlayer midi;

    public MenuLevel(Component parent) {
        super(parent);
        MasterPanel mp = (MasterPanel) parent;
        //	imgLoader = mp.imageLoader;
        keyboard = mp.getKeyboard();
        config = mp.getConfig();

        midi = mp.getMidiPlayer();
        midi.loadBackgroundSound("sounds/lazarus.mid");
        midi.play(true);
        midi.loop(-1);

        menuChoice = 0;
        menuNum = 0;
    }

    /**
     * loadData()
     * loads image and sound data for this level into memory. This method should be called before
     * running the running the level's timer loop and render methods for the first time.
     */

    public void loadData() {
        MenuBackgroundsSprite.loadImages();
        MenuSprite.loadImages(imgLoader);

        imgLoader.waitForAll();

        menuBg = new MenuBackgroundsSprite(0, 48);

        title = new MenuSprite(0, 24, 't');
        aiGame = new MenuSprite(48, 240, 'a');
        normalGame = new MenuSprite(48, 264, 'n');
        howToPlay = new MenuSprite(192, 240, 'h');
        quit = new MenuSprite(192, 264, 'q');
        loadMap = new MenuSprite(48, 252, 'l');
        randomMap = new MenuSprite(192, 252, 'r');

    }

    /**
     * clean()
     * This method unloads the level's image and sound data from memory. Any other memory management clean-up for
     * the level is also taken care of here.
     */

    public void clean() {
        super.clean();
        MenuBackgroundsSprite.clean();
        MenuSprite.clean();
        keyboard = null;
        config = null;
    }

    /**
     * timerLoop()
     * This method steps through one animation frame for the level.
     * This method should be called by the game's timer event handler before rendering the level and after loadData is used.
     */

    public void timerLoop() {
        MasterPanel mp = (MasterPanel) parent;

        aiGame.isSelected = false;
        normalGame.isSelected = false;
        howToPlay.isSelected = false;
        quit.isSelected = false;

        aiGame.isVisible = false;
        normalGame.isVisible = false;
        howToPlay.isVisible = false;
        quit.isVisible = false;
        randomMap.isVisible = false;
        loadMap.isVisible = false;

        if (keyboard.isTyped(config.getVK_ESC())) {
            mp.endGame();
            return;
        }
        if (menuNum == 0) {
            aiGame.isVisible = true;
            normalGame.isVisible = true;
            howToPlay.isVisible = true;
            quit.isVisible = true;

            if (keyboard.isTyped(config.getVK_UP()))
                menuChoice--;
            if (keyboard.isTyped(config.getVK_DOWN()))
                menuChoice++;
            if (keyboard.isTyped(config.getVK_LEFT()))
                menuChoice -= 2;
            if (keyboard.isTyped(config.getVK_RIGHT()))
                menuChoice += 2;

            if (menuChoice < 0)
                menuChoice = 0;
            if (menuChoice > 3)
                menuChoice = 3;

            if (menuChoice == 0)
                aiGame.isSelected = true;
            if (menuChoice == 1)
                normalGame.isSelected = true;
            if (menuChoice == 2)
                howToPlay.isSelected = true;
            if (menuChoice == 3)
                quit.isSelected = true;

            if (menuChoice == 0 && keyboard.isTyped(config.getVK_ENTER())) // AI game
            {
                //mp.changeCurrentLevel("MainLevel");
                //return;

                menuNum = 1;
                menuChoice = 0;
                mp.gameMode = 'A';
            }
            if (menuChoice == 1 && keyboard.isTyped(config.getVK_ENTER())) // Normal game
            {
                //mp.changeCurrentLevel("MainLevel");
                //return;

                menuNum = 1;
                menuChoice = 0;
                mp.gameMode = 'H';
            }
            if (menuChoice == 2 && keyboard.isTyped(config.getVK_ENTER())) // Normal game
            {
                mp.changeCurrentLevel("HowToPlay");
                return;
            }
            if (menuChoice == 3 && keyboard.isTyped(config.getVK_ENTER())) // Quit
            {
                log.d("closing the game");
                mp.endGame();
                return;
            }
        } else if (menuNum == 1) {
            loadMap.isVisible = true;
            randomMap.isVisible = true;

            loadMap.isSelected = false;
            randomMap.isSelected = false;

            if (keyboard.isTyped(config.getVK_LEFT()))
                menuChoice -= 1;
            if (keyboard.isTyped(config.getVK_RIGHT()))
                menuChoice += 1;
            if (menuChoice < 0)
                menuChoice = 0;
            if (menuChoice > 1)
                menuChoice = 1;

            if (menuChoice == 0)
                loadMap.isSelected = true;
            if (menuChoice == 1)
                randomMap.isSelected = true;

            if (keyboard.isTyped(config.getVK_CLIMB())) {
                menuNum = 0;
                menuChoice = 0;
            }
            // Load map
            if (menuChoice == 0 && keyboard.isTyped(config.getVK_ENTER())) {


//                URL mapsUrl = AgentSprite.class.getClassLoader().getResource("maps");
                try {
                    File maps = null;
//                    if (mapsUrl != null) {
                    maps = config.findMapsFolder();

                    if (maps != null) {
                        openMapDialog(maps, mp);
                        return;

                    } else {
                        JOptionPane.showMessageDialog(null, "Maps folder is not exist");
                    }
                } catch (Exception e) {
//                    log.d("URI: " + String.valueOf(mapsUrl));
                    log.printStackTrace(e);
//                    return;
                }
            }
            if (menuChoice == 1 && keyboard.isTyped(config.getVK_ENTER())) // Random map
            {
                mp.loadFile = null;
                mp.changeCurrentLevel("MainLevel");
                return;
            }
        }

        animateAll();

        imgLoader.waitForAll(); // wait for any dynamically loaded images to load.
    }


    private void openMapDialog(File maps, MasterPanel mp) {
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(maps.listFiles()));
        Collections.sort(files);

        JComboBox<Object> jComboBox = new JComboBox<Object>(files.toArray());
        final JComponent[] inputs = new JComponent[]{
                new JLabel("Select map"),
                jComboBox
        };
        JOptionPane.showMessageDialog(null, inputs, "Map Selector", JOptionPane.PLAIN_MESSAGE);

        log.d("You entered " +
                jComboBox.getSelectedItem());
        mp.loadFile = jComboBox.getSelectedItem().toString();
        log.d("Loading map: " + mp.loadFile);
        mp.changeCurrentLevel("MainLevel");
    }


    private void animateAll() {
        menuBg.animate(imgLoader);
        title.animate(imgLoader);
        aiGame.animate(imgLoader);
        normalGame.animate(imgLoader);
        howToPlay.animate(imgLoader);
        quit.animate(imgLoader);
        loadMap.animate(imgLoader);
        randomMap.animate(imgLoader);
    }

    /**
     * render(Graphics2D g2D
     */

    public void render(Graphics2D g2D) {
        MasterPanel mainScreen = (MasterPanel) parent;

        // Background graphic
        g2D.setBackground(new Color(8, 8, 16));
        g2D.clearRect(0, 0, 340, 288);

        menuBg.render(g2D);

        title.render(g2D);

//        g2D.setColor(new Color(200, 0, 50));
//        g2D.drawString("Created by Stephen Lindberg � 2011", 10, 286);
//        Copyright‎        is good

        aiGame.render(g2D);
        normalGame.render(g2D);
        howToPlay.render(g2D);
        quit.render(g2D);

        loadMap.render(g2D);
        randomMap.render(g2D);


    }


}



