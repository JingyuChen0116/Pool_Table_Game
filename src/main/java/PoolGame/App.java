package PoolGame;

import javafx.application.Application;
import javafx.stage.Stage;

/** Main application entry point. */
public class App extends Application {

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    /**
     * Starts the application.
     *
     * @param primaryStage The primary stage for the application.
     */
    public void start(Stage primaryStage) {
        // START GAME MANAGER
        GameManager gameManager = new GameManager();
        gameManager.buildGameManager();
        primaryStage.setTitle("Pool");
        primaryStage.setScene(gameManager.getScene());
        primaryStage.show();
        gameManager.run();
    }
}
