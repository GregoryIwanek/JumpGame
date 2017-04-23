package pl.grzegorziwanek.jumpgame.app.models.gameobjects.objects.bonus;

import android.graphics.Bitmap;

public class Trap extends Bonus {
    public Trap(Bitmap res, int x, int y, int width, int height, int speed, String type, String subtype) {
        super(res, x, y, width, height, speed, type, subtype);
    }
}
