package pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.objects;

import android.graphics.Canvas;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectService;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.GameDynamicObject;
import pl.grzegorziwanek.jumpgame.app.utilis.ObjectParameters;

public class Enemy extends GameDynamicObject implements GameObjectService {

    public Enemy(ObjectParameters parameters) {
        super(parameters);
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
            e.printStackTrace();
        }
    }

    @Override
    public int comparePosition() {
        return super.comparePosition();
    }
}