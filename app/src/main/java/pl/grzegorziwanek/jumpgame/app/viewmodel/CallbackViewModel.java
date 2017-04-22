package pl.grzegorziwanek.jumpgame.app.viewmodel;

public interface CallbackViewModel {

    void onBonusCollected(int bonusCount);

    void onScoreChanged(int score, int bestScore);
}
