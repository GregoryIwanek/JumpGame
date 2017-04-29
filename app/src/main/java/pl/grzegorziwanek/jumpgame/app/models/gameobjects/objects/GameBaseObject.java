package pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects;

import android.graphics.Rect;
import android.support.annotation.NonNull;

/**
 * Created by Grzegorz Iwanek on 25.08.2016.
 * Contains top level class of game objects in a game;
 * Class implements Comparable ( for sake positioning elements on the list depending by Y value->
 * they need to be drawn in correct order, objects with bigger Y value: in foreground, objects closer to 0 axis: in background);
 */
public abstract class GameBaseObject implements Comparable<GameBaseObject> {
    protected int mX;
    protected int mY;
    protected int mDy;
    protected int mWidth;
    protected int mHeight;
    protected int mSpeed;
    protected String mObjectType = "";
    protected String mObjectSubtype = "";

    protected GameBaseObject(ObjectParameters p) {
        mX = p.getX();
        mY = p.getY();
        mWidth = p.getWidth();
        mHeight = p.getHeight();
        mSpeed = p.getSpeed();
        mObjectType = p.getType();
        mObjectSubtype = p.getSubType();
    }

    @Override
    public int compareTo(@NonNull GameBaseObject compared) {
        if (comparePosition() > compared.comparePosition()) {
            return 1;
        } else if (comparePosition() < compared.comparePosition()) {
            return -1;
        } else {
            return 0;
        }
    }

    public int getX() {
        return mX;
    }

    protected int comparePosition() {
        return (mY + mHeight);
    }

    // get for checking collision, smaller than real one to prevent collision with "empty spot"
    // in the png pictures
    public Rect getRectangle() {
        return new Rect(
                (int)(mX + 0.1*mWidth),
                (int)(mY + 0.1*mHeight),
                (int)(mX + 0.9*mWidth),
                (int)(mY + 0.9*mHeight));
    }

    public String getObjectType() {
        return mObjectType;
    }

    public String getObjectSubtype() {
        return mObjectSubtype;
    }
}