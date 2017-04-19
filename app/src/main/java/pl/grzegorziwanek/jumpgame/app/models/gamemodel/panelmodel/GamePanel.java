package pl.grzegorziwanek.jumpgame.app.models.gamemodel.panelmodel;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import pl.grzegorziwanek.jumpgame.app.models.gamemodel.Callbacks.PanelCallback;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private PanelCallback mCallback;
    private Canvas mCanvas;
    private Context mContext;

    public GamePanel(Context context) {
        super(context);
        System.out.println("constructor 1");
        System.out.println("this address is: " + this);
        System.out.println("callback address is: ");
        System.out.println("constructor 1");
        System.out.println("callback after initiation is: " + mCallback);
        initVariables(context);
    }

    public GamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GamePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initVariables(Context context) {
        mContext = context;
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
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("surface changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("surface destroyed");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("on touch");
            System.out.println("this address is: " + this);
            System.out.println("callback address is: " + mCallback);
            System.out.println("on touch");
            mCallback.onTouchEventOccurred();
        }
        return super.onTouchEvent(event);
    }

    public void draw(ArrayList<GameObjectContainer> list) {
        for (GameObjectContainer object : list) {
            object.draw(mCanvas);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
