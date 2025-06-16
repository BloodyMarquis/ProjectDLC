package kr.ac.tukorea.rudy.projectdlc.game;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.rudy.projectdlc.R;
import kr.ac.tukorea.rudy.projectdlc.app.ProjectDlcActivity;
import kr.ac.tukorea.rudy.projectdlc.app.SongSelectActivity;
import kr.ac.tukorea.rudy.projectdlc.data.Song;

public class SongSelectScene extends Scene {
    private final SongSelectActivity activity;

    private ArrayList<Song> songs = new ArrayList<>();
    private int selectedIndex = 0;
    private String selectedDiff = "sun";
    private TextObject songTitle;
    private static final String TAG = SongSelectScene.class.getSimpleName();

    private Sprite coverSprite;
    private final HashMap<String, Button> diffButtons = new HashMap<>();
    private final float NORMAL_SIZE = 150f;
    private final float SELECTED_SIZE = 200f;

    public enum Layer {
        bg, ui, button;
        public static final int COUNT = values().length;
    }
    public SongSelectScene(SongSelectActivity activity) {
        this.activity = activity;
        this.songs = Song.songs;

        Bundle extras = GameActivity.activity.getIntent().getExtras();
        if (extras != null) {
            selectedIndex = extras.getInt(ProjectDlcActivity.SONG_INDEX, 0);
            selectedDiff = extras.getString(ProjectDlcActivity.SONG_DIFFICULTY, "sun");
        }

        initLayers(Layer.COUNT);

        Sprite back = new Sprite(R.mipmap.white1px_0);
        back.setPosition(800f, 450f, 1600f, 900f);
        add(Layer.bg, back);

        // 왼쪽 화살표
        add(Layer.button, new Button(R.mipmap.left, 150f, 500f, 100f, 100f, pressed -> {
            if (!pressed) return false;
            stopMusic();
            selectedIndex = (selectedIndex - 1 + songs.size()) % songs.size();
            updateSongDisplay();
            return false;
        }));

// 오른쪽 화살표
        add(Layer.button, new Button(R.mipmap.right, 1450f, 500f, 100f, 100f, pressed -> {
            if (!pressed) return false;
            stopMusic();
            selectedIndex = (selectedIndex + 1) % songs.size();
            updateSongDisplay();
            return false;
        }));

// 난이도 버튼들
        addDifficultyButton("sun", R.mipmap.diffsunlight, 500f);
        addDifficultyButton("twi", R.mipmap.difftwilight, 700f);
        addDifficultyButton("mid", R.mipmap.diffmidnight, 900f);
        addDifficultyButton("aby", R.mipmap.diffabyss, 1100f);
        updateDiffButtonSizes();

// 커버 이미지 Sprite

        coverSprite = new Sprite(0);
        coverSprite.setPosition(800f, 350f, 400f, 400f);
        add(Layer.ui, coverSprite);

        songTitle = new TextObject(songs.get(selectedIndex).getTitle(),60f, 0xFF000000, Paint.Align.CENTER);
        songTitle.setPosition(800f, 650f);
        add(Layer.ui, songTitle);
        updateSongDisplay();

        add(Layer.button, new Button(R.mipmap.gamestartbutton, 1450f, 750f, 200f, 200f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                stopMusic();
                activity.startGame(selectedIndex, selectedDiff);
                return false;
            }
        }));
    }

    protected int getTouchLayerIndex() {
        return Layer.button.ordinal();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopMusic();
    }

    private void stopMusic() {
        songs.get(selectedIndex).stop();
    }

    private void addDifficultyButton(String diff, int resId, float x) {
        Button button = new Button(resId, x, 800f, NORMAL_SIZE, NORMAL_SIZE, pressed -> {
            if (!pressed) return false;
            selectedDiff = diff;
            Log.d(TAG, "Difficulty selected: " + diff);
            updateDiffButtonSizes();
            return false;
        });
        add(Layer.button, button);
        diffButtons.put(diff, button);
    }

    private void updateDiffButtonSizes() {
        for (Map.Entry<String, Button> entry : diffButtons.entrySet()) {
            String key = entry.getKey();
            Button button = entry.getValue();

            float centerX = button.getX();
            float centerY = button.getY();
            float size = key.equals(selectedDiff) ? SELECTED_SIZE : NORMAL_SIZE;

            button.setPosition(centerX, centerY, size, size);
        }
    }

    private void updateSongDisplay() {
        setText(songs.get(selectedIndex).getTitle());
        songs.get(selectedIndex).playDemo();
        coverSprite.setBitmap(songs.get(selectedIndex).getBitmap());
    }

    public void setText(String text) {
        songTitle.text = text;
    }
}
