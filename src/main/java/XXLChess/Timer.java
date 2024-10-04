package XXLChess;

import processing.core.PApplet;

public class Timer {
    /**
     * Number of seconds
     */
    private int seconds;
    /**
     * Number of minutes
     */
    private int minutes;
    /**
     * Increment time
     */
    private int increment;
    /**
     * Marks if the timer is main player's timer
     */
    private boolean isHumanTimer;
    /**
     * A counter used to count frames
     */
    private int counter;

    /**
     * Timer constructor, initialzes seconds, minutes, increment, timer possesion status and counter
     * counter is set to number of FPS
     * @param seconds, number of seconds
     * @param increment, increment time for each move
     * @param isHumanTimer, marks if the timer belongs to main player
     */
    public Timer(int seconds, int increment, boolean isHumanTimer) {
        this.minutes = (seconds) / 60;
        this.seconds = (seconds - this.minutes * 60);
        this.increment = increment;
        this.isHumanTimer = isHumanTimer;
        this.counter = App.FPS;
    }

    /**
     * Returns number of seconds
     * @return number of seconds
     */
    public int getSeconds() {
        return this.seconds;
    }

    /**
     * Returns number of minutes
     * @return number of minutes
     */
    public int getMinutes() {
        return this.minutes;
    }

    /**
     * Returns true if the timer is used for main player
     * otherwise, returns false
     * @return true of false
     */
    public boolean isHumanTimer() {
        return this.isHumanTimer;
    }

    /**
     * Perform timer logics per frame
     */
    public void tick() {
        // if timer is running, then reduces count by one
        if (!reachZero()) {
            this.counter--;
            if (this.counter == 0) {
                this.seconds--;
                // updates seconds and minutes when seconds reaches 0
                if (this.seconds < 0) {
                    this.seconds = 59;
                    this.minutes--;
                }
                this.counter = App.FPS;
            }
        }
    }

    /**
     * Draws timers
     * @param app, application
     */
    public void draw(PApplet app) {
        app.textSize(32);
        app.fill(255);
        if (isHumanTimer()) {
            app.text(convertTimeToString(), 
            App.BOARD_WIDTH * App.CELLSIZE + 10, (App.BOARD_WIDTH / 2) * App.CELLSIZE + 300);
        } else {
            app.text(convertTimeToString(), 
            App.BOARD_WIDTH * App.CELLSIZE + 10, (App.BOARD_WIDTH / 2) * App.CELLSIZE - 300);
        }
    }

    /**
     * Increments timer's seconds by timer's increment
     */
    public void increment() {
        this.seconds += this.increment;
        if (this.seconds > 59) {
            this.minutes = this.minutes + (this.seconds / 60);
            this.seconds = (this.seconds % 60);
        }
    }

    /**
     * Returns true if timer runs out of time, otherwise, returns false
     * @return true or false
     */
    public boolean reachZero() {
        return this.seconds <= 0 && this.minutes <= 0;
    }

    /**
     * Converts timer to format: "ss::mm"
     * @return a string in format: "ss:mm"
     */
    public String convertTimeToString() {
        StringBuilder seconds = new StringBuilder();
        StringBuilder minutes = new StringBuilder();

        if (this.seconds < 10) {
            seconds.append("0");
        }
        seconds.append(this.seconds);
        if (this.minutes < 10) {
            minutes.append("0");
        }
        minutes.append(this.minutes);

        return minutes.toString() + ":" + seconds.toString();
    }

}
