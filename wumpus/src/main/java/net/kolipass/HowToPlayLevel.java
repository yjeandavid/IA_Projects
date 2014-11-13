package main.java.net.kolipass;

import net.kolipass.gameEngine.Keyboard;
import net.kolipass.gameEngine.Level;
import net.kolipass.gameEngine.MidiPlayer;

import java.awt.*;
import java.util.ArrayList;


public class HowToPlayLevel extends Level {
    // sprites

    private HUDSprite hudPanel;
    private HUDSprite hasGoldSprite;
    private HUDSprite hasFoodSprite;
    private HUDSprite hasArrowSprite;

    private HowToPlaySprite bgSprite;
    private HowToPlaySprite htp2Sprite;
    private HowToPlaySprite htp6Sprite;
    private HowToPlaySprite enterSprite;

    private AgentSprite agentSprite, agentSprite2;
    private WumpusSprite wumpusSprite;
    private WumpusSprite supmuwSprite, supmuwSprite2, supmuwSprite3;

    private FXSprite entranceSprite;
    private FXSprite glitterSprite;
    private ArrayList<FXSprite> breezeSprites;
    private ArrayList<FXSprite> stenchSprites;
    private ArrayList<FXSprite> mooSprites;

    private CaveNodeSprite testFloor, testFloor2, testFloor3, testFloor4, testFloor5;
    private CaveNodeSprite pitSprite;

    // vars

    private int slideNum;
    private int timer1;

    // obtained from parent

    private Keyboard keyboard;
    private ConfigWumpus config;
    private MidiPlayer midi;


    // CONSTRUCTOR

    public HowToPlayLevel(Component parent) {
        super(parent);
        MasterPanel mp = (MasterPanel) parent;
        //	imgLoader = mp.imageLoader;
        keyboard = mp.getKeyboard();
        config = mp.getConfig();

        midi = mp.getMidiPlayer();
//		midi.load("sounds/lazarus.mid");
        midi.play(true);
        midi.loop(-1);


    }

    /**
     * loadData()
     * loads image and sound data for this level into memory. This method should be called before
     * running the running the level's timer loop and render methods for the first time.
     */

    public void loadData() {
        MasterPanel mp = (MasterPanel) parent;

        // data that will actually be used in final version

        HUDSprite.loadImages(imgLoader);
        CaveNodeSprite.loadImages(imgLoader);
        AgentSprite.loadImages(imgLoader);
        WumpusSprite.loadImages(imgLoader);
        FXSprite.loadImages(imgLoader);
        HowToPlaySprite.loadImages(imgLoader);

        imgLoader.waitForAll();

        // create initial objects

        hudPanel = new HUDSprite(0, 0, 'p');
        hasGoldSprite = new HUDSprite(0, 48, 'g');
        hasFoodSprite = new HUDSprite(0, 120, 'f');
        hasArrowSprite = new HUDSprite(0, 192, 'a');

        bgSprite = new HowToPlaySprite(0, 10, 'b');
        htp2Sprite = new HowToPlaySprite(72, 96, '2');
        htp6Sprite = new HowToPlaySprite(72, 96, '6');
        enterSprite = new HowToPlaySprite(168, 264, 'e');

        Point coords;

        coords = gridToPanelCoords(0, 0);
        agentSprite = new AgentSprite(coords.x, coords.y);
        agentSprite2 = new AgentSprite(coords.x, coords.y);
        entranceSprite = new FXSprite(coords.x, coords.y, 'E');

        // wumpus

        wumpusSprite = new WumpusSprite(0, 0, 'W');

        // supmuw

        supmuwSprite = new WumpusSprite(0, 0, 'S');
        supmuwSprite2 = new WumpusSprite(0, 0, 'S');
        supmuwSprite3 = new WumpusSprite(0, 0, 'S');

        // gold

        glitterSprite = new FXSprite(0, 0, 'G');


        breezeSprites = new ArrayList<FXSprite>();
        stenchSprites = new ArrayList<FXSprite>();
        mooSprites = new ArrayList<FXSprite>();

        testFloor = new CaveNodeSprite(0, 0, 'F');
        testFloor.width = 24 * 3;
        testFloor.height = 24 * 3;

        testFloor2 = new CaveNodeSprite(0, 0, 'F');
        testFloor2.width = 24 * 3;
        testFloor2.height = 24 * 3;

        testFloor3 = new CaveNodeSprite(0, 0, 'F');
        testFloor3.width = 24 * 3;
        testFloor3.height = 24 * 3;

        testFloor4 = new CaveNodeSprite(0, 0, 'F');
        testFloor4.width = 24 * 3;
        testFloor4.height = 24 * 3;

        testFloor5 = new CaveNodeSprite(0, 0, 'F');
        testFloor5.width = 24 * 3;
        testFloor5.height = 24 * 3;

        pitSprite = new CaveNodeSprite(240, 168, 'P');

        slideNum = 0;
        timer1 = 0;
        initCurSlide();
    }

