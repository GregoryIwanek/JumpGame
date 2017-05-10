package pl.grzegorziwanek.jumpgame.app.models.gamemodel;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import io.reactivex.subjects.BehaviorSubject;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.panelmodel.GamePanel;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel.SessionData;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel.SessionThread;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.Background;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.objects.Player;
import pl.grzegorziwanek.jumpgame.app.utilis.RxCallbackParameters;
import pl.grzegorziwanek.jumpgame.app.viewmodel.CallbackViewModel;
import pl.grzegorziwanek.jumpgame.app.viewmodel.GameViewModel;

import static pl.grzegorziwanek.jumpgame.app.utilis.RxCallbackParameters.*;

/**
 * Controller class, responsible for coordination of {@link GamePanel}, {@link SessionData}
 * and {@link SessionThread}, and game state with {@link GameViewModel}.
 * Communicates by interface callbacks.
 */
public class GameModel {

    private CallbackViewModel mCallback;
    private final GamePanel mGamePanel;
    private SessionData mSessionData;
    private SessionThread mSessionThread;
    private BehaviorSubject<RxCallbackParameters> mCallbackParametersRx;

    public GameModel(Context context, GamePanel gamePanel, CallbackViewModel callback) {
        mCallback = callback;
        mGamePanel = gamePanel;
        mGamePanel.setCallback(new Callbacks.PanelCallback() {
            @Override
            public void onTouchEventOccurred() {
                if (!mSessionData.isRunning()) {
                    startGame();
                    mCallback.onGameStart();
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
            public void onObjectCollision(int bonusCount, String subtype) {
                mCallback.onObjectCollision(bonusCount, subtype);
                mCallbackParametersRx.onNext(setCallbackParams(EventType.OBJECT_COLLISION,
                        CollisionType.TRAP, 100, 100, bonusCount));
            }

            @Override
            public void onGameReset() {
                mCallback.onGameReset();
            }

            @Override
            public void onScoreChanged(int score, int bestScore) {
                mCallback.onScoreChanged(score, bestScore);
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

    private RxCallbackParameters setCallbackParams(EventType event, CollisionType type,
                                                   @Nullable int score, @Nullable int bestScore,
                                                   @Nullable int bonus) {
        return new RxCallbackParameters(event, type, score, bestScore, bonus);
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

    public BehaviorSubject<RxCallbackParameters> getCallbackSubjectRx() {
        mCallbackParametersRx = BehaviorSubject.create();
        return mCallbackParametersRx;
    }
}