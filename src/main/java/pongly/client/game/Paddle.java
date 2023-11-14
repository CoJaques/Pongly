package pongly.client.game;

public class Paddle extends DrawableObject {
    public Paddle(int x, int y, String displayString) {
        super(x, y, displayString);
    }

    public void moveUp() {
        if (y > 0) y--; // TODO avoid overload
    }

    public void moveDown(int screenHeight) {
        if (y < screenHeight) y++;
    }
}
