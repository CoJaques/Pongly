package pongly.client;

import pongly.client.game.Ball;
import pongly.client.game.DrawableObject;
import pongly.client.game.Paddle;

import java.util.ArrayList;
import java.util.List;

public class CliController {

    private final InputHandler inputHandler;
    private DisplayManager displayManager;
    private boolean gameRunning;
    private Paddle playerOnePaddle;
    private Paddle playerTwoPaddle;
    private Ball ball;
    private List<DrawableObject> gameObjects;

    public CliController(DisplayManager displayManager, Paddle playerOnePaddle, Paddle playerTwoPaddle, Ball ball) {
        this.playerOnePaddle = playerOnePaddle;
        this.playerTwoPaddle = playerTwoPaddle;
        this.ball = ball;
        this.displayManager = displayManager;
        this.inputHandler = new InputHandler(displayManager.getScreen(), playerOnePaddle, playerTwoPaddle);

        gameObjects = new ArrayList<>();
        gameObjects.add(playerOnePaddle);
        gameObjects.add(playerTwoPaddle);
        gameObjects.add(ball);
    }

    public void run() {
        while (!inputHandler.shouldExit()) {
            try {
                inputHandler.processInput(); // Process input
                updateGameObjects();         // Mise à jour des objets du jeu
                checkCollisions();          // Vérifier les collisions
                renderGame();               // Rendu du jeu sur l'écran

                Thread.sleep(50);           // Petite pause pour contrôler la vitesse de la boucle de jeu
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exception
            }
        }
    }

    private void renderGame() {
        try {
            displayManager.draw(gameObjects);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
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

        if(ball.getX() == displayManager.getScreenWidth() || ball.getX() == 0)
            ball.reverseXDirection();
    }
}
