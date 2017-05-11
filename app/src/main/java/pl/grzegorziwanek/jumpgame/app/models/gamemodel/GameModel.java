package pl.grzegorziwanek.jumpgame.app.models.gamemodel;

import android.content.Context;

import java.util.ArrayList;

import io.reactivex.subjects.BehaviorSubject;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.panelmodel.GamePanel;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel.SessionData;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel.SessionThread;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.Background;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.objects.Player;
import pl.grzegorziwanek.jumpgame.app.utilis.RxCallbackParam;
import pl.grzegorziwanek.jumpgame.app.viewmodel.GameViewModel;

import static pl.grzegorziwanek.jumpgame.app.utilis.RxCallbackParam.CollisionType;
import static pl.grzegorziwanek.jumpgame.app.utilis.RxCallbackParam.EventType;

/**
 * Controller class, responsible for coordination of {@link GamePanel}, {@link SessionData}
 * and {@link SessionThread}, and game state with {@link GameViewModel}.
 * Communicates by interface callbacks.
 */
public class GameModel {

    private final GamePanel mGamePanel;
    private SessionData mSessionData;
    private SessionThread mSessionThread;
    private BehaviorSubject<RxCallbackParam> mCallbackRx;

    public GameModel(Context context, GamePanel gamePanel) {
        mGamePanel = gamePanel;
        mCallbackRx = BehaviorSubject.create();
        mGamePanel.setCallback(new Callbacks.PanelCallback() {
            @Override
            public void onTouchEventOccurred() {
                if (!mSessionData.isRunning()) {
                    startGame();
                    mCallbackRx.onNext(new RxCallbackParam(EventType.GAME_START));
                }
            }

            @Override
            public void onCanvasLocked() {
                mSessionData.update();
            }

            @Override
            public void onAnimationEnded() {
                mSessionData.destroyEnemies();
            }
        });

        mSessionData = new SessionData(context, new Callbacks.DataCallback() {
            @Override
            public void onDataUpdated(Background background,
                                      Player player, ArrayList<GameObjectContainer> otherList) {
                mGamePanel.draw(background, player, otherList);
            }

            @Override
            public void onObjectCollision(int bonusCount, CollisionType type) {
                mCallbackRx.onNext(new RxCallbackParam(EventType.OBJECT_COLLISION, type, bonusCount));
            }

            @Override
            public void onGameReset() {
                mCallbackRx.onNext(new RxCallbackParam(EventType.GAME_RESET));
            }

            @Override
            public void onScoreChanged(int score, int bestScore) {
                mCallbackRx.onNext(new RxCallbackParam(EventType.SCORE_CHANGED, score, bestScore));
            }
        });

        mSessionThread = new SessionThread(new Callbacks.ThreadCallback() {
            @Override
            public void tryLockCanvasCallback() {
                mGamePanel.lockCanvas();
            }

            @Override
            public void tryUnlockCanvasCallback() {
                mGamePanel.unlockCanvas();
            }
        });
    }

    private void startGame() {
        if (!mSessionThread.isRunning()) {
            mSessionThread.setRunning(true);
            mSessionThread.start();
        }
        mSessionData.setRunning(true);
    }

    public void movePlayerUp() {
        mSessionData.movePlayerUp();
    }

    public void movePlayerDown() {
        mSessionData.movePlayerDown();
    }

    public void attack() {
        mSessionData.setIsAttackCalled(true);
    }

    public BehaviorSubject<RxCallbackParam> getCallbackSubjectRx() {
        return mCallbackRx;
    }
}