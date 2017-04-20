package pl.grzegorziwanek.jumpgame.app.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;
import android.view.View;

import pl.grzegorziwanek.jumpgame.app.BR;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.GameModel;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.panelmodel.GamePanel;

public class GameViewModel extends BaseObservable {

    private Context mContext;
    private GameModel mGameModel;
    @Bindable
    private ObservableInt mBonusNum = new ObservableInt(0);
    @Bindable
    private ObservableInt mScore = new ObservableInt(0);
    @Bindable
    ObservableInt mBestScore = new ObservableInt(0);

    public GameViewModel(Context context, GamePanel gamePanel) {
        mContext = context;
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

    public void onItemClick(View view) {
    }

    public void onButtonAttackClick(View view) {
    }

    public void onButtonUpClick(View view) {
        mGameModel.movePlayerUp();
    }

    public void onButtonDownClick(View view) {
        mGameModel.movePlayerDown();
    }

    public GamePanel getPanelForBinding() {
        return mGameModel.getPanelForBinding();
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

//    @OnClick({R.id.button_attack, R.id.button_up, R.id.button_down})
//    public void onButtonAttackClick(View view) {
//        int tag = (int) view.getTag();
//        switch (tag) {
//            case R.drawable.button_attack:
//                System.out.println("attack");
//                break;
//            case R.drawable.button_up:
//                System.out.println("up");
//                int y = mGamePanel.mPlayer.getY();
//                mGamePanel.mPlayer.setY(y - 50);
//                break;
//            case R.drawable.button_down:
//                System.out.println("down");
//                int yDown = mGamePanel.mPlayer.getY();
//                mGamePanel.mPlayer.setY(yDown + 50);
//                break;
//        }
//    }
