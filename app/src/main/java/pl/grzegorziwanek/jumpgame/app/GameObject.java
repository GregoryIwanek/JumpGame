package pl.grzegorziwanek.jumpgame.app;

import android.graphics.Rect;

/**
 * Created by Grzegorz Iwanek on 25.08.2016.
 */
public abstract class GameObject
{
    protected int mX;
    protected int mY;
    protected int mDy;
    protected int mWidth;
    protected int mHeight;

    //setters
    public void setX(int x)
    {
        mX = x;
    }
    public void setY(int y)
    {
        mY = y;
    }

    //getters
    public int getX()
    {
        return mX;
    }

    public int getY()
    {
        return mY;
    }

    public int getWidth()
    {
        return mWidth;
    }

    public int getHeight()
    {
        return mHeight;
    }

    //get for checking collision
    public Rect getRectangle()
    {
        return new Rect(mX, mY, mX+mWidth, mY+mHeight);
    }
}