package ru.hse.PocketMagic;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private ProgressBar playerHP;
    private ProgressBar playerMP;
    private ProgressBar opponentHP;
    private TextView opponentName;
    private ImageView opponentSpell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        playerHP = findViewById(R.id.playerHP);
        playerMP = findViewById(R.id.playerMP);
        opponentHP = findViewById(R.id.opponentHP);
        opponentName = findViewById(R.id.opponentName);
        opponentSpell = findViewById(R.id.opponentSpell);
    }

    @Override
    public void onBackPressed() { /* Do nothing. */}

    public void setOpponentName(String name) {
        opponentName.setText(name);
    }

    public void setPlayerHP(int value) {
        playerHP.setProgress(value);
    }

    public void setPlayerMP(int value) {
        playerMP.setProgress(value);
    }

    public void setOpponentHP(int value) {
        opponentHP.setProgress(value);
    }

    public void endGame(GameResult result) {

    }

    public void showOpponentSpell(String spell) {

    }
}
