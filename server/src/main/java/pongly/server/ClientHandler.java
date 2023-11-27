package pongly.server;

import pongly.common.Message;
import pongly.common.Utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This class is used to represent a client handler on the server side
 */
public class ClientHandler implements Runnable {
    private static int ID = 0;
    private final int id = ID++;

    /**
     * The score of the player
     */
    public int score = 0;
    private final Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean running = true;
    private int clientLastPosition = 0;
    private boolean ready = false;
    private final Party party;

    /**
     * @param socket The socket of the client
     */
    public ClientHandler(Socket socket, Party party) throws IOException {
        this.party = party;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    /**
     * Method called in a reserved thread to receive messages from the client
     */
    @Override
    public void run() {
        try {
            while (running) {
                String message = in.readLine();
                processMessage(message);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Player " + id + " disconnected");
        } finally {
            closeResources();
        }
    }

    /**
     * Send the oponent position and the ball position to the client
     *
     * @param advPosition The position of the oponent
     * @param ballX       The x position of the ball
     * @param ballY       The y position of the ball
     */
    public void updatePosition(int advPosition, int ballX, int ballY) {
        sendMessage(Message.UPDATE_SERVER.name() + Utils.SEPARATOR + advPosition + Utils.SEPARATOR + ballX + Utils.SEPARATOR + ballY + Utils.EndLineChar);
    }

    /**
     * Send the score to the client
     *
     * @param advScore The score of the oponent
     */
    public void sendScore(int advScore) {
        sendMessage(Message.UPDATE_SCORE.name() + Utils.SEPARATOR + advScore + Utils.SEPARATOR + score + Utils.EndLineChar);
    }

    /**
     * getter for the client last position
     */
    public int getClientLastPosition() {
        return clientLastPosition;
    }

    /**
     * Close the communication and send a message to the client to quit the game
     */
    public void endCommunication() {
        sendMessage(Message.QUIT.name() + Utils.EndLineChar);
        closeResources();
    }

    /**
     * @return the id of the client
     */
    public int getId() {
        return id;
    }

    /**
     * @return true if the client is connected, false otherwise
     */
    public boolean isConnected() {
        return socket.isConnected() && !socket.isClosed();
    }

    /**
     * @return true if the client is ready, false otherwise
     */
    public boolean isReady() {
        return ready;
    }

    private void sendMessage(String message) {
        try {
            out.write(message);
            out.flush();
        } catch (IOException e) {
            System.out.println("Error while sending message: " + e);
            running = false;
            closeResources();
        }
    }

    private void closeResources() {
        System.out.println("Closing resources for player " + id);

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

    private void processMessage(String message) {
        if (message == null)
            throw new IllegalArgumentException("Message cannot be null");

        String[] parts = message.split(";");

        Message msg;
        try {
            msg = Message.valueOf(parts[0]);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid message " + parts[0]);
            return;
        }

        manageReceivedMessages(msg, parts);
    }

    private void manageReceivedMessages(Message msg, String[] parts) {
        switch (msg) {
            case READY:
                System.out.println("Player " + id + " is ready");
                ready = true;
                break;
            case UPDATE_PLAYER:
                clientLastPosition = Integer.parseInt(parts[1]);
                break;
            case QUIT:
                party.removePlayer(this);
                running = false;
                closeResources();
                break;
            default:
                // TODO
                break;
        }
    }
}
