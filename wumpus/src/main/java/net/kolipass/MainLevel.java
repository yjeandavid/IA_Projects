package main.java.net.kolipass;

import main.java.net.kolipass.gameEngine.Keyboard;
import main.java.net.kolipass.gameEngine.Level;
import main.java.net.kolipass.gameEngine.MidiPlayer;
import main.java.net.kolipass.gameEngine.Sound;
import main.java.net.kolipass.wworld.Action;
import main.java.net.kolipass.wworld.*;
import main.java.net.kolipass.wworld.agent.AbstractAgent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;


public class MainLevel extends Level {
    // sprites
    private Log log = new Log();
    private HUDSprite hudPanel;
    private HUDSprite hasGoldSprite;
    private HUDSprite hasFoodSprite;
    private HUDSprite hasArrowSprite;

    private HUDSprite winSprite;
    private HUDSprite loseSprite;
    private HUDSprite dieSprite;

    private AgentSprite agentSprite;
    private WumpusSprite wumpusSprite;
    private WumpusSprite supmuwSprite;

    private FXSprite entranceSprite;
    private FXSprite glitterSprite;
    private ArrayList<FXSprite> breezeSprites;
    private ArrayList<FXSprite> stenchSprites;
    private ArrayList<FXSprite> mooSprites;

    private Hashtable<Point, CaveNodeSprite> caveNodeSprites;
    private Hashtable<Point, CaveNodeSprite> wallSprites;

    // vars

    private WumplusEnvironment wumplusEnvironment;
    private boolean stepDone;
    private boolean mapCompleted;
    private int victoryCondition; // -1 dead 0 stillGoing 1 hasNoGold 2 hasGold
    private int score;
    private int soundTimer;

    private Random rand;

    // testing

    private CaveNodeSprite testFloor;

    private int agentGridDestX;
    private int agentGridDestY;

    // obtained from parent

    private Keyboard keyboard;
    private ConfigWumpus config;
    private MidiPlayer midi;


    // CONSTRUCTOR

