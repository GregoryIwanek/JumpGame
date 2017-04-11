package pl.grzegorziwanek.jumpgame.app.gameobjects.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import pl.grzegorziwanek.jumpgame.app.GameDrawingPanel;
import pl.grzegorziwanek.jumpgame.app.utilis.CustomAnimation;
import pl.grzegorziwanek.jumpgame.app.gameobjects.GameObject;
import pl.grzegorziwanek.jumpgame.app.gameobjects.GameObjectService;
import pl.grzegorziwanek.jumpgame.app.utilis.Cons;

/**
 * Created by Grzegorz Iwanek on 25.08.2016.
 * Contains class responsible for Player object. Contains and calculate correct animation of a player, information etc.
 * Player goes only up and down on a screen ( along Y axis).
 */
public class Player extends GameObject implements GameObjectService {
    private Bitmap mFramesheet; //bitmap with frame pictures
    private CustomAnimation mCustomAnimation;
    private int mScore;
    private boolean mGoingUp; //will be true if player touches a screen
    private boolean mPlaying; //true, if game is running
    private long mStartTime; //used for change frames pictures

    public Player(Bitmap res, int width, int height, int numFrames) {
        //define basic data
        setVariables(width, height);
        //set animation information
        setAnimation(res, numFrames);
        //start counting time after finishing initiation
        mStartTime = System.nanoTime();
    }

    private void setVariables(int width, int height) {
        mPlaying = false;

        //set starting position
        mX = Cons.STARTPOSITION;
        mY = GameDrawingPanel.sScreenHeight/2;

        //set score and Y axis speed
        mScore = 0;
        mDy = 0;

        //set size
        mWidth = width;
        mHeight = height;
    }

    private void setAnimation(Bitmap res, int numFrames) {
        mCustomAnimation = new CustomAnimation();
        Bitmap[] imageArray = new Bitmap[numFrames];
        mFramesheet = res;

        //assign frames to cells in array Bitmap
        for (int i=0; i<imageArray.length; i++) {
            //x=0, because mFramesheet is vertical image, so we go down with each frame image with i*mHeight
            imageArray[i] = Bitmap.createBitmap(mFramesheet, i*mWidth, 0, mWidth, mHeight);
        }

        //pass array bitmap to the animation instance
        mCustomAnimation.setFrames(imageArray);
        mCustomAnimation.setDelay(10);
    }

    @Override
    public void update() {
        //methods for updating state of player object
        updateScore();
        updateAnimation();
        updateSpeed();
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
        mCustomAnimation.update();
    }

    private void updateSpeed() {
        //change speed, depending on if player touches a screen or not (mGoingUP); set it capped between -7/7;
        if (mGoingUp) {
            if(mDy > -7) {
                mDy -= 1;
            }
        } else {
            if (mDy < 7) {
                mDy += 1;
            }
        }
    }

    private void updatePosition() {
        //update position on Y axis
        mY += mDy*2;
        if (mY <= GameDrawingPanel.SPAWNMARGIN) {
            mY = GameDrawingPanel.SPAWNMARGIN;
        } else if (mY >= GameDrawingPanel.sScreenHeight - mHeight - GameDrawingPanel.SPAWNMARGIN) {
            mY = GameDrawingPanel.sScreenHeight - mHeight - GameDrawingPanel.SPAWNMARGIN;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        //draw current player object on a screen
        try {
            canvas.drawBitmap(mCustomAnimation.getImage(), mX, mY, null);
        } catch (Exception e) {
            System.out.println("Player, drawing exception!");
        }
    }
    public void setGoingUp(boolean goingUp) {
        //set if vector is up( true-player touches screen)/ down( false-player released screen)
        mGoingUp = goingUp;
    }

    public void setPlaying(boolean playing) {
        //set if game is running or not
        mPlaying = playing;
    }

    public void resetDY() {
        //reset speed of player
        mDy = 0;
    }

    @Override
    public Rect getRectangle() {
        return new Rect(
                (int)(mX + 0.4*mWidth),
                (int)(mY + 0.1*mHeight),
                (int)(mX + 0.9*mWidth),
                (int)(mY + 0.9*mHeight));
    }

    public void resetScore() {
        mScore = 0;
    }

    public int getScore() {
        return mScore;
    }

    public boolean getPlaying() {
        return mPlaying;
    }
}