    /**
     * clean()
     * This method unloads the level's image and sound data from memory. Any other memory management clean-up for
     * the level is also taken care of here.
     */

    public void clean() {
        super.clean();
        HUDSprite.clean();
        CaveNodeSprite.clean();
        AgentSprite.clean();
        HowToPlaySprite.clean();
        FXSprite.clean();
        keyboard = null;
        config = null;
    }


    // TIMER LOOP CODE

    public void timerLoop() {
        MasterPanel mp = (MasterPanel) parent;

        if (keyboard.isTyped(config.getVK_ESC())) {
            mp.startGame();
            return;
        }

        if (keyboard.isTyped(config.getVK_ENTER())) {
            slideNum++;
            initCurSlide();
        }

        // make everything invisible until it is needed for a slide.
        testFloor.isVisible = false;
        testFloor2.isVisible = false;
        testFloor3.isVisible = false;
        testFloor4.isVisible = false;
        testFloor5.isVisible = false;

        pitSprite.isVisible = false;

        hudPanel.isVisible = false;
        hasGoldSprite.isVisible = false;
        hasFoodSprite.isVisible = false;
        hasArrowSprite.isVisible = false;

        htp2Sprite.isVisible = false;
        htp6Sprite.isVisible = false;

        agentSprite.isVisible = false;
        agentSprite2.isVisible = false;
        wumpusSprite.isVisible = false;
        supmuwSprite.isVisible = false;
        supmuwSprite2.isVisible = false;
        supmuwSprite3.isVisible = false;

        entranceSprite.isVisible = false;
        glitterSprite.isVisible = false;


        ////// SLIDE SHOW CODE

        if (slideNum == 0) {
            agentSprite.isVisible = true;
            agentSprite.setDirection('S');
            agentSprite.x = 48;
            agentSprite.y = 144;

            agentSprite2.isVisible = true;

            testFloor.isVisible = true;
            testFloor.x = 216;
            testFloor.y = 120;

            glitterSprite.isVisible = true;
            glitterSprite.x = 216;
            glitterSprite.y = 144;

            if (agentSprite2.x > glitterSprite.x) {
                agentSprite2.x--;
                agentSprite2.setDirection('W');
            }
            if (agentSprite2.x == glitterSprite.x) {
                if (!agentSprite2.animation.equals("gold"))
                    agentSprite2.setAnimation("gold");
                timer1++;

                if (timer1 > 30) {
                    timer1 = 0;
                    agentSprite2.setAnimation("walk");
                    agentSprite2.x = 264;
                }
            }
        } else if (slideNum == 1) {
            agentSprite.isVisible = true;
            agentSprite.x = 168;
            agentSprite.y = 168;

            htp2Sprite.isVisible = true;
        } else if (slideNum == 2) {
            wumpusSprite.isVisible = true;
            wumpusSprite.x = 72;
            wumpusSprite.y = 144;

            testFloor.isVisible = true;
            testFloor.x = 72 - 24;
            testFloor.y = 144 - 24;

            pitSprite.isVisible = true;
            pitSprite.x = 240;
            pitSprite.y = 168;

            testFloor2.isVisible = true;
            testFloor2.x = 240 - 24;
            testFloor2.y = 168 - 24;
        } else if (slideNum == 3) {
            supmuwSprite.isVisible = true;
            supmuwSprite.x = 72;
            supmuwSprite.y = 144;

            supmuwSprite2.isVisible = true;
            supmuwSprite2.x = 216;
            supmuwSprite2.y = 96;

            supmuwSprite3.isVisible = true;
            supmuwSprite3.x = 264;
            supmuwSprite3.y = 216;

            pitSprite.isVisible = true;
            pitSprite.x = 264;
            pitSprite.y = 216;

            wumpusSprite.isVisible = true;
            wumpusSprite.x = 240;
            wumpusSprite.y = 96;

            testFloor.isVisible = true;
            testFloor.x = 48;
            testFloor.y = 120;

            testFloor2.isVisible = true;
            testFloor2.x = 192;
            testFloor2.y = 72;

            testFloor3.isVisible = true;
            testFloor3.x = 216;
            testFloor3.y = 72;

            testFloor4.isVisible = true;
            testFloor4.x = 192;
            testFloor4.y = 192;

            testFloor5.isVisible = true;
            testFloor5.x = 240;
            testFloor5.y = 192;

            agentSprite.isVisible = true;
            agentSprite.setDirection('E');
            if (timer1 < 48)
                agentSprite.x++;
            if (timer1 >= 48 && timer1 < 54)
                agentSprite.x -= 4;
            if (timer1 > 78) {
                agentSprite.x = 216;
                timer1 = 0;
            }
            timer1++;
        } else if (slideNum == 4) {
            agentSprite.isVisible = true;
            agentSprite.x = 96;
            agentSprite.y = 144;

            wumpusSprite.isVisible = true;
            wumpusSprite.x = 240;
            wumpusSprite.y = 144;

            testFloor.isVisible = true;
            testFloor.x = 72;
            testFloor.y = 120;

            testFloor2.isVisible = true;
            testFloor2.x = 216;
            testFloor2.y = 120;

            hudPanel.isVisible = true;
            hasGoldSprite.isVisible = true;
            hasFoodSprite.isVisible = true;
            hasArrowSprite.isVisible = true;

            if (timer1 < 60) {
                wumpusSprite.setAnimation("alive");
                hasArrowSprite.isSelected = true;
            }
            if (timer1 >= 60 && timer1 < 80) {
                wumpusSprite.setAnimation("dead");
                agentSprite.setAnimation("shoot");
                hasArrowSprite.isSelected = false;
            }
            if (timer1 >= 80) {
                agentSprite.setAnimation("walk");
            }
            if (timer1 > 100)
                timer1 = 0;

            timer1++;

        } else if (slideNum == 5) {
            entranceSprite.isVisible = true;
            entranceSprite.x = 120;
            entranceSprite.y = 192;
            agentSprite.isVisible = true;
            htp6Sprite.isVisible = true;
            glitterSprite.isVisible = true;
            glitterSprite.x = 216;
            glitterSprite.y = 168;

            if (timer1 < 48) {
                if (!agentSprite.animation.equals("walk"))
                    agentSprite.setAnimation("walk");
                agentSprite.setDirection('E');
                agentSprite.x++;
            }
            if (timer1 >= 48 && timer1 < 78) {
                if (!agentSprite.animation.equals("gold"))
                    agentSprite.setAnimation("gold");
            }
            if (timer1 >= 78 && timer1 < 174) {
                if (!agentSprite.animation.equals("walk"))
                    agentSprite.setAnimation("walk");
                agentSprite.setDirection('W');
                agentSprite.x--;
            }
            if (timer1 >= 174 && timer1 < 198) {
                agentSprite.setDirection('S');
                agentSprite.y++;
            }
            if (timer1 > 198 + 24) {
                agentSprite.x = 168;
                agentSprite.y = 168;
                timer1 = 0;
            }

            timer1++;
        } else if (slideNum == 6) {

        } else if (slideNum == 7) {

        } else {
            mp.changeCurrentLevel("Menu");
            return;
        }


        // wait for animations to finish
        animateAll();

        imgLoader.waitForAll(); // wait for any dynamically loaded images to load.
    }


