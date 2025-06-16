package kr.ac.tukorea.rudy.projectdlc.game;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Score;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.rudy.projectdlc.R;
import kr.ac.tukorea.rudy.projectdlc.app.ProjectDlcActivity;
import kr.ac.tukorea.rudy.projectdlc.data.Song;

public class MainScene extends Scene {
    private final ProjectDlcActivity activity;

    public static MainScene scene;
    public Song song;
    private float musicTime;

    public int songIndex = 0;
    public String songDiff = "sun";

    private static final String TAG = SongSelectScene.class.getSimpleName();
    public static final float JUDGE_LINE_Y = NoteSprite.GOAL_Y;
    private JudgeManager judgeManager;
    private Score totalScore;

    private Score comboScore;
    private ComboManager comboManager;

    private boolean autoMode = false;
    private boolean gameEnded = false;

    private Character character;
    private final List<TouchAreaSprite> touchAreas = new ArrayList<>();

    public enum Layer {
        bg, touch, button, character, bubble, note, effect, ui;
        public static final int COUNT = values().length;
    }

    public MainScene(ProjectDlcActivity activity) {
        scene = this;
        this.activity = activity;
        initLayers(Layer.COUNT);


        Bundle extras = GameActivity.activity.getIntent().getExtras();
        if (extras != null) {
            songIndex = extras.getInt(ProjectDlcActivity.SONG_INDEX, 0);
            songDiff = extras.getString(ProjectDlcActivity.SONG_DIFFICULTY, "sun");
        }
        song = Song.get(songIndex);

        Sprite back = new Sprite(R.mipmap.white1px_0);
        back.setPosition(800f, 450f, 1600f, 900f);
        add(Layer.bg, back);

        Sprite bge = new Sprite(R.mipmap.bg2);
        bge.setPosition(800f, 325f, 1200f, 650f);
        add(Layer.bg, bge);

        add(Layer.bg, new JudgeLine());
        setUI();

        character = new Character();
        add(Layer.character, character);

        TouchAreaSprite leftTouch = new TouchAreaSprite(
                200, 780, 350, 250, character);
        TouchAreaSprite rightTouch = new TouchAreaSprite(
                1400, 780, 350, 250, character);

        add(Layer.touch, leftTouch);
        add(Layer.touch, rightTouch);

        touchAreas.add(leftTouch);
        touchAreas.add(rightTouch);

        song.loadNotes(songDiff);
        song.noteIndex = 0;
    }

