package main.java.net.kolipass.gameEngine;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


public abstract class Sprite {
    public Component parent; // the parent component that this Sprite is drawn to.

    public double x, y, z;    // x, y, and (if necessary) z coordinates for the Sprite
    protected double fx, fy, fz; // current focal coordinates for the Sprite used by its current Image.
    public double width, height, depth;    // width, height, and (if necessary) depth for the Sprite

    protected double scaleX, scaleY;    // used for resize transformations
    protected double rotation;        // used for rotational transformations.
    protected AffineTransform transform; // the current transform matrix
    protected boolean transformChanged; // lets our rendering method know whether we need to update transform

    protected double r; // distance used for circular/spherical hit detection
    protected double boxScale; // scale of hitbox

    protected int collisionType; // -1 = box/fine collision only, 0 = circle, 1 = line segment

    public boolean isDestroyed;    // used to let any SpriteLayers that have a reference to this know that it has been destroyed.
    public boolean isVisible;

    protected Image curImage; // The current image displayed by this Sprite's animation before transforms.
    protected Image curImageTrans; // The current image displayed by this Sprite's animation after transforms.
    protected AffineTransform colorTransform;
    protected boolean colorTransformChanged;
    protected double semiTransparency; // semiTransparency percentage. 0.0 = 100% opaque, 1.0 = 100% transparent (invisible).

    //static int numInstances; // Every child of this class should have a static counter to keep track of the number of instances of it exist.
    //static Hashtable<String,Image> imageTable;
    //static Hashtable<String,Point> focusTable;


    // CONSTRUCTORS


    public Sprite() {
        this(0, 0);
    }

    public Sprite(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        width = 0;
        height = 0;
        depth = 0;

        scaleX = 1.0;
        scaleY = 1.0;
        boxScale = 1.0;
        rotation = 0;
        transform = new AffineTransform(); // identity matrix reflects no scale or rotation.
        transformChanged = false;
        colorTransformChanged = false;
        semiTransparency = 0.0; // opaque

        isVisible = true;
        isDestroyed = false;
    }

    // DATA JUGGLING METHODS

    public void setParent(Component comp) {
        parent = comp;
    }

    // EXPECTED STATIC METHODS


    // public static void loadImages()
    // loads this Spite's base images and their focal coordinates from their external files to hashtables keyed by image name

    // public static void clean()
    // cleans up all references and static members of this Sprite when its level is unloaded.


    // NECESSARY METHODS THAT USE EXPECTED STATIC FIELDS

	/*

	public void destroy()
	{
		numInstances++;
		this.isDestroyed = true;
	}
	*/

    // RENDERING METHODS

    /**
     * render(Graphics2D g)
     * Renders this sprite.
     * Preconditions: g is the Graphics context used by the parent component of this Sprite.
     * Postconditions: This sprite is rendered unless it is invisible or is flagged as destroyed. Returns false if it is flagged as destroyed
     * to let its caller know to get rid of its reference to this Sprite. Otherwise returns true.
     */

    public boolean render(Graphics2D g) {
        if (isDestroyed)    // flag the thing trying to render this that this is marked for deletion and the caller ought to delete its reference to this.
            return false;

        if (semiTransparency == 1.0 || !isVisible)    // if the Sprite is invisible, we don't need to draw its image.
            return true;

        if (transformChanged) // update our image transform so that it's ready for rendering
            updateTransform();

        AffineTransform oldTrans = g.getTransform(); // save the Graphics context's original transform.

        AffineTransform curTrans = g.getTransform();
        curTrans.translate(this.x, this.y);
        curTrans.concatenate(this.transform);
        curTrans.translate(0 - fx, 0 - fy); // offset image using focus point.
        g.setTransform(curTrans);

        this.draw(g);

        g.setTransform(oldTrans);    // restore the Graphics context's original transform.

        return true;
    }

