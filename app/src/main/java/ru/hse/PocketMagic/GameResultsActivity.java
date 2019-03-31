package ru.hse.PocketMagic;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_results);

        TextView textResult = findViewById(R.id.textResult);
        ImageView pictureResult = findViewById(R.id.pictureResult);

        int result = getIntent().getExtras().getInt("RESULT");
        switch (result) {
            case 1:
                textResult.setText("VICTORY");
                pictureResult.setImageBitmap(BitmapFactory.decodeFile("res/drawable/victory_image.png"));
                break;
            case 0:
                textResult.setText("DRAW");
                pictureResult.setImageBitmap(BitmapFactory.decodeFile("res/drawable/draw_image.png"));
                break;
            case -1:
                textResult.setText("DEFEAT");
                pictureResult.setImageBitmap(BitmapFactory.decodeFile("res/drawable/defeat_image.png"));
                break;
        }

        Button goToMenuButton = findViewById(R.id.goToMenuButton);
        goToMenuButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameResultsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() { /* Do nothing. */}
}
