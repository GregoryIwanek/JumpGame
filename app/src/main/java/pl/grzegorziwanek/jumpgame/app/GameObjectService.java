package pl.grzegorziwanek.jumpgame.app;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Grzegorz Iwanek on 28.08.2016.
 */
public interface GameObjectService
{
    void update();
    void draw(Canvas canvas);
    int getX();
    int comparePosition();
    Rect getRectangle();
    String getObjectType();
}