    /**
     * draw(Graphics2D g)
     * Helper method draws the image of this Sprite after transformations are readied. Called by render(Graphics2D g).
     * If you need to do something fancy while rendering this sprite's image, override this method.
     * Preconditions: g is the Graphics2D object passed in by render(Graphcis2D g).
     * Postconditions: this sprite is drawn, applying the transformations readied in render(Graphics2D g).
     */

    protected void draw(Graphics2D g) {
        g.drawImage(this.curImage, null, null);
    }

    /**
     * animate()
     * Prepares the current animation frame and then prepares the sprite for computing its next frame of animation. Called by render(Graphics2D g).
     * Preconditions: ImageLoader is used to load the next image of the Sprite if it is a transformed version of one of its base images.
     * Postconditions: curImage is set to the image of this Sprite's current animation frame
     * and necessary values for computing the next frame of animation are prepared.
     */

    protected void animate(ImageLoader il) {
        if (this.isDestroyed)
            return;

        // compute the base image from current animation math

        // do next animation math

        // If a filter is applied to the computed base image, add it to the ImageLoader
        //	so that the game will be forced to wait for the image to load before rendering.
        // This can be done by doing this:

        //	il.addImage(this.curImage);
    }

    // COLOR TRANSFORM METHODS

    /**
     * setSemiTransparency(double alpha)
     * Sets a uniform semi-transparency for this Sprite.
     * Preconditions: alpha is a value between 0.0 and 1.0 where 0.0 indicates 100% opaque and 1.0 indicates 100% transparent.
     * Postconditions: The nontransparent pixels in the Sprite's images become alpha percent semi-transparent when drawn. The original images
     * in our imageTable are unchanged.
     */

    public void setSemiTransparency(double alpha) {
        if (parent == null) // the Sprite must have a specific parent component to load its filtered image properly. See render(Graphics2D g).
            semiTransparency = 1.0;

        if (alpha > 1.0)
            alpha = 1.0;
        if (alpha < 0.0)
            alpha = 0.0;

        colorTransformChanged = true;
        semiTransparency = alpha;
    }

    /**
     * getSemiTransparency()
     * Returns the current semitransparency percentage for this sprite.
     */

    public double getSemiTransparency() {
        return semiTransparency;
    }

    // IMAGE TRANSFORM METHODS


    public void scale(double x, double y) {
        this.scaleX = x;
        this.scaleY = y;
        this.transformChanged = true;
    }

    public void rotate(double degrees) {
        this.rotation = degrees;
        this.transformChanged = true;
    }

    /**
     * updateTransform()
     * Helper method updates the transform matrix for this shape, excluding translation for its position.
     * Preconditions: none.
     * Postconditions: The AffineTransform for this Sprite's image is updated.
     */

    protected void updateTransform() {
        transform = new AffineTransform(); // ID matrix
        transform.scale(scaleX, scaleY);
        transform.rotate(rotation / 360 * Math.PI * 2); // converted from degrees to radians
    }


    // COLLISION METHODS


    /**
     * boxCollision(Sprite other)
     * Checks for a collision between this Sprite and another Sprite by seeing if their images' bounding boxes overlap.
     * Preconditions: other is the sprite we are checking for a collision with this Sprite.
     * Postconditions: returns true if the Sprites' bounding boxes overlap. Returns false otherwise.
     */

