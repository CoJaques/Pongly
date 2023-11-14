package pongly;

import pongly.client.CliController;
import pongly.client.DisplayManager;
import pongly.client.game.Ball;
import pongly.client.game.Paddle;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) {
        final int screenWidth = 80;
        final int screenHeight = 24;

        try {
            DisplayManager displayManager = new DisplayManager(screenWidth, screenHeight);

            Paddle playerPaddle = new Paddle(5, screenHeight / 2, "|");
            Paddle aiPaddle = new Paddle(screenWidth - 5, screenHeight / 2, "|");
            Ball ball = new Ball(screenWidth / 2, screenHeight / 2);

            CliController cliController = new CliController(displayManager, playerPaddle, aiPaddle, ball);
            cliController.run();
            displayManager.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}