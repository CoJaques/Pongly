package pongly.server;

import picocli.CommandLine;
import pongly.common.CommandLineOptions;

public class ServerLauncher {
    public static void main(String[] args) {
        CommandLineOptions cmdOptions = new CommandLineOptions();
        new CommandLine(cmdOptions).parseArgs(args);

        PongServer server = new PongServer(cmdOptions.getPort(), cmdOptions.getThread());
        server.startServer();
    }
}
