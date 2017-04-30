package pl.grzegorziwanek.jumpgame.app.models.gameobjects;

import android.graphics.Canvas;
import android.graphics.Rect;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.GameBaseObject;

/**
 * Service working as a bridge between {@link GameObjectContainer} and one of the extensions
 * of {@link GameBaseObject} class. Main purpose is to allow injection of different {@link GameBaseObject}
 * into a {@link GameObjectContainer} and perform collision check of all types, by one method.
 */
public interface GameObjectService {

    void update();

    void draw(Canvas canvas);

    int getX();

    int comparePosition();

    Rect getRectangle();

    String getObjectType();

    String getObjectSubtype();
}
