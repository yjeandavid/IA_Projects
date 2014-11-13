package main.java.net.kolipass;

import net.kolipass.gameEngine.ImageLoader;
import net.kolipass.gameEngine.Sprite;

import java.awt.*;
import java.net.URL;
import java.util.Hashtable;

public class MenuBackgroundsSprite extends Sprite {
    private static int numInstances = 0;
    private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
    private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
    private static Log log = new Log();
    // CONSTRUCTOR

    public MenuBackgroundsSprite(double x, double y) {
        super(x, y);
        numInstances++;
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

    public static void loadImages() {
        // get our default toolkit
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();

            // load our images
            URL imageURL = MenuBackgroundsSprite.class.getClassLoader().getResource("graphics/MenuBG.png");
            log.d("MenuBackgroundsSprite loading " + imageURL);
            Image titleImg = tk.getImage(imageURL);

            // setup focus points for the images

            focusTable.put("menuBg", new Point(0, 0));

            // store them into our hashtables

            imageTable.put("menuBg", titleImg);

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
        focusTable.clear();
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
        curImage = imageTable.get("menuBg");
        Point curFocus = focusTable.get("menuBg");

        fx = 0;
        fy = 0;
    }


}
