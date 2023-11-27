package pongly.common;

import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

@Command(name = "GameManager", mixinStandardHelpOptions = true, version = "1.0")
public class CommandLineOptions {

    @Option(names = {"-H", "--hostname"}, description = "Host name or IP address")
    private String hostname = "localhost";

    @Option(names = {"-P", "--port"}, description = "Port number")
    private int port = 1313;

    @Option(names = {"-T", "--thread"}, description = "Set the maximum number of threads")
    private int thread = 10;

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public int getThread() {
        return thread;
    }
}
