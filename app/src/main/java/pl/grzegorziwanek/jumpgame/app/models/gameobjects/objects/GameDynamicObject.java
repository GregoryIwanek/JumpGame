package pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects;

import pl.grzegorziwanek.jumpgame.app.utilis.CustomAnimation;

/**
 * Consists of abstract class responsible for animated objects.
 */
public abstract class GameDynamicObject extends GameBaseObject {
    
    protected CustomAnimation mAnimation;

    protected GameDynamicObject(ObjectParameters parameters) {
        super(parameters);
        mAnimation = new CustomAnimation(parameters);
    }
}
