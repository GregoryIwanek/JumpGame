package pl.grzegorziwanek.jumpgame.app.gameobjects;

import android.graphics.Rect;

/**
 * Created by Grzegorz Iwanek on 25.08.2016.
 * Contains top level class of game objects in a game;
 * Class implements Comparable ( for sake positioning elements on the list depending by Y value->
 * they need to be drawn in correct order, objects with bigger Y value: in foreground, objects closer to 0 axis: in background);
 */
public abstract class GameObject implements Comparable<GameObject> {
    protected int mX;
    protected int mY;
    protected int mDy;
    protected int mWidth;
    protected int mHeight;
    protected String mObjectType = "";

    @Override
    public int compareTo(GameObject compared) {
        if (comparePosition() > compared.comparePosition()) {
            return 1;
        } else if (comparePosition() < compared.comparePosition()) {
            return -1;
        } else {
            return 0;
        }
    }

    //setters
    public void setX(int x) {
        mX = x;
    }

    public void setY(int y) {
        mY = y;
    }

    //getters
    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int comparePosition() {
        return (mY + mHeight);
    }

    //get for checking collision
    public Rect getRectangle() {
        return new Rect(
                (int)(mX + 0.1*mWidth),
                (int)(mY + 0.1*mHeight),
                (int)(mX + 0.9*mWidth),
                (int)(mY + 0.9*mHeight));
    }

    public void setObjectType(String type) {
        mObjectType = type;
    }

    public String getObjectType() {
        return mObjectType;
    }
}