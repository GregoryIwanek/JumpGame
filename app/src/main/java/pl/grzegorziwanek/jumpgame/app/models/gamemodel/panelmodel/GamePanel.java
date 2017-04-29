package pl.grzegorziwanek.jumpgame.app.models.gamemodel.panelmodel;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import pl.grzegorziwanek.jumpgame.app.models.gamemodel.Callbacks.PanelCallback;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.GameModel;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel.SessionThread;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.Background;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus.Player;
import pl.grzegorziwanek.jumpgame.app.utilis.Cons;

/**
 * Consists of custom GamePanel. Class responsible for generating and drawing view content.
 * Communicates with drawing thread {@link SessionThread}
 * by callback {@link PanelCallback} with {@link GameModel}.
 * lock and unlock signals are sent and controlled by {@link SessionThread}.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private PanelCallback mCallback;
    private Canvas mCanvas;

    public GamePanel(Context context) {
        super(context);
    }

    public GamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GamePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCallback(PanelCallback callback) {
        mCallback = callback;
    }

    public void lockCanvas() {
        mCanvas = getHolder().lockCanvas();
        mCallback.onCanvasLocked();
    }

    public void unlockCanvas() {
        getHolder().unlockCanvasAndPost(mCanvas);
        mCallback.onCanvasUnlocked();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mCallback.onTouchEventOccurred();
        }
        return super.onTouchEvent(event);
    }

    public void draw(Background background, Player player, ArrayList<GameObjectContainer> list) {
        super.draw(mCanvas);
        background.draw(mCanvas);
        player.draw(mCanvas);
        for (GameObjectContainer object : list) {
            object.draw(mCanvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // when surface created, assign size of screen
        Cons.initScreenSize(getWidth(), getHeight());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}
}
