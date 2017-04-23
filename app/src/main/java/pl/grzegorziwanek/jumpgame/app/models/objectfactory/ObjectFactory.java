package pl.grzegorziwanek.jumpgame.app.models.objectfactory;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObject;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectService;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Enemy;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Player;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus.Bonus;

public abstract class ObjectFactory {
    /**
     * @param objectData Bundle with data about created object. Call only for: ENEMY, BONUS
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
    public static GameObjectContainer getWrappedObject(Bundle objectData, Context context) {
        return new GameObjectContainer(getObject(objectData, context));
    }

    /**
     * @param objectData Bundle with data about created object. Call only for: PLAYER
     *                      KEY         VALUE
     *                  1.  TYPE        String, type of the object ( PLAYER, ENEMY, BONUS)
     *                  2.  SUBTYPE     String, subtype of the object ( BONUS -> CHEESE, TRAP, BALL)
     *                  3.  ID          int, image id
     *                  4.  WIDTH       int, width of the object
     *                  5.  HEIGHT      int, height of the object
     *                  6.  FRAMES      int, number of the frames ( in case of animation)
     *
     * @return returns {@link GameObjectService} object type, without wrapped {@link GameObjectContainer};
     * upon initiation, has to be cast to proper {@link GameObject} children class ( all children classes
     * implement {@link GameObjectService});
     * e.g Player player = (Player) ObjectFactory.getUnwrappedObject(objectData);
     */
    public static GameObjectService getUnwrappedObject(Bundle objectData, Context context) {
        return getObject(objectData, context);
    }

    private static GameObjectService getObject(Bundle objectData, Context context) {
        switch (objectData.getString("TYPE")) {
            case "PLAYER":
                return generatePlayer(objectData, context);
            case "ENEMY":
                return generateEnemy(objectData, context);
            case "BONUS":
                return generateBonus(objectData, context);
            default:
                return generateEnemy(objectData, context);
        }
    }

    private static Player generatePlayer(Bundle b, Context context) {
        return new Player(BitmapFactory.decodeResource(
                context.getResources(),
                b.getInt("ID")),
                b.getInt("WIDTH"),
                b.getInt("HEIGHT"),
                b.getInt("FRAMES"));
    }

    private static Enemy generateEnemy(Bundle b, Context context) {
        return new Enemy(BitmapFactory.decodeResource(
                context.getResources(),
                b.getInt("ID")),
                b.getInt("X"),
                b.getInt("Y"),
                b.getInt("WIDTH"),
                b.getInt("HEIGHT"),
                b.getInt("SPEED"),
                b.getInt("FRAMES"),
                b.getString("SUBTYPE")
        );
    }

    private static Bonus generateBonus(Bundle b, Context context) {
        return new Bonus(BitmapFactory.decodeResource(
                context.getResources(),
                b.getInt("ID")),
                b.getInt("X"),
                b.getInt("Y"),
                b.getInt("WIDTH"),
                b.getInt("HEIGHT"),
                b.getInt("SPEED"),
                b.getString("TYPE"),
                b.getString("SUBTYPE")
        );
    }
}