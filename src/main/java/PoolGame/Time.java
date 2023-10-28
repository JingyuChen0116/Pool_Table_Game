package PoolGame;

/**
 * Stores the duration of a game.
 */
public class Time {
//    private int hour;
    private int minute;
    private int second;
    private int frameNum;

    public Time() {
//        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.frameNum = 0;
    }

    public Time(int minute, int second, int frameNum) {
//        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.frameNum = frameNum;
    }

    /**
     * Compose the label to be displayed on the game screen
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("Time:\t%02d : %02d", this.minute, this.second);
//        return this.hour + " : " + this.minute + " : " + this.second;
    }

    /**
     * Calculate the duration according to the number of frames.
     */
    public void addFrameNum() {
        this.frameNum++;
        if (this.frameNum == 60) {
            this.second++;
            this.frameNum = 0;
        }
        if (this.second == 60) {
            this.minute++;
            this.second = 0;
        }
//        if (this.minute == 60) {
//            this.hour++;
//            this.minute = 0;
//        }
    }

    /**
     * Make a copy of current time.
     *
     * @return Time
     */
    public Time copy() {
        return new Time(minute, second, frameNum);
    }

    /**
     * Reset the duration to be 0.
     */
    public void reset() {
        this.minute = 0;
        this.second = 0;
        this.frameNum = 0;
    }
}
