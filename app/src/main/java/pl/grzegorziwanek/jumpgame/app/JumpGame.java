package pl.grzegorziwanek.jumpgame.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Grzegorz Iwanek on 24.08.2016.
 */
public class JumpGame extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //turning off title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set screen to always full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //run the MainMenu
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
}
