package pl.grzegorziwanek.jumpgame.app.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import pl.grzegorziwanek.jumpgame.app.models.gamemodel.sessionmodel.SessionThreadddd;
import pl.grzegorziwanek.jumpgame.app.R;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.Background;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus.Bonus;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Enemy;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.GameObject;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.GameObjectContainer;
import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.Player;

/**
 * Created by Grzegorz Iwanek on 24.08.2016.
 * Contains main logic and implementation of a game. Contains surface for drawing on, and implements
 * every surface-action, like surface changed/created/destroyed etc.
 * Responsible for reading and reacting for each actions of a user, and managing life of objects
 * on a canvas.
 */
public class GamePanelOld extends SurfaceView implements SurfaceHolder.Callback {
    private SessionThreadddd mSessionThread;
    private Background mBackground;
    private Bitmap mScaledBackgroundSource;
    // TODO: 11.04.2017 change back to private, remove objects from GamePanel into separated class
    public Player mPlayer;

    //container list for all game objects, except Player object
    private ArrayList<GameObjectContainer> mGameObjects;
    private static long sEnemyStartTime;
    private static long sBonusStartTime;
    private static boolean sObjectAdded; //set true if object was added to list recently

    private static Random sRand = new Random();

    public static final int MOVESPEED = -5; //movement speed of background image
    public static final int SPAWNMARGIN = 55; //margin for spawning objects without collision with text
    public static int sScreenWidth;
    public static int sScreenHeight;

    private static int sBestScore = 0;
    private static int sBonusCollected = 0; //max 3 ( number of cheese pictures to show in interface)

    public GamePanelOld(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        System.out.println("called constructor of GamePanel");
    }

    public GamePanelOld(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GamePanelOld(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        System.out.println("GamePanel - surfaceCreated");
        //start thread
        setMainThread();

        //sets variables for defining other key values
        setVariables();

        //set objects
        mBackground = new Background(mScaledBackgroundSource, sScreenWidth);
        mPlayer = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.mouse_walk), 50, 50, 12);

