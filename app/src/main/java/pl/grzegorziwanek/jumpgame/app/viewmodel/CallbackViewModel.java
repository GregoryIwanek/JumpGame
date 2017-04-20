package pl.grzegorziwanek.jumpgame.app.viewmodel;

/**
 * Created by Grzegorz Iwanek on 20.04.2017.
 */

public interface CallbackViewModel {

    void onBonusCollected(int bonusCount);

    void onScoreChanged(int score, int bestScore);
}
