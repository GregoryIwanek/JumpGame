package pl.grzegorziwanek.jumpgame.app.models.gamemodel;

import java.util.ArrayList;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.Background;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Player;

public interface Callbacks {

    interface ThreadCallback {

        void lockCanvasCallback();

        void unlockCanvasCallback();
    }

    interface DataCallback {

        void onDataUpdated(Background background, Player player,
                           ArrayList<GameObjectContainer> list);
    }

    interface PanelCallback {

        void onTouchEventOccurred();

        void onCanvasLocked();

        void onCanvasUnlocked();
    }
}
