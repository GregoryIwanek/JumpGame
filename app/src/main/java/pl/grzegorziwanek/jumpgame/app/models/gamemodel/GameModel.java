package pl.grzegorziwanek.jumpgame.app.models.gamemodel;

import android.content.Context;

import java.util.ArrayList;

import pl.grzegorziwanek.jumpgame.app.models.gamemodel.panelmodel.GamePanel;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel.SessionData;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel.SessionThread;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.Background;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus.Player;
import pl.grzegorziwanek.jumpgame.app.viewmodel.CallbackViewModel;

public class GameModel {
    private CallbackViewModel mCallback;
    private final GamePanel mGamePanel;
    private SessionData mSessionData;
    private SessionThread mSessionThread;

    public GameModel(Context context, GamePanel gamePanel, CallbackViewModel callback) {
        mCallback = callback;
        mGamePanel = gamePanel;
        mGamePanel.setCallback(new Callbacks.PanelCallback() {
            @Override
            public void onTouchEventOccurred() {
                if (!mSessionData.isRunning()) {
                    startGame();
                }
            }

            @Override
            public void onCanvasLocked() {
                mSessionData.update();
            }

            @Override
            public void onCanvasUnlocked() {
            }
        });

        mSessionData = new SessionData(context, new Callbacks.DataCallback() {
            @Override
            public void onDataUpdated(Background background,
                                      Player player, ArrayList<GameObjectContainer> otherList) {
                mGamePanel.draw(background, player, otherList);
            }

            @Override
            public void onBonusCollected(int bonusCount, String type) {
                mCallback.onBonusCollected(bonusCount, type);
            }

            @Override
            public void onEnemyCollision(String subtype) {
                mCallback.onEnemyCollision(subtype);
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

    private void startGame() {
        // TODO: 19.04.2017 remove running from session data
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
}