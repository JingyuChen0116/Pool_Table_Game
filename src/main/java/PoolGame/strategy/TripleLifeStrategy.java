package PoolGame.strategy;

public class TripleLifeStrategy extends PocketStrategy {
    /**
     * Creates a new ball strategy.
     */
    public TripleLifeStrategy() {
        this.lives = 3;
    }

    public void reset() {
        this.lives = 3;
    }

    public PocketStrategy copy() {
        PocketStrategy strategyCopy = new TripleLifeStrategy();
        strategyCopy.setLives(this.lives);
        return strategyCopy;
    }
}
