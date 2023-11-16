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

/**
 * This class is responsible for handling the display
 */
public class DisplayManager {
    private final Screen screen;
    private final int screenHeight;
    private final int screenWidth;

    /**
     * @param column The width of the screen
     * @param row    The height of the screen
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * @param objects The list of objects to draw
     * @throws IOException if an I/O error occurs
     */
    public void drawObjects(List<DrawableObject> objects) throws IOException {
        screen.clear();

        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);

        for (var object : objects) {
            drawObject(object, textGraphics);
        }

        screen.refresh();
    }

    /**
     * Clear the screen
     *
     * @throws IOException if an I/O error occurs
     */
    public void clear() throws IOException {
        screen.clear();
        screen.refresh();
    }

    /**
     * Close the screen
     *
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException {
        screen.close();
    }

    /**
     * @return the height of the screen
     */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * @return the width of the screen
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * @return the screen
     */
    public Screen getScreen() {
        return screen;
    }

    private void drawObject(DrawableObject object, TextGraphics textGraphics) {
        for (int i = 0; i < object.getWidth(); i++)
            for (int j = 0; j < object.getHeight(); j++)
                textGraphics.putString(object.getX() + i, object.getY() + j, object.getDisplayString());
    }
}
