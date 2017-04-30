package pl.grzegorziwanek.jumpgame.app.view.fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import pl.grzegorziwanek.jumpgame.app.R;
import pl.grzegorziwanek.jumpgame.app.databinding.GameFragmentBinding;
import pl.grzegorziwanek.jumpgame.app.utilis.Cons;
import pl.grzegorziwanek.jumpgame.app.viewmodel.GameViewModel;

public class GameFragment extends Fragment {
    GameFragmentBinding gameFragmentBinding;
    GameViewModel mGameViewModel;
    FrameLayout mFrameLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_fragment, container, false);
        initiateBinding();
        return view;
    }

    private void initiateBinding() {
        gameFragmentBinding =
                DataBindingUtil.setContentView(this.getActivity(), R.layout.game_fragment);
        mGameViewModel = new GameViewModel(this.getActivity(),
                gameFragmentBinding.gamePanelContainer,
                gameFragmentBinding.backgroundCover);
        gameFragmentBinding.setMainGameViewModel(mGameViewModel);
    }
}
