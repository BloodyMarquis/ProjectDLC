package kr.ac.tukorea.rudy.projectdlc.game;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.rudy.projectdlc.R;
import kr.ac.tukorea.rudy.projectdlc.app.ProjectDlcActivity;

public class PauseScene extends Scene {
    private final ProjectDlcActivity activity;

    public enum Layer {
        bg, ui;
        public static final int COUNT = values().length;
    }

    public PauseScene(ProjectDlcActivity activity) {
        this.activity = activity;
        initLayers(Layer.COUNT);

        add(Layer.bg, new Sprite(R.mipmap.dropshadow, 800f, 450f, 1600f, 900f));

        add(Layer.ui, new Button(R.mipmap.gamestartbutton, 1200f, 450f, 300f, 300f, pressed -> {
            if (!pressed) return false;
            pop();
            MainScene.scene.song.resume();
            return false;
        }));

        add(Layer.ui, new Button(R.mipmap.restartbutton, 800f, 450f, 300f, 300f, pressed -> {
            if (!pressed) return false;
            GameView.view.post(() -> {
                GameView.view.replaceAllScenes(new MainScene(activity));
            });
            return false;
        }));

        add(Layer.ui, new Button(R.mipmap.lobbybutton, 400f, 450f, 300f, 300f, pressed -> {
            if (!pressed) return false;
            activity.endGame(MainScene.scene.songIndex, MainScene.scene.songDiff);
            return false;
        }));
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.ui.ordinal();
    }
}

