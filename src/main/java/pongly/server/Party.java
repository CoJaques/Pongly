package pongly.server;

import pongly.common.Ball;
import pongly.common.Paddle;

import static pongly.common.Utils.SCREEN_HEIGHT;
import static pongly.common.Utils.SCREEN_WIDTH;

public class Party {

    private final Paddle playerOnePaddle;
    private final Paddle playerTwoPaddle;
    private final Ball ball;

    public Party(Paddle playerOnePaddle, Paddle playerTwoPaddle, Ball ball) {
        this.playerOnePaddle = playerOnePaddle;
        this.playerTwoPaddle = playerTwoPaddle;
        this.ball = ball;
    }

    private void updateGameObjects() {
        ball.update();
    }

    private void checkCollisions() {
        if (ball.getY() <= 0 || ball.getY() >= SCREEN_HEIGHT - 1) {
            ball.reverseYDirection();
        }

        if (ball.getX() == SCREEN_WIDTH || ball.getX() == 0) {
            ball.reverseXDirection();
        }

        manageBallCollisionWithPaddle(playerOnePaddle);
        manageBallCollisionWithPaddle(playerTwoPaddle);
    }

    private void manageBallCollisionWithPaddle(Paddle paddle) {
        if (ball.getX() == paddle.getX() && ball.getY() >= paddle.getY() &&
                ball.getY() <= paddle.getY() + paddle.getHeight()) {
            ball.reverseXDirection();

            int delta = ball.getY() - (paddle.getY() + paddle.getHeight() / 2);

            if (delta == 0) {
                ball.goStraight();
            } else if (ball.getYVelocity() == 0) {
                if (delta > 0) {
                    ball.goDiagonalDown();
                } else {
                    ball.goDiagonalUp();
                }
            } else {
                ball.reverseYDirection();
            }
        }
    }
}
