package pl.grzegorziwanek.jumpgame.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Grzegorz Iwanek on 24.08.2016.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread mMainThread;
    private Background mBackground;
    private Bitmap mScaledBackgroundSource;
    private Player mPlayer;

    //container list for all game objects
    private ArrayList<GameObjectContainer> mGameObjects;
    private static long sEnemyStartTime;
    private static long sBonusStartTime;
    private static boolean sObjectAdded;

    private static Random sRand = new Random();

    public static final int MOVESPEED = -5; //movement speed of background image
    public static int sScreenWidth;
    public static int sScreenHeight;

    public GamePanel(Context context)
    {
        super(context);

        getHolder().addCallback(this);

        //initiate main thread
        mMainThread = new MainThread(getHolder(), this);

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
        mPlayer = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.mouse_walk), 75, 75, 12);

        mGameObjects = new ArrayList<>();
        sEnemyStartTime = System.nanoTime();
        sBonusStartTime = System.nanoTime();
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

            updateEnemy();
            updateBonus();
            updateObjects();
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
                mGameObjects.remove(i);
                mPlayer.setPlaying(false);
                break;
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
        long spawnDelay = 2500 - mPlayer.getScore()/4;
        if(spawnDelay < 1000)
        {
            spawnDelay = 1000;
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
        int yRand = (int)(sRand.nextDouble()*sScreenHeight - 100);
        if (yRand < 0)
        {
            yRand = 0;
        }
        //set random speed of enemy
        int speed = 7 + (int)(sRand.nextDouble()*mPlayer.getScore()/10); //rand.nextDouble() gives 1
        if(speed>40)
        {
            speed = 40;
        }

        //define bitmap frame image and numFrames, depending on speed of enemy, pick one of types of images to show ( fast running cat, walking cat etc;)
        Bitmap image;
        int numFrames;
        if (speed < 25)
        {
            image = BitmapFactory.decodeResource(getResources(), R.drawable.cat_normal_speed);
            numFrames = 12;
        }
        else
        {
            image = BitmapFactory.decodeResource(getResources(), R.drawable.cat_sprint_speed);
            numFrames = 13;
        }

        //add object to list
        mGameObjects.add(new GameObjectContainer(new Enemy(image, sScreenWidth+30, yRand, 200, 100, speed, numFrames, "enemy")));

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
                yRand = (int) (sRand.nextDouble() * sScreenHeight - image.getHeight());
                break;
            case 1:
                //set image to show
                image = BitmapFactory.decodeResource(getResources(), R.drawable.mouse_trap);
                yRand = (int) (sRand.nextDouble() * sScreenHeight - image.getHeight());
                break;
            default:
                //set image to show
                image = BitmapFactory.decodeResource(getResources(), R.drawable.cheese);
                yRand = (int) (sRand.nextDouble() * sScreenHeight - image.getHeight());
                break;
        }

        //make sure, object is on screen
        if(yRand < 0)
        {
            yRand = 0;
        }

        //set size of object depending on size of image
        int height = image.getHeight();
        int width = image.getWidth();

        //add object to container list
        mGameObjects.add(new GameObjectContainer(new Bonus(image, sScreenWidth+30, yRand, width, height, MOVESPEED)));

        //reset timer
        sBonusStartTime = System.nanoTime();
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        mBackground.draw(canvas);
        mPlayer.draw(canvas);

        for (GameObjectContainer gameObjectContainer : mGameObjects)
        {
            gameObjectContainer.draw(canvas);
        }
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
        //turn on main thread and game
        mMainThread.setRunning(true);
        mMainThread.start();
    }
}
