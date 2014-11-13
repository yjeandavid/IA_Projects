package main.java.net.kolipass;

import main.java.net.kolipass.gameEngine.ColorFilters;
import main.java.net.kolipass.gameEngine.ImageBlitter;
import main.java.net.kolipass.gameEngine.ImageLoader;
import main.java.net.kolipass.gameEngine.Sprite;

import java.awt.*;
import java.net.URL;
import java.util.Hashtable;

public class HUDSprite extends Sprite {
    private static int numInstances = 0;
    private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
    private static Log log = new Log();
    private char type; // p bgPanel g hasGold f hasFood a hasArrow d Died n NoGold v Victory
    public boolean isSelected;

    // CONSTRUCTOR

    public HUDSprite(double x, double y, char type) {
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

            URL imageURL = HUDSprite.class.getClassLoader().getResource("main/resources/graphics/hudPanel.png");
            log.d("HUDSprite loading " + imageURL);
            Image hudPanel = tk.getImage(imageURL);

            imageURL = HUDSprite.class.getClassLoader().getResource("main/resources/graphics/HudIconSprites.png");
            log.d("HUDSprite loading hudIconSprites.png : " + imageURL);
            Image spriteSheet = tk.getImage(imageURL);

            imageURL = HUDSprite.class.getClassLoader().getResource("main/resources/graphics/finishSprites.png");
            log.d("HUDSprite loading " + imageURL);
            Image winSheet = tk.getImage(imageURL);
            winSheet = ColorFilters.setTransparentColor(winSheet, new Color(0xFF00FF));

            // store them into our hashtables
            int w = 48;
            int h = 48;


            Image img;

            img = ImageBlitter.cropTiled(spriteSheet, 0, 0, w, h);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("hg1", img);

            img = ImageBlitter.cropTiled(spriteSheet, 1, 0, w, h);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("hg2", img);

            img = ImageBlitter.cropTiled(spriteSheet, 0, 1, w, h);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("hf1", img);

            img = ImageBlitter.cropTiled(spriteSheet, 1, 1, w, h);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("hf2", img);

            img = ImageBlitter.cropTiled(spriteSheet, 0, 2, w, h);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("ha1", img);

            img = ImageBlitter.cropTiled(spriteSheet, 1, 2, w, h);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("ha2", img);

            imageTable.put("panel", hudPanel);

            w = 200;
            h = 48;

            img = ImageBlitter.cropTiled(winSheet, 0, 0, w, h);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("win", img);

            img = ImageBlitter.cropTiled(winSheet, 0, 1, w, h);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("lose", img);

            img = ImageBlitter.cropTiled(winSheet, 0, 2, w, h);
            img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
            imgLoader.addImage(img);
            imageTable.put("die", img);


            log.d("loaded HUDSprite");
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
        if (type == 'g' && !isSelected)
            curImage = imageTable.get("hg1");
        if (type == 'g' && isSelected)
            curImage = imageTable.get("hg2");

        if (type == 'f' && !isSelected)
            curImage = imageTable.get("hf1");
        if (type == 'f' && isSelected)
            curImage = imageTable.get("hf2");

        if (type == 'a' && !isSelected)
            curImage = imageTable.get("ha1");
        if (type == 'a' && isSelected)
            curImage = imageTable.get("ha2");

        if (type == 'p')
            curImage = imageTable.get("panel");

        if (type == 'v')
            curImage = imageTable.get("win");
        if (type == 'n')
            curImage = imageTable.get("lose");
        if (type == 'd')
            curImage = imageTable.get("die");


        fx = 0;
        fy = 0;
    }


}