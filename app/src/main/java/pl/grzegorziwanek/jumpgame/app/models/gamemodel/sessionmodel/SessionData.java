package pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import pl.grzegorziwanek.jumpgame.app.R;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.Callbacks.DataCallback;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.Background;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.GameBaseObject;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.baseobjects.objects.Player;
import pl.grzegorziwanek.jumpgame.app.models.objectfactory.ObjectFactory;
import pl.grzegorziwanek.jumpgame.app.utilis.Cons;
import pl.grzegorziwanek.jumpgame.app.utilis.ObjectParameters;

/**
 * Session data model class. Consists of essential data and methods responsible for control
 * over state of the game.
 */
public class SessionData {
    private DataCallback mCallback;
    private Context mContext;
    private Resources mResources;
    private ArrayList<GameObjectContainer> mGameObjects;
    private Bitmap mScaledImage;
    private Background mBackground;
    private Player mPlayer;
    private Random mRand = new Random();
    private long mEnemyStartTime;
    private long mBonusStartTime;
    private boolean sObjectWasAdded; //set true if object was added to list recently
    private int mScreenWidth;
    private int mScreenHeight;
    private int mBestScore;
    private int mBonusCollected;
    private boolean isRunning;
    private boolean isAttackCalled;

    public SessionData(Context context, DataCallback callback) {
        mContext = context;
        mCallback = callback;
        initData();
        setNewGame();
    }

    private void initData() {
        initVariables();
        initStartingObjects();
    }

    private void initVariables() {
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        Cons.initScreenSize(mScreenWidth, mScreenHeight);
        mBestScore = 0;
        mBonusCollected = 0;
        sObjectWasAdded = false;
        isRunning = false;
        isAttackCalled = false;
        mResources = mContext.getResources();
    }

    private void initStartingObjects() {
        Bitmap backImage = BitmapFactory.decodeResource(mResources, R.drawable.floor_background);
        Bitmap playerImage = BitmapFactory.decodeResource(mResources, R.drawable.mouse_walk);
        mScaledImage = Bitmap.createScaledBitmap(backImage, mScreenWidth, mScreenHeight, true);

        mBackground = new Background(mScaledImage, mScreenWidth);
        mPlayer = (Player) ObjectFactory.getUnwrappedObject(new ObjectParameters(
                Cons.START_POSITION, Cons.sScreenHeight/2, 50, 50, 0, "PLAYER", "NONE",
                playerImage, 12));

        mGameObjects = new ArrayList<>();
    }

    public void update() {
        if (isRunning) {
            updateStartingObjects();
            updateGlobalScore();
            updateEnemySpawn();
            updateBonusSpawn();
            updateObjectsData();
            updateOnAttackCalled();
        } else {
            setNewGame();
        }
        mCallback.onDataUpdated(mBackground, mPlayer, mGameObjects);
        mCallback.onScoreChanged(mPlayer.getScore(), mBestScore);
    }

    private void updateStartingObjects() {
        mBackground.update();
        mPlayer.update();
    }

    private void updateGlobalScore() {
        if (mBestScore < mPlayer.getScore()) {
            mBestScore = mPlayer.getScore();
        }
    }

    private void updateEnemySpawn() {
        //calculate time passed till now
        long enemyElapsed = (System.nanoTime() - mEnemyStartTime) / 1000000;

        //calculate period between spawning enemies ( possible range 1.5 - 4 sec);
        long spawnDelay = 3500 - mPlayer.getScore()/3;
        if(spawnDelay < 750) {
            spawnDelay = 750;
        }

        //spawn enemy on the screen
        if(enemyElapsed > spawnDelay) {
            spawnObject("ENEMY");
            sObjectWasAdded = true;
        }
    }

