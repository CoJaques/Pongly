package pongly.client.game;

/**
 * A class that represents an object that can be drawn on the screen
 */
public abstract class DrawableObject {

    private final String displayString;
    /**
     * The top left corner x coordinate
     */
    protected int x;
    /**
     * The top left corner y coordinate
     */
    protected int y;
    /**
     * The width of the object
     */
    protected int width;
    /**
     * The height of the object
     */
    protected int height;

    /**
     * Creates a new DrawableObject
     *
     * @param x             The top left corner x coordinate
     * @param y             The top left corner y coordinate
     * @param width         The width of the object
     * @param height        The height of the object
     * @param displayString The string to display for this object
     */
    public DrawableObject(int x, int y, int width, int height, String displayString) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.displayString = displayString;
    }

    /**
     * Creates a new DrawableObject with a width and height of 1
     *
     * @param x             The top left corner x coordinate
     * @param y             The top left corner y coordinate
     * @param displayString The string to display for this object
     */
    public DrawableObject(int x, int y, String displayString) {
        this(x, y, 1, 1, displayString);
    }

    /**
     * @return the string to display for this object
     */
    public String getDisplayString() {
        return displayString;
    }

    /**
     * @return the top left corner x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return the top left corner y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @return the width of the object
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the object
     */
    public int getHeight() {
        return height;
    }
}
