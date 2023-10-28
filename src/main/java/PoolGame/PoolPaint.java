package PoolGame;

import javafx.scene.paint.Paint;

public class PoolPaint {
    private String colourString;
    private Paint colour;

    public PoolPaint(String colourString) {
        this.colourString = colourString;
        this.colour = Paint.valueOf(colourString);
    }

    public String toString() {
        return this.colourString;
    }

    public Paint getColour() {
        return this.colour;
    }
}
