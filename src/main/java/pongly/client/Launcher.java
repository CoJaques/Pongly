package pongly.client;

import pongly.common.Ball;
import pongly.common.Paddle;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) {
        final int screenWidth = 80;
        final int screenHeight = 24;

        try {
            DisplayManager displayManager = new DisplayManager(screenWidth, screenHeight);

            Paddle playerPaddle = new Paddle(5, screenHeight / 2, 3);
            Paddle aiPaddle = new Paddle(screenWidth - 5, screenHeight / 2, 3);
            Ball ball = new Ball(screenWidth / 2, screenHeight / 2);

            CliController cliController = new CliController(displayManager, playerPaddle, aiPaddle, ball);
            cliController.run();
            displayManager.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}