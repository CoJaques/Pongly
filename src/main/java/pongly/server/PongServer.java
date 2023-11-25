package pongly.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PongServer {
    private static final int PORT = 1313;
    private static final int NUMBER_OF_THREADS = 10;
    private final List<Party> parties = new ArrayList<>();

    public static void main(String[] args) {
        new PongServer().startServer();
    }

    public void startServer() {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Pong Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Party party = findAvailableParty();
                ClientHandler handler = new ClientHandler(clientSocket, party);
                executor.execute(handler);

                System.out.println("New client connected from " + clientSocket.getInetAddress().getHostAddress());

                party.addPlayer(handler);
                executor.submit(handler);

                if (party.isFull()) {
                    party.startGame();
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e);
        } finally {
            executor.shutdown();
        }
    }

    private Party findAvailableParty() {
        for (Party party : parties) {
            if (!party.isFull()) {
                return party;
            }
        }
        Party newParty = new Party();
        parties.add(newParty);
        return newParty;
    }
}
