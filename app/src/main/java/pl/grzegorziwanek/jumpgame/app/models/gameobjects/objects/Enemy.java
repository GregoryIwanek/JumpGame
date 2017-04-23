package pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObject;
import pl.grzegorziwanek.jumpgame.app.utilis.CustomAnimation;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectService;

public class Enemy extends GameObject implements GameObjectService {
    private int mSpeed;
    private CustomAnimation mAnimation = new CustomAnimation();
    private Bitmap mFramesheet;

    public Enemy(Bitmap res, int x, int y, int width, int height, int speed, int numFrames, String type) {
        mObjectType = type;
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        mSpeed = speed;

        Bitmap[] imageArray = new Bitmap[numFrames];
        mFramesheet = res;
        for (int i=0; i<imageArray.length; i++) {
            imageArray[i] = Bitmap.createBitmap(mFramesheet, 0, i*mHeight, mWidth, mHeight);
        }

        mAnimation.setFrames(imageArray);
        mAnimation.setDelay(100-mSpeed);
    }

    @Override
    public void update() {
        mX -= mSpeed;
        mAnimation.update();
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(mAnimation.getImage(), mX, mY, null);
        } catch (Exception e) {
            System.out.println("Enemy, drawing exception!");
        }
    }

    @Override
    public int getWidth() {
        return  mWidth - 10;
    }
}