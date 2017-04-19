package pl.grzegorziwanek.jumpgame.app.models.gamemodel;

import java.util.ArrayList;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;

public interface Callbacks {

    interface ThreadCallback {

        void lockCanvasCallback();

        void unlockCanvasCallback();
    }

    interface DataCallback {

        void onDataUpdated(ArrayList<GameObjectContainer> list);
    }

    interface PanelCallback {

        void onTouchEventOccurred();

        void onCanvasLocked();

        void onCanvasUnlocked();
    }
}
