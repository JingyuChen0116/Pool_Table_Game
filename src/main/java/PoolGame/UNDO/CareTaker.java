package PoolGame.UNDO;

/**
 * Keeps the Memento for game manager.
 */
public class CareTaker {
    private Memento memento;

    /**
     * Getter method for current memento.
     *
     * @return memento
     */
    public Memento getMemento() { return this.memento; }

    /**
     * Sets the memento.
     *
     * @param memento
     */
    public void setMemento(Memento memento) { this.memento = memento; }

}
