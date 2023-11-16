package pongly.client.InputManager;

import com.googlecode.lanterna.input.KeyStroke;
import pongly.client.DisplayManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputHandler {
    private boolean IsStopRequested = false;
    private final DisplayManager display;
    private final List<KeyListener> listeners = new ArrayList<>();
    private final Thread inputThread = new Thread(() -> {
        try {
            this.processInput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });

    public InputHandler(DisplayManager display) {
        this.display = display;
        inputThread.start();
    }

    public void addListener(KeyListener listener) {
        listeners.add(listener);
    }

    public void triggerExit() {
        IsStopRequested = true;

        try {
            inputThread.join(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void processInput() throws IOException {
        while (!IsStopRequested) {
            KeyStroke keyStroke = display.getScreen().readInput();
            notifyListeners(keyStroke);
        }
    }

    private void notifyListeners(KeyStroke keyStroke) {
        for (KeyListener listener : listeners) {
            listener.onKeyPressed(keyStroke);
        }
    }
}
