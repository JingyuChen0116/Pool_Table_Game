package PoolGame.objects;

import PoolGame.PoolPaint;
import PoolGame.strategy.PocketStrategy;
import PoolGame.strategy.SingleLifeStrategy;
import PoolGame.strategy.DoubleLifeStrategy;
import PoolGame.strategy.TripleLifeStrategy;
import javafx.scene.paint.Paint;

/** Builds pool balls. */
public class PoolBallBuilder implements BallBuilder {
    // Required Parameters
    private String colour;
    private double xPosition;
    private double yPosition;
    private double xVelocity;
    private double yVelocity;
    private double mass;
    private int score;

    // Variable Parameters
    private boolean isCue = false;
    public PocketStrategy strategy;

    @Override
    public void setColour(String colour) {
        this.colour = colour;
    };

    @Override
    public void setxPos(double xPosition) {
        this.xPosition = xPosition;
    };

    @Override
    public void setyPos(double yPosition) {
        this.yPosition = yPosition;
    };

    @Override
    public void setxVel(double xVelocity) {
        this.xVelocity = xVelocity;
    };

    @Override
    public void setyVel(double yVelocity) {
        this.yVelocity = yVelocity;
    };

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    };

    /**
     * Builds the ball.
     * 
     * @return ball
     */
    public Ball build() {
        if (colour.equals("white")) {
            isCue = true;
            strategy = new SingleLifeStrategy();
        } else if (colour.equals("blue")) {
            strategy = new DoubleLifeStrategy();
            score = 5;
        } else if (colour.equals("green")) {
            strategy = new DoubleLifeStrategy();
            score = 3;
        } else if (colour.equals("purple")) {
            strategy = new DoubleLifeStrategy();
            score = 6;
        } else if (colour.equals("red")) {
            strategy = new SingleLifeStrategy();
            score = 1;
        } else if (colour.equals("yellow")) {
            strategy = new SingleLifeStrategy();
            score = 2;
        } else if (colour.equals("orange")) {
            strategy = new SingleLifeStrategy();
            score = 8;
        } else if (colour.equals("black")) {
            strategy = new TripleLifeStrategy();
            score = 7;
        } else if (colour.equals("brown")) {
            strategy = new TripleLifeStrategy();
            score = 4;
        }

        PoolPaint poolPaint = new PoolPaint(colour);
        return new Ball(poolPaint, xPosition, yPosition, xVelocity, yVelocity, mass, score, isCue, strategy);
    }
}
