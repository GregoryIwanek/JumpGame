package pl.grzegorziwanek.jumpgame.app.models.objectfactory;

import android.graphics.Bitmap;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus.Bonus;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Enemy;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Player;

// TODO: 19.04.2017 convert this one to proper shape
public abstract class ObjectFactory {
    public static GameObjectContainer getWrappedObject(String mainType, String subType) {
        return new GameObjectContainer(getObject(mainType, subType));
    }

    private static GameObjectContainer getObject(String mainType, String subType) {
        switch (mainType) {
            case "PLAYER":
                return new GameObjectContainer(new Player(Bitmap.createBitmap(0,0, Bitmap.Config.ARGB_8888), 0, 0, 0));
            case "ENEMY":
                return new GameObjectContainer(new Enemy(Bitmap.createBitmap(0,0, Bitmap.Config.ARGB_8888), 0, 0, 0, 0, 0, 0, ""));
            case "BONUS":
                if (subType.equals("TRAP")) {
                    return new GameObjectContainer(new Bonus(Bitmap.createBitmap(0,0, Bitmap.Config.ARGB_8888), 0, 0, 0, 0, 0, ""));
                } else {
                    return new GameObjectContainer(new Bonus(Bitmap.createBitmap(0,0, Bitmap.Config.ARGB_8888), 0, 0, 0, 0, 0, ""));
                }
                default:
                    return new GameObjectContainer(new Enemy(Bitmap.createBitmap(0,0, Bitmap.Config.ARGB_8888), 0, 0, 0, 0, 0, 0, ""));
        }
    }
}
