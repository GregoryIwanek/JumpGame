package pl.grzegorziwanek.jumpgame.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
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

    public GameViewModel(Context context, GamePanel gamePanel) {
        mContext = context;
        setSoundPools();
        setGameModel(context, gamePanel);
    }

    private void setGameModel(Context context, GamePanel gamePanel) {
        mGameModel = new GameModel(context, gamePanel, new CallbackViewModel() {
            @Override
            public void onObjectCollision(int bonusCount, String subtype) {
                mBonusNum.set(bonusCount);
                playSound(subtype);
                notifyChange();
            }

            @Override
            public void onScoreChanged(int score, int bestScore) {
                mScore.set(score);
                mBestScore.set(bestScore);
                notifyPropertyChanged(BR.score);
                notifyPropertyChanged(BR.bestScore);
            }

            @Override
            public void onGameStart() {
                soundPoolBackground.load(mContext, R.raw.pim_poy_quiet, 1);
            }

            @Override
            public void onGameStop() {

            }
        });
    }

    private void setSoundPools() {
        // AudioAttributes.Builder() requires API >=  21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPoolBackground = new SoundPool.Builder().
                    setAudioAttributes(attributes)
                    .build();
            soundPoolCollision = new SoundPool.Builder().
                    setAudioAttributes(attributes)
                    .build();
        } else {
            soundPoolBackground = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            soundPoolCollision = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        soundPoolBackground.setOnLoadCompleteListener(
                (soundPool, sampleId, status) -> soundPool.play(sampleId, 0.5f, 0.5f, 1, -1, 1f)
        );
        soundPoolCollision.setOnLoadCompleteListener(
                (soundPool, sampleId, status) -> soundPool.play(sampleId, 0.5f, 0.5f, 1, 0, 1f)
        );
    }

    private void playSound(String type) {
        switch (type) {
            case "CHEESE":
                soundPoolCollision.load(mContext, R.raw.point_collected, 1);
                break;
            case "TRAP":
                soundPoolCollision.load(mContext, R.raw.mouse_trap_sound, 1);
                break;
            case "CAT":
                soundPoolCollision.load(mContext, R.raw.cat_angry, 1);
                break;
            case "BALL":
                soundPoolCollision.load(mContext, R.raw.ball_collision, 1);
                break;
            case "RESET":
                break;
        }
    }

    public void onButtonAttackClick(View view) {
        soundPoolBackground.load(mContext, R.raw.pim_poy_quiet, 1);
    }

    public void onButtonUpClick(View view) {
        mGameModel.movePlayerUp();
    }

    public void onButtonDownClick(View view) {
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
