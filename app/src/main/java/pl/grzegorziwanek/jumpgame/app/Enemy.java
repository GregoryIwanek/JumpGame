package pl.grzegorziwanek.jumpgame.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Grzegorz Iwanek on 26.08.2016.
 */
public class Enemy extends GameObject
{
    private int mSpeed;
    private MyAnimation mAnimation = new MyAnimation();
    private Bitmap mFramesheet;

    private String mTypeOfObject = "enemy";

    public Enemy(Bitmap res, int x, int y, int width, int height, int speed, int numFrames, String typeOfObject)
    {
        mTypeOfObject = typeOfObject;
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        mSpeed = speed;

        Bitmap[] imageArray = new Bitmap[numFrames];
        mFramesheet = res;
        for (int i=0; i<imageArray.length; i++)
        {
            imageArray[i] = Bitmap.createBitmap(mFramesheet, 0, i*mHeight, mWidth, mHeight);
        }

        mAnimation.setFrames(imageArray);
        mAnimation.setDelay(100-mSpeed);
    }

    public void update()
    {
        mX -= mSpeed;
        mAnimation.update();
    }

    public void draw(Canvas canvas)
    {
        try
        {
            canvas.drawBitmap(mAnimation.getImage(), mX, mY, null);
        }
        catch (Exception e)
        {
            System.out.println("Enemy, drawing exception");
        }
    }

    @Override
    public int getWidth() {
        return  mWidth - 10;
    }
}