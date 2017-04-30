package pl.grzegorziwanek.jumpgame.app.viewmodel;

public interface CallbackViewModel {

    void onObjectCollision(int bonusCount, String subtype);

    void onGameReset();

    void onScoreChanged(int score, int bestScore);

    void onGameStart();
}
