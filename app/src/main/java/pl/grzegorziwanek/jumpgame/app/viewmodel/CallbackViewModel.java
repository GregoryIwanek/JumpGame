package pl.grzegorziwanek.jumpgame.app.viewmodel;

public interface CallbackViewModel {

    void onEnemyCollision();

    void onBonusCollected(int bonusCount, String type);

    void onScoreChanged(int score, int bestScore);

    void onGameStart();

    void onGameStop();
}
