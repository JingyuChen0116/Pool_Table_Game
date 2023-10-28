package PoolGame.strategy;

public class DoubleLifeStrategy extends PocketStrategy {
    /** Creates a new blue strategy. */
    public DoubleLifeStrategy() {
        this.lives = 2;
    }

    public void reset() {
        this.lives = 2;
    }

    public PocketStrategy copy() {
        PocketStrategy strategyCopy = new DoubleLifeStrategy();
        strategyCopy.setLives(this.lives);
        return strategyCopy;
    }
}
