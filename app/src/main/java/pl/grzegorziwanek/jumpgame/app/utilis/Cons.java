package pl.grzegorziwanek.jumpgame.app.utilis;

public abstract class Cons {
    public static final int MOVE_SPEED = -5; //movement speed of background image
    public static final int SPAWN_MARGIN = 55; //margin for spawning objects without collision with text
    public static final int START_POSITION = 150; //starting position of a player
    public static final int TARGET_FPS = 30;//target FPS of game

    public static int sScreenWidth = 0;
    public static int sScreenHeight = 0;

    public static void initScreenSize(int width, int height) {
        sScreenWidth = width;
        sScreenHeight = height;
    }
}
