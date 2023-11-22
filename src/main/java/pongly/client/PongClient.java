package pongly.client;

import pongly.common.Utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PongClient {
    private final GameManager gameManager;
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final Thread receiverThread = new Thread(this::receive);

    public PongClient(String ip, int port, GameManager gameManager) throws IOException {
        this.gameManager = gameManager;

        this.socket = new Socket(ip, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

        receiverThread.start();
    }

    public void updatePosition() {
        try {
            out.write(Message.UPDATE.name() + Utils.SEPARATOR + gameManager.getPlayerOnePaddleY() + "\n");
            out.flush();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            closeConnection();
        }
    }

    private void receive() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                processMessage(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
            // Gérer l'exception ici
        }
    }

    private void processMessage(String message) {
        String[] parts = message.split(";");
        switch (parts[0]) {
            case "POSITION_UPDATE":
                updateGameObjects(parts);
                break;
            // Ajoutez d'autres cas pour d'autres types de messages
        }
    }

    private void updateGameObjects(String[] data) {
        // Supposons que l'ordre est xRaquette, yRaquette, xBalle, yBalle
        int yRaquetteAdverse = Integer.parseInt(data[1]);
        int xBalle = Integer.parseInt(data[2]);
        int yBalle = Integer.parseInt(data[3]);

        // Mettre à jour les positions dans le jeu
        gameManager.setPlayerTwoPaddleY(yRaquetteAdverse);
        gameManager.updateBallPosition(xBalle, yBalle);
    }

    public void closeConnection() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            receiverThread.join();
        } catch (IOException e) {
            e.printStackTrace();
            // Vous pouvez également gérer l'exception ici, par exemple en affichant un message d'erreur.
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
