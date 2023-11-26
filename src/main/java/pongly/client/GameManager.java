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

        player = new Player(new Score(SCREEN_WIDTH / 2 - 5, 1), new Paddle(5, SCREEN_HEIGHT / 2, 3));
        opponent = new Player(new Score(SCREEN_WIDTH / 2 + 5, 1), new Paddle(SCREEN_WIDTH - 5, SCREEN_HEIGHT / 2, 3));
        this.ball = new Ball(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);

        this.displayManager = new DisplayManager(SCREEN_WIDTH, SCREEN_HEIGHT);
        client = new PongClient(host, port, this);

        inputHandler = new InputHandler(displayManager);
        inputHandler.addListener(this);

        initGameObjects();
    }

    public void run() {

        try {
            while (true) {
                switch (gameState) {
                    case INITIALIZING:
                        manageInitializingState();
                        if (lastInput != null && lastInput.getKeyType() == KeyType.Enter) {
                            client.sendReady();
                            gameState = GameState.PLAYING;
                        }
                        break;

                    case PLAYING:
                        displayManager.drawObjects(gameObjects);
                        client.updatePosition();

                        if (lastInput != null && lastInput.getKeyType() == KeyType.Escape)
                            gameState = GameState.Exiting;
                        break;

                    case Exiting:
                        printScore();
                        client.quitGame();
                        gameState = GameState.End;
                        break;

                    case End:
                        if (lastInput != null && lastInput.getKeyType() == KeyType.Character && lastInput.getCharacter() == 'q') {
                            displayManager.close();
                            inputHandler.triggerExit();
                            return;
                        }
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

    private void printScore() {
        try {
            displayManager.drawScore(player.getScore(), opponent.getScore());
        } catch (IOException e) {
            System.out.println("Error while drawing score: " + e);
            quitGame();
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
        player.updateScore(scorePlayer);
        opponent.updateScore(ScoreOpponent);
    }

    private void initGameObjects() {
        gameObjects = new ArrayList<>();
        gameObjects.add(player.getScoreObject());
        gameObjects.add(opponent.getScoreObject());
        gameObjects.add(player.getPaddle());
        gameObjects.add(opponent.getPaddle());
        gameObjects.add(ball);
    }

    public void quitGame() {
        gameState = GameState.Exiting;
    }
}