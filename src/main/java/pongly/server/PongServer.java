package pongly.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PongServer {
    private static final int PORT = 1234;
    private static final int SERVER_ID = (int) (Math.random() * 1000000);
    private static final int NUMBER_OF_THREADS = 2;
    private static final String TEXTUAL_DATA = "👋 from Server " + SERVER_ID;

    public static void main(String[] args) {
        ExecutorService executor = null;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(new ClientHandler(clientSocket));

                // add player to a party, create new party when 2 players are connected
            }
        } catch (IOException e) {
            System.out.println("[Server " + SERVER_ID + "] exception: " + e);
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }
}
