package pl.grzegorziwanek.jumpgame.app.gameobjects.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import pl.grzegorziwanek.jumpgame.app.gameobjects.GameObject;
import pl.grzegorziwanek.jumpgame.app.gameobjects.GameObjectService;

/**
 * Created by Grzegorz Iwanek on 27.08.2016.
 * Contains class responsible for static objects (no animation, just static image);
 * these objects impact specific characteristic of a game and a player when in collision with Player;
 */
public class Bonus extends GameObject implements GameObjectService {
    private int mSpeed;
    private Bitmap mImage;

    public Bonus(Bitmap res, int x, int y, int width, int height, int speed, String type) {
        mImage = res;
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        mSpeed = speed;
        mObjectType = type;
    }

    @Override
    public void update() {
        mX += mSpeed;
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(mImage, mX, mY, null);
        } catch (Exception e) {
            System.out.println("Bonus, drawing exception!");
        }
    }
}
