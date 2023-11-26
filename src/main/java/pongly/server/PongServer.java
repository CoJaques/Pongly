package pongly.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
        ExecutorService clientExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        ExecutorService gameExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS / 2);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Pong Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Party party = findAvailableParty();

                ClientHandler handler;
                try {
                    handler = new ClientHandler(clientSocket);
                } catch (IOException e) {
                    System.out.println("Error during handler connection : " + e.getMessage());
                    continue;
                }

                checkPartiesStatus();
                clientExecutor.execute(handler);

                party.addPlayer(handler);

                if (party.isFull()) {
                    gameExecutor.execute(party);
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e);
        } finally {
            gameExecutor.shutdown();
            clientExecutor.shutdown();
        }
    }

    private void checkPartiesStatus() {
        for (Party party : parties) {
            if (party.isFinished()) {
                parties.remove(party);
                System.out.println("Destroying party " + party.getId());
            }
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
