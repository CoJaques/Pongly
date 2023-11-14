package pongly.client;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import pongly.client.game.DrawableObject;

import java.io.IOException;
import java.util.List;

public class DisplayManager {
    private final Screen screen;
    private final int screenHeight;
    private final int screenWidth;

    public DisplayManager(int column, int row) throws IOException {
        screenHeight = row;
        screenWidth = column;
        TerminalSize defaultTerminalSize = new TerminalSize(screenWidth, screenHeight);
        DefaultTerminalFactory factory = new DefaultTerminalFactory().setInitialTerminalSize(defaultTerminalSize);
        Terminal terminal = factory.createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null); // we don't need a cursor
        screen.doResizeIfNecessary();
    }

    public void draw(List<DrawableObject> objects) throws IOException {
        screen.clear();

        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);

        for (var object : objects) {
            textGraphics.putString(object.getX(), object.getY(), object.getDisplayString());
        }

        screen.refresh();
    }

    public void clear() throws IOException {
        screen.clear();
        screen.refresh();
    }

    public void close() throws IOException {
        screen.close();
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public Screen getScreen() {
        return screen;
    }
}
