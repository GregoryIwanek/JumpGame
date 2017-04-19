package pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel;

import android.view.SurfaceHolder;

import pl.grzegorziwanek.jumpgame.app.models.gamemodel.Callbacks.ThreadCallback;
import pl.grzegorziwanek.jumpgame.app.utilis.Cons;

public class SessionThread extends Thread {

    private ThreadCallback mCallback;
    private SurfaceHolder mSurfaceHolder;
    private boolean mRunning;
    private long mTargetTime = 1000/Cons.TARGET_FPS;
    private long mStartTime = 0;
    private long mTameTaken = 0;
    private long mWaitTime = 0;
    private long mTotalTime = 0;
    private int mFrameCount = 0;
    private long mAverageFPS = 0;

    public SessionThread(SurfaceHolder surfaceHolder, ThreadCallback callback) {
        mSurfaceHolder = surfaceHolder;
        mCallback = callback;
    }

    @Override
    public void run() {
        // game loop
        while (mRunning) {
            mStartTime = System.nanoTime();

            drawAndUpdateCanvas();
            setThreadSleep();

            // TODO: 19.04.2017 to remove
            mFrameCount++;
            mTotalTime += System.nanoTime() - mStartTime;

            printAverageFPS();
        }
    }

    private void drawAndUpdateCanvas() {
        //series of methods to draw and update game canvas
        //scheme: lock canvas->update and draw on->unlock canvas
        try {
            mCallback.lockCanvasCallback();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mCallback.unlockCanvasCallback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setThreadSleep() {
        mTameTaken =(System.nanoTime() - mStartTime)/1000000;
        mWaitTime = mTargetTime - mTameTaken;
        if (mWaitTime > 0) {
            try {
                sleep(mWaitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void printAverageFPS() {
        //TODO:remove that method, just for measuring speed of app in emulator
        //calculate information about current FPS speed of an app
        if (mFrameCount == Cons.TARGET_FPS) {
            mAverageFPS = 1000/((mTotalTime/mFrameCount)/1000000);
            System.out.println("average fps is: " + mAverageFPS);
            mFrameCount = 0;
            mTotalTime = 0;
        }
    }

    public void setRunning(boolean running) {
        mRunning = running;
    }
}

