package pongly.client.InputManager;

import com.googlecode.lanterna.input.KeyStroke;

import java.util.EventListener;

public interface KeyListener extends EventListener {
    void onKeyPressed(KeyStroke keyStroke);
}