    public void setUI() {
        float baseY = 750f;
        float[] laneX = new float[] {
                150f,
                500f,
                800f,
                1100f,
                1450f
        };

        float[] laneY = new float[] {
                400f,
                780f,
                780f,
                780f,
                400f
        };

        int[] resId = new int[] {
                R.mipmap.buttonpink,
                R.mipmap.buttonred,
                R.mipmap.buttongreen,
                R.mipmap.buttonblue,
                R.mipmap.buttonpurple
        };

        float[] btnWidth = new float[] {
                200f,
                350f,
                350f,
                350f,
                200f
        };

        float[] btnHeight = new float[] {
                420f,
                250f,
                250f,
                250f,
                420f
        };

        for (int i = 0; i < 5; i++) {
            final int lane = i;
            add(Layer.button, new Button(resId[i], laneX[i], laneY[i], btnWidth[i], btnHeight[i], new Button.OnTouchListener() {
                @Override
                public boolean onTouch(boolean pressed) {
                    Log.d(TAG, "Button: note - pressed:" + pressed);
                    handleInput(lane);
                    return false;
                }
            }));
        }

        add(Layer.button, new Button(R.mipmap.pausebutton, 1500f, 100f, 100f, 100f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                Log.d(TAG, "Button: pause - pressed:" + pressed);
                song.pause();
                new PauseScene(activity).push();
                return false;
            }
        }));
    }

    private void handleInput(int lane) {
        float currentTime = MainScene.scene.getMusicTime();
        Log.d("JudgeDebug", "handleInput 호출됨 lane=" + lane + " currentTime=" + currentTime);

        NoteSprite bestNote = null;
        float bestTimeDiff = JudgeManager.MISS_WINDOW + 1;

        for (IGameObject obj : objectsAt(Layer.note)) {
            if (!(obj instanceof NoteSprite)) continue;
            NoteSprite ns = (NoteSprite) obj;
            if (ns.note.lane != lane) continue;

            float noteTime = ns.note.bar;
            float timeDiff = Math.abs(noteTime - currentTime);
            if (timeDiff > JudgeManager.MISS_WINDOW) continue;

            if (timeDiff < bestTimeDiff) {
                bestNote = ns;
                bestTimeDiff = timeDiff;
            }
        }

        if (bestNote == null) return;

        JudgeResult result = judgeManager.judge(bestNote.note.bar, currentTime);
        if (result == JudgeResult.NONE) return;

        if (result != JudgeResult.MISS) {
            totalScore.add((int) judgeManager.getScore(result));
            comboManager.increase();
            comboScore.setScore(comboManager.getCombo());
        } else {
            comboManager.miss();
            comboScore.setScore(0);
        }

        remove(Layer.note, bestNote);
        showJudgmentEffect(result.name(), bestNote.getBoundingRect().centerY());
    }

    private void showJudgmentEffect(String judgment, float y) {
        if (Objects.equals(judgment, "NONE")) return;
        add(Layer.effect, JudgmentEffect.get(judgment, 800, y));
    }

    public float getMusicTime() {
        return musicTime;
    }

    // Game Loop Functions
    @Override
    public void update() {
        musicTime += GameView.frameTime;
        super.update();

        float timeOffset = NoteSprite.screenfulTime();
        while (true) {
            Note note = song.popNoteBefore(musicTime + timeOffset);
            if (note == null) break;
            add(Layer.note, NoteSprite.get(note));
        }

        float currentTime = getMusicTime();
        ArrayList<NoteSprite> toRemove = new ArrayList<>();
        for (IGameObject obj : objectsAt(Layer.note)) {
            if (!(obj instanceof NoteSprite)) continue;
            NoteSprite ns = (NoteSprite) obj;

            float noteTime = ns.note.bar;
            float diff = noteTime - currentTime;

            if (autoMode && Math.abs(diff) < GameView.frameTime) {
                handleInput(ns.note.lane);
                break;
            }

            if (currentTime - noteTime > JudgeManager.MISS_WINDOW) {
                toRemove.add(ns);
                comboManager.miss();
                comboScore.setScore(0);
                showJudgmentEffect(JudgeResult.MISS.name(), ns.getBoundingRect().centerY());
            }
        }
        for (NoteSprite ns : toRemove) {
            remove(Layer.note, ns);
        }

        if (song.isFinished()) {
            autoMode = false;
            endGame();
        }
    }

    protected int getTouchLayerIndex() {
        return MainScene.Layer.button.ordinal();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();
        int pointerCount = event.getPointerCount();

        boolean handled = false;

        for (int i = 0; i < pointerCount; ++i) {
            float[] pts = Metrics.fromScreen(event.getX(i), event.getY(i));
            float tx = pts[0], ty = pts[1];

            for (TouchAreaSprite ta : touchAreas) {
                if (ta.contains(tx, ty)) {
                    ta.onTouch(tx);
                    handled = true;
                    break;
                }
            }
        }

        for (int i = 0; i < pointerCount; ++i) {
            int pointerId = event.getPointerId(i);
            int a = event.getActionMasked();

            if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_POINTER_DOWN) {
                float[] pts = Metrics.fromScreen(event.getX(i), event.getY(i));
                float x = pts[0], y = pts[1];
                for (IGameObject obj : objectsAt(Layer.button)) {
                    if (!(obj instanceof Button)) continue;
                    Button btn = (Button) obj;

                    if (btn.getDstRect().contains(x, y)) {
                        btn.onTouchEvent(event);
                        handled = true;
                    }
                }
            }

            if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_POINTER_UP) {
                int upPointerId = event.getPointerId(actionIndex);
                if (pointerId != upPointerId) continue;

                float[] pts = Metrics.fromScreen(event.getX(i), event.getY(i));
                float x = pts[0], y = pts[1];
                for (IGameObject obj : objectsAt(Layer.button)) {
                    if (!(obj instanceof Button)) continue;
                    Button btn = (Button) obj;

                    btn.onTouchEvent(event);
                    handled = true;
                }
            }
        }

        return handled || super.onTouchEvent(event);
    }

    @Override
    public void onEnter() {
        super.onEnter();
        scene = this;

        judgeManager = new JudgeManager(song.notes.size());
        comboManager = new ComboManager();

        totalScore = new Score(R.mipmap.number_24x32, 1000, 50, 40);
        totalScore.setScore(0);
        add(Layer.ui, totalScore);

        comboScore = new Score(R.mipmap.number_24x32, 1000, 150, 40);  // 위치와 크기 조정 가능
        comboScore.setScore(0);
        add(Layer.ui, comboScore);

        song.play();
    }

    public void onPause() {
        super.onPause();
        song.pause();
    }

    public void onResume() {
        song.resume();
        super.onResume();
    }

    @Override
    public void onExit() {
        song.stop();

        scene = null;
        super.onExit();
    }

    private void endGame() {
        if (gameEnded) return;
        Log.d(TAG, "endGame 호출 확인 : " + gameEnded);
        gameEnded = true;
        Log.d(TAG, "endGame 호출 확인 : " + gameEnded);


        song.stop();

        int finalScore = totalScore.getScore();
        int maxCombo = comboManager.getMaxCombo();

        // 딜레이 후 Scene 전환 (0.5초 뒤)
        GameView.view.postDelayed(() -> {
            new ResultScene(activity, finalScore, maxCombo).push();
        }, 500);
    }
}