    public void initCurSlide() {
        if (slideNum == 0) {
            agentSprite2.x = 264;
            agentSprite2.y = 144;
            timer1 = 0;
        } else if (slideNum == 1) {

        } else if (slideNum == 2) {
            FXSprite stench;
            stench = new FXSprite(72, 120, 'S');
            stenchSprites.add(stench);

            stench = new FXSprite(48, 144, 'S');
            stenchSprites.add(stench);

            stench = new FXSprite(96, 144, 'S');
            stenchSprites.add(stench);

            stench = new FXSprite(72, 168, 'S');
            stenchSprites.add(stench);


            FXSprite breeze;
            breeze = new FXSprite(240, 144, 'B');
            breezeSprites.add(breeze);

            breeze = new FXSprite(216, 168, 'B');
            breezeSprites.add(breeze);

            breeze = new FXSprite(264, 168, 'B');
            breezeSprites.add(breeze);

            breeze = new FXSprite(240, 168 + 24, 'B');
            breezeSprites.add(breeze);

        } else if (slideNum == 3) {
            stenchSprites.clear();
            breezeSprites.clear();

            FXSprite stench;
            stench = new FXSprite(240, 72, 'S');
            stenchSprites.add(stench);
            stench = new FXSprite(240 - 24, 72 + 24, 'S');
            stenchSprites.add(stench);
            stench = new FXSprite(240 + 24, 72 + 24, 'S');
            stenchSprites.add(stench);
            stench = new FXSprite(240, 72 + 48, 'S');
            stenchSprites.add(stench);

            FXSprite moo;
            moo = new FXSprite(48, 120, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(48 + 24, 120, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(48 + 48, 120, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(48, 120 + 24, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(48 + 48, 120 + 24, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(48, 120 + 48, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(48 + 24, 120 + 48, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(48 + 48, 120 + 48, 'M');
            mooSprites.add(moo);

            moo = new FXSprite(192, 72, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(192 + 24, 72, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(192 + 48, 72, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(192, 72 + 24, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(192 + 48, 72 + 24, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(192, 72 + 48, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(192 + 24, 72 + 48, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(192 + 48, 72 + 48, 'M');
            mooSprites.add(moo);

            moo = new FXSprite(240, 192, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(240 + 24, 192, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(240 + 48, 192, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(240, 192 + 24, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(240 + 48, 192 + 24, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(240, 192 + 48, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(240 + 24, 192 + 48, 'M');
            mooSprites.add(moo);
            moo = new FXSprite(240 + 48, 192 + 48, 'M');
            mooSprites.add(moo);

            FXSprite breeze;
            breeze = new FXSprite(264 - 24, 216, 'B');
            breezeSprites.add(breeze);

            breeze = new FXSprite(264 + 24, 216, 'B');
            breezeSprites.add(breeze);

            breeze = new FXSprite(264, 192, 'B');
            breezeSprites.add(breeze);

            breeze = new FXSprite(264, 216 + 24, 'B');
            breezeSprites.add(breeze);

            agentSprite.x = 216;
            agentSprite.y = 216;
            timer1 = 0;
        } else if (slideNum == 4) {
            stenchSprites.clear();
            breezeSprites.clear();
            mooSprites.clear();

            FXSprite stench;
            stench = new FXSprite(240, 120, 'S');
            stenchSprites.add(stench);
            stench = new FXSprite(240 - 24, 120 + 24, 'S');
            stenchSprites.add(stench);
            stench = new FXSprite(240 + 24, 120 + 24, 'S');
            stenchSprites.add(stench);
            stench = new FXSprite(240, 120 + 48, 'S');
            stenchSprites.add(stench);

            timer1 = 0;
        } else if (slideNum == 5) {
            stenchSprites.clear();
            timer1 = 0;

            agentSprite.x = 168;
            agentSprite.y = 168;
        } else if (slideNum == 6) {

        }
    }


    private Point gridToPanelCoords(int x, int y) {
        x = 48 + 24 * x;
        y = 264 - 24 * y;
        return new Point(x, y);
    }


    // RENDERING CODE

    private void animateAll() {
        // real objects

        hudPanel.animate(imgLoader);
        hasGoldSprite.animate(imgLoader);
        hasFoodSprite.animate(imgLoader);
        hasArrowSprite.animate(imgLoader);

        agentSprite.animate(imgLoader);
        agentSprite2.animate(imgLoader);
        wumpusSprite.animate(imgLoader);
        supmuwSprite.animate(imgLoader);
        supmuwSprite2.animate(imgLoader);
        supmuwSprite3.animate(imgLoader);

        bgSprite.animate(imgLoader);
        htp2Sprite.animate(imgLoader);
        htp6Sprite.animate(imgLoader);
        enterSprite.animate(imgLoader);

        // test objects

/*		for(CaveNodeSprite cns : caveNodeSprites)
            cns.animate(imgLoader);
		for(CaveNodeSprite cns : wallSprites)
			cns.animate(imgLoader);*/

        for (FXSprite fxs : breezeSprites)
            fxs.animate(imgLoader);
        for (FXSprite fxs : stenchSprites)
            fxs.animate(imgLoader);
        for (FXSprite fxs : mooSprites)
            fxs.animate(imgLoader);

        glitterSprite.animate(imgLoader);
        entranceSprite.animate(imgLoader);
    }

    /**
     * render(Graphics2D g2D
     */

    public void render(Graphics2D g2D) {
        MasterPanel mainScreen = (MasterPanel) parent;

        // Background graphic
        g2D.setBackground(new Color(8, 8, 16));
        g2D.clearRect(0, 0, 340, 288);

        bgSprite.render(g2D);
        htp2Sprite.render(g2D);
        htp6Sprite.render(g2D);

        testFloor.render(g2D);
        testFloor2.render(g2D);
        testFloor3.render(g2D);
        testFloor4.render(g2D);
        testFloor5.render(g2D);

        pitSprite.render(g2D);


        hudPanel.render(g2D);

        hasGoldSprite.render(g2D);
        hasFoodSprite.render(g2D);
        hasArrowSprite.render(g2D);

        wumpusSprite.render(g2D);
        supmuwSprite.render(g2D);
        supmuwSprite2.render(g2D);
        supmuwSprite3.render(g2D);
        agentSprite.render(g2D);
        agentSprite2.render(g2D);

        // FXSprites

        for (FXSprite fxs : breezeSprites)
            fxs.render(g2D);
        for (FXSprite fxs : stenchSprites)
            fxs.render(g2D);
        for (FXSprite fxs : mooSprites)
            fxs.render(g2D);

        glitterSprite.render(g2D);

        entranceSprite.render(g2D);

        // Text

        g2D.setColor(new Color(255, 255, 255));
        //	g2D.setFont(new Font("Arial",Font.PLAIN,8));
        if (slideNum == 0) {
            g2D.drawString("This is the Hero.", 24, 96);
            g2D.drawString("The Hero likes gold!", 192, 96);
            g2D.drawString("Gold likes to sparkle!", 192, 96 + 15);
        } else if (slideNum == 1) {
            drawCenteredString("The cave the gold hides in is very dark. The Hero can only", 168, 48, g2D);
            drawCenteredString("see parts of the cave that he's already been to. ", 168, 48 + 15, g2D);
            drawCenteredString("Walls also become visible only if you bump into them.", 168, 48 + 30, g2D);
        } else if (slideNum == 2) {
            drawCenteredString("This is the Wumpus.", 72, 48, g2D);
            drawCenteredString("The Wumpus likes eating heroes!", 72, 48 + 15, g2D);
            drawCenteredString("He also smells bad.", 72, 48 + 30, g2D);
            drawCenteredString("Nobody likes the Wumpus.", 72, 48 + 45, g2D);

            drawCenteredString("This is a pit.", 240, 72, g2D);
            drawCenteredString("Pits like surrounding themselves", 240, 72 + 15, g2D);
            drawCenteredString("with gentle breezes!", 240, 72 + 30, g2D);
            drawCenteredString("Pits also eat heroes.", 240, 72 + 45, g2D);
        } else if (slideNum == 3) {
            drawCenteredString("This is the Supmuw.", 72, 48, g2D);
            drawCenteredString("The Supmuw likes to say Moo!", 72, 48 + 15, g2D);
            drawCenteredString("Usually the Supmuw is friendly", 72, 48 + 30, g2D);
            drawCenteredString("and he'll give the Hero food!", 72, 48 + 45, g2D);

            drawCenteredString("However, the Supmuw becomes", 240, 48, g2D);
            drawCenteredString("murderous if he smells the Wumpus.", 240, 48 + 15, g2D);

            drawCenteredString("If the Supmuw is in a pit, he'll", 240, 168, g2D);
            drawCenteredString("keep the Hero from falling in!", 240, 168 + 15, g2D);

        } else if (slideNum == 4) {
            drawCenteredString("The Hero has a Magic Arrow!", 168, 48, g2D);
            drawCenteredString("He can fire it from his bow to slay the Wumpus!", 168, 48 + 15, g2D);
            drawCenteredString("Be careful... The Hero only has ONE Arrow.", 168, 48 + 30, g2D);
        } else if (slideNum == 5) {
            drawCenteredString("Help the Hero find the gold,", 168, 48, g2D);
            drawCenteredString("return to the cave's entrance alive,", 168, 48 + 15, g2D);
            drawCenteredString("and climb out to be VICTORIOUS!,", 168, 48 + 30, g2D);
        } else if (slideNum == 6) {
            drawCenteredString("Here are the controls:,", 168, 48, g2D);

            drawCenteredString("Turn left .......LEFT ARROW", 168, 48 + 30, g2D);
            drawCenteredString("Turn right .......RIGHT ARROW", 168, 48 + 45, g2D);
            drawCenteredString("Move forward ...........UP ARROW", 168, 48 + 60, g2D);
            drawCenteredString("Grab gold ................ENTER", 168, 48 + 75, g2D);
            drawCenteredString("Shoot arrow .............SHIFT", 168, 48 + 90, g2D);
            drawCenteredString("Climb out of cave entrance ............CTRL", 168, 48 + 105, g2D);
            drawCenteredString("Return to Title .............ESC", 168, 48 + 120, g2D);
        } else if (slideNum == 7) {
            drawCenteredString("Credits:,", 168, 48, g2D);

            drawCenteredString("Design: Stephen Lindberg, Dr. Bill Eberle", 168, 48 + 30, g2D);
            drawCenteredString("Art: Stephen Lindberg", 168, 48 + 45, g2D);
            drawCenteredString("Programming: Stephen Lindberg", 168, 48 + 60, g2D);
            drawCenteredString("Co-programming: Chris Leatherwood", 168, 48 + 75, g2D);
            drawCenteredString("Music: Circy, Mega Man 1, Zero Wing", 168, 48 + 90, g2D);
            drawCenteredString("Sounds: Stephen Lindberg, Clickteam, teh Interwebs", 168, 48 + 105, g2D);
            drawCenteredString("", 168, 48 + 120, g2D);
        }


        enterSprite.render(g2D);


    }


    public void drawCenteredString(String s, int x, int y, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        x -= fm.stringWidth(s) / 2;
        g.drawString(s, x, y);
    }


}



