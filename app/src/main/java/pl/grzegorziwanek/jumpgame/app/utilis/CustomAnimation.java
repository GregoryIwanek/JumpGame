package pl.grzegorziwanek.jumpgame.app.utilis;

import android.graphics.Bitmap;
import android.view.animation.Animation;

/**
 * Created by Grzegorz Iwanek on 25.08.2016.
 * Contains class which extends Animation. Responsible for making animation from given source .pgn file with series of drawings.
 * Is played in a loop with specific intervals.
 */
public class CustomAnimation extends Animation {

    private Bitmap[] mFrames;
    private int mCurrentFrame;
    private long mStartTime;
    private long mFramesInterval;

    public CustomAnimation(ObjectParameters p) {
        Bitmap imageRes = p.getImageRes();
        int numFrames = p.getNumFrames();
        int width = p.getWidth();
        int height = p.getHeight();

        setAnimationFrames(imageRes, numFrames, width, height);
        mStartTime = System.nanoTime();
        mFramesInterval = p.getFramesInterval();
    }

    private void setAnimationFrames(Bitmap imageRes, int numFrames, int width, int height) {
        mCurrentFrame = 0;
        mFrames = new Bitmap[numFrames];
        for (int i=0; i<mFrames.length; i++) {
            // x=0, because animation is set of images group vertically
            // every next image is a i*height
            mFrames[i] = Bitmap.createBitmap(imageRes, 0, i*height, width, height);
        }
    }

    public void update() {
        if(isPastFramesInterval()) {
            mCurrentFrame++;
            mStartTime = System.nanoTime();
        }
        if (isLoopFinished()) {
            mCurrentFrame = 0;
        }
    }

    private boolean isPastFramesInterval() {
        long mElapsed = (System.nanoTime() - mStartTime) / 1000000;
        return mElapsed > mFramesInterval;
    }

    private boolean isLoopFinished() {
        return mCurrentFrame == mFrames.length;
    }

    //get frame to draw on canvas
    public Bitmap getImage() {
        return mFrames[mCurrentFrame];
    }
}
