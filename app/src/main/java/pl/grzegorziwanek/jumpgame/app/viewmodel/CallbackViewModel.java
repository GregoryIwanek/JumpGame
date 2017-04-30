package pl.grzegorziwanek.jumpgame.app.viewmodel;

public interface CallbackViewModel {

    void onObjectCollision(int bonusCount, String subtype);

    void onScoreChanged(int score, int bestScore);

    void onGameStart();

    void onGameStop();
}
