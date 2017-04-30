package pl.grzegorziwanek.jumpgame.app.utilis;

import java.util.HashMap;
import java.util.Map;

import pl.grzegorziwanek.jumpgame.app.R;

public abstract class Cons {
    public static final int MOVE_SPEED = -5; //movement speed of background image
    public static final int SPAWN_MARGIN = 55; //margin for spawning objects without collision with text
    public static final int START_POSITION = 150; //starting position of a player
    public static final int TARGET_FPS = 30;//target FPS of game


    public static int sScreenWidth;
    public static int sScreenHeight;
    public static int sSpawnX;

    public static Map<String, AnimationParameters> drawableMap;

    public static void initScreenSize(int width, int height) {
        sScreenWidth = width;
        sScreenHeight = height;
        sSpawnX = width + 30;
    }

    /**
     *
     */
    public static void initDrawableMap() {
        drawableMap = new HashMap<>();
        drawableMap.put("CAT_STATIC_COLOR_1", new AnimationParameters(R.drawable.cat_static_black, 1));
        drawableMap.put("CAT_SLOW_COLOR_1", new AnimationParameters(R.drawable.cat_slow_black, 12));
        drawableMap.put("CAT_MEDIUM_COLOR_1", new AnimationParameters(R.drawable.cat_medium_black, 16));
        drawableMap.put("CAT_FAST_COLOR_1", new AnimationParameters(R.drawable.cat_fast_black, 13));
        drawableMap.put("CAT_STATIC_COLOR_2", new AnimationParameters(R.drawable.cat_static_white, 1));
        drawableMap.put("CAT_SLOW_COLOR_2", new AnimationParameters(R.drawable.cat_slow_white, 12));
        drawableMap.put("CAT_MEDIUM_COLOR_2", new AnimationParameters(R.drawable.cat_medium_white, 16));
        drawableMap.put("CAT_FAST_COLOR_2", new AnimationParameters(R.drawable.cat_fast_white, 13));

        drawableMap.put("BALL_STATIC_COLOR_1", new AnimationParameters(R.drawable.ball_4colors, 1));
        drawableMap.put("BALL_SLOW_COLOR_1", new AnimationParameters(R.drawable.ball_4colors, 4));
        drawableMap.put("BALL_MEDIUM_COLOR_1", new AnimationParameters(R.drawable.ball_4colors, 4));
        drawableMap.put("BALL_FAST_COLOR_1", new AnimationParameters(R.drawable.ball_4colors, 4));
        drawableMap.put("BALL_STATIC_COLOR_2", new AnimationParameters(R.drawable.ball_8colors, 1));
        drawableMap.put("BALL_SLOW_COLOR_2", new AnimationParameters(R.drawable.ball_8colors, 8));
        drawableMap.put("BALL_MEDIUM_COLOR_2", new AnimationParameters(R.drawable.ball_8colors, 8));
        drawableMap.put("BALL_FAST_COLOR_2", new AnimationParameters(R.drawable.ball_8colors, 8));
    }
}
