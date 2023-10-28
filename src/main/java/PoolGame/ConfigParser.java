package PoolGame;

import PoolGame.config.*;

/**
 * Process the configuration file into a game manager.
 */
public class ConfigParser {
    private static ReaderFactory tableFactory = new TableReaderFactory();
    private static ReaderFactory ballFactory = new BallReaderFactory();
    private static ReaderFactory pocketFactory = new PocketReaderFactory();

    private static Reader tableReader = tableFactory.buildReader();
    private static Reader ballReader = ballFactory.buildReader();
    private static Reader pocketReader = pocketFactory.buildReader();

    /**
     * Parse the table, ball, pocket section using corresponding readers.
     *
     * @param configPath configuration file path
     * @param gameManager game manager
     */
    public static void parse(String configPath, GameManager gameManager) {
        tableReader.parse(configPath, gameManager);
        ballReader.parse(configPath, gameManager);
        pocketReader.parse(configPath, gameManager);
    }
}
