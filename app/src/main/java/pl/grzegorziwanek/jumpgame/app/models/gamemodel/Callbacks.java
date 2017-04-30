package pl.grzegorziwanek.jumpgame.app.models.gamemodel;

import java.util.ArrayList;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.Background;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus.Player;

public interface Callbacks {

    interface ThreadCallback {
        void tryLockCanvasCallback();

        void tryUnlockCanvasCallback();
    }

    interface DataCallback {
        void onDataUpdated(Background background, Player player,
                           ArrayList<GameObjectContainer> list);

        void onObjectCollision(int bonusCount, String subtype);

        void onScoreChanged(int score, int bestScore);
    }

    interface PanelCallback {
        void onTouchEventOccurred();

        void onCanvasLocked();

        void onCanvasUnlocked();
    }
}
