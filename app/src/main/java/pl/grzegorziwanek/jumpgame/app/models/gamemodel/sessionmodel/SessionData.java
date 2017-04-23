package pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import pl.grzegorziwanek.jumpgame.app.R;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.Callbacks.DataCallback;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.Background;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObject;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Enemy;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Player;
import pl.grzegorziwanek.jumpgame.app.models.objectfactory.ObjectFactory;
import pl.grzegorziwanek.jumpgame.app.utilis.Cons;

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

    private int mWidth;
    private int mHeight;
    private int mBestScore;
    private int mBonusCollected;
    private boolean isRunning;

    public SessionData(Context context, DataCallback callback) {
        mContext = context;
        mCallback = callback;
        initData();
        setNewGame();
    }

    private void initData() {
        initVariables();
        initObjects();
    }

    private void initVariables() {
        mWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        Cons.initScreenSize(mWidth, mHeight);
        mBestScore = 0;
        mBonusCollected = 0;
        sObjectWasAdded = false;
        isRunning = false;
        mResources = mContext.getResources();
    }

    private void initObjects() {
        // background
        Bitmap backImage = BitmapFactory.decodeResource(mResources, R.drawable.floor_background);
        mScaledImage = Bitmap.createScaledBitmap(backImage, mWidth, mHeight, true);
        mBackground = new Background(mScaledImage, mWidth);

        mPlayer = (Player) ObjectFactory.getUnwrappedObject(generatePlayerData(), mContext);
        mGameObjects = new ArrayList<>();
    }

    public void update() {
        if (isRunning) {
            mBackground.update();
            mPlayer.update();
            updateVariables();
            updateEnemy();
            updateBonus();
            updateObjects();
        } else {
            setNewGame();
        }
        mCallback.onDataUpdated(mBackground, mPlayer, mGameObjects);
        mCallback.onScoreChanged(mPlayer.getScore(), mBestScore);
    }

    private void updateVariables() {
        if (mBestScore < mPlayer.getScore()) {
            mBestScore = mPlayer.getScore();
        }
    }

    private void updateEnemy() {
        //calculate time passed till now
        long enemyElapsed = (System.nanoTime() - mEnemyStartTime) / 1000000;

        //calculate period between spawning enemies ( possible range 1.5 - 4 sec);
        long spawnDelay = 4000 - mPlayer.getScore()/3;
        if(spawnDelay < 1500) {
            spawnDelay = 1500;
        }

        //spawn enemy on the screen
        if(enemyElapsed > spawnDelay) {
            spawnEnemy();
            sObjectWasAdded = true;
        }
    }

    private void spawnEnemy() {
        //set random starting position and calculate speed, set caps (inside enemy class - if needed)
        int yRand = (int)(mRand.nextDouble()*mHeight - (100+Cons.SPAWNMARGIN));
        if (yRand < Cons.SPAWNMARGIN) {
            yRand = Cons.SPAWNMARGIN;
        }
        //set random speed of enemy, hold it between 40 and MOVESPEED (5) of a background
        int speed = 4 + (int)(mRand.nextDouble()*mPlayer.getScore()/20); //rand.nextDouble() gives 1
        if (speed < 10) {
            speed = 5;
        } else if(speed>40) {
            speed = 40;
        }

        //define bitmap frame image and numFrames, depending on speed of enemy, pick one of types
        // of images to show ( fast running cat, walking cat etc;)
        Bitmap image;
        int numFrames;
        int width = 200;
        int height = 75;

        if (speed == 5) {
            //picture - cat sitting in one place
            image = BitmapFactory.decodeResource(mResources, R.drawable.cat_sleeping);
            numFrames = 1;
        } else if (speed < 20) {
            //picture - cat walking slowly
            image = BitmapFactory.decodeResource(mResources, R.drawable.cat_normal_speed);
            numFrames = 12;
        } else if (speed < 30) {
            //picture - cat running slowly
            image = BitmapFactory.decodeResource(mResources, R.drawable.cat_slow_run);
            numFrames = 16;
        } else {
            //picture - fast running cat
            image = BitmapFactory.decodeResource(mResources, R.drawable.cat_sprint_speed);
            numFrames = 13;
        }

        //add object to list
        mGameObjects.add(new GameObjectContainer(new Enemy(image, mWidth+30, yRand, width, height, speed, numFrames, "ENEMY")));

        //reset timer
        mEnemyStartTime = System.nanoTime();
    }

    private void updateBonus() {
        //calculate time passed till now
        long bonusElapsed = (System.nanoTime() - mBonusStartTime) / 1000000;

        //calculate spawn delay ( time between spawning bonuses)
        long spawnDelay = 5000 - mPlayer.getScore()/5;
        if(spawnDelay < 2000) {
            spawnDelay = 2000;
        }

        //spawn object if spawn delay has passed
        if(bonusElapsed > spawnDelay) {
            spawnBonus();
            sObjectWasAdded = true;
        }
    }

    private void spawnBonus() {
        String subtype;
        int height;
        int width;
        int resId;
        int yRand;
        int x = mWidth+30;

        int randomType = mRand.nextInt(2);
        switch (randomType) {
            case 0:
                resId = R.drawable.cheese;
                yRand = (int) (mRand.nextDouble() * mHeight - (Cons.SPAWNMARGIN + 50));
                width = 75;
                height = 50;
                subtype = "CHEESE";
                break;
            case 1:
                resId = R.drawable.mouse_trap;
                yRand = (int) (mRand.nextDouble() * mHeight - (Cons.SPAWNMARGIN + 50));
                width = 50;
                height = 50;
                subtype = "TRAP";
                break;
            default:
                resId = R.drawable.cheese;
                yRand = (int) (mRand.nextDouble() * mHeight - (Cons.SPAWNMARGIN + 50));
                width = 75;
                height = 50;
                subtype = "CHEESE";
                break;
        }

        //make sure, object is on screen (cant't be below 0-> example: yRand = got 10 - 200(image height) = -190;)
        if(yRand < Cons.SPAWNMARGIN) {
            yRand = Cons.SPAWNMARGIN;
        }


        //add object to container list
        mGameObjects.add(ObjectFactory.getWrappedObject(
                generateBonusData(subtype, resId, x, yRand, width, height, Cons.MOVESPEED),
                mContext));

        //reset timer
        mBonusStartTime = System.nanoTime();
    }

    private void updateObjects() {
        //update all objects except player object and background object; sort list according to Y position
        //(sorting, because we want to draw objects in right order); sort ONLY if there is new object on a sorted list
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

    private boolean collision(GameObjectContainer object1, GameObject object2) {
        return Rect.intersects(object1.getRectangle(), object2.getRectangle());
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
            if(collision(mGameObjects.get(i), mPlayer)) {
                switch (mGameObjects.get(i).getObjectType()) {
                    case "ENEMY":
                        mGameObjects.remove(i);
                        mCallback.onEnemyCollision();
                        isRunning = false;
                        break;
                    case "BONUS":
                        // TODO: 23.04.2017 refactor that part (split subtypes of bonuses)
                        System.out.println("CHECK COLLISION: " + mGameObjects.get(i).getObjectSubtype());
                        if (mGameObjects.get(i).getObjectSubtype().equals("CHEESE")) {
                            mGameObjects.remove(i);
                            updateBonusValue("CHEESE");
                            mPlayer.addExtraScore(25);
                        } else if (mGameObjects.get(i).getObjectSubtype().equals("TRAP")) {
                            mGameObjects.remove(i);
                            updateBonusValue("TRAP");
                            mPlayer.addExtraScore(-50);
                        }
                        break;
                    default:
                        System.out.println("ERROR OF OBJECT TYPE OCCUR: UNDEFINED TYPE OF OBJECT COLLIDING WITH PLAYER");
                        break;
                }
            }
        }
    }

    private void updateBonusValue(String collidingObject) {
        if (collidingObject.equals("CHEESE")) {
            if(mBonusCollected <5) {
                mBonusCollected++;
            }
            mCallback.onBonusCollected(mBonusCollected, "CHEESE");
        } else if (collidingObject.equals("TRAP")) {
            mBonusCollected -= 2;
            //if is below zero, set GAME OVER
            if (mBonusCollected < 0) {
                isRunning = false;
                mBonusCollected = 0;
            }
            mCallback.onBonusCollected(mBonusCollected, "TRAP");
        }
    }

    private Bundle generatePlayerData() {
        Bundle b = new Bundle();
        b.putString("TYPE", "PLAYER");
        b.putInt("ID", R.drawable.mouse_walk);
        b.putInt("WIDTH", 50);
        b.putInt("HEIGHT", 50);
        b.putInt("FRAMES", 12);
        return b;
    }

    private Bundle generateEnemeyData() {
        Bundle b = new Bundle();
        b.putString("TYPE", "PLAYER");
        b.putInt("ID", R.drawable.mouse_walk);
        b.putInt("WIDTH", 50);
        b.putInt("HEIGHT", 50);
        b.putInt("FRAMES", 12);
        return b;
    }

    private Bundle generateBonusData(String subtype,int resId, int x, int y, int width, int height, int speed) {
        Bundle b = new Bundle();
        b.putString("TYPE", "BONUS");
        b.putString("SUBTYPE", subtype);
        b.putInt("ID", resId);
        b.putInt("X", x);
        b.putInt("Y", y);
        b.putInt("WIDTH", width);
        b.putInt("HEIGHT", height);
        b.putInt("SPEED", speed);
        return b;
    }

    public void setNewGame() {
        mGameObjects.clear();
        mPlayer.setY(mHeight/2);
        mPlayer.reset();
        mEnemyStartTime = 0;
        mBonusStartTime = 0;
        sObjectWasAdded = false;
        mBonusCollected = 0;
        // TODO: 20.04.2017 add callback with score
        mCallback.onBonusCollected(mBonusCollected, "RESET");
    }

    public Background getBackground() {
        return mBackground;
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

//    public void drawText(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(30);
//        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//
//        canvas.drawText("SCORE: " + mPlayer.getScore(), 25, sScreenHeight - 25, paint);
//        canvas.drawText("BEST SCORE: " + mBestScore, sScreenWidth - 325, sScreenHeight-25, paint);
//    }
//
//    public void drawBonusBar(Canvas canvas) {
//        for (int i=0; i<5; i++) {
//            Bitmap image;
//            if (i<mBonusCollected) {
//                image = BitmapFactory.decodeResource(getResources(), R.drawable.cheese_50x50);
//            } else {
//                image = BitmapFactory.decodeResource(getResources(), R.drawable.x_sign);
//            }
//
//            canvas.drawBitmap(image, 25+i*60, 10, null);
//        }
//    }
}