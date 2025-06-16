package kr.ac.tukorea.rudy.projectdlc.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.rudy.projectdlc.BuildConfig;
import kr.ac.tukorea.rudy.projectdlc.data.Song;
import kr.ac.tukorea.rudy.projectdlc.game.SongSelectScene;

public class SongSelectActivity extends GameActivity {
    public static final String SONG_INDEX = "songIndex";
    public static final String SONG_DIFFICULTY = "songDifficulty";
    private static final String TAG = ProjectDlcActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //GameView.drawsDebugStuffs = BuildConfig.DEBUG;
        Metrics.setGameSize(1600,900);
        super.onCreate(savedInstanceState);
        Song.load(this, "songs.json");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int index = extras.getInt(SONG_INDEX);
            String diff = extras.getString(SONG_DIFFICULTY);
            Log.d(TAG, "Selected Song Index = " + index + "Selected Song Diff = " + diff);
        }

        new SongSelectScene(this).push();
    }

    protected void onDestroy() {
        Song.unload();
        super.onDestroy();
    }

    public void startGame(int index, String diff) {

        Intent intent = new Intent(this, ProjectDlcActivity.class);
        intent.putExtra(ProjectDlcActivity.SONG_INDEX, index);
        intent.putExtra(ProjectDlcActivity.SONG_DIFFICULTY, diff);
        startActivity(intent);
    }
}
