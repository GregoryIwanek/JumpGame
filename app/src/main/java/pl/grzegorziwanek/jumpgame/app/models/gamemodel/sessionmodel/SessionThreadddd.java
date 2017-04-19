package pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import pl.grzegorziwanek.jumpgame.app.view.GamePanelOld;

/**
 * Created by Grzegorz Iwanek on 24.08.2016.
 * Main thread of the game. Inherits from Thread class, and is responsible for updating and drawing current state of the game on canvas.
 * Also, limits possible maximum FPS (Frame Per Second) in game, by putting app to sleep ( in case it would have bigger FPS than defined).
 *
 */
public class SessionThreadddd extends Thread {
    private GamePanelOld mGamePanel;
    private final SurfaceHolder mSurfaceHolder;
    private boolean running;

    public static Canvas canvas;

    private static final int FPS = 30;//target FPS of game
    private long targetTime = 1000/FPS;//milliseconds to get target number of FPS
    private long startTime = 0;
    private long timeTaken = 0;
    private long waitTime = 0;
    private long totalTime = 0;
    private int frameCount = 0;
    private long averageFPS = 0;

    public SessionThreadddd(SurfaceHolder surfaceHolder, GamePanelOld gamePanel) {
        super();
        mGamePanel = gamePanel;
        mSurfaceHolder = surfaceHolder;
    }

    // TODO: 19.04.2017 leave this constructor as a correct one
    // TODO: 19.04.2017 separate GamePanel from this Thread
    public SessionThreadddd(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            //most important part: update and draw state of game in real time
            drawAndUpdateCanvas();

            //calculate time of process so far, and put app in sleep if needed to get 30FPS
            timeTaken = (System.nanoTime() - startTime)/1000000;

            //set app sleep if FPS would be higher than defined ( FPS > 30)
            setAppSleep();

            //TODO: remove what's below later, just for emulator use and measuring FPS in emulator
            //increment count of loop and calculate total time of running loop
            frameCount++;
            totalTime += System.nanoTime() - startTime;

            //calculate information about current FPS in emulator
            printAverageFPS();
        }
    }

    private void drawAndUpdateCanvas() {
        //series of methods to draw and update game canvas
        //scheme: lock canvas->update and draw on->unlock canvas
        try {
            canvas = this.mSurfaceHolder.lockCanvas();

            //synchronized-> make sure that no other process is messing with canvas right now
            synchronized (mSurfaceHolder) {
                mGamePanel.update();
                mGamePanel.draw(canvas);
            }
        } catch (Exception e) {
            System.out.println("Exception main thread: update and draw");
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                try {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setAppSleep() {
        //calculate time to put app to sleep, to get target FPS speed, no more
        waitTime = targetTime - timeTaken;
        if(waitTime > 0) {
            try {
                sleep(waitTime);
            } catch (Exception e) {
                System.out.println("Exception: sleep");
            }
        }
    }

    private void printAverageFPS() {
        //TODO:remove that method, just for measuring speed of app in emulator
        //calculate information about current FPS speed of an app
        if (frameCount == FPS) {
            averageFPS = 1000/((totalTime/frameCount)/1000000);

            //reset variables
            frameCount = 0;
            totalTime = 0;
        }
    }



    public void setRunning(boolean running) {
        this.running = running;
    }
}

