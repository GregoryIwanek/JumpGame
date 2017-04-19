package pl.grzegorziwanek.jumpgame.app.view.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.grzegorziwanek.jumpgame.app.R;
import pl.grzegorziwanek.jumpgame.app.databinding.GameFragmentBinding;
import pl.grzegorziwanek.jumpgame.app.models.gamemodel.panelmodel.GamePanel;
import pl.grzegorziwanek.jumpgame.app.viewmodel.GameViewModel;

// TODO: 11.04.2017 add transaction to the GameActivity class
// TODO: 11.04.2017 create layout with underlying GamePanel SurfaceView and UI interface
// TODO: 11.04.2017 (buttons, text views with scores)
public class GameFragment extends Fragment {
    GameFragmentBinding gameFragmentBinding;
    GameViewModel mGameViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_fragment, container, false);
        initiateBinding(view);
        return view;
    }

    private void initiateBinding(View view) {
        System.out.println("Fragment initiates data binding");
        gameFragmentBinding =
                DataBindingUtil.setContentView(this.getActivity(), R.layout.game_fragment);
        System.out.println("Fragment, data binding has been initiated");
        mGameViewModel = new GameViewModel(this.getActivity(), gameFragmentBinding.gamePanelContainer);
        System.out.println("mGameViewModel, data binding has been initiated");
        gameFragmentBinding.setMainGameViewModel(mGameViewModel);

        System.out.println("gameFragmentBinding, data binded");
    }
}
