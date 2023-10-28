package PoolGame.config;

/** Builds PocketReader. */
public class PocketReaderFactory implements ReaderFactory {

    /**
     * Builds a BallReader.
     *
     * @return ball reader.
     */
    public Reader buildReader() { return new PocketReader(); }
}
