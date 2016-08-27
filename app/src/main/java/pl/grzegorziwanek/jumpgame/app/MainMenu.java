package pl.grzegorziwanek.jumpgame.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainMenu extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //turning off title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_menu);
    }

    public void onClickPlay(View view)
    {
        setContentView(new GamePanel(this));
    }

    public void onClickOptions(View view)
    {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void onClickQuit(View view)
    {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
