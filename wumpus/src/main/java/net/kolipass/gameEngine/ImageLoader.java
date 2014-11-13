package net.kolipass.gameEngine;

import java.awt.*;

public class ImageLoader {
    private MediaTracker mt;
    private int nextId;
    private Component parent;

    public ImageLoader(Component parent) {
        this.parent = parent;
        //	mt = new MediaTracker(this.parent);
        this.reset();
    }

    public void reset() {
        mt = new MediaTracker(this.parent);
        nextId = 0;
    }

    public void addImage(Image img) {
        mt.addImage(img, this.nextId);
        this.nextId++;
    }

    public void waitForAll() {
        try {
            //	log.d("waiting for images to load " + mt);
            mt.waitForAll();    // wait for the filtered image to load before drawing anything.
        } catch (InterruptedException e) {
            //	log.d("Interrupted while waiting for images to load");
            Thread.currentThread().interrupt();
        }
        //	log.d("finished waiting for images to load");
        reset();
    }


}

