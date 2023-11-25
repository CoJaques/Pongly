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
        try {
            out.write(Message.UPDATE_PLAYER.name() + Utils.SEPARATOR + gameManager.getPlayerOnePaddleY() + Utils.EndLineChar);
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
               processMessage(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            case QUIT:
                // TODO
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

        gameManager.setPlayerTwoPaddleY(yPaddlePlayerTwo);
        gameManager.updateBallPosition(xBall, yBall);
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
