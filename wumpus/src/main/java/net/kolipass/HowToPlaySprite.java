package main.java.net.kolipass;

import main.java.net.kolipass.gameEngine.ColorFilters;
import main.java.net.kolipass.gameEngine.ImageLoader;
import main.java.net.kolipass.gameEngine.Sprite;

import java.awt.*;
import java.net.URL;
import java.util.Hashtable;

public class HowToPlaySprite extends Sprite {
    private static int numInstances = 0;
    private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
    private static Log log = new Log();
    private char type;

    // CONSTRUCTOR

    public HowToPlaySprite(double x, double y, char type) {
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

            Image img;

            // setup focus points for the images

            // store them into our hashtables

            URL imageURL = HowToPlaySprite.class.getClassLoader().getResource("main/resources/graphics/HowToPlayBG.png");
            log.d("HowToPlaySprite loading " + imageURL);
            img = tk.getImage(imageURL);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("bg", img);

            imageURL = HowToPlaySprite.class.getClassLoader().getResource("main/resources/graphics/HowToPlay2.png");
            log.d("HowToPlaySprite loading " + imageURL);
            img = tk.getImage(imageURL);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("htp2", img);

            imageURL = HowToPlaySprite.class.getClassLoader().getResource("main/resources/graphics/HowToPlay6.png");
            log.d("HowToPlaySprite loading " + imageURL);
            img = tk.getImage(imageURL);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("htp6", img);

            imageURL = HowToPlaySprite.class.getClassLoader().getResource("main/resources/graphics/PressEnterText.png");
            log.d("HowToPlaySprite loading " + imageURL);
            img = tk.getImage(imageURL);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("enter", img);

            log.d("loaded HowToPlaySprite");

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
        if (type == 'b')
            curImage = imageTable.get("bg");
        if (type == '2')
            curImage = imageTable.get("htp2");
        if (type == '6')
            curImage = imageTable.get("htp6");
        if (type == 'e')
            curImage = imageTable.get("enter");


        fx = 0;
        fy = 0;
    }


}