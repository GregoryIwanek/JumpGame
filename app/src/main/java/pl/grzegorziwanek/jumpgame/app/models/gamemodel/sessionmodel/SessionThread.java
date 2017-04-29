package pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel;

import pl.grzegorziwanek.jumpgame.app.models.gamemodel.Callbacks.ThreadCallback;
import pl.grzegorziwanek.jumpgame.app.utilis.Cons;

public class SessionThread extends Thread {

    private ThreadCallback mCallback;
    private boolean isRunning;
    private long mStartTime = 0;

    public SessionThread(ThreadCallback callback) {
        mCallback = callback;
    }

    @Override
    public void run() {
        // game loop
        while (isRunning) {
            mStartTime = System.nanoTime();
            drawAndUpdateCanvas();
            setThreadSleep();
        }
    }

    private void drawAndUpdateCanvas() {
        //series of methods to draw and update game canvas
        //scheme: lock canvas->update and draw on->unlock canvas
        try {
            mCallback.tryLockCanvasCallback();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mCallback.tryUnlockCanvasCallback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setThreadSleep() {
        long mTameTaken = (System.nanoTime() - mStartTime) / 1000000;
        long mTargetTime = 1000 / Cons.TARGET_FPS;
        long mWaitTime = mTargetTime - mTameTaken;
        if (mWaitTime > 0) {
            try {
                sleep(mWaitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isRunning() {
        return isRunning;
    }
}

