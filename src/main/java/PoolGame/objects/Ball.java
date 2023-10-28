package PoolGame.objects;

import PoolGame.Config;
import PoolGame.PoolPaint;
import PoolGame.strategy.PocketStrategy;
import javafx.scene.paint.Paint;

/** Holds information for all ball-related objects. */
public class Ball {

    private PoolPaint colour;
    private double xPosition;
    private double yPosition;
    private double startX;
    private double startY;
    private double xVelocity;
    private double yVelocity;
    private double mass;
    private double radius;
    private int score;
    private boolean isCue;
    private boolean isActive;
    private PocketStrategy strategy;

    private final double MAXVEL = 30;
    private final double NONZEROMINVEL = 0.01;

    public Ball(PoolPaint colour, double xPosition, double yPosition, double xVelocity, double yVelocity, double mass,
            int score, boolean isCue, PocketStrategy strategy) {
        this.colour = colour;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.startX = xPosition;
        this.startY = yPosition;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.mass = mass;
        this.radius = 10;
        this.score = score;
        this.isCue = isCue;
        this.isActive = true;
        this.strategy = strategy;
    }

    /**
     * Updates ball position per tick.
     */
    public void tick() {
        xPosition += xVelocity;
        yPosition += yVelocity;
    }

    /**
     * Resets ball position, velocity, and activity.
     */
    public void reset() {
        resetPosition();
        isActive = true;
        strategy.reset();
    }

    /**
     * Resets ball position and velocity.
     */
    public void resetPosition() {
        xPosition = startX;
        yPosition = startY;
        xVelocity = 0;
        yVelocity = 0;
    }

    /**
     * Removes ball from play.
     * 
     * @return true if ball is successfully removed
     */
    public boolean remove() {
        if (strategy.remove()) {
            isActive = false;
            return true;
        } else {
            resetPosition();
            return false;
        }
    }

    /**
     * Set ball to be active or not.
     *
     * @param isActive
     */
    public void setActivity(boolean isActive) { this.isActive = isActive; }

    /**
     * Sets x-axis velocity of ball.
     * 
     * @param xVelocity of ball.
     */
    public void setxVel(double xVelocity) {
        if (xVelocity > MAXVEL) {
            this.xVelocity = MAXVEL;
        } else if (xVelocity < -MAXVEL) {
            this.xVelocity = -MAXVEL;
        } else {
            this.xVelocity = xVelocity;
        }

        if (Math.abs(xVelocity) < NONZEROMINVEL) {
            this.xVelocity = 0.0;
        }
    }

    /**
     * Sets y-axis velocity of ball.
     * 
     * @param yVelocity of ball.
     */
    public void setyVel(double yVelocity) {
        if (yVelocity > MAXVEL) {
            this.yVelocity = MAXVEL;
        } else if (yVelocity < -MAXVEL) {
            this.yVelocity = -MAXVEL;
        } else {
            this.yVelocity = yVelocity;
        }

        if (Math.abs(yVelocity) < NONZEROMINVEL) {
            this.yVelocity = 0.0;
        }
    }

    /**
     * Sets x-axis position of ball.
     * 
     * @param xPosition of ball.
     */
    public void setxPos(double xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Sets y-axis position of ball.
     * 
     * @param yPosition of ball.
     */
    public void setyPos(double yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Sets x start position of ball.
     *
     * @param startX of ball.
     */
    public void setxStart(double startX) {
        this.startX = startX;
    }

    /**
     * Sets y start position of ball.
     *
     * @param startY of ball.
     */
    public void setyStart(double startY) {
        this.startY = startY;
    }

    /**
     * Getter method for radius of ball.
     * 
     * @return radius length.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Getter method for x-position of ball.
     * 
     * @return x position.
     */
    public double getxPos() {
        return xPosition + Config.getTableBuffer();
    }

    /**
     * Getter method for y-position of ball.
     * 
     * @return y position.
     */
    public double getyPos() {
        return yPosition + Config.getTableBuffer();
    }

    /**
     * Getter method for starting x-position of ball.
     * 
     * @return starting x position.
     */
    public double getStartXPos() {
        return startX;
    }

    /**
     * Getter method for starting y-position of ball.
     * 
     * @return starting y position.
     */
    public double getStartYPos() {
        return startY;
    }

    /**
     * Getter method for starting mass of ball.
     * 
     * @return mass.
     */
    public double getMass() {
        return mass;
    }

    /**
     * Getter method for colour of ball.
     * 
     * @return colour.
     */
    public PoolPaint getColour() {
        return this.colour;
    }

    /**
     * Getter method for x-axis velocity of ball.
     * 
     * @return x velocity.
     */
    public double getxVel() {
        return xVelocity;
    }

    /**
     * Getter method for y-axis velocity of ball.
     * 
     * @return y velocity.
     */
    public double getyVel() {
        return yVelocity;
    }

    /**
     * Getter method for score of ball.
     *
     * @return score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter method for whether ball is cue ball.
     * 
     * @return true if ball is cue ball.
     */
    public boolean isCue() {
        return isCue;
    }

    /**
     * Getter method for whether ball is active.
     * 
     * @return true if ball is active.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Make a copy of current ball.
     *
     * @return Ball
     */
    public Ball copy() {
        Ball ballCopy = new Ball(this.colour, this.xPosition, this.yPosition, this.xVelocity, this.yVelocity, this.mass, this.score, this.isCue, this.strategy.copy());
        ballCopy.setActivity(this.isActive);
        return ballCopy;
    }

}
