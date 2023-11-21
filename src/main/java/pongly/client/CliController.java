package pongly.client;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import pongly.client.InputManager.InputHandler;
import pongly.client.InputManager.KeyListener;
import pongly.common.Ball;
import pongly.common.DrawableObject;
import pongly.common.Paddle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static pongly.common.Utils.SCREEN_HEIGHT;

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
                        displayManager.drawObjects(gameObjects);
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

    @Override
    public void onKeyPressed(KeyStroke keyStroke) {
        lastInput = keyStroke;
        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            if (playerOnePaddle.getY() > 0)
                playerOnePaddle.moveUp();
        } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            if (playerOnePaddle.getY() + playerOnePaddle.getHeight() < SCREEN_HEIGHT - 1)
                playerOnePaddle.moveDown();
        }
    }

    private boolean continueGameConditions() {
        return lastInput == null || lastInput.getCharacter() == null || lastInput.getCharacter() != 'q';
    }
}