package main.java.net.kolipass;

import main.java.net.kolipass.gameEngine.ColorFilters;
import main.java.net.kolipass.gameEngine.ImageBlitter;
import main.java.net.kolipass.gameEngine.ImageLoader;
import main.java.net.kolipass.gameEngine.Sprite;

import java.awt.*;
import java.net.URL;
import java.util.Hashtable;

public class FXSprite extends Sprite {
    private static int numInstances = 0;
    private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
    private static Log log = new Log();
    private char type; // B breeze S stench M moo E entrance G glitter

    private String animation;
    private int animFrame;
    private int animTimer;

    // CONSTRUCTOR

    public FXSprite(double x, double y, char type) {
        super(x, y);
        numInstances++;

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

            URL imageURL = FXSprite.class.getClassLoader().getResource("main/resources/graphics/BreezeSprite.png");
            log.d("FXSprite loading " + imageURL);
            Image breezeSheet = tk.getImage(imageURL);
            breezeSheet = ColorFilters.setTransparentColor(breezeSheet, new Color(0xFF00FF));
            breezeSheet = ColorFilters.setSemiTransparency(breezeSheet, 0.5);

            imageURL = FXSprite.class.getClassLoader().getResource("main/resources/graphics/StenchSprite.png");
            log.d("FXSprite loading " + imageURL);
            Image stenchSheet = tk.getImage(imageURL);
            stenchSheet = ColorFilters.setTransparentColor(stenchSheet, new Color(0xFF00FF));
            stenchSheet = ColorFilters.setSemiTransparency(stenchSheet, 0.5);

            imageURL = FXSprite.class.getClassLoader().getResource("main/resources/graphics/MooSprite.png");
            log.d("FXSprite loading " + imageURL);
            Image mooSheet = tk.getImage(imageURL);
            mooSheet = ColorFilters.setTransparentColor(mooSheet, new Color(0xFF00FF));
            mooSheet = ColorFilters.setSemiTransparency(mooSheet, 0.5);

            imageURL = FXSprite.class.getClassLoader().getResource("main/resources/graphics/GlitterSprite.png");
            log.d("FXSprite loading " + imageURL);
            Image glitterSheet = tk.getImage(imageURL);
            glitterSheet = ColorFilters.setTransparentColor(glitterSheet, new Color(0xFF00FF));
            glitterSheet = ColorFilters.setSemiTransparency(glitterSheet, 0.25);

            imageURL = FXSprite.class.getClassLoader().getResource("main/resources/graphics/EntranceSprite.png");
            log.d("FXSprite loading " + imageURL);
            Image entranceImg = tk.getImage(imageURL);
            entranceImg = ColorFilters.setTransparentColor(entranceImg, new Color(0xFF00FF));
            entranceImg = ColorFilters.setSemiTransparency(entranceImg, 0.75);

            // setup focus points for the images

            // store them into our hashtables

            int w = 24;
            int h = 24;
            Image img;

            // Breeze animation

            for (int i = 0; i < 11; i++) {
                img = ImageBlitter.cropTiled(breezeSheet, i, 0, w, h);
                imgLoader.addImage(img);
                imageTable.put("breeze" + i, img);
            }

            // Stench animation

            for (int i = 0; i < 24; i++) {
                img = ImageBlitter.cropTiled(stenchSheet, i, 0, w, h);
                imgLoader.addImage(img);
                imageTable.put("stench" + i, img);
            }

            // Moo animation

            for (int i = 0; i < 6; i++) {
                img = ImageBlitter.cropTiled(mooSheet, i, 0, w, h);
                imgLoader.addImage(img);
                imageTable.put("moo" + i, img);
            }

            // Glitter animation

            for (int i = 0; i < 6; i++) {
                img = ImageBlitter.cropTiled(glitterSheet, i, 0, w, h);
                imgLoader.addImage(img);
                imageTable.put("glitter" + i, img);
            }

            // Entrance animation

            imageTable.put("entrance", entranceImg);
            log.d("loaded FXSprite");
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


        if (type == 'B') {
            animFrame = 0;
            delay = 5;

            animFrame = animTimer / delay;
            if (animFrame >= 11) {
                animFrame = 0;
                animTimer = 0;
            }

            curImage = imageTable.get("breeze" + animFrame);
        } else if (type == 'S') {
            animFrame = 0;
            delay = 5;

            animFrame = animTimer / delay;
            if (animFrame >= 24) {
                animFrame = 0;
                animTimer = 0;
            }

            curImage = imageTable.get("stench" + animFrame);
        } else if (type == 'M') {
            animFrame = 0;
            delay = 5;
            last = 0;

            if (animTimer > delay + last)
                animFrame = 1;

            last += delay;
            if (animTimer > delay + last)
                animFrame = 2;

            delay = 15;
            last += delay;
            if (animTimer > delay + last)
                animFrame = 3;

            delay = 5;
            last += delay;
            if (animTimer > delay + last)
                animFrame = 4;

            last += delay;
            if (animTimer > delay + last)
                animFrame = 5;

            last += delay;
            if (animTimer > delay + last)
                animTimer = 0;


            curImage = imageTable.get("moo" + animFrame);
        } else if (type == 'G') {
            animFrame = 0;
            delay = 5;

            animFrame = animTimer / delay;
            if (animFrame >= 6) {
                animFrame = 0;
                animTimer = 0;
            }

            curImage = imageTable.get("glitter" + animFrame);
        } else if (type == 'E') {

            curImage = imageTable.get("entrance");
        } else
            log.d("FX error: bad animation");

        animTimer++;


    }


}