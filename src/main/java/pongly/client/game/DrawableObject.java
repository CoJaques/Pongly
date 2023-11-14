package pongly.client.game;

public abstract class DrawableObject {
    public DrawableObject(int x, int y, String displayString) {
        this.x = x;
        this.y = y;
        this.displayString = displayString;
    }

    private final String displayString;
    protected int x, y;

    public String getDisplayString() {
        return displayString;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
