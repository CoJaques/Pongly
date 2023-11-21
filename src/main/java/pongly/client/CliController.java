package pongly.client;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import pongly.client.InputManager.InputHandler;
import pongly.client.InputManager.KeyListener;
import pongly.client.game.Ball;
import pongly.client.game.DrawableObject;
import pongly.client.game.Paddle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for handling the game logic
 */
public class CliController implements KeyListener {

    private final static int REFRESH_FREQUENCY = 100;
    private GameState gameState = GameState.INITIALIZING;
    private final InputHandler inputHandler;
    private final DisplayManager displayManager;
    private final Paddle playerOnePaddle;
    private final Paddle playerTwoPaddle;
    private final Ball ball;
    private final List<DrawableObject> gameObjects;
    private KeyStroke lastInput;

    /**
     * @param displayManager  DisplayManager instance
     * @param playerOnePaddle Paddle instance
     * @param playerTwoPaddle Paddle instance
     * @param ball            Ball instance
     */
    public CliController(DisplayManager displayManager, Paddle playerOnePaddle, Paddle playerTwoPaddle, Ball ball) {
        this.playerOnePaddle = playerOnePaddle;
        this.playerTwoPaddle = playerTwoPaddle;
        this.ball = ball;
        this.displayManager = displayManager;

        inputHandler = new InputHandler(displayManager);
        inputHandler.addListener(this);

        gameObjects = new ArrayList<>();
        gameObjects.add(playerOnePaddle);
        gameObjects.add(playerTwoPaddle);
        gameObjects.add(ball);
    }

    public void run() {
        try {
            while (continueGameConditions()) {
                switch (gameState) {
                    case INITIALIZING:
                        manageInitializingState();
                        if (lastInput != null && lastInput.getKeyType() == KeyType.Enter)
                            gameState = GameState.PLAYING;
                        break;
                    case LOBBY:
                        manageLobbyState();
                        break;
                    case PLAYING:
                        managePlayingState();
                        break;
                }
                Thread.sleep(REFRESH_FREQUENCY);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            inputHandler.triggerExit();
        }
    }

    private void manageLobbyState() throws IOException {
        displayManager.drawLobby();
    }

    private void manageInitializingState() throws IOException {
        displayManager.drawTitle();
    }

    /**
     * Run the game
     */
    public void managePlayingState() throws IOException {
        updateGameObjects();
        checkCollisions();
        renderGame();
    }

    @Override
    public void onKeyPressed(KeyStroke keyStroke) {
        lastInput = keyStroke;
        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            if (playerOnePaddle.getY() > 0)
                playerOnePaddle.moveUp();
        } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            if (playerOnePaddle.getY() + playerOnePaddle.getHeight() < displayManager.getScreenHeight() - 1)
                playerOnePaddle.moveDown();
        }
    }

    private boolean continueGameConditions() {
        return lastInput == null || lastInput.getCharacter() == null || lastInput.getCharacter() != 'q';
    }

    private void renderGame() throws IOException {
        displayManager.drawObjects(gameObjects);
    }

    private void updateGameObjects() {
        ball.update();
    }

    private void checkCollisions() {
        if (ball.getY() <= 0 || ball.getY() >= displayManager.getScreenHeight() - 1) {
            ball.reverseYDirection();
        }

        if (ball.getX() == displayManager.getScreenWidth() || ball.getX() == 0) {
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