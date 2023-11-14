package pongly.client;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import pongly.client.game.Paddle;

import java.io.IOException;

public class InputHandler {

    private final Screen screen;
    private final Paddle playerPaddle;
    private final Paddle aiPaddle;
    private boolean exit;

    public InputHandler(Screen screen, Paddle playerPaddle, Paddle aiPaddle) {
        this.screen = screen;
        this.playerPaddle = playerPaddle;
        this.aiPaddle = aiPaddle;
        this.exit = false;
    }

    public void processInput() throws IOException {
        KeyStroke keyStroke = screen.pollInput();

        if (keyStroke != null && keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == 'q') {
            exit = true;
        } else if (keyStroke != null && keyStroke.getKeyType() == KeyType.ArrowUp) {
            playerPaddle.moveUp();
        } else if (keyStroke != null && keyStroke.getKeyType() == KeyType.ArrowDown) {
            playerPaddle.moveDown(screen.getTerminalSize().getRows());
        }
    }

    public boolean shouldExit() {
        return exit;
    }
}
