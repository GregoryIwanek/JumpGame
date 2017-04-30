package pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.objects;

import android.graphics.Canvas;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectService;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.GameStaticObject;
import pl.grzegorziwanek.jumpgame.app.utilis.ObjectParameters;

/**
 * Created by Grzegorz Iwanek on 27.08.2016.
 * Contains class responsible for static objects (no animation, just static image);
 * these objects impact specific characteristic of a game and a player when in collision with Player;
 */
public class Bonus extends GameStaticObject implements GameObjectService {

    public Bonus(ObjectParameters parameters) {
        super(parameters);
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
            e.printStackTrace();
        }
    }

    @Override
    public int comparePosition() {
        return super.comparePosition();
    }
}
