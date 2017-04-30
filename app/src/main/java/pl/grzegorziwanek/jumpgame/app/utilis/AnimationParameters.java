package pl.grzegorziwanek.jumpgame.app.utilis;

/**
 * Wrapper class for drawable animation information.
 */
public class AnimationParameters {

    private int resId;
    private int numFrames;

    public AnimationParameters(int id, int frames) {
        resId = id;
        numFrames = frames;
    }

    public int getResId() {
        return resId;
    }

    public int getNumFrames() {
        return numFrames;
    }
}
