package main.java.net.kolipass;

import main.java.net.kolipass.gameEngine.ColorFilters;
import main.java.net.kolipass.gameEngine.ImageBlitter;
import main.java.net.kolipass.gameEngine.ImageLoader;
import main.java.net.kolipass.gameEngine.Sprite;

import java.awt.*;
import java.net.URL;
import java.util.Hashtable;

public class WumpusSprite extends Sprite {
    private static int numInstances = 0;
    private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
    private static Log log = new Log();
    private char type; // W wumpus S supmuw
    private String animation;
    private int animFrame;
    private int animTimer;

    // CONSTRUCTOR

    public WumpusSprite(double x, double y, char type) {
        super(x, y);
        numInstances++;

        setAnimation("alive");
        this.type = type;
    }

    // MEM MANAGEMENT METHODS

    /**
     * destroy()
     * decrements number of instances of this class and marks it as destroyed.
     */

    public void destroy() {
        this.isDestroyed = true;
        numInstances--;
    }

    /**
     * loadImages()
     * loads and stores image data for this Sprite.
     */

    public static void loadImages(ImageLoader imgLoader) {
        // get our default toolkit
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();

            // load our images

            URL imageURL = WumpusSprite.class.getClassLoader().getResource("main/resources/graphics/WumpusSprite.png");
            log.d("WumpusSprite loading " + imageURL);
            Image wumpusSheet = tk.getImage(imageURL);
            wumpusSheet = ColorFilters.setTransparentColor(wumpusSheet, new Color(0xFF00FF));

            imageURL = WumpusSprite.class.getClassLoader().getResource("main/resources/graphics/SupmuwSprite.png");
            log.d("WumpusSprite loading " + imageURL);
            Image supmuwSheet = tk.getImage(imageURL);
            supmuwSheet = ColorFilters.setTransparentColor(supmuwSheet, new Color(0xFF00FF));

            // setup focus points for the images

            // store them into our hashtables

            int w = 24;
            int h = 24;
            Image img;

            // Wumpus

            img = ImageBlitter.cropTiled(wumpusSheet, 0, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("WAlive0", img);

            img = ImageBlitter.cropTiled(wumpusSheet, 1, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("WAlive1", img);

            img = ImageBlitter.cropTiled(wumpusSheet, 0, 1, w, h);
            imgLoader.addImage(img);
            imageTable.put("WDead", img);

            // Supmuw

            img = ImageBlitter.cropTiled(supmuwSheet, 0, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("SAlive0", img);

            img = ImageBlitter.cropTiled(supmuwSheet, 1, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("SAlive1", img);

            img = ImageBlitter.cropTiled(supmuwSheet, 0, 1, w, h);
            imgLoader.addImage(img);
            imageTable.put("SDead", img);

            log.d("loaded WumpusSprite");

        } catch (Exception ex) {
        }
    }

    /**
     * clean()
     * Used to unload image data and cleans up static members of this Sprite extension
     * when the parent component is done with it.
     */

    public static void clean() {
        numInstances = 0;
        imageTable.clear();
    }


    // RENDERING METHODS

    /**
     * animate()
     * Prepares the current animation frame and then prepares the sprite for computing its next frame of animation. Called by render(Graphics2D g).
     * Preconditions: none.
     * Postconditions: curImage is set to the image of this Sprite's current animation frame
     * and necessary values for computing the next frame of animation are prepared.
     */

    protected void animate(ImageLoader il) {
        super.animate(il);
        fx = 0;
        fy = 0;

        int delay, last;

        if (animation.equals("alive")) {
            animFrame = 0;
            delay = 20;

            last = 0;
            if (animTimer > last + delay)
                animFrame = 1;

            last += delay;
            if (animTimer > last + delay)
                animTimer = -1;

            curImage = imageTable.get(type + "Alive" + animFrame);

        } else {
            curImage = imageTable.get(type + "Dead");
            animTimer = 0;
        }

        animTimer++;


    }

    public void setAnimation(String anim) {
        animTimer = 0;
        animFrame = 0;
        animation = anim;
    }


}