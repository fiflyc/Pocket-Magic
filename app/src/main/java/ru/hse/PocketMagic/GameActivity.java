package ru.hse.PocketMagic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private Controller controller;

    private int maxHP;
    private int maxMP;

    private ProgressBar playerHP;
    private TextView valueHP;
    private ProgressBar playerMP;
    private TextView valueMP;

    private TextView opponentName;
    private ProgressBar opponentHP;
    private TextView valueOHP;
    private ImageView opponentAvatar;
    private ImageView opponentSpell;

    private ImageView playerBuffA;
    private ImageView playerBuffB;
    private ImageView opponentBuffA;
    private ImageView opponentBuffB;

    private GestureOverlayView gestureOverlayView;

    public class Caster {

        public void cast(String spell) {
            controller.playerSpell(spell, Target.BODY);
        }
    }

    private class Painter implements ru.hse.PocketMagic.Painter {

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
            if (spell.equals("Heal")) {
                opponentSpell.setImageResource(R.drawable.heal);
                opponentSpell.setVisibility(View.VISIBLE);
            } else if (spell.equals("Sun Shield")) {
                opponentSpell.setImageResource(R.drawable.sunshield);
                opponentSpell.setVisibility(View.VISIBLE);
            }

            sendNotification("Opponent is casting " + spell);
        }

        public void hideOpponentSpell() {
            opponentSpell.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showOpponentBuff(String buff) {
            if (buff.equals("Heal")) {
                opponentBuffB.setImageResource(R.drawable.buff_heal);
                opponentBuffB.setVisibility(View.VISIBLE);
            } else if (buff.equals("SunShieldA")) {
                opponentBuffA.setImageResource(R.drawable.buff_sunshield_a);
                opponentBuffA.setVisibility(View.VISIBLE);
            } else if (buff.equals("SunShieldB")) {
                opponentBuffA.setImageResource(R.drawable.buff_sunshield_b);
                opponentBuffA.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void hideOpponentBuff(String buff) {
            if (buff.equals("Heal")) {
                opponentBuffB.setVisibility(View.INVISIBLE);
            } else if (buff.equals("SunShieldA")) {
                opponentBuffA.setVisibility(View.INVISIBLE);
            } else if (buff.equals("SunShieldB")) {
                opponentBuffA.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void setPlayerBuff(String buff) {
            if (buff.equals("Heal")) {
                playerBuffB.setImageResource(R.drawable.buff_heal);
                playerBuffB.setVisibility(View.VISIBLE);
            } else if (buff.equals("SunShieldA")) {
                playerBuffA.setImageResource(R.drawable.buff_sunshield_a);
                playerBuffA.setVisibility(View.VISIBLE);
            } else if (buff.equals("SunShieldB")) {
                playerBuffA.setImageResource(R.drawable.buff_sunshield_b);
                playerBuffA.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void hidePlayerBuff(String buff) {
            if (buff.equals("Heal")) {
                opponentBuffB.setVisibility(View.INVISIBLE);
            } else if (buff.equals("SunShieldA")) {
                opponentBuffA.setVisibility(View.INVISIBLE);
            } else if (buff.equals("SunShieldB")) {
                opponentBuffA.setVisibility(View.INVISIBLE);
            }
        }

        synchronized public void sendNotification(String notification) {
            /* TODO */
        }

        public Context getContext() {
            return GameActivity.this;
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

        playerBuffA = findViewById(R.id.playerBuffA);
        playerBuffB = findViewById(R.id.playerBuffB);
        opponentBuffA = findViewById(R.id.opponentBuffA);
        opponentBuffB = findViewById(R.id.opponentBuffB);

        controller = new Controller(this.new Painter());

        Caster caster = this.new Caster();

        opponentAvatar.setEnabled(false);
        gestureOverlayView.addOnGesturePerformedListener(new GestureListener(gestureLibrary, caster));
    }

    @Override
    public void onBackPressed() { /* Do nothing. */}
}
