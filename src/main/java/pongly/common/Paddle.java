package pongly.common;

/**
 * A class that represents  a paddle in the game
 */
public class Paddle extends DrawableObject {

    /**
     * Creates a new Paddle
     *
     * @param x      The top left corner x coordinate
     * @param y      The top left corner y coordinate
     * @param height The height of the paddle
     */
    public Paddle(int x, int y, int height) {
        super(x, y, 1, height, "║");
    }

    /**
     * Moves the paddle up
     */
    public void moveUp() {
        y--;
    }

    /**
     * Moves the paddle down
     */
    public void moveDown() {
        y++;
    }
}
