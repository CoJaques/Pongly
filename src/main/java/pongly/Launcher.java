package pongly;

import pongly.client.game.Ball;
import pongly.client.CliController;
import pongly.client.DisplayManager;
import pongly.client.game.Paddle;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) {
        final int screenWidth = 80;  // Assurez-vous que ces valeurs correspondent à la taille souhaitée
        final int screenHeight = 24;

        try {
            // Création du DisplayManager avec la taille définie de l'écran
            DisplayManager displayManager = new DisplayManager(screenWidth, screenHeight);

            // Initialisation des paddles et de la balle
            Paddle playerPaddle = new Paddle(5, screenHeight / 2,  "|");
            Paddle aiPaddle = new Paddle(screenWidth - 5, screenHeight / 2, "|");
            Ball ball = new Ball(screenWidth / 2, screenHeight / 2);

            // Création du CliController avec tous les composants
            CliController cliController = new CliController(displayManager, playerPaddle, aiPaddle, ball);

            // Lancement du jeu
            cliController.run();

            // Nettoyage et fermeture de l'écran et du terminal
            displayManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}