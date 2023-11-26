package pongly.client;

import pongly.common.Message;
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
        String message = Message.UPDATE_PLAYER.name() + Utils.SEPARATOR + gameManager.getPlayerPosition() + Utils.EndLineChar;
        send(message);
    }

    public void quitGame() {
        if(socket.isConnected())
            send(Message.QUIT.name() + Utils.EndLineChar);

        closeConnection();
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

            receiverThread.join();
        } catch (IOException e) {

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void send(String message) {
        try {
            out.write(message);
            out.flush();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            closeConnection();
        }
    }

    private void receive() {

        System.out.println("Waiting messages from server...");
        try {
            while (true) {
                String line = in.readLine();

                if (line == null) {
                    System.out.println("Server disconnected");
                    closeConnection();
                    return;
                }

                processMessage(line);
            }
        } catch (IOException e) {
            System.out.println("Server disconnected " + e);
            closeConnection();
        }
    }

    private void processMessage(String message) {
        String[] parts = message.split(";");

        Message msg;
        try {
            msg = Message.valueOf(parts[0]);
        } catch (IllegalArgumentException e) {
            System.out.println("Unvalid message: " + parts[0]);
            return;
        }

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
                // TODO
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
