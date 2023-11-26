package pongly.client;

import java.io.IOException;

public class Launcher {
    public final static String HOSTNAME = "localhost";
    public final static int PORT = 1313;

    public static void main(String[] args) {

        try {
            GameManager gameManager = new GameManager(HOSTNAME, PORT);
            gameManager.run();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}