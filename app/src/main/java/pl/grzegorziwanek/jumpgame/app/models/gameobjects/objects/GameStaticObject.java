package pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects;

import android.graphics.Bitmap;

/**
 * Consists of abstract class responsible for non-animated objects.
 */
public abstract class GameStaticObject extends GameBaseObject {

    protected Bitmap mImage;

    protected GameStaticObject(ObjectParameters parameters) {
        super(parameters);
        mImage = parameters.getImageRes();
    }
}
