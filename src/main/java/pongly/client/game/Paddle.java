package pongly.client.game;

public class Paddle extends DrawableObject {
    public Paddle(int x, int y, String displayString) {
        super(x, y, displayString);
    }

    public void moveUp() {
        y--;
    }

    public void moveDown() {
        y++;
    }
}
