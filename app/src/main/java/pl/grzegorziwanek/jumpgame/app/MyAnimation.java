package pl.grzegorziwanek.jumpgame.app;

import android.graphics.Bitmap;
import android.view.animation.Animation;

/**
 * Created by Grzegorz Iwanek on 25.08.2016.
 * Contains class which extends Animation. Responsible for making animation from given source .pgn file with series of drawings.
 * Can be played once, or in constant loop (mPlayedOnce).
 */
public class MyAnimation extends Animation
{
    private Bitmap[] mFrames; //array with every single frame of an animation (.png source file)
    private int mCurrentFrame;
    private long mStartTime;
    private long mDelay; //used as time between showing next frame
    private boolean mPlayedOnce = false; //used to determine if animation was completed (at least one time)

    public void setFrames(Bitmap[] frames)
    {
        //assign frames to show
        mFrames = frames;
        mCurrentFrame = 0;
        mStartTime = System.nanoTime();
    }

    public void setDelay(int delay)
    {
        mDelay = delay;
    }

    public void setFrame(int currentFrame)
    {
        mCurrentFrame = currentFrame;
    }

    public void update()
    {
        //get time passed since last frame was draw
        long mElapsed = (System.nanoTime() - mStartTime) / 1000000;

        //increment if delay was passed
        if(mElapsed > mDelay)
        {
            mCurrentFrame++;
            mStartTime = System.nanoTime();
        }

        //if all frames were shown, reset count and set loop has been completed (at least once)
        if(mCurrentFrame == mFrames.length)
        {
            mCurrentFrame = 0;
            mPlayedOnce = true;
        }
    }

    //get frame to draw on canvas
    public Bitmap getImage()
    {
        return mFrames[mCurrentFrame];
    }

    public int getCurrentFrame()
    {
        return mCurrentFrame;
    }

    public boolean getPlayedOnce()
    {
        return mPlayedOnce;
    }
}
