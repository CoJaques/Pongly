package pongly.client;

import pongly.common.Paddle;

import static pongly.common.Utils.SCREEN_HEIGHT;

public class Player {

    private final Score score;
    private final Paddle paddle;

    public Player(Score score, Paddle paddle) {
        this.score = score;
        this.paddle = paddle;
    }

    public Score getScore() {
        return score;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void moveUp() {
        if (paddle.getY() > 0)
            paddle.moveUp();
    }

    public void moveDown() {
        if (paddle.getY() + paddle.getHeight() < SCREEN_HEIGHT - 1)
            paddle.moveDown();
    }
}