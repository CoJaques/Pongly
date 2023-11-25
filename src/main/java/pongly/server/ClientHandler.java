package pongly.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Party party;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean running = true;

    private int clientLastPosition = 0;

    public ClientHandler(Socket socket, Party party) {
        this.socket = socket;
        this.party = party;
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
                System.out.println("Received message from client: " + message);

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
        switch (parts[0]) {
            case "UPDATE":
                clientLastPosition = Integer.parseInt(parts[1]);
                break;
        }
    }

    public void sendMessage(String message) throws IOException {
//        System.out.println("Sending message to client: " + message);
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

    public void stopRunning() {
        running = false;
    }

    public int getClientLastPosition() {
        return clientLastPosition;
    }
}
