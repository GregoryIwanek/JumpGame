package pl.grzegorziwanek.jumpgame.app.utilis;

public class RxCallbackParam {

    private int mScore;
    private int mBestScore;
    private int mBonusCount;
    private EventType mEventType;
    private CollisionType mCollisionType;

    /**
     * Constructor used in GAME_START and GAME_RESET events;
     * @param eventType event type enum;
     */
    public RxCallbackParam(EventType eventType) {
        this(eventType, CollisionType.NONE, 0, 0, 0);
    }

    /**
     * Constructor used in SCORE_CHANGED event;
     * @param eventType event type enum;
     * @param score score number;
     * @param bestScore best score number;
     */
    public RxCallbackParam(EventType eventType, int score, int bestScore) {
        this(eventType, CollisionType.NONE, score, bestScore, 0);
    }

    /**
     * Constructor used in OBJECT_COLLISION event;
     * @param eventType event type enum;
     * @param collisionType enum object type colliding with;
     * @param bonus collected bonus number;
     */
    public RxCallbackParam(EventType eventType, CollisionType collisionType, int bonus) {
        this(eventType, collisionType, 0, 0, bonus);
    }

    private RxCallbackParam(EventType eventType, CollisionType collisionType, int score, int bestScore,
                            int bonusCount) {
        mEventType = eventType;
        mCollisionType = collisionType;
        mScore = score;
        mBestScore = bestScore;
        mBonusCount = bonusCount;
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

    public int getBonusCount() {
        return mBonusCount;
    }

    public enum EventType {
        GAME_START, GAME_RESET, OBJECT_COLLISION, SCORE_CHANGED;
    }

    public enum CollisionType {
        CHEESE, TRAP, CAT, BALL, NONE
    }
}