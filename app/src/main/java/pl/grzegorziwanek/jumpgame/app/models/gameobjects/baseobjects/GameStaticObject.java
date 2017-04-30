package pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects;

import android.graphics.Bitmap;

import pl.grzegorziwanek.jumpgame.app.utilis.ObjectParameters;

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
