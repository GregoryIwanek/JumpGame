package pl.grzegorziwanek.jumpgame.app.models.objectfactory;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectService;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.GameBaseObject;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.ObjectParameters;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus.Bonus;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus.Enemy;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus.Player;

public abstract class ObjectFactory {
    // TODO: 29.04.2017 edit description ( method has been changed)
    /**
     * Bundle with data about created object. Call only for: ENEMY, BONUS;
     *                      KEY         VALUE
     *                  1.  TYPE        String, type of the object ( PLAYER, ENEMY, BONUS)
     *                  2.  SUBTYPE     String, subtype of the object ( BONUS -> CHEESE, TRAP, BALL)
     *                  3.  ID          int, image id
     *                  4.  WIDTH       int, width of the object
     *                  5.  HEIGHT      int, height of the object
     *                  6.  FRAMES      int, number of the frames ( in case of animation)
     *
     * @return  returns object, wrapped by {@link GameObjectContainer}
     */
    public static GameObjectContainer getWrappedObject(ObjectParameters parameters) {
        return new GameObjectContainer(getObject(parameters));
    }

    // TODO: 29.04.2017 edit description ( method has been changed)
    /**
     *  Bundle with data about created object. Call only for: PLAYER;
     *                      KEY         VALUE
     *                  1.  TYPE        String, type of the object ( PLAYER, ENEMY, BONUS)
     *                  2.  SUBTYPE     String, subtype of the object ( BONUS -> CHEESE, TRAP, BALL)
     *                  3.  ID          int, image id
     *                  4.  WIDTH       int, width of the object
     *                  5.  HEIGHT      int, height of the object
     *                  6.  FRAMES      int, number of the frames ( in case of animation)
     *
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