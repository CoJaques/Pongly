package pongly.common;

/**
 * A class that represents the ball in the game
 */
public class Ball extends DrawableObject {
    private final int initialPositionX;
    private final int initialPositionY;
    private int xVelocity = 1;
    private int yVelocity = 0;
    private int initialXVelocity = 1;

    /**
     * Creates a new Ball
     *
     * @param x The top left corner x coordinate
     * @param y The top left corner y coordinate
     */
    public Ball(int x, int y) {
        super(x, y, "O");
        initialPositionX = x;
        initialPositionY = y;
    }

    /**
     * updates the position of the ball
     */
    public void update() {
        x += xVelocity;
        y += yVelocity;
    }

    /**
     * reverse the x direction of the ball
     */
    public void reverseXDirection() {
        xVelocity *= -1;
    }

    /**
     * reverse the y direction of the ball
     */
    public void reverseYDirection() {
        yVelocity *= -1;
    }

    /**
     * set the yVelocity to -1
     */
    public void goDiagonalUp() {
        yVelocity = -1;
    }

    /**
     * set the yVelocity to 1
     */
    public void goDiagonalDown() {
        yVelocity = 1;
    }

    /**
     * set the yVelocity to 0
     */
    public void goStraight() {
        yVelocity = 0;
    }

    /**
     * @return the y velocity of the ball
     */
    public int getYVelocity() {
        return yVelocity;
    }

    /**
     * Set the X position of the ball
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set the Y position of the ball
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Reset the ball to its initial position
     */
    public void reset() {

        xVelocity = initialXVelocity;
        initialXVelocity *= -1;
        yVelocity = 0;

        x = initialPositionX;
        y = initialPositionY;
    }
}
