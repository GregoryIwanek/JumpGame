package pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import pl.grzegorziwanek.jumpgame.app.R;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.Callbacks.DataCallback;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.Background;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Enemy;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.GameObject;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Player;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus.Bonus;
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

        mPlayer = new Player(BitmapFactory.decodeResource(mResources, R.drawable.mouse_walk), 50, 50, 12);
        mGameObjects = new ArrayList<>();
    }

    public void update() {
        mBackground.update();
        mPlayer.update();
        updateVariables();
        updateEnemy();
        updateBonus();
        updateObjects();
        mCallback.onDataUpdated(mBackground, mPlayer, mGameObjects);
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
        //set random bonus (cheese, trap etc) and random position
        Bitmap image;
        String objectType;
        int yRand;

        int type = mRand.nextInt(2);
        switch (type) {
            case 0:
                image = BitmapFactory.decodeResource(mResources, R.drawable.cheese);
                yRand = (int) (mRand.nextDouble() * mHeight - (Cons.SPAWNMARGIN + image.getHeight()));
                objectType = "BONUS_CHEESE";
                break;
            case 1:
                //set image to show
                image = BitmapFactory.decodeResource(mResources, R.drawable.mouse_trap);
                yRand = (int) (mRand.nextDouble() * mHeight - (Cons.SPAWNMARGIN + image.getHeight()));
                objectType = "BONUS_TRAP";
                break;
            default:
                //set image to show
                image = BitmapFactory.decodeResource(mResources, R.drawable.cheese);
                yRand = (int) (mRand.nextDouble() * mHeight - (Cons.SPAWNMARGIN + image.getHeight()));
                objectType = "BONUS_CHEESE";
                break;
        }

        //make sure, object is on screen (cant't be below 0-> example: yRand = got 10 - 200(image height) = -190;)
        if(yRand < Cons.SPAWNMARGIN) {
            yRand = Cons.SPAWNMARGIN;
        }

        //set size of object depending on size of image
        int height = image.getHeight();
        int width = image.getWidth();

        //add object to container list
        mGameObjects.add(new GameObjectContainer(new Bonus(image, mWidth+30, yRand, width, height, Cons.MOVESPEED, objectType)));

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
        Collections.sort(mGameObjects, new Comparator<GameObjectContainer>() {
            public int compare (GameObjectContainer object1, GameObjectContainer object2) {
                return object1.comparePosition() - object2.comparePosition();
            }
        });
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
                        mPlayer.setPlaying(false);
                        //mSessionThread = null;
                        break;
                    case "BONUS_CHEESE":
                        mGameObjects.remove(i);
                        updateBonusValue("BONUS_CHEESE");
                        mPlayer.addExtraScore(25);
                        break;
                    case "BONUS_TRAP":
                        mGameObjects.remove(i);
                        updateBonusValue("BONUS_TRAP");
                        mPlayer.addExtraScore(-50);
                        break;
                    default:
                        System.out.println("ERROR OF OBJECT TYPE OCCUR: UNDEFINED TYPE OF OBJECT COLLIDING WITH PLAYER");
                        break;
                }
            }
        }
    }

    private void updateBonusValue(String collidingObject) {
        if (collidingObject.equals("BONUS_CHEESE")) {
            if(mBonusCollected <5) {
                mBonusCollected++;
            }
        } else if (collidingObject.equals("BONUS_TRAP")) {
            mBonusCollected -= 2;
            //if is below zero, set GAME OVER
            if (mBonusCollected < 0) {
                mPlayer.setPlaying(false);
                //mSessionThread = null;
                mBonusCollected = 0;
            }
        }
    }

    public void setNewGame() {
        mGameObjects.clear();
        mPlayer.resetScore();
        mPlayer.resetDY();
        mPlayer.setY(mHeight/2);
        mPlayer.setPlaying(false);
        mEnemyStartTime = 0;
        mBonusStartTime = 0;
        sObjectWasAdded = false;
        mBonusCollected = 0;
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

    public String getScore() {
        return "SCORE " + mPlayer.getScore();
    }

    public String getBestScore() {
        return "BEST SCORE " + mBestScore;
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