    private void updateBonusSpawn() {
        //calculate time passed till now
        long bonusElapsed = (System.nanoTime() - mBonusStartTime) / 1000000;

        //calculate spawn delay ( time between spawning bonuses)
        long spawnDelay = 4000 - mPlayer.getScore()/4;
        if(spawnDelay < 500) {
            spawnDelay = 500;
        }

        //spawn object if spawn delay has passed
        if(bonusElapsed > spawnDelay) {
            spawnObject("BONUS");
            sObjectWasAdded = true;
        }
    }

    private void spawnObject(String type) {
        ObjectParameters parameters = setObjectParameters(type);
        mGameObjects.add(ObjectFactory.getWrappedObject(parameters));
    }

    private ObjectParameters setObjectParameters(String type) {
        ObjectParameters parameters = new ObjectParameters();
        parameters.setX(Cons.sSpawnX);
        parameters.setType(type);
        switch (type) {
            case "ENEMY":
                parameters = setEnemyParameters(parameters);
                break;
            case "BONUS":
                parameters = setBonusParameters(parameters);
                break;
        }
        return parameters;
    }

    private ObjectParameters setEnemyParameters(ObjectParameters p) {
        int randSubType = mRand.nextInt(3);
        switch (randSubType) {
            case 0:
            case 1:
                p.setSubType("CAT");
                setBasicObjectParameters(p, 200, 75, getRandomSpeed(false));
                setAnimatedImage(p);
                break;
            case 2:
                p.setSubType("BALL");
                setBasicObjectParameters(p, 50, 50,getRandomSpeed(true));
                setAnimatedImage(p);
                break;
        }
        mEnemyStartTime = System.nanoTime();
        return p;
    }

    private ObjectParameters setBonusParameters(ObjectParameters p) {
        int randSubType = mRand.nextInt(2);
        switch (randSubType) {
            case 0:
                p.setSubType("CHEESE");
                p.setImageRes(BitmapFactory.decodeResource(mResources, R.drawable.cheese_yellow));
                setBasicObjectParameters(p, 75, 50);
                break;
            case 1:
                p.setSubType("TRAP");
                p.setImageRes(BitmapFactory.decodeResource(mResources, R.drawable.mouse_trap));
                setBasicObjectParameters(p, 125, 75);
                break;
        }
        mBonusStartTime = System.nanoTime();
        return p;
    }

    private int getRandomPosition(int objectHeight) {
        int yRandom = (int) (mRand.nextDouble() * mScreenHeight - (Cons.SPAWN_MARGIN + objectHeight));
        if (yRandom < Cons.SPAWN_MARGIN) {
            yRandom = Cons.SPAWN_MARGIN;
        }
        return yRandom;
    }

    // TODO: 30.04.2017 refactor this part
    private int getRandomSpeed(boolean isAlwaysMoving) {
        int speed = 5 + (int)(mRand.nextDouble()*mPlayer.getScore()/20);
        if (speed < 8 && !isAlwaysMoving) {
            speed = 5;
        } else if (speed < 8 && isAlwaysMoving) {
            speed = 8;
        } else if(speed > 35) {
            speed = 30;
        }
        return speed;
    }

    private void setBasicObjectParameters(ObjectParameters p, int width, int height) {
        setBasicObjectParameters(p, width, height, Cons.MOVE_SPEED);
    }

    private void setBasicObjectParameters(ObjectParameters p, int width, int height, int speed) {
        p.setWidth(width);
        p.setHeight(height);
        p.setSpeed(speed);
        p.setY(getRandomPosition(height));
    }

    private void setAnimatedImage(ObjectParameters p) {
        Bitmap bitmap;
        int resId;
        int numFrames;

        String key = setDrawableKey(p.getSubType(), p.getSpeed());
        resId = Cons.drawableMap.get(key).getResId();
        numFrames = Cons.drawableMap.get(key).getNumFrames();
        bitmap = BitmapFactory.decodeResource(mResources, resId);

        p.setImageRes(bitmap);
        p.setNumFrames(numFrames);
    }

    private String setDrawableKey(String subtype, int speed) {
        String key = addTypeToKey(subtype);
        key = addSpeedToKey(key, speed);
        key = addColorToKey(key);
        return key;
    }

