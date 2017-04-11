package pl.grzegorziwanek.jumpgame.app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO: 11.04.2017 add transaction to the JumpGame class
// TODO: 11.04.2017 create layout with underlying GamePanel SurfaceView and UI interface
// TODO: 11.04.2017 (buttons, text views with scores)
public class GameFragment extends Fragment {
    @BindView(R.id.gamePanelContainer) GameDrawingPanel mGameDrawingPanel;
    @BindView(R.id.button_up) ImageButton mButtonUp;
    @BindView(R.id.button_down) ImageButton mButtonDown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_container, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.button_up)
    public void onButtonUpClick() {
        Toast.makeText(this.getActivity(), "Button up clicked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_down)
    public void onButtonDownClick() {
        Toast.makeText(this.getActivity(), "Button down clicked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_attack)
    public void onButtonAttackClick() {
        Toast.makeText(this.getActivity(), "Button attack clicked", Toast.LENGTH_SHORT).show();
    }
}
