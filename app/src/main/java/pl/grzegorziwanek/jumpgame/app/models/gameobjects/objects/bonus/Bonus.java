package pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObject;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectService;

/**
 * Created by Grzegorz Iwanek on 27.08.2016.
 * Contains class responsible for static objects (no animation, just static image);
 * these objects impact specific characteristic of a game and a player when in collision with Player;
 */
public class Bonus extends GameObject implements GameObjectService {
    private int mSpeed;
    private Bitmap mImage;

    public Bonus(Bitmap res, int x, int y, int width, int height, int speed, String type, String subtype) {
        mImage = res;
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
        mSpeed = speed;
        // TODO: 23.04.2017 remove conflict between subtype and type
        mObjectType = type;
        mObjectSubtype = subtype;
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
