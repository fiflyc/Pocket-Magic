package ru.hse.PocketMagic;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private Controller controller;

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
    private ImageView opponentAvatar;

    private TextView textNotifications;

    private GestureOverlayView gestureOverlayView;

    public class Caster {

        private float x0;
        private float y0;
        private float x1;
        private float y1;

        private String spell = null;
        private float x = -1;
        private float y = -1;

        Caster(float x0, float y0, float x1, float y1) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
        }

        public void setSpell(String spell) {
            this.spell = spell;
            gestureOverlayView.setVisibility(View.INVISIBLE);
            opponentAvatar.setEnabled(true);
            sendNotification("Choose target");

            if (x != -1 && y != -1) {
                sendCastInfo();
            }
        }

        public void setPos(float x, float y) {
            this.x = x;
            this.y = y;
            gestureOverlayView.setVisibility(View.VISIBLE);
            opponentAvatar.setEnabled(false);
            if (textNotifications.getText().equals("Choose target")) {
                sendNotification("");
            }

            if (spell != null) {
                sendCastInfo();
            }
        }

        private void sendCastInfo() {
            if (x0 <= x && x <= x1 && y0 <= y && y <= y1) {
                controller.playerSpell(spell, Target.BODY);
            } else {
                controller.playerSpell(spell, Target.NOWHERE);
            }

            x = -1;
            y = -1;
            spell = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GestureLibrary gestureLibrary = GestureLibraries.fromRawResource(getApplicationContext(), R.raw.gestures);
        gestureOverlayView = findViewById(R.id.gestureListener);
        if (!gestureLibrary.load()) {
            finish();
        }

        playerHP = findViewById(R.id.playerHP);
        valueHP = findViewById(R.id.valueHP);
        playerMP = findViewById(R.id.playerMP);
        valueMP = findViewById(R.id.valueMP);
        opponentHP = findViewById(R.id.opponentHP);
        valueOHP = findViewById(R.id.valueOHP);

        opponentName = findViewById(R.id.opponentName);
        opponentSpell = findViewById(R.id.opponentSpell);
        opponentAvatar = findViewById(R.id.opponentAvatar);

        textNotifications = findViewById(R.id.textNotifications);

        controller = new Controller(this);

        Caster caster = this.new Caster(0, 0, 2000, 2000);

        opponentAvatar.setOnTouchListener(new TouchListener(caster));
        opponentAvatar.setEnabled(false);
        gestureOverlayView.addOnGesturePerformedListener(new GestureListener(gestureLibrary, caster));
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
        Intent intent = new Intent(GameActivity.this, GameResultsActivity.class);

        switch (result) {
            case WIN:
                intent.putExtra("RESULT", 1);
                break;
            case LOSE:
                intent.putExtra("RESULT", -1);
                break;
            case DRAW:
                intent.putExtra("RESULT", 0);
                break;
        }

        startActivity(intent);
        finish();
    }

    public void showOpponentSpell(String spell) {
        opponentSpell.setVisibility(View.VISIBLE);
        opponentSpell.setImageResource(R.drawable.fireball);
        sendNotification("Opponent is casting FireBall!");
    }

    public void hideOpponentSpell() {
        opponentSpell.setVisibility(View.INVISIBLE);
    }

    synchronized public void sendNotification(String notification) {
       textNotifications.setText(notification);
    }

    private Caster createCaster() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap opponentPicture = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.temp_bot_image, options);

        FaceDetector faceDetector = new FaceDetector(opponentPicture.getWidth(), opponentPicture.getHeight(), 1);
        FaceDetector.Face[] faces = new FaceDetector.Face[1];
        if (faceDetector.findFaces(opponentPicture, faces) == 0) {
            sendAlert("No faces found!");
            return null;
        }

        PointF midPoint = new PointF();
        faces[0].getMidPoint(midPoint);
        float eyesDistance = faces[0].eyesDistance();
        float x0 = midPoint.x - eyesDistance * 2;
        float y0 = midPoint.y - eyesDistance * 2;
        float x1 = midPoint.x + eyesDistance * 2;
        float y1 = midPoint.y + eyesDistance * 2;
        return this.new Caster(x0, y0, x1, y1);
    }

    public void sendAlert(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
