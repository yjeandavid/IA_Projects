package main.java.net.kolipass;

import main.java.net.kolipass.gameEngine.ColorFilters;
import main.java.net.kolipass.gameEngine.ImageBlitter;
import main.java.net.kolipass.gameEngine.ImageLoader;
import main.java.net.kolipass.gameEngine.Sprite;

import java.awt.*;
import java.net.URL;
import java.util.Hashtable;

public class CaveNodeSprite extends Sprite {
    private static Log log = new Log();
    private static int numInstances = 0;
    private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();

    private char type; //F floor O obstacle P pit W wall
    public int width, height; // floor only

    public boolean hasBottomWall;
    public boolean hasTopWall;
    public boolean hasRightWall;
    public boolean hasLeftWall;

    // CONSTRUCTOR

    public CaveNodeSprite(double x, double y, char type) {
        super(x, y);
        numInstances++;
        this.type = type;

        hasBottomWall = false;
        hasTopWall = false;
        hasRightWall = false;
        hasLeftWall = false;
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


            imageURL = CaveNodeSprite.class.getClassLoader().getResource("main/resources/graphics/CaveFloorSprite.png");
            log.d("CaveNodeSprite loading " + imageURL);
            Image floorImg = tk.getImage(imageURL);


            imageURL = CaveNodeSprite.class.getClassLoader().getResource("main/resources/graphics/BGColor.png");
            log.d("CaveNodeSprite loading " + imageURL);
            Image wallImg = tk.getImage(imageURL);


            imageURL = CaveNodeSprite.class.getClassLoader().getResource("main/resources/graphics/PitSprite.png");
            log.d("CaveNodeSprite loading " + imageURL);
            Image pitImg = tk.getImage(imageURL);
            pitImg = ColorFilters.setTransparentColor(pitImg, new Color(0xFF00FF));

            imageURL = CaveNodeSprite.class.getClassLoader().getResource("main/resources/graphics/WallSprites.png");
            log.d("CaveNodeSprite loading " + imageURL);
            Image wallsImg = tk.getImage(imageURL);
            wallsImg = ColorFilters.setTransparentColor(wallsImg, new Color(0xFF00FF));

            // setup focus points for the images

            // store them into our hashtables

            imageTable.put("wall", wallImg);
            imageTable.put("pit", pitImg);
            imageTable.put("floor", floorImg);

            imgLoader.addImage(wallImg);
            imgLoader.addImage(pitImg);
            imgLoader.addImage(floorImg);

            for (int i = 0; i < 16; i++) {
                int w = 24;
                int h = 24;
                Image img;

                img = ImageBlitter.cropTiled(wallsImg, i, 0, w, h);
                imageTable.put("walls" + i, img); // walls0 - walls15
                imgLoader.addImage(img);
            }

            log.d("loaded CaveNodeSprite");

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
    }

    protected void draw(Graphics2D g) {
        if (type == 'O')
            g.drawImage(this.imageTable.get("wall"), null, null);

        if (type == 'P')
            g.drawImage(this.imageTable.get("pit"), null, null);

        if (type == 'F') // tiled image
        {
            int i, j;
            for (i = 0; i < this.height; i += 32) {
                for (j = 0; j < this.width; j += 32) {
                    int xCut, yCut;
                    if (j + 32 > this.width)
                        xCut = this.width % 32;
                    else
                        xCut = 32;
                    if (i + 32 > this.height)
                        yCut = this.height % 32;
                    else
                        yCut = 32;

                    g.drawImage(this.imageTable.get("floor"), j, i, j + xCut, i + yCut,
                            0, 0, xCut, yCut, null);
                }
            }
        }

        if (type == 'W') {
            int wallNum = 0;
            if (this.hasBottomWall)
                wallNum += 1;
            if (this.hasTopWall)
                wallNum += 2;
            if (this.hasRightWall)
                wallNum += 4;
            if (this.hasLeftWall)
                wallNum += 8;

            g.drawImage(this.imageTable.get("walls" + wallNum), null, null);
        }

    }


}