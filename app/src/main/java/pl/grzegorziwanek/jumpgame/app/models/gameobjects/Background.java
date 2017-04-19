package pl.grzegorziwanek.jumpgame.app.models.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.GameObject;
import pl.grzegorziwanek.jumpgame.app.view.GamePanelOld;

/**
 * Created by Grzegorz Iwanek on 25.08.2016.
 * Contains class responsible for storing information about background image, updating them (position), and redrawing them on given canvas
 */
public class Background {
    private Bitmap image;

    //position coordinates and step on X axis
    private int x = 0;
    private int y = 0;
    private int dx = 0;
    private int mScreenWidth = 0;

    public Background(Bitmap image, int screenWidth) {
        this.image = image;
        mScreenWidth = screenWidth;
        this.dx = GamePanelOld.MOVESPEED;
    }

    public void update() {
        //update position of background
        x+=dx;

        //if image is off screen, reset it's position to start position
        if (x <= -mScreenWidth) {
            x=0;
        }
    }

    public void draw(Canvas canvas) {
        //draw first background image on the canvas
        canvas.drawBitmap(image, x, y, null);

        //draw second image, just right after first one ( illusion of constant backgrount terrain)
        canvas.drawBitmap(image, x+ mScreenWidth, y, null);

        //TODO: test one below on real phone, not in emulator, to make sure it's needed
        //Just in case: in emulator there is a black space for a second, after resetting images x position (update() method)
        canvas.drawBitmap(image, x+2*mScreenWidth, y, null);
    }
}
