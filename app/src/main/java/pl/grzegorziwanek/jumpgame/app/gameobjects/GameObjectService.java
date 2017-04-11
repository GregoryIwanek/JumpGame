package pl.grzegorziwanek.jumpgame.app.gameobjects;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface GameObjectService {
    void update();
    void draw(Canvas canvas);
    int getX();
    int comparePosition();
    Rect getRectangle();
    String getObjectType();
}
