package pongly.client;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import pongly.common.DrawableObject;
import pongly.common.Utils;

import java.io.IOException;
import java.util.List;

/**
 * This class is responsible for handling the display
 */
public class DisplayManager {
    private final Screen screen;

    /**
     * @param column The width of the screen
     * @param row    The height of the screen
     * @throws IOException if an I/O error occurs
     */
    public DisplayManager(int column, int row) throws IOException {
        TerminalSize defaultTerminalSize = new TerminalSize(Utils.SCREEN_WIDTH, Utils.SCREEN_HEIGHT);
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

        objects.forEach(object -> drawObject(object, textGraphics));

        screen.refresh();
    }

    /**
     * @throws IOException if an I/O error occurs
     */
    public void drawTitle() throws IOException {
        screen.clear();

        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.putString(0, 0, "Pongly");
        textGraphics.putString(0, 1, "Press enter key to start");
        screen.refresh();
    }

    /**
     * @param score  The score of the player
     * @param score1 The score of the opponent
     * @throws IOException if an I/O error occurs
     */
    public void drawScore(int score, int score1) throws IOException {
        screen.clear();

        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        int centerX = Utils.SCREEN_WIDTH / 2;
        int centerY = Utils.SCREEN_HEIGHT / 2;

        String result;
        if (score > score1) {
            result = "YOU WIN";
            textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
        } else if (score < score1) {
            result = "YOU LOOSE";
            textGraphics.setForegroundColor(TextColor.ANSI.RED);
        } else {
            result = "EQUALITY";
            textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
        }

        textGraphics.putString(centerX - 10, centerY, "Score: " + score + " - " + score1);
        textGraphics.putString(centerX - result.length() / 2, centerY + 1, result);

        screen.refresh();
    }

    /**
     * Close the screen
     *
     * @throws IOException if an I/O error occurs
     */
    public void close() {
        try {
            screen.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close the screen" + e);
        }
    }

    /**
     * wait for the user to press a key and return the key pressed
     *
     * @return the input read from the screen
     */
    public KeyStroke readInput() throws IOException {
        return screen.readInput();
    }

    /**
     * @param message The message to display
     * @throws IOException if an I/O error occurs
     */
    public void drawError(String message) throws IOException {
        screen.clear();

        TextGraphics textGraphics = screen.newTextGraphics();
        textGraphics.putString(0, 0, message);
        screen.refresh();
    }

    private void drawObject(DrawableObject object, TextGraphics textGraphics) {
        for (int i = 0; i < object.getWidth(); i++)
            for (int j = 0; j < object.getHeight(); j++)
                textGraphics.putString(object.getX() + i, object.getY() + j, object.getDisplayString());
    }
}
