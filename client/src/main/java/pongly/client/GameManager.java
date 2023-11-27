package pongly.client;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import pongly.client.InputManager.InputHandler;
import pongly.client.InputManager.KeyListener;
import pongly.common.Ball;
import pongly.common.DrawableObject;
import pongly.common.Paddle;
import pongly.common.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for handling the game logic
 */
public class GameManager implements KeyListener {
    private final static int REFRESH_FREQUENCY = 100;
    private final InputHandler inputHandler;
    private final DisplayManager displayManager;
    private GameState gameState = GameState.INITIALIZING;
    private final Player player;
    private final Player opponent;
    private final Ball ball;
    private List<DrawableObject> gameObjects;
    private KeyStroke lastInput;
    private final PongClient client;

    /**
     * @throws IOException if an I/O error occurs
     */
    public GameManager(String host, int port) throws IOException {
        player = new Player(new Score(Utils.SCREEN_WIDTH / 2 - 5, 1), new Paddle(5, Utils.SCREEN_HEIGHT / 2, 3));
        opponent = new Player(new Score(Utils.SCREEN_WIDTH / 2 + 5, 1), new Paddle(Utils.SCREEN_WIDTH - 5, Utils.SCREEN_HEIGHT / 2, 3));
        ball = new Ball(Utils.SCREEN_WIDTH / 2, Utils.SCREEN_HEIGHT / 2);

        displayManager = new DisplayManager(Utils.SCREEN_WIDTH, Utils.SCREEN_HEIGHT);
        client = new PongClient(host, port, this);

        inputHandler = new InputHandler(displayManager);
        inputHandler.addListener(this);

        initGameObjects();
    }

    /**
     * Start the game
     */
    public void run() {
        try {
            do {
                manageGameState();
                Thread.sleep(REFRESH_FREQUENCY);
            } while (gameState != GameState.Ended);
        } catch (InterruptedException | IOException e) {
            displayManager.close();
            inputHandler.triggerExit();
        } finally {
            inputHandler.triggerExit();
        }
    }

    @Override
    public void onKeyPressed(KeyStroke keyStroke) {
        lastInput = keyStroke;
        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            player.moveUp();
        } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            player.moveDown();
        }
    }

    /**
     * @return the y position of the player
     */
    public int getPlayerPosition() {
        return player.getPaddle().getY();
    }

    /**
     * Update the position of the opponent
     *
     * @param y the y position of the opponent
     */
    public void setOpponentPosition(int y) {
        opponent.getPaddle().setY(y);
    }

    /**
     * Update the position of the ball
     *
     * @param xBall the x position of the ball
     * @param yBall the y position of the ball
     */
    public void updateBallPosition(int xBall, int yBall) {
        ball.setX(xBall);
        ball.setY(yBall);
    }

    /**
     * Update the score of the player and the opponent
     *
     * @param scorePlayer   the score of the player
     * @param ScoreOpponent the score of the opponent
     */
    public void updateScore(int scorePlayer, int ScoreOpponent) {
        player.updateScore(scorePlayer);
        opponent.updateScore(ScoreOpponent);
    }

    /**
     * Quit the game
     */
    public void quitGame() {
        gameState = GameState.Exiting;
    }

    private void manageGameState() throws IOException {
        switch (gameState) {
            case INITIALIZING:
                manageInitialiazing();
                break;

            case PLAYING:
                managePlayingState();
                break;

            case Exiting:
                printScore();
                manageExit();
                break;

            case End:
                manageQuitGame();
                break;

            case Ended:
                break;
        }
    }

    private void manageInitialiazing() throws IOException {
        manageInitializingState();
        if (lastInput != null && lastInput.getKeyType() == KeyType.Enter) {
            client.sendReady();
            gameState = GameState.PLAYING;
        }
    }

    private void manageInitializingState() throws IOException {
        displayManager.drawTitle();
    }

    private void managePlayingState() throws IOException {
        displayManager.drawObjects(gameObjects);
        client.updatePosition();

        if (lastInput != null && lastInput.getKeyType() == KeyType.Escape)
            gameState = GameState.Exiting;
    }

    private void manageExit() {
        client.quitGame();
        gameState = GameState.End;
    }

    private void manageQuitGame() {
        if (lastInput != null && lastInput.getKeyType() == KeyType.Character && lastInput.getCharacter() == 'q') {
            displayManager.close();
            inputHandler.triggerExit();
            gameState = GameState.Ended;
        }
    }

    private void printScore() throws IOException {
        displayManager.drawScore(player.getScore(), opponent.getScore());
    }

    private void initGameObjects() {
        gameObjects = new ArrayList<>();
        gameObjects.add(player.getScoreObject());
        gameObjects.add(opponent.getScoreObject());
        gameObjects.add(player.getPaddle());
        gameObjects.add(opponent.getPaddle());
        gameObjects.add(ball);
    }
}