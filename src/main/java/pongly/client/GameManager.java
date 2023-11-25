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
import static pongly.common.Utils.SCREEN_WIDTH;

/**
 * This class is responsible for handling the game logic
 */
public class GameManager implements KeyListener {

    private final static int REFRESH_FREQUENCY = 100;
    private GameState gameState = GameState.INITIALIZING;
    private final InputHandler inputHandler;
    private final DisplayManager displayManager;
    private final Paddle playerOnePaddle;
    private final Paddle playerTwoPaddle;
    private final Ball ball;
    private final List<DrawableObject> gameObjects;
    private KeyStroke lastInput;

    private final PongClient client;

    /**
     * @throws IOException if an I/O error occurs
     */
    public GameManager(String host, int port) throws IOException {

        this.displayManager = new DisplayManager(SCREEN_WIDTH, SCREEN_HEIGHT);
        client = new PongClient(host, port, this);

        this.playerOnePaddle = new Paddle(5, SCREEN_HEIGHT / 2, 3);
        this.playerTwoPaddle = new Paddle(SCREEN_WIDTH - 5, SCREEN_HEIGHT / 2, 3);
        this.ball = new Ball(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);

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
            displayManager.close();

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

        client.updatePosition();
    }

    private boolean continueGameConditions() {
        return lastInput == null || lastInput.getCharacter() == null || lastInput.getCharacter() != 'q';
    }

    public int getPlayerOnePaddleY() {
        return playerOnePaddle.getY();
    }

    public void setPlayerTwoPaddleY(int y) {
        playerTwoPaddle.setY(y);
    }

    public void updateBallPosition(int xBalle, int yBalle) {
        ball.setX(xBalle);
        ball.setY(yBalle);
    }
}