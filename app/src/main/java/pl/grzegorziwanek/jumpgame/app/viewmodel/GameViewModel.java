package pl.grzegorziwanek.jumpgame.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import pl.grzegorziwanek.jumpgame.app.BR;
import pl.grzegorziwanek.jumpgame.app.R;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.GameModel;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.panelmodel.GamePanel;

public class GameViewModel extends BaseObservable {

    private SoundPool soundPoolCollision;
    private SoundPool soundPoolBackground;
    private SoundPool soundPoolExplossion;
    int myStreamId;
    private AudioAttributes attributes;
    private Animation animation;
    private FrameLayout backgroundCover;
    private Context mContext;
    private GameModel mGameModel;
    @Bindable private ObservableInt mBonusNum = new ObservableInt(0);
    @Bindable private ObservableInt mScore = new ObservableInt(0);
    @Bindable private ObservableInt mBestScore = new ObservableInt(0);

    public GameViewModel(Context context, GamePanel gamePanel, FrameLayout cover) {
        mContext = context;
        backgroundCover = cover;
        setSoundPools();
        setGameModel(context, gamePanel);
        setAnimation();
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
            public void onGameReset() {
                mBonusNum.set(0);
                soundPoolBackground.stop(myStreamId);
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
                myStreamId = soundPoolBackground.load(mContext, R.raw.pim_pom_music, 1);
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
            soundPoolExplossion = new SoundPool.Builder().
                    setAudioAttributes(attributes)
                    .build();
        } else {
            soundPoolBackground = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            soundPoolCollision = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            soundPoolExplossion = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        // set listeners
        soundPoolBackground.setOnLoadCompleteListener(
                (soundPool, sampleId, status) -> soundPool.play(sampleId, 0.5f, 0.5f, 1, -1, 1f)
        );
        soundPoolCollision.setOnLoadCompleteListener(
                (soundPool, sampleId, status) -> soundPool.play(sampleId, 0.5f, 0.5f, 1, 0, 1f)
        );
        soundPoolExplossion.setOnLoadCompleteListener((
                soundPool, sampleId, status) -> soundPool.play(sampleId, 0.5f, 0.5f, 1, 0, 1f));
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
        }
    }

    private void setAnimation() {
        animation = AnimationUtils.loadAnimation(mContext, R.anim.attack_animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mGameModel.attack();
                backgroundCover.setBackgroundColor(Color.TRANSPARENT);
            }
        });
    }

    public void onButtonAttackClick(View view) {
        if (mBonusNum.get() >= 2) {
            soundPoolExplossion.load(mContext, R.raw.explosion, 1);
            backgroundCover.setBackgroundColor(Color.BLACK);
            backgroundCover.startAnimation(animation);
            mBonusNum.set(0);
            notifyChange();
        }
    }

    public void onButtonUpClick(View view) {
        mGameModel.movePlayerUp();
    }

    public void onButtonDownClick(View view) {
        mGameModel.movePlayerDown();
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
