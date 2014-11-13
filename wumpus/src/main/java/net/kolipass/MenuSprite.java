package main.java.net.kolipass;

import net.kolipass.gameEngine.ColorFilters;
import net.kolipass.gameEngine.ImageBlitter;
import net.kolipass.gameEngine.ImageLoader;
import net.kolipass.gameEngine.Sprite;

import java.awt.*;
import java.net.URL;
import java.util.Hashtable;

public class MenuSprite extends Sprite {
    private static Log log = new Log();
    private static int numInstances = 0;
    private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();

    private char type;
    public boolean isSelected;

    // CONSTRUCTOR

    public MenuSprite(double x, double y, char type) {
        super(x, y);
        numInstances++;
        this.type = type;
        isSelected = false;
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

            URL imageURL = MenuSprite.class.getClassLoader().getResource("graphics/MenuTextSprites.png");
            log.d("MenuSprite loading " + imageURL);
            Image menuSpriteSheet = tk.getImage(imageURL);
            menuSpriteSheet = ColorFilters.setTransparentColor(menuSpriteSheet, new Color(0xFF00FF));

            // setup focus points for the images

            // store them into our hashtables
            int w = 100;
            int h = 16;
            Image img;

            img = ImageBlitter.cropTiled(menuSpriteSheet, 0, 0, w, h);
            imageTable.put("ai1", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 1, 0, w, h);
            imageTable.put("ai2", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 0, 1, w, h);
            imageTable.put("norm1", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 1, 1, w, h);
            imageTable.put("norm2", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 0, 2, w, h);
            imageTable.put("htp1", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 1, 2, w, h);
            imageTable.put("htp2", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 0, 3, w, h);
            imageTable.put("quit1", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 1, 3, w, h);
            imageTable.put("quit2", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 0, 4, w, h);
            imageTable.put("load1", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 1, 4, w, h);
            imageTable.put("load2", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 0, 5, w, h);
            imageTable.put("random1", img);
            imgLoader.addImage(img);

            img = ImageBlitter.cropTiled(menuSpriteSheet, 1, 5, w, h);
            imageTable.put("random2", img);
            imgLoader.addImage(img);

            imageURL = MenuSprite.class.getClassLoader().getResource("graphics/TitleSprite.png");
            log.d("MenuSprite loading " + imageURL);
            img = tk.getImage(imageURL);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imageTable.put("title", img);
            imgLoader.addImage(img);

            log.d("loaded MenuSprite");
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
        if (type == 'a' && !isSelected)
            curImage = imageTable.get("ai1");
        if (type == 'a' && isSelected)
            curImage = imageTable.get("ai2");

        if (type == 'n' && !isSelected)
            curImage = imageTable.get("norm1");
        if (type == 'n' && isSelected)
            curImage = imageTable.get("norm2");

        if (type == 'h' && !isSelected)
            curImage = imageTable.get("htp1");
        if (type == 'h' && isSelected)
            curImage = imageTable.get("htp2");

        if (type == 'q' && !isSelected)
            curImage = imageTable.get("quit1");
        if (type == 'q' && isSelected)
            curImage = imageTable.get("quit2");

        if (type == 't')
            curImage = imageTable.get("title");

        if (type == 'l' && !isSelected)
            curImage = imageTable.get("load1");
        if (type == 'l' && isSelected)
            curImage = imageTable.get("load2");

        if (type == 'r' && !isSelected)
            curImage = imageTable.get("random1");
        if (type == 'r' && isSelected)
            curImage = imageTable.get("random2");


        fx = 0;
        fy = 0;
    }


}