package pl.grzegorziwanek.jumpgame.app.utilis;

import android.graphics.Bitmap;

/**
 * Wrapper class for game objects parameters ( to avoid creation of long constructors).
 */
public class ObjectParameters {
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private String type;
    private String subType;
    private Bitmap imageRes;
    private int numFrames;
    private int framesInterval;

    /**
     * Constructor used with objects without known parameters;
     * Parameters are defined during a roll process through setters;
     */
    public ObjectParameters() {}

    /**
     * Constructor used for objects with known parameters;
     * @param x position on X axis;
     * @param y position on Y axis;
     * @param width width of the object;
     * @param height height of the object;
     * @param speed speed on X axis;
     * @param type main category of object;
     * @param subType subcategory of main category;
     * @param imageRes image resource of the object;
     * @param numFrames in case it's animated object, number of frames to show in animation;
     */
    public ObjectParameters(int x, int y, int width, int height, int speed,
                            String type, String subType, Bitmap imageRes, int numFrames) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.type = type;
        this.subType = subType;
        this.imageRes = imageRes;
        this.numFrames = numFrames;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Bitmap getImageRes() {
        return imageRes;
    }

    public void setImageRes(Bitmap imageRes) {
        this.imageRes = imageRes;
    }

    public int getNumFrames() {
        return numFrames;
    }

    public void setNumFrames(int numFrames) {
        this.numFrames = numFrames;
    }

    public int getFramesInterval() {
        return framesInterval;
    }

    public void setFramesInterval(int framesInterval) {
        this.framesInterval = framesInterval;
    }

    public void setX(int x) {
        this.x = x;
    }
}
