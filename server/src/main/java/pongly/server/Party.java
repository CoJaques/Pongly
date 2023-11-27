package pongly.server;

import pongly.common.Ball;
import pongly.common.Paddle;

import java.util.ArrayList;
import java.util.List;

import static pongly.common.Utils.SCREEN_HEIGHT;
import static pongly.common.Utils.SCREEN_WIDTH;

/**
 * This class is used to represent a party, composed by two players
 */
public class Party implements Runnable {
    public static int PARTY_ID = 0;
    private final int id = PARTY_ID++;
    private final List<ClientHandler> players = new ArrayList<>();
    private final Paddle playerOnePaddle;
    private final Paddle playerTwoPaddle;
    private final Ball ball;
    private boolean isFinished = false;

    /**
     * Create a new party
     */
    public Party() {
        ball = new Ball(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        playerOnePaddle = new Paddle(5, SCREEN_HEIGHT / 2, 3);
        playerTwoPaddle = new Paddle(SCREEN_WIDTH - 5, SCREEN_HEIGHT / 2, 3);

        System.out.println("New party created with id " + id);
    }

    /**
     * Add a player to the party
     *
     * @param player The player to add
     */
    public void addPlayer(ClientHandler player) {
        if (players.size() == 2) {
            throw new IllegalStateException("Party is full");
        }

        players.add(player);
        System.out.println("Player : " + player.getId() + " added to party " + id);
    }

    /**
     * Remove a player to the party
     *
     * @param player The player to add
     */
    public void removePlayer(ClientHandler player) {
        players.remove(player);
        System.out.println("Player : " + player.getId() + " removed from party " + id);
    }

    /**
     * @return true if the party is full
     */
    public boolean isFull() {
        return players.size() == 2;
    }

    @Override
    public void run() {
        System.out.println("Starting game for party " + id);

        while (playersConnected()) {

            if (players.stream().allMatch(ClientHandler::isReady))
                executeRunningParty();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        endParty();
    }

    /**
     * @return true if the party is finished
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * @return the id of the party
     */
    public int getId() {
        return id;
    }

    private boolean playersConnected() {
        return players.size() == 2 && players.stream().allMatch(ClientHandler::isConnected);
    }

    private void sendPositions() {
        players.get(0).updatePosition(playerTwoPaddle.getY(), ball.getX(), ball.getY());
        players.get(1).updatePosition(playerOnePaddle.getY(), SCREEN_WIDTH - ball.getX(), ball.getY());
    }

    private void updateGameObjects() {
        playerOnePaddle.setY(players.get(0).getClientLastPosition());
        playerTwoPaddle.setY(players.get(1).getClientLastPosition());
        ball.update();
    }

    private void checkCollisions() {
        managePoint();

        if (ball.getY() <= 0 || ball.getY() >= SCREEN_HEIGHT - 1) {
            ball.reverseYDirection();
        }

        manageBallCollisionWithPaddle(playerOnePaddle);
        manageBallCollisionWithPaddle(playerTwoPaddle);
    }

    private void managePoint() {
        if (ball.getX() == SCREEN_WIDTH || ball.getX() == 0) {
            if (ball.getX() == 0)
                players.get(0).score++;
            else
                players.get(1).score++;

            ball.reset();

            players.get(0).sendScore(players.get(1).score);
            players.get(1).sendScore(players.get(0).score);
        }
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

    private void executeRunningParty() {
        updateGameObjects();
        checkCollisions();
        sendPositions();
    }

    private void endParty() {
        System.out.println("Party " + id + " finished");

        players.forEach(ClientHandler::endCommunication);
        isFinished = true;
    }
}
