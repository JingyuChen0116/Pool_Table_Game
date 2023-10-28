package PoolGame.strategy;

public class SingleLifeStrategy extends PocketStrategy {
    /**
     * Creates a new ball strategy.
     */
    public SingleLifeStrategy() {
        this.lives = 1;
    }

    public void reset() {
        this.lives = 1;
    }

    public PocketStrategy copy() {
        PocketStrategy strategyCopy = new SingleLifeStrategy();
        strategyCopy.setLives(this.lives);
        return strategyCopy;
    }
}
