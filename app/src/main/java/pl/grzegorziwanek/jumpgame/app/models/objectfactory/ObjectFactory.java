package pl.grzegorziwanek.jumpgame.app.models.objectfactory;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectService;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.GameBaseObject;
import pl.grzegorziwanek.jumpgame.app.utilis.ObjectParameters;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.objects.Bonus;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.objects.Enemy;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.objects.Player;

/**
 * Factory class responsible for creation of all game objects.
 */
public abstract class ObjectFactory {
    /**
     * Get's fully defined object wrapped by container.
     * @param parameters container class consists of all parameters required to create {@link GameBaseObject}
     *                   or one of it's children implementing {@link GameObjectService};
     * @return extension object of {@link GameBaseObject} wrapped by container {@link GameObjectContainer};
     */
    public static GameObjectContainer getWrappedObject(ObjectParameters parameters) {
        return new GameObjectContainer(getObject(parameters));
    }

    /**
     * Get's fully defined object {@link GameObjectService} unwrapped by any container.
     * @param parameters container class consists of all parameters required to create {@link GameBaseObject}
     *                   or one of it's children implementing {@link GameObjectService};
     * @return returns {@link GameObjectService} object type, without wrapped {@link GameObjectContainer};
     * upon initiation, has to be cast to proper {@link GameBaseObject} children class ( all children classes
     * implement {@link GameObjectService});
     * e.g Player player = (Player) ObjectFactory.getUnwrappedObject(objectData);
     */
    public static GameObjectService getUnwrappedObject(ObjectParameters parameters) {
        return getObject(parameters);
    }

    private static GameObjectService getObject(ObjectParameters parameters) {
        switch (parameters.getType()) {
            case "PLAYER":
                return generatePlayer(parameters);
            case "ENEMY":
                return generateEnemy(parameters);
            case "BONUS":
                return generateBonus(parameters);
            default:
                return generateEnemy(parameters);
        }
    }

    private static Player generatePlayer(ObjectParameters parameters) {
        parameters.setFramesInterval(10);
        return new Player(parameters);
    }

    private static Enemy generateEnemy(ObjectParameters parameters) {
        int framesInterval = 100 - parameters.getSpeed();
        parameters.setFramesInterval(framesInterval);
        return new Enemy(parameters);
    }

    private static Bonus generateBonus(ObjectParameters parameters) {
        return new Bonus(parameters);
    }
}