    public boolean boxCollision(Sprite other) {
        if (this.transformChanged)
            this.updateTransform();
        if (other.transformChanged)
            other.updateTransform();

        // set up the Points describing this's bounding box

        int x1 = (int) (this.x - this.fx * this.boxScale + 0.5);
        int w1 = (int) (this.width * this.boxScale + 0.5);
        int y1 = (int) (this.y - this.fy * this.boxScale + 0.5);
        int h1 = (int) (this.height * this.boxScale + 0.5);

        AffineTransform thisTrans = AffineTransform.getTranslateInstance(this.x, this.y);
        thisTrans.concatenate(this.transform);
        thisTrans.translate(0 - this.x, 0 - this.y);

        Point2D[] tPoints = new Point2D[4];
        tPoints[0] = thisTrans.transform(new Point(x1, y1), tPoints[0]);
        tPoints[1] = thisTrans.transform(new Point(x1 + w1, y1), tPoints[1]);
        tPoints[2] = thisTrans.transform(new Point(x1 + w1, y1 + h1), tPoints[2]);
        tPoints[3] = thisTrans.transform(new Point(x1, y1 + h1), tPoints[3]);

        // set up the Points describing other's bounding box

        int x2 = (int) (other.x - other.fx * other.boxScale + 0.5);
        int w2 = (int) (other.width * other.boxScale + 0.5);
        int y2 = (int) (other.y - other.fy * other.boxScale + 0.5);
        int h2 = (int) (other.height * other.boxScale + 0.5);

        AffineTransform otherTrans = AffineTransform.getTranslateInstance(other.x, other.y);
        otherTrans.concatenate(other.transform);
        otherTrans.translate(0 - other.x, 0 - other.y);


        Point2D[] oPoints = new Point2D[4];
        oPoints[0] = otherTrans.transform(new Point(x2, y2), oPoints[0]);
        oPoints[1] = otherTrans.transform(new Point(x2 + w2, y2), oPoints[1]);
        oPoints[2] = otherTrans.transform(new Point(x2 + w2, y2 + h2), oPoints[2]);
        oPoints[3] = otherTrans.transform(new Point(x2, y2 + h2), oPoints[3]);

        // use axis of separation theorem to test for collisions.

        //log.d("AoST this vs other");
        if (axisOfSeparation(tPoints, oPoints)) {
            //log.d("AoST other vs this");
            return axisOfSeparation(oPoints, tPoints);
        } else
            return false;
    }

    /**
     * axisOfSeparation(Point[] tPoitns, Point[] oPoints)
     * Helper method for box collision that checks for collisions using the Axis of Separation Theorem.
     * Preconditions: tPoints is the array of points of the shape whose segments we are using as axises.
     * oPoints is the array of points of the shape whose points we are testing against the axises of the first shape.
     * The shapes described by tPoints and oPoints are assumed to be convex.
     * Postconditions: returns false if a collision between the shapes is impossible. Otherwise returns true.
     */

    private boolean axisOfSeparation(Point2D[] tPoints, Point2D[] oPoints) {
        // loop over this Sprite's bounding box's segments.
        // Vectors describing the segments travel clockwise around the bounding box.

        for (int i = 0; i < 4; i++) {
            Point2D q = tPoints[i];
            Point2D r = tPoints[(i + 1) % 4];

            //log.d("Comparing segment " + q + "->" + r);

            // vector b pointing in the direction of this segment from q to r.

            int bx = (int) (r.getX() - q.getX());
            int by = (int) (r.getY() - q.getY());

            // vector n is the normal vector to b and a (0,0,1) defined by a x b.

            int nx = by;
            int ny = 0 - bx;

            // compute denominator for alpha test

            int denom = bx * ny - by * nx;
            if (denom == 0)
                denom = 1;

            // assume no collision until proved that a collision is still possible.

            boolean impossible = true;

            // loop through each point in the other polygon to check whether it is above or below the current segment.

            for (int j = 0; j < 4; j++) {
                Point2D p = oPoints[j];
                double alpha = (bx * (p.getY() - q.getY()) + by * (q.getX() - p.getX())) / denom;

                //log.d("...to Point " + p);

                // alpha < 0 if other's point is below current axis of separation. This means that it is probable that there is a collision.

                if (alpha < 0) {
                    impossible = false;
                    break;
                }
            }

            // impossible if all of other's points are above current axis of separation. This means no collision is possible.

            if (impossible)
                return false;
        }
        return true;
    }


    //

}

