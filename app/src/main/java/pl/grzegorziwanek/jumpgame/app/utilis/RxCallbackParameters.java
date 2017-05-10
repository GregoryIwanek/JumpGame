package pl.grzegorziwanek.jumpgame.app.utilis;

public class RxCallbackParameters {

    private int mScore;
    private int mBestScore;
    private int mBonus;
    private EventType mEventType;
    private CollisionType mCollisionType;

    // TODO: 10.05.2017 think about adding few more constructors with fewer arguments
    public RxCallbackParameters(EventType eventType, CollisionType collisionType,
                                int score, int bestScore, int bonus) {
        mEventType = eventType;
        mCollisionType = collisionType;
        mScore = score;
        mBestScore = bestScore;
        mBonus = bonus;
    }

    public EventType getEventType() {
        return mEventType;
    }

    public CollisionType getCollisionType() {
        return mCollisionType;
    }

    public int getScore() {
        return mScore;
    }

    public int getBestScore() {
        return mBestScore;
    }

    public int getBonus() {
        return mBonus;
    }

    public enum EventType {
        GAME_START, GAME_RESET, OBJECT_COLLISION, SCORE_CHANGED;
    }

    public enum CollisionType {
        CHEESE, TRAP, CAT, BALL, NONE
    }
}