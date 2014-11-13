package net.kolipass;

import net.kolipass.gameEngine.ColorFilters;
import net.kolipass.gameEngine.ImageBlitter;
import net.kolipass.gameEngine.ImageLoader;
import net.kolipass.gameEngine.Sprite;

import java.awt.*;
import java.net.URL;
import java.util.Hashtable;

public class AgentSprite extends Sprite {
    private static Log log = new Log();
    private static int numInstances = 0;
    private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();

    private char direction; // N S E W

    public String animation;
    private int animFrame;
    private int animTimer;
    public boolean animationDone;

    // CONSTRUCTOR

    public AgentSprite(double x, double y) {
        super(x, y);
        numInstances++;

        direction = 'E';
        setAnimation("walk");
        animationDone = true;
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
            URL imageURL = null;


            imageURL = AgentSprite.class.getClassLoader().getResource("graphics/AgentSprite.png");
            log.d("AgentSprite loading " + imageURL);

            Image spriteSheet = tk.getImage(imageURL);
            spriteSheet = ColorFilters.setTransparentColor(spriteSheet, new Color(0xFF00FF));

            // setup focus points for the images

            // store them into our hashtables

            int w = 24;
            int h = 24;
            Image img;

            // walking animation

            img = ImageBlitter.cropTiled(spriteSheet, 0, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("walk0E", img);

            img = ImageBlitter.cropTiled(spriteSheet, 1, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("walk1E", img);

            img = ImageBlitter.cropTiled(spriteSheet, 2, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("walk0N", img);

            img = ImageBlitter.cropTiled(spriteSheet, 3, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("walk1N", img);

            img = ImageBlitter.cropTiled(spriteSheet, 4, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("walk0W", img);

            img = ImageBlitter.cropTiled(spriteSheet, 5, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("walk1W", img);

            img = ImageBlitter.cropTiled(spriteSheet, 6, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("walk0S", img);

            img = ImageBlitter.cropTiled(spriteSheet, 7, 0, w, h);
            imgLoader.addImage(img);
            imageTable.put("walk1S", img);

            // grab gold animation

            img = ImageBlitter.cropTiled(spriteSheet, 0, 1, w, h);
            imgLoader.addImage(img);
            imageTable.put("gold0", img);

            img = ImageBlitter.cropTiled(spriteSheet, 1, 1, w, h);
            imgLoader.addImage(img);
            imageTable.put("gold1", img);

            img = ImageBlitter.cropTiled(spriteSheet, 2, 1, w, h);
            imgLoader.addImage(img);
            imageTable.put("gold2", img);

            img = ImageBlitter.cropTiled(spriteSheet, 3, 1, w, h);
            imgLoader.addImage(img);
            imageTable.put("gold3", img);

            // shoot arrow animation

            img = ImageBlitter.cropTiled(spriteSheet, 4, 1, w, h);
            imgLoader.addImage(img);
            imageTable.put("shootE", img);

            img = ImageBlitter.cropTiled(spriteSheet, 5, 1, w, h);
            imgLoader.addImage(img);
            imageTable.put("shootN", img);

            img = ImageBlitter.cropTiled(spriteSheet, 6, 1, w, h);
            imgLoader.addImage(img);
            imageTable.put("shootW", img);

            img = ImageBlitter.cropTiled(spriteSheet, 7, 1, w, h);
            imgLoader.addImage(img);
            imageTable.put("shootS", img);

            // die animation

            img = ImageBlitter.cropTiled(spriteSheet, 0, 2, w, h);
            imgLoader.addImage(img);
            imageTable.put("die0", img);

            img = ImageBlitter.cropTiled(spriteSheet, 1, 2, w, h);
            imgLoader.addImage(img);
            imageTable.put("die1", img);

            img = ImageBlitter.cropTiled(spriteSheet, 2, 2, w, h);
            imgLoader.addImage(img);
            imageTable.put("die2", img);

            img = ImageBlitter.cropTiled(spriteSheet, 3, 2, w, h);
            imgLoader.addImage(img);
            imageTable.put("die3", img);

            img = ImageBlitter.cropTiled(spriteSheet, 4, 2, w, h);
            imgLoader.addImage(img);
            imageTable.put("die4", img);

            img = ImageBlitter.cropTiled(spriteSheet, 5, 2, w, h);
            imgLoader.addImage(img);
            imageTable.put("die5", img);

            log.d("loaded AgentSprite");

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

        if (animation.equals("walk")) {
            animationDone = true;
            animFrame = 0;
            delay = 20;

            last = 0;
            if (animTimer > last + delay)
                animFrame = 1;

            last += delay;
            if (animTimer > last + delay)
                animTimer = -1;

            curImage = imageTable.get("walk" + animFrame + direction);

        } else if (animation.equals("gold")) {
            animFrame = 0;

            last = 0;
            delay = 5;
            if (animTimer > last + delay)
                animFrame = 1;

            last += delay;
            delay = 5;
            if (animTimer > last + delay)
                animFrame = 2;

            last += delay;
            delay = 5;
            if (animTimer > last + delay)
                animFrame = 3;

            last += delay;
            delay = 15;
            if (animTimer > last + delay)
                setAnimation("walk");

            curImage = imageTable.get("gold" + animFrame);
        } else if (animation.equals("shoot")) {
            if (animTimer > 30)
                setAnimation("walk");

            curImage = imageTable.get("shoot" + direction);
        } else if (animation.equals("die")) {
            animFrame = 0;

            last = 0;
            delay = 10;
            if (animTimer > last + delay)
                animFrame = 1;

            last += delay;
            delay = 10;
            if (animTimer > last + delay)
                animFrame = 2;

            last += delay;
            delay = 10;
            if (animTimer > last + delay)
                animFrame = 3;

            last += delay;
            delay = 10;
            if (animTimer > last + delay)
                animFrame = 4;

            last += delay;
            delay = 10;
            if (animTimer > last + delay) {
                animFrame = 5;
                animTimer--; // animation stops
            }

            curImage = imageTable.get("die" + animFrame);
        } else
            log.d("AbstractAgent error: bad animation");

        animTimer++;


    }

    public void setAnimation(String anim) {
        animationDone = false;
        animTimer = 0;
        animFrame = 0;
        animation = anim;
    }

    public void setDirection(char nsew) {
        if (nsew == 'N' || nsew == 'S' || nsew == 'E' || nsew == 'W')
            direction = nsew;
        else
            log.d("bad direction given to agent.");
    }


}