package pl.grzegorziwanek.jumpgame.app.view.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import pl.grzegorziwanek.jumpgame.app.R;
import pl.grzegorziwanek.jumpgame.app.view.fragments.GameFragment;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDefaultFlags();
        setContentView(R.layout.game_activity);
        addFragment();
    }

    // call it before setContentView(...) - sys requirement
    private void initDefaultFlags() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void addFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFrame, new GameFragment())
                .commit();
    }
}
