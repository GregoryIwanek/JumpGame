package pl.grzegorziwanek.jumpgame.app.viewmodel;

import android.content.Context;
import android.view.View;

import java.util.Observable;

import pl.grzegorziwanek.jumpgame.app.models.gamemodel.GameModel;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.panelmodel.GamePanel;

public class GameViewModel extends Observable {

    private Context mContext;
    private GameModel mGameModel;
    private String score = "SCORE 0";
    private String bestScore = "BEST SCORE 0";

    public GameViewModel(Context context, GamePanel gamePanel) {
        System.out.println("GameViewModel constructor called");
        mContext = context;
        mGameModel = new GameModel(context, gamePanel);
    }

    public void onItemClick(View view) {
    }

    public void onButtonAttackClick(View view) {
    }

    public void onButtonUpClick(View view) {
    }

    public void onButtonDownClick(View view) {
    }

    public GamePanel getPanelForBinding() {
        return mGameModel.getPanelForBinding();
    }

    private void fetchGameResults() {

    }

    public String getScore() {
        System.out.println(mGameModel.getScore());
        return mGameModel.getScore();
    }

    public String getBestScore() {
        System.out.println(mGameModel.getBestScore());
        return mGameModel.getBestScore();
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
