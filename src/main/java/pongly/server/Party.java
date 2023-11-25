package pongly.server;

import pongly.common.Ball;
import pongly.common.Message;
import pongly.common.Paddle;
import pongly.common.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static pongly.common.Utils.SCREEN_HEIGHT;
import static pongly.common.Utils.SCREEN_WIDTH;

public class Party implements Runnable {

    public static int PARTY_ID = 0;

    private int id = PARTY_ID++;

    private final List<ClientHandler> players = new ArrayList<>();
    private final Ball ball;
    Paddle playerOnePaddle;
    Paddle playerTwoPaddle;

    public Party() {
        ball = new Ball(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        playerOnePaddle = new Paddle(5, SCREEN_HEIGHT / 2, 3);
        playerTwoPaddle = new Paddle(SCREEN_WIDTH - 5, SCREEN_HEIGHT / 2, 3);

        System.out.println("New party created with id " + id);
    }

    public void addPlayer(ClientHandler player) {
        // TODO throw exception if full
        players.add(player);
        System.out.println("Player added to party " + id);
    }

    public boolean isFull() {
        return players.size() == 2;
    }

    @Override
    public void run() {

        System.out.println("Starting game for party " + id);

        while (true) {
            updateGameObjects();
            checkCollisions();
            try {
                players.get(0).sendMessage(Message.UPDATE_SERVER.name() + Utils.SEPARATOR + playerTwoPaddle.getY() + Utils.SEPARATOR + ball.getX() + Utils.SEPARATOR + ball.getY() + Utils.EndLineChar);
                players.get(1).sendMessage(Message.UPDATE_SERVER.name() + Utils.SEPARATOR + playerOnePaddle.getY() + Utils.SEPARATOR + (Utils.SCREEN_WIDTH - ball.getX()) + Utils.SEPARATOR + ball.getY() + Utils.EndLineChar);

                Thread.sleep(100);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateGameObjects() {
        playerOnePaddle.setY(players.get(0).getClientLastPosition());
        playerTwoPaddle.setY(players.get(1).getClientLastPosition());
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
