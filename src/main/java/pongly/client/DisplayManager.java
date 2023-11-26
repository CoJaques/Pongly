package pongly.client;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import pongly.common.DrawableObject;

import java.io.IOException;
import java.util.List;

import static pongly.common.Utils.SCREEN_HEIGHT;
import static pongly.common.Utils.SCREEN_WIDTH;

/**
 * This class is responsible for handling the display
 */
public class DisplayManager {
    private final Terminal terminal;
    private final Screen screen;

    /**
     * @param column The width of the screen
     * @param row    The height of the screen
     * @throws IOException if an I/O error occurs
     */
    public DisplayManager(int column, int row) throws IOException {
        TerminalSize defaultTerminalSize = new TerminalSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        DefaultTerminalFactory factory = new DefaultTerminalFactory().setInitialTerminalSize(defaultTerminalSize);
        terminal = factory.createTerminal();
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
     * Close the screen
     *
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException {
        screen.close();
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

    public void drawTitle() throws IOException {
        screen.clear();

        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.putString(0, 0, "Pongly");
        textGraphics.putString(0, 1, "Press enter key to start");
        screen.refresh();
    }
}
