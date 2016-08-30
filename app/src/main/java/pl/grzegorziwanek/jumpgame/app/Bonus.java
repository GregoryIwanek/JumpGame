package pl.grzegorziwanek.jumpgame.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Grzegorz Iwanek on 27.08.2016.
 * Contains class responsible for static objects (no animation, just static image);
 * these objects impact specific characteristic of a game and a player when in collision with Player;
 */
public class Bonus extends GameObject implements GameObjectService
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

    @Override
    public void update()
    {
        mX += mSpeed;
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(mImage, mX, mY, null);
    }
}
