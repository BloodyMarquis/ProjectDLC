package kr.ac.tukorea.rudy.projectdlc.game;

import android.graphics.Paint;
import android.util.Log;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.rudy.projectdlc.R;
import kr.ac.tukorea.rudy.projectdlc.app.ProjectDlcActivity;

public class ResultScene extends Scene {
    private static final String TAG = SongSelectScene.class.getSimpleName();
    private long allowInputTime;

    public enum Layer {
        bg, button, ui;
        public static final int COUNT = values().length;
    }

    public ResultScene(ProjectDlcActivity activity, int score, int maxCombo) {
        initLayers(Layer.COUNT);

        Sprite background = new Sprite(R.mipmap.bg1);
        background.setPosition(0, 0, 3200f, 1800f);
        add(Layer.bg, background);

        allowInputTime = System.currentTimeMillis() + 500;

        TextObject songTitle = new TextObject(MainScene.scene.song.getTitle(),100f, 0xFFFFFFFF, Paint.Align.LEFT);
        songTitle.setPosition(700f, 300f);
        add(ResultScene.Layer.ui, songTitle);

        TextObject scoreText = new TextObject("Score: " + score,100f, 0xFFFFFFFF, Paint.Align.LEFT);
        scoreText.setPosition(700f, 450f);
        add(ResultScene.Layer.ui, scoreText);

        TextObject comboText = new TextObject("Combo: " + maxCombo,100f, 0xFFFFFFFF, Paint.Align.LEFT);
        comboText.setPosition(700f, 600f);
        add(ResultScene.Layer.ui, comboText);

        Sprite coverImage = new Sprite(0);
        coverImage.setBitmap(MainScene.scene.song.getBitmap());
        coverImage.setPosition(350f, 450f, 500f, 500f);
        add(MainScene.Layer.bg, coverImage);

        add(Layer.button, new Button(R.mipmap.gamestartbutton, 1450f, 600f, 200f, 200f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                if (!pressed) return false;
                if (System.currentTimeMillis() < allowInputTime) return false;
                Log.d(TAG, "Button: selectIndex - pressed:" + pressed);
                activity.endGame(MainScene.scene.songIndex, MainScene.scene.songDiff);
                return false;
            }
        }));
    }

    protected int getTouchLayerIndex() {
        return ResultScene.Layer.button.ordinal();
    }
}