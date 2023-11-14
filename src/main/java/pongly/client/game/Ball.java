package pongly.client.game;

public class Ball extends DrawableObject {
    private int xVelocity = 1;
    private int yVelocity = 1;

    public Ball(int x, int y) {
        super(x, y, "O");
    }

    public void update() {
        x += xVelocity;
        y += yVelocity;
    }

    public void reverseXDirection() {
        xVelocity *= -1;
    }

    public void reverseYDirection() {
        yVelocity *= -1;
    }
}
