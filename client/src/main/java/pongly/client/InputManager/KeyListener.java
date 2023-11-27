package pongly.client.InputManager;

import com.googlecode.lanterna.input.KeyStroke;

import java.util.EventListener;

/**
 * This interface is used to listen to key events
 */
public interface KeyListener extends EventListener {

    /**
     * Method called when a KeyEvent is triggered
     *
     * @param keyStroke KeyStroke instance
     */
    void onKeyPressed(KeyStroke keyStroke);
}
