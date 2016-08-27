package pl.grzegorziwanek.jumpgame.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Grzegorz Iwanek on 27.08.2016.
 */
public class Bonus extends GameObject
{
    private int mSpeed;
    private Bitmap mImage;

    public Bonus(Bitmap res, int x, int y, int width, int height, int speed)
    {
        mImage = res;
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        mSpeed = speed;
    }

    public void update()
    {
        mX += mSpeed;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(mImage, mX, mY, null);
    }
}
