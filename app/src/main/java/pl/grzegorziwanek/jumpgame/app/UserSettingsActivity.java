package pl.grzegorziwanek.jumpgame.app;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UserSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.activity_user_settings);
    }

    public void onClickMovement(View view)
    {

    }

    public void onClickDifficulty(View view)
    {

    }

    public void onClickLife(View view)
    {

    }

    public void onClickEnemies(View view)
    {

    }

    public void onClickBonuses(View view)
    {

    }
}
