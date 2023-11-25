package pongly.client;

import pongly.common.DrawableObject;

public class Score extends DrawableObject {

    private int score = 0;

    /**
     * Creates a new Score
     *
     * @param x The top left corner x coordinate
     * @param y The top left corner y coordinate
     */
    public Score(int x, int y) {
        super(x, y, "0");
    }

    /**
     * updates the score
     */
    public void update(int newScore) {
        score = newScore;
        displayString = Integer.toString(score);
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }
}