        mGameObjects = new ArrayList<>();
        sObjectAdded = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        System.out.println("GamePanel - surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        System.out.println("GamePanel - surfaceDestroyed");
        //condition for retry loop, because, sometimes it might skip turning off thread
        boolean retry = true;
        int counter = 0;

        while (retry && counter < 1000) {
            counter++;
            try {
                //set condition for mainThread while() running false
                mSessionThread.setRunning(false);
                //block current thread
                mSessionThread.join();

                //set null, so GC will collect it
                mSessionThread = null;
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //when screen pressed
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            //start game if touched first time
//            if(!mPlayer.getPlaying()) {
//                mPlayer.setPlaying(true);
//                mPlayer.setGoingUp(true);
//                sEnemyStartTime = System.nanoTime();
//                sBonusStartTime = System.nanoTime();
//            } else {
//                //if already touched, set going up
//                mPlayer.setGoingUp(true);
//            }
//            //have to return boolean value
//            return true;
//        } else if(event.getAction() == MotionEvent.ACTION_UP) {
//            //when screen released
//            mPlayer.setGoingUp(false);
//            return true;
//        }
//
//        return super.onTouchEvent(event);
//    }

    public void update() {
        if (mPlayer.getPlaying()) {
            mBackground.update();
            mPlayer.update();

            updateVariables();
            updateEnemy();
            updateBonus();
            updateObjects();
        } else {
            setNewGame();
        }
    }

    public void updateVariables() {
        if (sBestScore < mPlayer.getScore()) {
            sBestScore = mPlayer.getScore();
        }
    }

    public void updateObjects() {
        //update all objects except player object and background object; sort list according to Y position
        //(sorting, because we want to draw objects in right order); sort ONLY if there is new object on a sorted list
        if (sObjectAdded) {
            sortList();
            sObjectAdded = false;
        }

        updatePosition();

        checkCollision();
    }

    private void sortList() {
        Collections.sort( mGameObjects, new Comparator<GameObjectContainer>() {
            public int compare (GameObjectContainer object1, GameObjectContainer object2) {
                return object1.comparePosition() - object2.comparePosition();
            }
        });
    }

    public void updatePosition() {
        for (int i=0; i<mGameObjects.size(); i++) {
            mGameObjects.get(i).update();
            if (mGameObjects.get(i).getX() < -200) {
                mGameObjects.remove(i);
            }
        }
    }


    public void checkCollision() {
        for (int i=0; i<mGameObjects.size(); i++) {
            if(collision(mGameObjects.get(i), mPlayer)) {
                switch (mGameObjects.get(i).getObjectType()) {
                    case "ENEMY":
                        mGameObjects.remove(i);
                        mPlayer.setPlaying(false);
                        mSessionThread = null;
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

    public boolean collision(GameObjectContainer object1, GameObject object2) {
        return Rect.intersects(object1.getRectangle(), object2.getRectangle());
    }

    private void updateBonusValue(String collidingObject) {
        if (collidingObject.equals("BONUS_CHEESE")) {
            if(sBonusCollected <3) {
                sBonusCollected++;
            }
        } else if (collidingObject.equals("BONUS_TRAP")) {
            sBonusCollected -= 2;
            //if is below zero, set GAME OVER
            if (sBonusCollected < 0) {
                mPlayer.setPlaying(false);
                mSessionThread = null;
                sBonusCollected = 0;
            }
        }
    }

    public void updateEnemy() {
        //calculate time passed till now
        long enemyElapsed = (System.nanoTime() - sEnemyStartTime) / 1000000;

        //calculate period between spawning enemies ( possible range 1.0 - 2.5 sec); getting shorter;
        long spawnDelay = 4000 - mPlayer.getScore()/3;
        if(spawnDelay < 1500) {
            spawnDelay = 1500;
        }

        //spawn enemy on the screen
        if(enemyElapsed > spawnDelay) {
            spawnEnemy();
            sObjectAdded = true;
        }
    }

    public void spawnEnemy() {
        //set random starting position and calculate speed, set caps (inside enemy class - if needed)
        int yRand = (int)(sRand.nextDouble()*sScreenHeight - (100+SPAWNMARGIN));
        if (yRand < SPAWNMARGIN) {
            yRand = SPAWNMARGIN;
        }
        //set random speed of enemy, hold it between 40 and MOVESPEED (5) of a background
        int speed = 4 + (int)(sRand.nextDouble()*mPlayer.getScore()/20); //rand.nextDouble() gives 1
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
            image = BitmapFactory.decodeResource(getResources(), R.drawable.cat_sleeping);
            numFrames = 1;
        } else if (speed < 20) {
            //picture - cat walking slowly
            image = BitmapFactory.decodeResource(getResources(), R.drawable.cat_normal_speed);
            numFrames = 12;
        } else if (speed < 30) {
            //picture - cat running slowly
            image = BitmapFactory.decodeResource(getResources(), R.drawable.cat_slow_run);
            numFrames = 16;
        } else {
            //picture - fast running cat
            image = BitmapFactory.decodeResource(getResources(), R.drawable.cat_sprint_speed);
            numFrames = 13;
        }

        //add object to list
        mGameObjects.add(new GameObjectContainer(new Enemy(image, sScreenWidth+30, yRand, width, height, speed, numFrames, "ENEMY")));

        //reset timer
        sEnemyStartTime = System.nanoTime();
    }

    public void updateBonus() {
        //calculate time passed till now
        long bonusElapsed = (System.nanoTime() - sBonusStartTime) / 1000000;

        //calculate spawn delay ( time between spawning bonuses)
        long spawnDelay = 5000 - mPlayer.getScore()/5;
        if(spawnDelay < 2000) {
            spawnDelay = 2000;
        }

        //spawn object if spawn delay has passed
        if(bonusElapsed > spawnDelay) {
            spawnBonus();
            sObjectAdded = true;
        }
    }

    public void spawnBonus() {
        //set random bonus (cheese, trap etc) and random position
        Bitmap image;
        String objectType;
        int yRand;

        int type = sRand.nextInt(2);
        switch (type) {
            case 0:
                image = BitmapFactory.decodeResource(getResources(), R.drawable.cheese);
                yRand = (int) (sRand.nextDouble() * sScreenHeight - (SPAWNMARGIN + image.getHeight()));
                objectType = "BONUS_CHEESE";
                break;
            case 1:
                //set image to show
                image = BitmapFactory.decodeResource(getResources(), R.drawable.mouse_trap);
                yRand = (int) (sRand.nextDouble() * sScreenHeight - (SPAWNMARGIN + image.getHeight()));
                objectType = "BONUS_TRAP";
                break;
            default:
                //set image to show
                image = BitmapFactory.decodeResource(getResources(), R.drawable.cheese);
                yRand = (int) (sRand.nextDouble() * sScreenHeight - (SPAWNMARGIN + image.getHeight()));
                objectType = "BONUS_CHEESE";
                break;
        }

        //make sure, object is on screen (cant't be below 0-> example: yRand = got 10 - 200(image height) = -190;)
        if(yRand < SPAWNMARGIN) {
            yRand = SPAWNMARGIN;
        }

        //set size of object depending on size of image
        int height = image.getHeight();
        int width = image.getWidth();

        //add object to container list
        mGameObjects.add(new GameObjectContainer(new Bonus(image, sScreenWidth+30, yRand, width, height, MOVESPEED, objectType)));

        //reset timer
        sBonusStartTime = System.nanoTime();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mBackground.draw(canvas);

        //draw objects
        mPlayer.draw(canvas);
        for (GameObjectContainer gameObjectContainer : mGameObjects) {
            gameObjectContainer.draw(canvas);
        }

        //draw score and results
        drawText(canvas);
        drawBonusBar(canvas);
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.drawText("SCORE: " + mPlayer.getScore(), 25, sScreenHeight - 25, paint);
        canvas.drawText("BEST SCORE: " + sBestScore, sScreenWidth - 325, sScreenHeight-25, paint);
    }

    public void drawBonusBar(Canvas canvas) {
        for (int i=0; i<3; i++) {
            Bitmap image;
            if (i<sBonusCollected) {
                image = BitmapFactory.decodeResource(getResources(), R.drawable.cheese_50x50);
            } else {
                image = BitmapFactory.decodeResource(getResources(), R.drawable.x_sign);
            }

            canvas.drawBitmap(image, 25+i*60, 10, null);
        }
    }

    private void setVariables() {
        //save screen width and height for further use
        sScreenWidth = getWidth();
        sScreenHeight = getHeight();

        //scale background (floor picture) bitmap to fill view of the phone and be background of a game
        Bitmap mBackgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.floor_background);
        mScaledBackgroundSource = Bitmap.createScaledBitmap(mBackgroundImage, sScreenWidth, sScreenHeight, true);
    }

    private void setMainThread() {
        //initiate main thread
        mSessionThread = new SessionThreadddd(getHolder(), this);

        //turn on main thread and game
        mSessionThread.setRunning(true);
        mSessionThread.start();
    }

    private void setNewGame() {
        mGameObjects.clear();

        mPlayer.resetScore();
        mPlayer.resetDY();
        mPlayer.setY(sScreenHeight/2);
        mPlayer.setPlaying(false);

        //sEnemyStartTime = 0;
        //sBonusStartTime = 0;
        sObjectAdded = false;
        sBonusCollected = 0;
    }
}
