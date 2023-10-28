package PoolGame;

import PoolGame.UNDO.*;
import PoolGame.objects.*;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.paint.Paint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;

import javafx.scene.text.FontWeight;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 * Controls the game interface; drawing objects, handling logic and collisions.
 */
public class GameManager {
    private Table table;
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private CueStick cueStick;
    private boolean winFlag = false;
    private int score;
    private int totalScore;
    private Label scoreLabel;
    private Time time;
    private Label timeLabel;
    private Button UNDOButton;
    private Label cheatLabel;
    private ArrayList<Button> cheatButtons = new ArrayList<>();
    private CareTaker stateKeeper;
    private String difficulty;
    private Label difficultyLabel;
    private ArrayList<Button> difficultyButtons = new ArrayList<>();

    private final double TABLEBUFFER = Config.getTableBuffer();
    private final double TABLEEDGE = Config.getTableEdge();
    private final double SIDESPACE = 300;

    private Pane pane;
    private Scene scene;
    private GraphicsContext gc;

    /**
     * Initialises timeline and cycle count.
     */
    public void run() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(17),
                    t -> {
                        this.draw();
                        if (!this.winFlag) {
                            time.addFrameNum();
                        }
                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Builds GameManager properties such as initialising pane, canvas,
     * graphicscontext, setting up timer, score, undo and cheat, and
     * setting events related to clicks.
     */
    public void buildGameManager() {
        // Default difficulty level - easy
        this.difficulty = "EASY";
        ConfigParser.parse("src/main/resources/config_easy.json", this);

        this.pane = new Pane();
        //setClickEvents(pane);
        this.scene = new Scene(pane, table.getxLength() + TABLEBUFFER * 2 + SIDESPACE, table.getyLength() + TABLEBUFFER * 2);
        Canvas canvas = new Canvas(table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        this.setUpCueStick(canvas);

        this.setUpTimer();

        this.setUpScore();

        this.setUpUndo();

        this.setUpCheat();

        this.setUpDifficultyLevels();
        this.difficultyLabel.setText("Current difficulty: EASY\n\nSelect your difficulty at any time:");
    }

    /**
     * Set the difficulty level and handle the logic, such as updating
     * the label, score and time, cue stick position etc.
     *
     * @param difficulty
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        this.difficultyLabel.setText("Current difficulty: " + this.difficulty + "\n\nSelect your difficulty at any time:");
        ConfigParser.parse("src/main/resources/config_" + this.difficulty.toLowerCase() + ".json", this);
        this.cueStick.visible(balls.get(0).getxPos(), balls.get(0).getyPos());
        this.setScore(0);
        this.time.reset();
        this.updateCheatButtons();
    }

    /**
     * Set up functionality of selecting difficulty levels, such as
     * setting up labels and buttons.
     */
    public void setUpDifficultyLevels() {
        // set up the label;
        this.difficultyLabel = new Label();
        this.difficultyLabel.setLayoutX(table.getxLength() + TABLEBUFFER * 2 + 20);
        this.difficultyLabel.setLayoutY((table.getyLength() + TABLEBUFFER * 2) / 20);
        this.difficultyLabel.setFont(Font.font("Impact", 20));
        this.pane.getChildren().add(this.difficultyLabel);

        // set up the buttons;
        createDifficultyLevelButton("EASY");
        createDifficultyLevelButton("NORMAL");
        createDifficultyLevelButton("HARD");
    }

    /**
     * Create the button for a difficulty level and save it to levelButtons.
     *
     * @param label The difficulty level
     */
    public void createDifficultyLevelButton(String label) {
        Button button = new Button(label);
        button.setLayoutX(table.getxLength() + TABLEBUFFER * 2 + 30 + difficultyButtons.size() * 90);
        button.setLayoutY((table.getyLength() + TABLEBUFFER * 2) / 5 + 10);
        button.setFont(Font.font("Impact", FontWeight.BOLD, 18));
        this.difficultyButtons.add(button);
        this.pane.getChildren().add(button);
        this.setDifficultyButtonEventHandler(button);
    }

    /**
     * Set up the click on action for a difficulty level button.
     *
     * @param button
     */
    public void setDifficultyButtonEventHandler(Button button) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stateKeeper.setMemento(saveState());
                setDifficulty(button.getText());
            }
        });
    }

    /**
     * Set up score, such as creating label, setting layouts.
     */
    public void setUpScore() {
        this.totalScore = this.calculateTotalScore();
        this.scoreLabel = new Label();
        this.scoreLabel.setLayoutX(table.getxLength() + TABLEBUFFER * 2 + 20);
        this.scoreLabel.setLayoutY((table.getyLength() + TABLEBUFFER * 2) * 2/5);
        this.scoreLabel.setFont(Font.font("Impact", 20));
        this.setScore(0);
        this.pane.getChildren().add(this.scoreLabel);
    }

    /**
     * Calculate total score.
     * @return The total score according to current difficulty level.
     */
    public int calculateTotalScore() {
        int total = 0;
        for (Ball ball: this.balls) {
            total += ball.getScore();
        }
        return total;
    }

    /**
     * Set the score and update the label displaying on the screen.
     * @param score
     */
    private void setScore(int score) {
        this.score = score;
        this.scoreLabel.setText("Score:\t" + this.score);
    }

    /**
     * Set up timer, such as creating label, setting layouts.
     */
    public void setUpTimer() {
        this.time = new Time();
        this.timeLabel = new Label();
        this.timeLabel.setLayoutX(table.getxLength() + TABLEBUFFER * 2 + 20 + 100);
        this.timeLabel.setLayoutY((table.getyLength() + TABLEBUFFER * 2) * 2/5);
        this.timeLabel.setFont(Font.font("Impact", 20));
        this.pane.getChildren().add(this.timeLabel);
    }

    /**
     * Set up cue stick and relevant mouse control.
     *
     * @param canvas
     */
    public void setUpCueStick(Canvas canvas) {
        this.cueStick =new CueStick(this.table.getxLength()/3);
        this.pane.getChildren().add(this.cueStick.getCueStickImage());
        this.setUpMouseControl(canvas);
    }

    /**
     * Set up UNDO functionality, such as creating button and state
     * keeper, set layouts, and click on action.
     */
    public void setUpUndo() {
        this.stateKeeper = new CareTaker();
        this.UNDOButton = new Button("UNDO");
        this.UNDOButton.setLayoutX(table.getxLength() + TABLEBUFFER * 2 + 20);
        this.UNDOButton.setLayoutY((table.getyLength() + TABLEBUFFER * 2) /2);
        this.UNDOButton.setFont(Font.font("Impact", 16));
        this.UNDOButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                recoverState(stateKeeper.getMemento());
            }
        });
        this.pane.getChildren().add(this.UNDOButton);
    }

    /**
     * Set up cheat functionality - set up label and buttons for selecting colours.
     */
    public void setUpCheat() {
        this.cheatLabel = new Label("Cheat:");
        this.cheatLabel.setLayoutX(table.getxLength() + TABLEBUFFER * 2 + 20 + 100);
        this.cheatLabel.setLayoutY((table.getyLength() + TABLEBUFFER * 2) /2 + 5);
        this.cheatLabel.setFont(Font.font("Impact", 18));
        this.pane.getChildren().add(this.cheatLabel);
        this.updateCheatButtons();
    }

    /**
     * Update the colour buttons for cheat according to current active balls.
     */
    public void updateCheatButtons() {
        // remove all the cheat buttons from pane and clear the cheat button arraylist
        this.pane.getChildren().removeAll(this.cheatButtons);
        this.cheatButtons.clear();

        // find the current colours of active balls
        ArrayList<String> currentColours = new ArrayList<>();
        for (Ball ball: this.balls) {
            if (ball.isActive()) {
                String colour = ball.getColour().toString();
                if (!currentColours.contains(colour)) {
                    currentColours.add(colour);
                }
            }
        }
        currentColours.remove("white");

        // create one button for each existing colour
        for (String colour: currentColours) {
            Button button = new Button(colour);
            button.setLayoutX(table.getxLength() + TABLEBUFFER * 2 + 20 + 100 + 55);
            button.setLayoutY((table.getyLength() + TABLEBUFFER * 2) /2 + 5 + this.cheatButtons.size() * 30);
            button.setFont(Font.font("Impact", 12));
            button.setOnAction( new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stateKeeper.setMemento(saveState());
                    removeBalls(colour);
                    updateCheatButtons();
                }
            });
            this.cheatButtons.add(button);
        }

        // add all the cheat buttons to the pane
        this.pane.getChildren().addAll(this.cheatButtons);
    }

    /**
     * Remove the active balls with a specific colour.
     * @param colour
     */
    public void removeBalls(String colour) {
        for (Ball ball: this.balls) {
            if (ball.isActive() && ball.getColour().toString().equals(colour)) {
                ball.setActivity(false);
                this.setScore(this.score + ball.getScore());
            }
        }
    }

    /**
     * Sets the cue stick movements according to mouse actions.
     *
     * @param canvas
     */
    public void setUpMouseControl(Canvas canvas) {

        canvas.setOnMouseMoved(event -> {
            if (this.cueStick.isVisible()) {
                double x = event.getSceneX();
                double y = event.getSceneY();
                double xPos = this.balls.get(0).getxPos();
                double yPos = this.balls.get(0).getyPos();

                double angle = Math.toDegrees(Math.atan((y - yPos) / (x - xPos)));
                if (x > xPos) {
                    angle = 180 + angle;
                }

                this.cueStick.rotate(angle, xPos, yPos);
            }
        });

        this.cueStick.getCueStickImage().setOnMousePressed(event -> {
            if (this.cueStick.isVisible()) {
                this.cueStick.press(event.getSceneX(), event.getSceneY());
                this.cueStick.getCueStickImage().getTransforms().add(new Translate());
//                System.out.println((event.getSceneX()-this.margin) + "  " + (event.getSceneY()-this.margin));
            }
        });

        this.cueStick.getCueStickImage().setOnMouseDragged(event-> {
            if (this.cueStick.isVisible()) {
                double x = event.getSceneX();
                double y = event.getSceneY();
                double xPressed = this.cueStick.getxPressed();
                double yPressed = this.cueStick.getyPressed();

                double distance = Math.sqrt((x - xPressed) * (x - xPressed) + (y - yPressed) * (y - yPressed));
                this.cueStick.calVel(distance);

                // move cue stick
                this.cueStick.drag();
            }
        });
//
        this.cueStick.getCueStickImage().setOnMouseReleased(event -> {
            stateKeeper.setMemento(this.saveState());
            this.cueStick.getCueStickImage().getTransforms().remove(1);
            hitBall(this.balls.get(0), this.cueStick.getVelocity(), this.cueStick.getAngle());
            this.cueStick.invisible();
        });
    }

    /**
     * Draws all relevant items - table, cue, balls, pockets - onto Canvas.
     * Used Exercise 6 as reference.
     */
    private void draw() {
        tick();

        // Fill in background
        gc.setFill(Paint.valueOf("white"));
        gc.fillRect(0, 0, table.getxLength() + TABLEBUFFER * 2, table.getyLength() + TABLEBUFFER * 2);

        // Fill in edges
        gc.setFill(Paint.valueOf("brown"));
        gc.fillRect(TABLEBUFFER - TABLEEDGE, TABLEBUFFER - TABLEEDGE, table.getxLength() + TABLEEDGE * 2,
                table.getyLength() + TABLEEDGE * 2);

        // Fill in Table
        gc.setFill(table.getColour());
        gc.fillRect(TABLEBUFFER, TABLEBUFFER, table.getxLength(), table.getyLength());

        // Fill in Pockets
        for (Pocket pocket : table.getPockets()) {
            gc.setFill(Paint.valueOf("black"));
            gc.fillOval(pocket.getxPos() - pocket.getRadius(), pocket.getyPos() - pocket.getRadius(),
                    pocket.getRadius() * 2, pocket.getRadius() * 2);
        }

//        // Cue
//        if (this.cue != null && cueActive) {
//            gc.strokeLine(cue.getStartX(), cue.getStartY(), cue.getEndX(), cue.getEndY());
//        }

        for (Ball ball : balls) {
            if (ball.isActive()) {
                gc.setFill(ball.getColour().getColour());
                gc.fillOval(ball.getxPos() - ball.getRadius(),
                        ball.getyPos() - ball.getRadius(),
                        ball.getRadius() * 2,
                        ball.getRadius() * 2);
            }

        }

        // Win
        if (winFlag) {
            gc.setStroke(Paint.valueOf("white"));
            gc.setFont(new Font("Impact", 80));
            gc.strokeText("Win and bye", table.getxLength() / 2 + TABLEBUFFER - 180,
                    table.getyLength() / 2 + TABLEBUFFER);
        }

    }

    /**
     * Updates positions of all balls, handles logic related to collisions.
     * Used Exercise 6 as reference.
     */
    public void tick() {
        if (this.score == this.totalScore) {
            winFlag = true;
        }

        this.timeLabel.setText(this.time.toString());

        // when no ball is moving, set cue stick visible and save state
        if (!cueStick.isVisible()) {
            boolean allStable = true;
            for (Ball ball: this.balls) {
                if (ball.isActive() && (ball.getxVel() != 0.0 || ball.getyVel() != 0.0)) {
                    allStable = false;
                }
            }
            if (allStable) {
                cueStick.visible(balls.get(0).getxPos(), balls.get(0).getyPos());
            }
        }


        for (Ball ball : balls) {
            ball.tick();

            double width = table.getxLength();
            double height = table.getyLength();

            // Check if ball landed in pocket
            for (Pocket pocket : table.getPockets()) {
                if (pocket.isInPocket(ball)) {
                    if (ball.isCue()) {
                        this.reset();
                    } else {
                        if (ball.remove()) {
                            this.setScore(this.score + ball.getScore());
                        } else {
                            // Check if when ball is removed, any other balls are present in its space. (If
                            // another ball is present, blue ball is removed)
                            for (Ball otherBall : balls) {
                                double deltaX = ball.getxPos() - otherBall.getxPos();
                                double deltaY = ball.getyPos() - otherBall.getyPos();
                                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                                if (otherBall != ball && otherBall.isActive() && distance < 10) {
                                    ball.remove();
                                }
                            }
                        }
                    }
                    break;
                }
            }

            // Handle the edges (balls don't get a choice here)
            if (ball.getxPos() + ball.getRadius() > width + TABLEBUFFER) {
                ball.setxPos(width - ball.getRadius());
                ball.setxVel(ball.getxVel() * -1);
            }
            if (ball.getxPos() - ball.getRadius() < TABLEBUFFER) {
                ball.setxPos(ball.getRadius());
                ball.setxVel(ball.getxVel() * -1);
            }
            if (ball.getyPos() + ball.getRadius() > height + TABLEBUFFER) {
                ball.setyPos(height - ball.getRadius());
                ball.setyVel(ball.getyVel() * -1);
            }
            if (ball.getyPos() - ball.getRadius() < TABLEBUFFER) {
                ball.setyPos(ball.getRadius());
                ball.setyVel(ball.getyVel() * -1);
            }

            // Apply table friction
            double friction = table.getFriction();
            ball.setxVel(ball.getxVel() * friction);
            ball.setyVel(ball.getyVel() * friction);

            // Check ball collisions
            for (Ball ballB : balls) {
                if (checkCollision(ball, ballB)) {
                    Point2D ballPos = new Point2D(ball.getxPos(), ball.getyPos());
                    Point2D ballBPos = new Point2D(ballB.getxPos(), ballB.getyPos());
                    Point2D ballVel = new Point2D(ball.getxVel(), ball.getyVel());
                    Point2D ballBVel = new Point2D(ballB.getxVel(), ballB.getyVel());
                    Pair<Point2D, Point2D> changes = calculateCollision(ballPos, ballVel, ball.getMass(), ballBPos,
                            ballBVel, ballB.getMass(), false);
                    calculateChanges(changes, ball, ballB);
                }
            }
        }
    }

    /**
     * Resets the game.
     */
    public void reset() {
        for (Ball ball : balls) {
            ball.reset();
        }

        this.setScore(0);
        this.time.reset();
    }

    /**
     * @return gameScene.
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Sets the table of the game.
     * 
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * @return table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * Sets the balls of the game.
     * 
     * @param balls
     */
    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    /**
     * Hits the ball with the cue, distance of the cue indicates the strength of the
     * strike.
     * 
     * @param ball
     */
    private void hitBall(Ball ball, double velocity, double angle) {
//        double deltaX = ball.getxPos() - cue.getStartX();
//        double deltaY = ball.getyPos() - cue.getStartY();
//        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Check that start of cue is within cue ball
//        if (distance < ball.getRadius()) {
//            // Collide ball with cue
//            double hitxVel = (cue.getStartX() - cue.getEndX()) * FORCEFACTOR;
//            double hityVel = (cue.getStartY() - cue.getEndY()) * FORCEFACTOR;
//
//            ball.setxVel(hitxVel);
//            ball.setyVel(hityVel);
//        }

        double ang;
        if (angle > 90 && angle < 270) {
            ang = Math.abs(angle - 180);
        } else {
            ang = Math.abs(angle);
        }

        if (angle > 90 && angle < 270) {
            ball.setxVel(ball.getxVel() - velocity * Math.cos(Math.toRadians(ang)));
        } else {
            ball.setxVel(ball.getxVel() + velocity * Math.cos(Math.toRadians(ang)));
        }

        if (angle > 0 && angle < 180) {
            ball.setyVel(ball.getyVel() + velocity * Math.sin(Math.toRadians(ang)));
        } else {
            ball.setyVel(ball.getyVel() - velocity * Math.sin(Math.toRadians(ang)));
        }

        this.cueStick.invisible();

    }

    /**
     * Changes values of balls based on collision (if ball is null ignore it)
     * 
     * @param changes
     * @param ballA
     * @param ballB
     */
    private void calculateChanges(Pair<Point2D, Point2D> changes, Ball ballA, Ball ballB) {
        ballA.setxVel(changes.getKey().getX());
        ballA.setyVel(changes.getKey().getY());
        if (ballB != null) {
            ballB.setxVel(changes.getValue().getX());
            ballB.setyVel(changes.getValue().getY());
        }
    }


    /**
     * Checks if two balls are colliding.
     * Used Exercise 6 as reference.
     * 
     * @param ballA
     * @param ballB
     * @return true if colliding, false otherwise
     */
    private boolean checkCollision(Ball ballA, Ball ballB) {
        if (ballA == ballB) {
            return false;
        }

        if (!ballA.isActive() || !ballB.isActive()) {
            return false;
        }

        return Math.abs(ballA.getxPos() - ballB.getxPos()) < ballA.getRadius() + ballB.getRadius() &&
                Math.abs(ballA.getyPos() - ballB.getyPos()) < ballA.getRadius() + ballB.getRadius();
    }

    /**
     * Collision function adapted from assignment, using physics algorithm:
     * http://www.gamasutra.com/view/feature/3015/pool_hall_lessons_fast_accurate_.php?page=3
     *
     * @param positionA The coordinates of the centre of ball A
     * @param velocityA The delta x,y vector of ball A (how much it moves per tick)
     * @param massA     The mass of ball A (for the moment this should always be the
     *                  same as ball B)
     * @param positionB The coordinates of the centre of ball B
     * @param velocityB The delta x,y vector of ball B (how much it moves per tick)
     * @param massB     The mass of ball B (for the moment this should always be the
     *                  same as ball A)
     *
     * @return A Pair in which the first (key) Point2D is the new
     *         delta x,y vector for ball A, and the second (value) Point2D is the
     *         new delta x,y vector for ball B.
     */
    public static Pair<Point2D, Point2D> calculateCollision(Point2D positionA, Point2D velocityA, double massA,
            Point2D positionB, Point2D velocityB, double massB, boolean isCue) {

        // Find the angle of the collision - basically where is ball B relative to ball
        // A. We aren't concerned with
        // distance here, so we reduce it to unit (1) size with normalize() - this
        // allows for arbitrary radii
        Point2D collisionVector = positionA.subtract(positionB);
        collisionVector = collisionVector.normalize();

        // Here we determine how 'direct' or 'glancing' the collision was for each ball
        double vA = collisionVector.dotProduct(velocityA);
        double vB = collisionVector.dotProduct(velocityB);

        // If you don't detect the collision at just the right time, balls might collide
        // again before they leave
        // each others' collision detection area, and bounce twice.
        // This stops these secondary collisions by detecting
        // whether a ball has already begun moving away from its pair, and returns the
        // original velocities
        if (vB <= 0 && vA >= 0 && !isCue) {
            return new Pair<>(velocityA, velocityB);
        }

        // This is the optimisation function described in the gamasutra link. Rather
        // than handling the full quadratic
        // (which as we have discovered allowed for sneaky typos)
        // this is a much simpler - and faster - way of obtaining the same results.
        double optimizedP = (2.0 * (vA - vB)) / (massA + massB);

        // Now we apply that calculated function to the pair of balls to obtain their
        // final velocities
        Point2D velAPrime = velocityA.subtract(collisionVector.multiply(optimizedP).multiply(massB));
        Point2D velBPrime = velocityB.add(collisionVector.multiply(optimizedP).multiply(massA));

        return new Pair<>(velAPrime, velBPrime);
    }

    /**
     * Save the current state into a Memento.
     *
     * @return Memento
     */
    public Memento saveState() {
        return (new Memento(this.score, this.difficulty, this.time, this.balls, this.winFlag));
    }

    /**
     * Recover to a state store in a given Memento.
     *
     * @param memento
     */
    public void recoverState(Memento memento) {
        if (memento == null) { return; }
        this.setScore(memento.getScore());
        if (!this.difficulty.equals(memento.getDifficulty())) {
            this.difficulty = memento.getDifficulty();
            this.totalScore = calculateTotalScore();
        }
        this.time = memento.getTimer();
        this.balls = memento.getBallsCopy();
        this.winFlag = memento.getWinFlag();
        this.cueStick.visible(balls.get(0).getxPos(), balls.get(0).getyPos());
        this.updateCheatButtons();
    }
}
