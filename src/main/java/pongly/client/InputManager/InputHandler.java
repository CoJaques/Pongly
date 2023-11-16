package pongly.client.InputManager;

import com.googlecode.lanterna.input.KeyStroke;
import pongly.client.DisplayManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for handling user input.
 */
public class InputHandler {
    private final DisplayManager display;
    private final List<KeyListener> listeners = new ArrayList<>();
    private boolean IsStopRequested = false;
    private final Thread inputThread = new Thread(() -> {
        try {
            this.processInput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });

    /**
     * @param display DisplayManager instance
     */
    public InputHandler(DisplayManager display) {
        this.display = display;
        inputThread.start();
    }

    /**
     * add a listener to the list of KeyEvent listeners
     *
     * @param listener KeyListener instance
     */
    public void addListener(KeyListener listener) {
        listeners.add(listener);
    }

    /**
     * Launch the exit process of the inputHandler
     */
    public void triggerExit() {
        IsStopRequested = true;

        try {
            inputThread.join(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Process the input from the user
     * Notify each listener when an input is pressed
     *
     * @throws IOException if an I/O error occurs
     */
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
