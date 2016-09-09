package pl.grzegorziwanek.jumpgame.app;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Grzegorz Iwanek on 28.08.2016.
 */
public class GameObjectContainer implements GameObjectService
{
    private GameObjectService mGameObjectService;

    public GameObjectContainer(GameObjectService gameObjectService)
    {
        mGameObjectService = gameObjectService;
    }

    @Override
    public void update()
    {
        mGameObjectService.update();
    }

    @Override
    public void draw(Canvas canvas)
    {
        mGameObjectService.draw(canvas);
    }

    @Override
    public int getX()
    {
        return mGameObjectService.getX();
    }

    @Override
    public int comparePosition()
    {
        return mGameObjectService.comparePosition();
    }

    @Override
    public Rect getRectangle()
    {
        return mGameObjectService.getRectangle();
    }

    @Override
    public String getObjectType()
    {
        return mGameObjectService.getObjectType();
    }
}
