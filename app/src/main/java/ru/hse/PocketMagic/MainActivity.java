package ru.hse.PocketMagic;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        final Animation animSize = AnimationUtils.loadAnimation(this, R.anim.size);

        Button flameButton = findViewById(R.id.flameButton);
        flameButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                Intent intent = new Intent(MainActivity.this, OpponentSearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button botButton = findViewById(R.id.botButton);
        botButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animSize);
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button spellsButton = findViewById(R.id.spellsButton);
        spellsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
