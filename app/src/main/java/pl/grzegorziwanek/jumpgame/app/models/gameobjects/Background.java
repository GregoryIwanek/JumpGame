package pl.grzegorziwanek.jumpgame.app.models.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import pl.grzegorziwanek.jumpgame.app.utilis.Cons;


/**
 * Created by Grzegorz Iwanek on 25.08.2016.
 * Contains class responsible for storing information about background mImage, updating them (position), and redrawing them on given canvas
 */
public class Background {

    private Bitmap mImage;
    private int mX = 0;
    private int mY = 0;
    private int mDx = 0;
    private int mScreenWidth = 0;

    public Background(Bitmap image, int screenWidth) {
        mImage = image;
        mScreenWidth = screenWidth;
        mDx = Cons.MOVE_SPEED;
    }

    public void update() {
        //update position of background
        mX += mDx;

        //if mImage is off screen, reset it's position to start position
        if (mX <= -mScreenWidth) {
            mX =0;
        }
    }

    public void draw(Canvas canvas) {
        //draw first background mImage on the canvas
        canvas.drawBitmap(mImage, mX, mY, null);

        //draw second mImage, just right after first one ( illusion of continuous background)
        canvas.drawBitmap(mImage, mX + mScreenWidth, mY, null);
    }
}