    public MainLevel(Component parent) {
        super(parent);
        MasterPanel mp = (MasterPanel) parent;
        //	imgLoader = mp.imageLoader;
        keyboard = mp.getKeyboard();
        config = mp.getConfig();

        midi = mp.getMidiPlayer();
        midi.loadBackgroundSound("sounds/lazarus.mid");
        midi.play(true);
        midi.loop(-1);
        rand = new Random();

        stepDone = true;
        mapCompleted = false;
        victoryCondition = 0;

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

        imgLoader.waitForAll();

        log.d("loaded images for MainLevel");

        // create Wumplus environment

        wumplusEnvironment = new WumplusEnvironment(mp);

        // create initial objects

        hudPanel = new HUDSprite(0, 0, 'p');
        hasGoldSprite = new HUDSprite(0, 48, 'g');
        hasFoodSprite = new HUDSprite(0, 120, 'f');
        hasArrowSprite = new HUDSprite(0, 192, 'a');

        winSprite = new HUDSprite(96, 120, 'v');
        loseSprite = new HUDSprite(96, 120, 'n');
        dieSprite = new HUDSprite(96, 120, 'd');
        winSprite.isVisible = false;
        loseSprite.isVisible = false;
        dieSprite.isVisible = false;

        Point coords;

        coords = gridToPanelCoords(1, 1);
        agentSprite = new AgentSprite(coords.x, coords.y);
        agentGridDestX = 1;
        agentGridDestY = 1;
        entranceSprite = new FXSprite(coords.x, coords.y, 'E');

        // wumpus

        Wumpus wumpus = wumplusEnvironment.wumpus;
        coords = gridToPanelCoords(wumpus.x, wumpus.y);
        wumpusSprite = new WumpusSprite(coords.x, coords.y, 'W');

        // supmuw

        Supmuw supmuw = wumplusEnvironment.supmuw;
        coords = gridToPanelCoords(supmuw.x, supmuw.y);
        supmuwSprite = new WumpusSprite(coords.x, coords.y, 'S');

        // gold

        Gold gold = wumplusEnvironment.gold;
        coords = gridToPanelCoords(gold.x, gold.y);
        glitterSprite = new FXSprite(coords.x, coords.y, 'G');


        // the cave

        Hashtable<Point, CaveNode> grid = wumplusEnvironment.grid;
        caveNodeSprites = new Hashtable<Point, CaveNodeSprite>();
        wallSprites = new Hashtable<Point, CaveNodeSprite>();

        breezeSprites = new ArrayList<FXSprite>();
        stenchSprites = new ArrayList<FXSprite>();
        mooSprites = new ArrayList<FXSprite>();

        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                Point point = new Point(j, i);
                CaveNode curNode = grid.get(point);

                coords = gridToPanelCoords(j, i);
                char type = ' ';

                if (curNode.hasObstacle)
                    type = 'O';
                if (curNode.hasPit)
                    type = 'P';

                CaveNodeSprite cns = new CaveNodeSprite(coords.x, coords.y, type);
                CaveNodeSprite cnsWalls = new CaveNodeSprite(coords.x, coords.y, 'W');

                caveNodeSprites.put(point, cns);
                wallSprites.put(point, cnsWalls);

                // add special effects for breezes, stenches, moos

                FXSprite fxs;
                if (curNode.hasBreeze) {
                    fxs = new FXSprite(coords.x, coords.y, 'B');
                    breezeSprites.add(fxs);
                }
                if (curNode.hasStench) {
                    fxs = new FXSprite(coords.x, coords.y, 'S');
                    stenchSprites.add(fxs);
                }
                if (curNode.hasMoo) {
                    fxs = new FXSprite(coords.x, coords.y, 'M');
                    mooSprites.add(fxs);
                }

            }
        }


        testFloor = new CaveNodeSprite(72, 24, 'F');
        testFloor.width = 240;
        testFloor.height = 240;

        soundTimer = 0;

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
        if (keyboard.isTyped(config.getVK_PAUSE())) {
            JOptionPane.showMessageDialog(null, "PAUSED");
            return;
        }

        AbstractAgent agent = wumplusEnvironment.agent;

        if (stepDone && agentSprite.animationDone && !mapCompleted) // Allow agent to act if not endgame and not between frames of animation.
        {
            // endgame conditions.
            mapIsComplete(agent);

            // human / AI input

            int action = Action.IDLE;

            if (!agent.isDead) {
                action = agent.getNextAction(wumplusEnvironment, keyboard);

            }

            // animation/sound responses from agent's action
            if (action != Action.IDLE) {
                String agentAnimation = agent.act(action, wumplusEnvironment);
                String worldActAnimation = wumplusEnvironment.act(agent);
                if (worldActAnimation != null) {
                    agentAnimation = worldActAnimation;
                }

                if (agentAnimation.equals("BUMP")) {
                    Sound sound = new Sound("main/resources/sounds/tok2.wav");
                    sound.play();
                }
                if (agentAnimation.equals("GOLDGET")) {
                    agentSprite.setAnimation("gold");
                    Sound sound = new Sound("main/resources/sounds/gold.wav");
                    sound.play();
                }
                if (agentAnimation.equals("FOODGET")) {
                    Sound sound = new Sound("main/resources/sounds/food.wav");
                    sound.play();
                }
                if (agentAnimation.equals("SCREAM")) {
                    agentSprite.setAnimation("shoot");
                    Sound sound = new Sound("main/resources/sounds/arrow.wav");
                    sound.play();
                    sound = new Sound("main/resources/sounds/wumpusDie.wav");
                    sound.play();
                    // play monster scream sound
                }
                if (agentAnimation.equals("MISS")) {
                    agentSprite.setAnimation("shoot");
                    Sound sound = new Sound("main/resources/sounds/arrow.wav");
                    sound.play();

                    // play miss sound
                }
                if (agentAnimation.equals("MOVED")) {
                    soundTimer = 0;
                }
            }


            // Percept noises

            CaveNode agentNode = wumplusEnvironment.grid.get(new Point(agent.x, agent.y));

            if (soundTimer <= 0) {
                if (agentNode.hasStench) {
                    int fartNumber = rand.nextInt(5) + 1;
                    Sound fart = new Sound("main/resources/sounds/stench" + fartNumber + ".wav");
                    fart.play();
                }
                if (agentNode.hasMoo && !wumplusEnvironment.supmuw.isDead) {
                    int fartNumber = rand.nextInt(5) + 1;
                    Sound fart = new Sound("main/resources/sounds/moo" + fartNumber + ".wav");
                    fart.play();
                }
                if (agentNode.hasBreeze) {
                    Sound breeze = new Sound("main/resources/sounds/wind1.wav");
                    breeze.play();
                }
                if (agentNode.hasGold) {
                    Sound sparkle = new Sound("main/resources/sounds/sparkleLoop.wav");
                    sparkle.play();
                }

                soundTimer = 35;
            }


            soundTimer--;


        }

        // map completed state

        if (mapCompleted) {
            // unveil all nodes
            Hashtable<Point, CaveNode> grid = wumplusEnvironment.grid;
            for (int i = 0; i <= 11; i++) {
                for (int j = 0; j <= 11; j++) {
                    Point point = new Point(j, i);
                    CaveNode curNode = grid.get(point);

                    curNode.wasVisited = true;

                }
            }

            // press ENTER to restart the game
            if (keyboard.isTyped(config.getVK_ENTER())) {
                mp.startGame();
                return;
            }
        }

        // move the agent

        switch (agent.direction) {
            case 'N':
                agentSprite.setDirection('N');
                break;
            case 'S':
                agentSprite.setDirection('S');
                break;
            case 'W':
                agentSprite.setDirection('W');
                break;
            case 'E':
                agentSprite.setDirection('E');
                break;
            default:
                break;
        }

        Point destCoords = gridToPanelCoords(agent.x, agent.y);
        if (agentSprite.x < destCoords.x) {
            agentSprite.x++;
            stepDone = false;
        }
        if (agentSprite.x > destCoords.x) {
            agentSprite.x--;
            stepDone = false;
        }
        if (agentSprite.y < destCoords.y) {
            agentSprite.y++;
            stepDone = false;
        }
        if (agentSprite.y > destCoords.y) {
            agentSprite.y--;
            stepDone = false;
        }
        if (agentSprite.x == destCoords.x && agentSprite.y == destCoords.y)
            stepDone = true;

        hasArrowSprite.isSelected = agent.hasArrow;
        hasGoldSprite.isSelected = agent.hasGold;
        hasFoodSprite.isSelected = agent.hasFood;

        if (wumplusEnvironment.wumpus.isDead) {
            stenchSprites.clear();
            wumpusSprite.setAnimation("dead");
        }
        if (wumplusEnvironment.supmuw.isDead) {
            mooSprites.clear();
            supmuwSprite.setAnimation("dead");
        }
        if (agent.hasGold) {
            glitterSprite.isVisible = false;
        }

        updateWalls(); // update the images for walls we've bumped into.

        score = agent.score;


        // wait for animations to finish
        animateAll();

        imgLoader.waitForAll(); // wait for any dynamically loaded images to load.
    }

    private void mapIsComplete(AbstractAgent agent) {
        if (agent.isDead || agent.isVictorious) {
            String label = "";
            mapCompleted = true;

            if (agent.isDead) {
                label = "isDead";

                agentSprite.setAnimation("die");
                victoryCondition = -1;
                dieSprite.isVisible = true;
                if (config.isMIDI_PLAY()) {
                    midi.load("sounds/gameOver.mid");
                    midi.play(true);
                    midi.loop(0);
                }
            }
            if (agent.isVictorious) {
                label = "isVictorious";
                agentSprite.isVisible = false;
                if (agent.hasGold) {
                    label += " hasGold";

                    victoryCondition = 2;
                    midi.stop();
                    Sound victory = new Sound("main/resources/sounds/mmVictory.wav");
                    victory.play();
                    winSprite.isVisible = true;
                } else {
                    label += " hasNoGold";
                    victoryCondition = 1;
                    loseSprite.isVisible = true;
                }
            }

            final GameState gameState = getGameState(agent);

            gameState.map = wumplusEnvironment.getMapName();
            new Runnable() {
                @Override
                public void run() {
                    JFrame frame = new ResultFrame(gameState);
                    frame.pack();
                    frame.setVisible(true);
                }

            }.run();


        }
    }

    private GameState getGameState(AbstractAgent agent) {
        GameState gameState = new GameState();
        wumplusEnvironment.getGameState(gameState);
        agent.getGameState(gameState);
        gameState.gameIsFinish = mapCompleted;
        gameState.score = score;
        return gameState;
    }


    private Point gridToPanelCoords(int x, int y) {
        x = 48 + 24 * x;
        y = 264 - 24 * y;
        return new Point(x, y);
    }


    private void updateWalls() {
        Hashtable<Point, CaveNode> grid = wumplusEnvironment.grid;

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Point point = new Point(j, i);

                CaveNode curNode = grid.get(point);
                CaveNodeSprite walls = wallSprites.get(point);

                // update the borders of any adjacent walls we've visited.

                CaveNode northwall = wumplusEnvironment.getNextNode(curNode, 'N');
                if (northwall.hasObstacle && northwall.wasVisited && !curNode.hasObstacle)
                    curNode.foundNorthWall = true;

                CaveNode southwall = wumplusEnvironment.getNextNode(curNode, 'S');
                if (southwall.hasObstacle && southwall.wasVisited && !curNode.hasObstacle)
                    curNode.foundSouthWall = true;

                CaveNode eastwall = wumplusEnvironment.getNextNode(curNode, 'E');
                if (eastwall.hasObstacle && eastwall.wasVisited && !curNode.hasObstacle)
                    curNode.foundEastWall = true;

                CaveNode westwall = wumplusEnvironment.getNextNode(curNode, 'W');
                if (westwall.hasObstacle && westwall.wasVisited && !curNode.hasObstacle)
                    curNode.foundWestWall = true;

                walls.hasBottomWall = curNode.foundSouthWall;
                walls.hasTopWall = curNode.foundNorthWall;
                walls.hasLeftWall = curNode.foundWestWall;
                walls.hasRightWall = curNode.foundEastWall;
            }
        }
    }


    // RENDERING CODE

    private void animateAll() {
        // real objects

        hudPanel.animate(imgLoader);
        hasGoldSprite.animate(imgLoader);
        hasFoodSprite.animate(imgLoader);
        hasArrowSprite.animate(imgLoader);

        winSprite.animate(imgLoader);
        loseSprite.animate(imgLoader);
        dieSprite.animate(imgLoader);

        agentSprite.animate(imgLoader);
        wumpusSprite.animate(imgLoader);
        supmuwSprite.animate(imgLoader);

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
        g2D.setBackground(new Color(0, 0, 0));
        g2D.clearRect(0, 0, 340, 288);

        testFloor.render(g2D);

        hudPanel.render(g2D);

        hasGoldSprite.render(g2D);
        hasFoodSprite.render(g2D);
        hasArrowSprite.render(g2D);

        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                Point point = new Point(j, i);
                caveNodeSprites.get(point).render(g2D);
                wallSprites.get(point).render(g2D);
            }
        }

        wumpusSprite.render(g2D);
        supmuwSprite.render(g2D);
        agentSprite.render(g2D);

        // FXSprites

        for (FXSprite fxs : breezeSprites)
            fxs.render(g2D);
        for (FXSprite fxs : stenchSprites)
            fxs.render(g2D);
        for (FXSprite fxs : mooSprites)
            fxs.render(g2D);

        glitterSprite.render(g2D);

        entranceSprite.render(g2D);

        // The Darkness

        Hashtable<Point, CaveNode> grid = wumplusEnvironment.grid;
        if (mainScreen.gameMode == 'H')
            g2D.setColor(new Color(0, 0, 0, 255));
        else
            g2D.setColor(new Color(0, 0, 0, 128));

        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                Point point = new Point(j, i);
                CaveNode curNode = grid.get(point);

                if (!curNode.wasVisited) {
                    Point coords = gridToPanelCoords(point.x, point.y);
                    g2D.drawRect(coords.x, coords.y, 24, 24);
                    g2D.fillRect(coords.x, coords.y, 24, 24);
                }

            }
        }


        // Score
        g2D.setColor(new Color(255, 255, 255));
        g2D.drawString("score: " + score, 72, 10);

        // endgame message

        winSprite.render(g2D);
        loseSprite.render(g2D);
        dieSprite.render(g2D);

        // end game message

        //	if(victoryCondition == -1)
    }


}



