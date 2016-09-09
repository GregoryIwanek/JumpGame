package pl.grzegorziwanek.jumpgame.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Grzegorz Iwanek on 24.08.2016.
 * Contains main logic and implementation of a game. Contains surface for drawing on, and implements
 * every surface-action, like surface changed/created/destroyed etc.
 * Responsible for reading and reacting for each actions of a user, and managing life of objects
 * on a canvas.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread mMainThread;
    private Background mBackground;
    private Bitmap mScaledBackgroundSource;
    private Player mPlayer;

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

    private static final Paint squareColor = new Paint(Color.CYAN);
    private static int sBestScore = 0;
    private static int sBonusCollected = 0;

    public GamePanel(Context context)
    {
        super(context);

        getHolder().addCallback(this);

        //enable reading user touch
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder)
    {
        //start thread
        setMainThread();

        //sets variables for defining other key values
        setVariables();

        //set objects
        mBackground = new Background(mScaledBackgroundSource);
        mPlayer = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.mouse_walk), 50, 50, 12);

        mGameObjects = new ArrayList<>();
        sObjectAdded = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2)
    {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder)
    {
        //condition for retry loop, because, sometimes it might skip turning off thread
        boolean retry = true;
        int counter = 0;

        while (retry && counter < 1000)
        {
            counter++;
            try
            {
                //set condition for mainThread while() running false
                mMainThread.setRunning(false);
                //block current thread
                mMainThread.join();

                mMainThread = null;
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //when screen pressed
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            //start game if touched first time
            if(!mPlayer.getPlaying())
            {
                mPlayer.setPlaying(true);
                mPlayer.setGoingUp(true);
                sEnemyStartTime = System.nanoTime();
                sBonusStartTime = System.nanoTime();
            }
            //if already touched, set going up
            else
            {
                mPlayer.setGoingUp(true);
            }
            //have to return boolean value
            return true;
        }
        //when screen released
        else if(event.getAction() == MotionEvent.ACTION_UP)
        {
            mPlayer.setGoingUp(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()
    {
        if (mPlayer.getPlaying())
        {
            mBackground.update();
            mPlayer.update();

            updateVariables();
            updateEnemy();
            updateBonus();
            updateObjects();
        }
        else
        {
            setNewGame();
        }
    }

    public void updateVariables()
    {
        if (sBestScore < mPlayer.getScore())
        {
            sBestScore = mPlayer.getScore();
        }
    }

    public void updateObjects()
    {
        //update all objects except player object and background object; sort list according to Y position
        //(sorting, because we want to draw objects in right order); sort ONLY if there is new object on a sorted list
        if (sObjectAdded)
        {
            sortList();
            sObjectAdded = false;
        }

        updatePosition();

        checkCollision();
    }

    private void sortList()
    {
        Collections.sort( mGameObjects, new Comparator<GameObjectContainer>() {
            public int compare (GameObjectContainer object1, GameObjectContainer object2) {
                return object1.comparePosition() - object2.comparePosition();
            }
        });
    }

    public void updatePosition()
    {
        for (int i=0; i<mGameObjects.size(); i++)
        {
            mGameObjects.get(i).update();
            if (mGameObjects.get(i).getX() < -200)
            {
                mGameObjects.remove(i);
            }
        }
    }

    public void checkCollision()
    {
        for (int i=0; i<mGameObjects.size(); i++)
        {
            if(collision(mGameObjects.get(i), mPlayer))
            {
                if (mGameObjects.get(i).getObjectType() != "BONUS")
                {
                    System.out.println(mGameObjects.get(i).getObjectType());
                    mGameObjects.remove(i);
                    mPlayer.setPlaying(false);
                    mMainThread = null;
                    break;
                }
                else
                {
                    mGameObjects.remove(i);
                    sBonusCollected++;
                }
            }
        }
    }
    public boolean collision(GameObjectContainer object1, GameObject object2) {
        return Rect.intersects(object1.getRectangle(), object2.getRectangle());
    }

    public void updateEnemy()
    {
        //calculate time passed till now
        long enemyElapsed = (System.nanoTime() - sEnemyStartTime) / 1000000;

        //calculate period between spawning enemies ( possible range 1.0 - 2.5 sec); getting shorter;
        long spawnDelay = 3000 - mPlayer.getScore()/4;
        if(spawnDelay < 1500)
        {
            spawnDelay = 1500;
        }

        //spawn enemy on the screen
        if(enemyElapsed > spawnDelay)
        {
            spawnEnemy();
            sObjectAdded = true;
        }
    }

    public void spawnEnemy()
    {
        //set random starting position and calculate speed, set caps (inside enemy class - if needed)
        int yRand = (int)(sRand.nextDouble()*sScreenHeight - (100+SPAWNMARGIN));
        if (yRand < SPAWNMARGIN)
        {
            yRand = SPAWNMARGIN;
        }
        //set random speed of enemy, hold it between 40 and MOVESPEED (5) of a background
        int speed = 4 + (int)(sRand.nextDouble()*mPlayer.getScore()/10); //rand.nextDouble() gives 1
        if (speed < 7)
        {
            speed = 5;
        }
        else if(speed>40)
        {
            speed = 40;
        }

        //define bitmap frame image and numFrames, depending on speed of enemy, pick one of types
        // of images to show ( fast running cat, walking cat etc;)
        Bitmap image;
        int numFrames;
        if (speed == 5)
        {
            //picture - cat sitting in one place
            image = BitmapFactory.decodeResource(getResources(), R.drawable.cat_sitting);
            numFrames = 6;
        }
        else if (speed < 25)
        {
            //picture - cat walking slowly
            image = BitmapFactory.decodeResource(getResources(), R.drawable.cat_normal_speed);
            numFrames = 12;
        }
        else
        {
            //picture - fast running cat
            image = BitmapFactory.decodeResource(getResources(), R.drawable.cat_sprint_speed);
            numFrames = 13;
        }

        //add object to list
        mGameObjects.add(new GameObjectContainer(new Enemy(image, sScreenWidth+30, yRand, 200, 100, speed, numFrames, "ENEMY")));

        //reset timer
        sEnemyStartTime = System.nanoTime();
    }

    public void updateBonus()
    {
        //calculate time passed till now
        long bonusElapsed = (System.nanoTime() - sBonusStartTime) / 1000000;

        //calculate spawn delay ( time between spawning bonuses)
        long spawnDelay = 5000 - mPlayer.getScore()/5;
        if(spawnDelay < 2000)
        {
            spawnDelay = 2000;
        }

        //spawn object if spawn delay has passed
        if(bonusElapsed > spawnDelay)
        {
            spawnBonus();
            sObjectAdded = true;
        }
    }

    public void spawnBonus()
    {
        //set random bonus (cheese, trap etc) and random position
        Bitmap image;
        int yRand;

        int type = sRand.nextInt(2);
        switch (type)
        {
            case 0:
                image = BitmapFactory.decodeResource(getResources(), R.drawable.cheese);
                yRand = (int) (sRand.nextDouble() * sScreenHeight - (SPAWNMARGIN + image.getHeight()));
                break;
            case 1:
                //set image to show
                image = BitmapFactory.decodeResource(getResources(), R.drawable.mouse_trap);
                yRand = (int) (sRand.nextDouble() * sScreenHeight - (SPAWNMARGIN + image.getHeight()));
                break;
            default:
                //set image to show
                image = BitmapFactory.decodeResource(getResources(), R.drawable.cheese);
                yRand = (int) (sRand.nextDouble() * sScreenHeight - (SPAWNMARGIN + image.getHeight()));
                break;
        }

        //make sure, object is on screen (cant't be below 0-> example: yRand = got 10 - 200(image height) = -190;)
        if(yRand < SPAWNMARGIN)
        {
            yRand = SPAWNMARGIN;
        }

        //set size of object depending on size of image
        int height = image.getHeight();
        int width = image.getWidth();

        //add object to container list
        mGameObjects.add(new GameObjectContainer(new Bonus(image, sScreenWidth+30, yRand, width, height, MOVESPEED, "BONUS")));

        //reset timer
        sBonusStartTime = System.nanoTime();
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        mBackground.draw(canvas);

        //draw objects
        mPlayer.draw(canvas);
        for (GameObjectContainer gameObjectContainer : mGameObjects)
        {
            gameObjectContainer.draw(canvas);
        }
        //draw score and results
        drawText(canvas);
    }

    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.drawText("SCORE: " + mPlayer.getScore(), 25, sScreenHeight - 25, paint);
        canvas.drawText("CHEESE: " + sBonusCollected, 25, 50, paint);
        canvas.drawText("BEST SCORE: " + sBestScore, sScreenWidth - 350, sScreenHeight-25, paint);
    }

    private void setVariables()
    {
        //save screen width and height for further use
        sScreenWidth = getWidth();
        sScreenHeight = getHeight();

        //scale background (floor picture) bitmap to fill view of the phone and be background of a game
        Bitmap mBackgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.floor_background);
        mScaledBackgroundSource = Bitmap.createScaledBitmap(mBackgroundImage, sScreenWidth, sScreenHeight, true);
    }

    private void setMainThread()
    {
        //initiate main thread
        mMainThread = new MainThread(getHolder(), this);

        //turn on main thread and game
        mMainThread.setRunning(true);
        mMainThread.start();
    }

    private void setNewGame()
    {
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
