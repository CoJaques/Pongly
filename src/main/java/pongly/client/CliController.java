package pongly.client;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import pongly.client.InputManager.InputHandler;
import pongly.client.InputManager.KeyListener;
import pongly.client.game.Ball;
import pongly.client.game.DrawableObject;
import pongly.client.game.Paddle;

import java.util.ArrayList;
import java.util.List;

public class CliController implements KeyListener {

    private final InputHandler inputHandler;
    private final DisplayManager displayManager;
    private final Paddle playerOnePaddle;
    private final Ball ball;
    private final List<DrawableObject> gameObjects;

    private KeyStroke lastInput;

    public CliController(DisplayManager displayManager, Paddle playerOnePaddle, Paddle playerTwoPaddle, Ball ball) {
        this.playerOnePaddle = playerOnePaddle;
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
        while (continueGameConditions()) {
            try {
                updateGameObjects();
                checkCollisions();
                renderGame();

                Thread.sleep(75);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exception
            }
        }

        inputHandler.triggerExit();

    }

    @Override
    public void onKeyPressed(KeyStroke keyStroke) {
        lastInput = keyStroke;
        if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            if (playerOnePaddle.getY() > 0)
                playerOnePaddle.moveUp();
        } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            if (playerOnePaddle.getY() < displayManager.getScreenHeight() - 1)
                playerOnePaddle.moveDown();
        }
    }

    private boolean continueGameConditions() {
        return lastInput == null || lastInput.getCharacter() == null || lastInput.getCharacter() != 'q';
    }

    private void renderGame() {
        try {
            displayManager.draw(gameObjects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateGameObjects() {
        ball.update();
    }

    private void checkCollisions() {
        if (ball.getY() <= 0 || ball.getY() >= displayManager.getScreenHeight() - 1) {
            ball.reverseYDirection();
        }

        if (ball.getX() == playerOnePaddle.getX() && ball.getY() >= playerOnePaddle.getY() &&
                ball.getY() <= playerOnePaddle.getY()) {
            ball.reverseXDirection();
        }

        if (ball.getX() == displayManager.getScreenWidth() || ball.getX() == 0)
            ball.reverseXDirection();
    }
}