    private String addTypeToKey(String subType) {
        return subType + "_";
    }

    private String addSpeedToKey(String key, int speed) {
        if (speed == 5) {
            key += "STATIC_";
        } else if (speed < 15) {
            key += "SLOW_";
        } else if (speed < 25) {
            key += "MEDIUM_";
        } else {
            key += "FAST_";
        }
        return key;
    }

    private String addColorToKey(String key) {
        int rand = mRand.nextInt(2);
        if (rand == 0) {
            key += "COLOR_1";
        } else {
            key += "COLOR_2";
        }
        return key;
    }

    // update all objects except player object and background object; sort list according to Y position
    // (sorting, because we want to draw objects in right order); sort ONLY if there is new object on a sorted list
    private void updateObjectsData() {
        if (sObjectWasAdded) {
            sortObjectsByY();
            sObjectWasAdded = false;
        }

        updateObjectsPosition();
        checkCollision();
    }

    private void sortObjectsByY() {
        Collections.sort(mGameObjects,
                (object1, object2) -> object1.comparePosition() - object2.comparePosition());
    }

    private void updateObjectsPosition() {
        for (int i=0; i<mGameObjects.size(); i++) {
            mGameObjects.get(i).update();
            if (mGameObjects.get(i).getX() < -200) {
                mGameObjects.remove(i);
            }
        }
    }

    private void checkCollision() {
        for (int i=0; i<mGameObjects.size(); i++) {
            if (collision(mGameObjects.get(i), mPlayer)) {
                switch (mGameObjects.get(i).getObjectSubtype()) {
                    case "CAT":
                        mGameObjects.remove(i);
                        updateStateOnCollision("CAT", -5, -100);
                        break;
                    case "BALL":
                        mGameObjects.remove(i);
                        updateStateOnCollision("BALL", -1, -50);
                        break;
                    case "CHEESE":
                        mGameObjects.remove(i);
                        updateStateOnCollision("CHEESE", 1, 25);
                        break;
                    case "TRAP":
                        mGameObjects.remove(i);
                        updateStateOnCollision("TRAP", -2, -50);
                        break;
                    default:
                        Log.d(getClass().getSimpleName(), " checkCollision error, wrong subtype of colliding object");
                }
            }
        }
    }

    private boolean collision(GameObjectContainer object1, GameBaseObject object2) {
        return Rect.intersects(object1.getRectangle(), object2.getRectangle());
    }

    private void updateStateOnCollision(String objectType, int bonus, int points) {
        mPlayer.addExtraScore(points);
        mBonusCollected += bonus;
        if (mBonusCollected > 5) {
            mBonusCollected = 5;
        }
        if (isGameOver()) {
            isRunning = false;
            mBonusCollected = 0;
        }
        mCallback.onObjectCollision(mBonusCollected, objectType);
    }

    private boolean isGameOver() {
        return mBonusCollected < 0;
    }

    private void updateOnAttackCalled() {
        if (isAttackCalled) {
            isAttackCalled = false;
            destroyEnemies();
            resetBonus();
        }
    }

    public void setNewGame() {
        mGameObjects.clear();
        mPlayer.reset();
        mEnemyStartTime = 0;
        mBonusStartTime = 0;
        sObjectWasAdded = false;
        mBonusCollected = 0;
        mCallback.onGameReset();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void movePlayerUp() {
        mPlayer.moveUp();
    }

    public void movePlayerDown() {
        mPlayer.moveDown();
    }

    public void destroyEnemies() {
        for (int i=0; i<mGameObjects.size(); i++) {
            if (mGameObjects.get(i).getObjectType().equals("ENEMY")) {
                mGameObjects.remove(i);
                mPlayer.addExtraScore(mBonusCollected*100);
            }
        }
    }

    public void resetBonus() {
        mBonusCollected = 0;
    }

    public void setIsAttackCalled(boolean isCalled) {
        isAttackCalled = isCalled;
    }
}