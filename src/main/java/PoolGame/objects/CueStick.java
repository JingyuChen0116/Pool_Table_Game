package PoolGame.objects;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/** Holds properties of the cue stick object. */
public class CueStick {
    private ImageView cueStickImage;
    private double margin;
    private double height;
    private double width;
    private boolean visible;
    private double xPressed;
    private double yPressed;
    private double angle;
    private double velocity;
    private double distanceDragged;

    public CueStick(double width) {
        Image image = new Image("CueStick.png");
        this.width = width;
        this.height = image.getHeight() * (width/image.getWidth());
        this.cueStickImage = new ImageView(image);
        this.cueStickImage.setFitWidth(this.width);
        this.cueStickImage.setFitHeight(this.height);

        this.cueStickImage.setVisible(false);
        this.visible = false;
    }

    /**
     * Set the cue stick to be visible.
     *
     * @param xPos Current x position of white cue ball.
     * @param yPos Current y position of white cue ball.
     */
    public void visible(double xPos, double yPos) {
        this.cueStickImage.getTransforms().clear();
        this.cueStickImage.setVisible(true);
        this.visible = true;
        this.cueStickImage.setX(xPos - (this.width + 20) + margin);
        this.cueStickImage.setY(yPos - this.height/2 + margin);
    }

    /**
     * Set the cue stick to be invisible.
     */
    public void invisible() {
        this.cueStickImage.setVisible(false);
        this.visible = false;
    }

    /**
     * Rotate the cue stick with pivot.
     *
     * @param angle The angle of rotation.
     * @param xPos The x coordinate of pivot.
     * @param yPos The y coordinate of pivot.
     */
    public void rotate(double angle, double xPos, double yPos) {
        this.cueStickImage.getTransforms().clear();
        this.cueStickImage.getTransforms().add(new Rotate(angle, xPos, yPos));
        this.angle = angle;
    }

    /**
     * Record the starting position for dragging mouse
     *
     * @param x The x coordinate of starting position.
     * @param y The y coordinate of starting position.
     */
    public void press(double x, double y) {
        this.xPressed = x - this.margin;
        this.yPressed = y - this.margin;
    }

    /**
     * Calculate the velocity according to drag distance
     *
     * @param distance The distance of drag.
     */
    public void calVel(double distance) {
        this.distanceDragged = distance;
        this.velocity = distance / 8.0;
        if (this.velocity > 10.0) {
            this.velocity = 10.0;
        }
    }

    /*
     * Drag the cue stick.
     */
    public void drag() {
        ( (Translate) this.cueStickImage.getTransforms().get(1) ).setX(-1 * this.distanceDragged);
    }

    /**
     * Get the image of cue stick.
     *
     * @return cueStickImage
     */
    public ImageView getCueStickImage() {
        return this.cueStickImage;
    }

    /**
     * Set the margin of cue stick.
     *
     * @param margin
     */
    public void setMargin(double margin) {
        this.margin = margin;
    }

    /**
     * Get the visibility of cue stick.
     *
     * @return visible
     */
    public boolean isVisible() { return this.visible; }

    /**
     * Get the angle of rotation
     *
     * @return angle
     */
    public double getAngle() { return this.angle; }

    /**
     * Get the x coordinate of starting dragging position.
     *
     * @return xPressed
     */
    public double getxPressed() { return this.xPressed; }

    /**
     * Get the y coordinate of starting dragging position.
     *
     * @return yPressed
     */
    public double getyPressed() { return this.yPressed; }

    /**
     * Get the distance of the cue stick being dragged.
     *
     * @return distanceDragged
     */
    public double getDistanceDragged() { return this.distanceDragged; }

    /**
     * Get the velocity of hit.
     *
     * @return velocity
     */
    public double getVelocity() { return this.velocity; }

}
