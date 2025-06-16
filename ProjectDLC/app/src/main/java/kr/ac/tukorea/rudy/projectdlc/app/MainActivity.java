package kr.ac.tukorea.rudy.projectdlc.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.tukorea.rudy.projectdlc.BuildConfig;
import kr.ac.tukorea.rudy.projectdlc.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //if (BuildConfig.DEBUG) {
            //startActivity(new Intent(this, SongSelectActivity.class));
        //}
    }

    public void onBtnStartGame(View view) {
        startGame();
    }

    private void startGame() {
        startActivity(new Intent(this, SongSelectActivity.class));
    }
}