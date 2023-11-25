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

        this.displayManager = new DisplayManager(SCREEN_WIDTH, SCREEN_HEIGHT);
        client = new PongClient(host, port, this);

        inputHandler = new InputHandler(displayManager);
        inputHandler.addListener(this);

        player = new Player(new Score(SCREEN_WIDTH / 2 - 5, 1), new Paddle(5, SCREEN_HEIGHT / 2, 3));
        opponent = new Player(new Score(SCREEN_WIDTH / 2 + 5, 1), new Paddle(SCREEN_WIDTH - 5, SCREEN_HEIGHT / 2, 3));
        this.ball = new Ball(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);

        initGameObjects();
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

    private void manageInitializingState() throws IOException {
        displayManager.drawTitle();
    }

    @Override
    public void onKeyPressed(KeyStroke keyStroke) {
        lastInput = keyStroke;
        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            player.moveUp();
        } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            player.moveDown();
        }

        client.updatePosition();
    }

    public int getPlayerPosition() {
        return player.getPaddle().getY();
    }

    public void setOpponentPosition(int y) {
        opponent.getPaddle().setY(y);
    }

    public void updateBallPosition(int xBall, int yBall) {
        ball.setX(xBall);
        ball.setY(yBall);
    }

    public void updateScore(int scorePlayer, int ScoreOpponent) {
        player.getScore().update(scorePlayer);
        opponent.getScore().update(ScoreOpponent);
    }

    private void initGameObjects() {
        gameObjects = new ArrayList<>();
        gameObjects.add(player.getScore());
        gameObjects.add(opponent.getScore());
        gameObjects.add(player.getPaddle());
        gameObjects.add(opponent.getPaddle());
        gameObjects.add(ball);
    }

    private boolean continueGameConditions() {
        return lastInput == null || lastInput.getCharacter() == null || lastInput.getCharacter() != 'q';
    }
}