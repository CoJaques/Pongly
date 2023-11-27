package pongly.client;

import pongly.common.Message;
import pongly.common.Utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This class is used to represent a pong player client
 */
public class PongClient {
    private static final String SERVER_DISCONNECTED_ERROR = "Server disconnected ";
    private final GameManager gameManager;
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final Thread receiverThread = new Thread(this::receive);
    private final int lastSentPosition = 0;

    private boolean isRunning;

    /**
     * @param ip          The ip of the server
     * @param port        The port of the server
     * @param gameManager The game manager
     * @throws IOException if an I/O error occurs
     */
    public PongClient(String ip, int port, GameManager gameManager) throws IOException {
        this.gameManager = gameManager;

        try {
            this.socket = new Socket(ip, port);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IOException("Error while connecting to server: " + e.getMessage());
        }

        isRunning = true;
        receiverThread.start();
    }

    /**
     * Send the actual position of the player to the server
     */
    public void updatePosition() {
        if (gameManager.getPlayerPosition() == lastSentPosition)
            return;

        String message = Message.UPDATE_PLAYER.name() + Utils.SEPARATOR + gameManager.getPlayerPosition() + Utils.EndLineChar;
        send(message);
    }

    /**
     * Send a message to the server to quit the game
     */
    public void quitGame() {
        if (!socket.isClosed() && socket.isConnected())
            send(Message.QUIT.name() + Utils.EndLineChar);

        closeConnection();
    }

    /**
     * Send a message to the server to start the game
     */
    public void sendReady() {
        send(Message.READY.name() + Utils.EndLineChar);
    }

    private void closeConnection() {
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

            isRunning = false;
        } catch (IOException e) {
            System.out.println("Error while closing connection: " + e.getMessage());
        }
    }

    private void send(String message) {
        try {
            out.write(message);
            out.flush();
        } catch (IOException e) {
            System.out.println(SERVER_DISCONNECTED_ERROR + e);
            gameManager.quitGame();
        }
    }

    private void receive() {
        System.out.println("Waiting messages from server...");
        try {
            while (isRunning) {
                String line = in.readLine();
                processMessage(line);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println(SERVER_DISCONNECTED_ERROR + e);
        } finally {
            closeConnection();
        }
    }

    private void processMessage(String message) {
        if (message == null)
            throw new IllegalArgumentException("Message cannot be null");

        String[] parts = message.split(";");

        Message msg;
        try {
            msg = Message.valueOf(parts[0]);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid message: " + parts[0]);
            return;
        }

        manageReceivedCommand(msg, parts);
    }

    private void manageReceivedCommand(Message msg, String[] parts) {
        switch (msg) {
            case UPDATE_SERVER:
                updateGameObjects(parts);
                break;
            case UPDATE_SCORE:
                gameManager.updateScore(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                break;
            case QUIT:
                gameManager.quitGame();
                break;
            default:
                System.out.println("Invalid message: " + msg);
                break;
        }
    }

    private void updateGameObjects(String[] data) {
        int yPaddlePlayerTwo = Integer.parseInt(data[1]);
        int xBall = Integer.parseInt(data[2]);
        int yBall = Integer.parseInt(data[3]);

        gameManager.setOpponentPosition(yPaddlePlayerTwo);
        gameManager.updateBallPosition(xBall, yBall);
    }
}
