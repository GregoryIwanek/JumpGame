package pl.grzegorziwanek.jumpgame.app.models.gameobjects;

import android.graphics.Canvas;
import android.graphics.Rect;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.GameBaseObject;

/**
 * Wrapper class.
 * Holds one of extensions of {@link GameBaseObject} as a {@link GameObjectService} instance.
 * Allows injection of different types of game objects and performing collision check by one method.
 */
public class GameObjectContainer implements GameObjectService {

    private GameObjectService mGameObjectService;

    public GameObjectContainer(GameObjectService gameObjectService) {
        mGameObjectService = gameObjectService;
    }

    @Override
    public void update() {
        mGameObjectService.update();
    }

    @Override
    public void draw(Canvas canvas) {
        mGameObjectService.draw(canvas);
    }

    @Override
    public int getX() {
        return mGameObjectService.getX();
    }

    @Override
    public int comparePosition() {
        return mGameObjectService.comparePosition();
    }

    @Override
    public Rect getRectangle() {
        return mGameObjectService.getRectangle();
    }

    @Override
    public String getObjectType() {
        return mGameObjectService.getObjectType();
    }

    @Override
    public String getObjectSubtype() {
        return mGameObjectService.getObjectSubtype();
    }
}
