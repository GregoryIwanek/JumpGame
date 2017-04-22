package pl.grzegorziwanek.jumpgame.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import pl.grzegorziwanek.jumpgame.app.BR;
import pl.grzegorziwanek.jumpgame.app.R;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.GameModel;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.panelmodel.GamePanel;

public class GameViewModel extends BaseObservable {

    private SoundPool soundPoolBackground;
    private SoundPool soundPoolCollision;
    private AudioAttributes attributes;
    private Context mContext;
    private GameModel mGameModel;
    @Bindable private ObservableInt mBonusNum = new ObservableInt(0);
    @Bindable private ObservableInt mScore = new ObservableInt(0);
    @Bindable private ObservableInt mBestScore = new ObservableInt(0);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameViewModel(Context context, GamePanel gamePanel) {
        mContext = context;
        attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        setSoundPools();
        mGameModel = new GameModel(context, gamePanel, new CallbackViewModel() {
            @Override
            public void onBonusCollected(int bonusCount) {
                mBonusNum.set(bonusCount);
                notifyChange();
            }

            @Override
            public void onScoreChanged(int score, int bestScore) {
                
                mScore.set(score);
                mBestScore.set(bestScore);
                notifyPropertyChanged(BR.score);
                notifyPropertyChanged(BR.bestScore);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setSoundPools() {
        soundPoolBackground = new SoundPool.Builder().
                setAudioAttributes(attributes)
                .build();
        soundPoolBackground.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 0.5f, 0.5f, 1, -1, 1f);
            }
        });
        soundPoolCollision = new SoundPool.Builder().
                setAudioAttributes(attributes)
                .build();
        soundPoolCollision.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 0.5f, 0.5f, 1, -1, 1f);
            }
        });
    }

    public void onButtonAttackClick(View view) {
        soundPoolBackground.load(mContext, R.raw.pim_poy_quiet, 1);

//        MediaPlayer player = MediaPlayer.create(mContext, R.raw.pim_poy_quiet);
//        player.start();
    }

    public void onButtonUpClick(View view) {
        mGameModel.movePlayerUp();
        soundPoolCollision.load(mContext, R.raw.mouse_trap_sound, 1);
    }

    public void onButtonDownClick(View view) {
        soundPoolCollision.load(mContext, R.raw.cat_hissing_sound, 1);
        mGameModel.movePlayerDown();
    }

    private void fetchGameResults() {

    }

    @Bindable
    public String getScore() {
        return "SCORE " + mScore.get();
    }

    @Bindable
    public String getBestScore() {
        return "BEST SCORE " + mBestScore.get();
    }

    @Bindable
    public int getBonusNum() {
        return mBonusNum.get();
    }
}
