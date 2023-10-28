package PoolGame.objects;

/** Interface for building pool pockets. */
public interface PocketBuilder {
    /**
     * Sets the x position of the pocket.
     *
     * @param xPosition of pocket.
     */
    public void setxPos(double xPosition);

    /**
     * Sets the y position of the pocket.
     *
     * @param yPosition of pocket.
     */
    public void setyPos(double yPosition);

    /**
     * Sets the radius of the pocket.
     *
     * @param radius of pocket.
     */
    public void setRadius(double radius);
}
