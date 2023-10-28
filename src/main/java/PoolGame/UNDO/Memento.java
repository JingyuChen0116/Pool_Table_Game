package PoolGame.UNDO;

import PoolGame.Time;
import PoolGame.objects.Ball;
import java.util.ArrayList;

/**
 * Stores an internal state of game.
 */
public class Memento {

    private int score;
    private String difficulty;
    private Time time;
    private ArrayList<Ball> ballsCopy;
    private boolean winFlag;

    public Memento(int score, String difficulty, Time time, ArrayList<Ball> balls, boolean winFlag) {
        this.score = score;
        this.difficulty = difficulty;
        this.time = time.copy();
        this.ballsCopy = new ArrayList<>();
        for (Ball ball: balls) {
            Ball ballCopy = ball.copy();
            ballCopy.setxStart(ball.getStartXPos());
            ballCopy.setyStart(ball.getStartYPos());
            this.ballsCopy.add(ballCopy);
        }
        this.winFlag = winFlag;
    }

    /**
     * @return score
     */
    public int getScore() { return this.score; }

    /**
     * @return difficulty
     */
    public String getDifficulty() { return this.difficulty; }

    /**
     * @return time
     */
    public Time getTimer() { return this.time; }

    /**
     * @return ballsCopy
     */
    public ArrayList<Ball> getBallsCopy() {
        return this.ballsCopy;
    }

    /**
     * @return winFlag
     */
    public boolean getWinFlag() { return this.winFlag; }

}
