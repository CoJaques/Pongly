package pongly.client;

import pongly.common.Paddle;
import pongly.common.Utils;

/**
 * This class is used to represent a player
 */
public class Player {
    private final Score score;
    private final Paddle paddle;

    /**
     * @param score  The score of the player
     * @param paddle The paddle of the player
     */
    public Player(Score score, Paddle paddle) {
        this.score = score;
        this.paddle = paddle;
    }

    /**
     * @return The score of the player as an object
     */
    public Score getScoreObject() {
        return score;
    }

    /**
     * @return The score of the player as a number
     */
    public int getScore() {
        return score.getScore();
    }

    /**
     * @param newScore The new score of the player
     */
    public void updateScore(int newScore) {
        score.update(newScore);
    }

    /**
     * @return The paddle of the player
     */
    public Paddle getPaddle() {
        return paddle;
    }

    /**
     * Move the paddle up
     */
    public void moveUp() {
        if (paddle.getY() > 0)
            paddle.moveUp();
    }

    /**
     * Move the paddle down
     */
    public void moveDown() {
        if (paddle.getY() + paddle.getHeight() < Utils.SCREEN_HEIGHT - 1)
            paddle.moveDown();
    }
}
