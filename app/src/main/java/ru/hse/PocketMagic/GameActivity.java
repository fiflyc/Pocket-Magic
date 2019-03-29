package ru.hse.PocketMagic;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private int maxHP;
    private int maxMP;

    private ProgressBar playerHP;
    private TextView valueHP;
    private ProgressBar playerMP;
    private TextView valueMP;
    private ProgressBar opponentHP;
    private TextView valueOHP;
    private TextView opponentName;
    private ImageView opponentSpell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        playerHP = findViewById(R.id.playerHP);
        valueHP = findViewById(R.id.valueHP);
        playerMP = findViewById(R.id.playerMP);
        valueMP = findViewById(R.id.valueMP);
        opponentHP = findViewById(R.id.opponentHP);
        valueOHP = findViewById(R.id.valueOHP);

        opponentName = findViewById(R.id.opponentName);
        opponentSpell = findViewById(R.id.opponentSpell);
    }

    @Override
    public void onBackPressed() { /* Do nothing. */}

    public void setOpponentName(String name) {
        opponentName.setText(name);
    }

    public void setMaxHP(int value) {
        maxHP = value;
    }

    public void setMaxMP(int value) {
        maxMP = value;
    }

    public void setPlayerHP(int value) {
        playerHP.setProgress(value);
        String text = value + "/" + maxHP;
        valueHP.setText(text);
    }

    public void setPlayerMP(int value) {
        playerMP.setProgress(value);
        String text = value + "/" + maxMP;
        valueMP.setText(text);
    }

    public void setOpponentHP(int value) {
        opponentHP.setProgress(value);
        String text = value + "/" + maxHP;
        valueOHP.setText(text);
    }

    public void endGame(GameResult result) {

    }

    public void showOpponentSpell(String spell) {

    }
}
