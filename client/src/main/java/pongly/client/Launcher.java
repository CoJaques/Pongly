package pongly.client;

import java.io.IOException;

import picocli.CommandLine;
import pongly.common.CommandLineOptions;

public class Launcher {

    public static void main(String[] args) {
        CommandLineOptions cmdOptions = new CommandLineOptions();
        new CommandLine(cmdOptions).parseArgs(args);

        try {
            GameManager gameManager = new GameManager(cmdOptions.getHostname(), cmdOptions.getPort());
            gameManager.run();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
