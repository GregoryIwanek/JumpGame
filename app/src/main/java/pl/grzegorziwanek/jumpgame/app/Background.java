package pl.grzegorziwanek.jumpgame.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Grzegorz Iwanek on 25.08.2016.
 * Contains class responsible for storing information about background image, updating them (position), and redrawing them on given canvas
 */
public class Background
{

    private Bitmap image;

    //position coordinates and step on X axis
    private int x = 0;
    private int y = 0;
    private int dx = 0;

    public Background(Bitmap image)
    {
        this.image = image;
        this.dx = GamePanel.MOVESPEED;
    }

    public void update()
    {
        //update position of background
        x+=dx;

        //if image is off screen, reset it's position to start position
        if(x <= -GamePanel.sScreenWidth)
        {
            x=0;
        }
    }

    public void draw(Canvas canvas)
    {
        //draw first background image on the canvas
        canvas.drawBitmap(image, x, y, null);

        //draw second image, just right after first one ( illusion of constant backgrount terrain)
        canvas.drawBitmap(image, x+GamePanel.sScreenWidth, y, null);

        //TODO: test one below on real phone, not in emulator, to make sure it's needed
        //Just in case: in emulator there is a black space for a second, after resetting images x position (update() method)
        canvas.drawBitmap(image, x+2*GamePanel.sScreenWidth, y, null);
    }
}
