package pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus;

import android.graphics.Canvas;
import android.graphics.Rect;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.GameDynamicObject;
import pl.grzegorziwanek.jumpgame.app.utilis.ObjectParameters;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectService;
import pl.grzegorziwanek.jumpgame.app.utilis.Cons;

/**
 * Created by Grzegorz Iwanek on 25.08.2016.
 * Contains class responsible for Player object. Contains and calculate correct animation of a player, information etc.
 * Player goes only up and down on a screen ( along Y axis).
 */
public class Player extends GameDynamicObject implements GameObjectService {

    private int mScore;
    private boolean mPlaying;
    private long mStartTime;
    private int mYTopLimit;
    private int mYBottomLimit;

    public Player(ObjectParameters parameters) {
        super(parameters);
        initPlayerVariables();
    }

    private void initPlayerVariables() {
        mPlaying = false;
        mScore = 0;
        mDy = 0;
        mStartTime = System.nanoTime();
    }

    @Override
    public void update() {
        updateScore();
        updateAnimation();
        updatePosition();
    }

    private void updateScore() {
        //increment score as game continues
        long elapsed = (System.nanoTime() - mStartTime)/1000000;
        if (elapsed > 100) {
            mScore++;
            mStartTime = System.nanoTime();
        }
    }

    //used when player colliding with bonus type object
    public void addExtraScore(int extraScore) {
        mScore += extraScore;
    }

    private void updateAnimation() {
        mAnimation.update();
    }

    private void updatePosition() {
        mY += mDy*2;
        if (mY <= mYTopLimit) {
            mY = mYTopLimit;
        } else if (mY >= mYBottomLimit) {
            mY = mYBottomLimit;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        //draw current player object on a screen
        try {
            canvas.drawBitmap(mAnimation.getImage(), mX, mY, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int comparePosition() {
        return super.comparePosition();
    }

    public void moveUp() {
        mYTopLimit = mY - 50;
        mDy = -7;
        if (mYTopLimit <= Cons.SPAWN_MARGIN) {
            mYTopLimit = Cons.SPAWN_MARGIN;
        }
    }

    public void moveDown() {
        mYBottomLimit = mY + 50;
        mDy = 7;
        if (mYBottomLimit >= Cons.sScreenHeight - mHeight - Cons.SPAWN_MARGIN) {
            mYBottomLimit = Cons.sScreenHeight - mHeight - Cons.SPAWN_MARGIN;
        }
    }

    @Override
    public Rect getRectangle() {
        return new Rect(
                (int)(mX + 0.4*mWidth),
                (int)(mY + 0.1*mHeight),
                (int)(mX + 0.9*mWidth),
                (int)(mY + 0.9*mHeight));
    }

    public void reset() {
        mScore = 0;
        mY = Cons.sScreenHeight/2;
        mDy = 0;
        mPlaying = false;
        mYBottomLimit = mY;
        mYTopLimit = mY;
    }

    public int getScore() {
        return mScore;
    }

    public boolean getPlaying() {
        return mPlaying;
    }
}