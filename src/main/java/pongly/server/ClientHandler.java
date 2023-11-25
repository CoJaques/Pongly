package pongly.server;

import pongly.common.Message;
import pongly.common.Utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {

    public int score = 0;
    private final Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean running = true;

    private int clientLastPosition = 0;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("ClientHandler exception: " + e);
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                String message = in.readLine();

                if (message != null) {
                    processMessage(message);
                }
            }
        } catch (IOException e) {
            System.out.println("ClientHandler exception: " + e);
        } finally {
            closeResources();
        }
    }

    private void processMessage(String message) {
        String[] parts = message.split(";");

        Message msg;
        try {
            msg = Message.valueOf(parts[0]);
        } catch (IllegalArgumentException e) {
            // Gérez le cas où la chaîne ne correspond à aucune énumération
            System.out.println("Message non valide: " + parts[0]);
            return;
        }

        switch (msg) {
            case UPDATE_PLAYER:
                clientLastPosition = Integer.parseInt(parts[1]);
                break;
            case QUIT:
                // TODO
                break;
            default:
                // TODO
                break;
        }
    }

    public void updatePosition(int advPosition, int ballX, int ballY) throws IOException {
        sendMessage(Message.UPDATE_SERVER.name() + Utils.SEPARATOR + advPosition + Utils.SEPARATOR + ballX + Utils.SEPARATOR + ballY + Utils.EndLineChar);
    }

    public void sendScore(int advScore) throws IOException {
        sendMessage(Message.UPDATE_SCORE.name() + Utils.SEPARATOR + advScore + Utils.SEPARATOR + score + Utils.EndLineChar);
    }

    private void sendMessage(String message) throws IOException {
        out.write(message);
        out.flush();
    }

    private void closeResources() {
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
        } catch (IOException e) {
            System.out.println("Error closing resources: " + e);
        }
    }

    public int getClientLastPosition() {
        return clientLastPosition;
    }
}
