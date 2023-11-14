package pongly.client;

import com.googlecode.lanterna.input.KeyStroke;
import pongly.client.game.Paddle;

import java.io.IOException;

public class InputHandler {

    private final DisplayManager display;
    private final Paddle playerPaddle;
    private final Paddle aiPaddle;
    private boolean exit;

    public InputHandler(DisplayManager display, Paddle playerPaddle, Paddle aiPaddle) {
        this.display = display;
        this.playerPaddle = playerPaddle;
        this.aiPaddle = aiPaddle;
        this.exit = false;
    }

    public void processInput() throws IOException {
        KeyStroke keyStroke = display.getScreen().pollInput();

        if (keyStroke == null)
            return;

        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                if (playerPaddle.getY() > 0) {
                    playerPaddle.moveUp();
                }
                break;
            case ArrowDown:
                if (playerPaddle.getY() < display.getScreenHeight() - 1) {
                    playerPaddle.moveDown();
                }
                break;
            case Character:
                if (keyStroke.getCharacter() == 'q') {
                    exit = true;
                }
                break;
        }
    }

    public boolean shouldExit() {
        return exit;
    }
}
