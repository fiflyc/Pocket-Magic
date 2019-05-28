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
import android.widget.Toast;

import com.cunoraz.gifview.library.GifView;

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

    private ImageView fog;
    private GifView breeze;
    private GifView ices;
    private ImageView playerEffect;

    private GifView playerCast;
    private GifView playerSun;
    private GifView opponentCast;
    private GifView opponentSun;

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
            } else if (spell.equals("Freeze")) {
                opponentSpell.setImageResource(R.drawable.freeze);
                opponentSpell.setVisibility(View.VISIBLE);
            } else if (spell.equals("Fog")) {
                opponentSpell.setImageResource(R.drawable.fog);
                opponentSpell.setVisibility(View.VISIBLE);
            } else if (spell.equals("Breeze")) {
                opponentSpell.setImageResource(R.drawable.breeze);
                opponentSpell.setVisibility(View.VISIBLE);
            } else if (spell.equals("Fire Ball")) {
                opponentSpell.setImageResource(R.drawable.fireball);
                opponentSpell.setVisibility(View.VISIBLE);
            } else if (spell.equals("Lightning")) {
                opponentSpell.setImageResource(R.drawable.lightning);
                opponentSpell.setVisibility(View.VISIBLE);
            } else if (spell.equals("Exhausting Sun")) {
                opponentSpell.setImageResource(R.drawable.exhausting_sun);
                opponentSpell.setVisibility(View.VISIBLE);
            }
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
            } else if (buff.equals("SunShield")) {
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
            } else if (buff.equals("SunShield")) {
                opponentBuffA.setVisibility(View.INVISIBLE);
            } else if (buff.equals("SunShieldB")) {
                opponentBuffA.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void setPlayerState(String state) {
            if (state.equals("Cold")) {
                playerEffect.setImageResource(R.drawable.effect_cold_player);
                playerEffect.setVisibility(View.VISIBLE);
            } else if (state.equals("Frozen")) {
                playerEffect.setImageResource(R.drawable.effect_frozen_player);
                playerEffect.setVisibility(View.VISIBLE);
            } else if (state.equals("Wet")) {
                playerEffect.setImageResource(R.drawable.effect_wet_player);
                playerEffect.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void hidePlayerState() {
            playerEffect.setVisibility(View.INVISIBLE);
        }

        @Override
        public void setOpponentState(String state) {
            /* TODO */
        }

        @Override
        public void hideOpponentState() {
            /* TODO */
        }

        @Override
        public void showPlayerCast(String spell) {
            if (spell.equals("Fire Ball")) {
                playerCast.setGifResource(R.drawable.fireball_back);
                playerCast.setVisibility(View.VISIBLE);
                playerCast.play();
            } else if (spell.equals("Lightning")) {
                playerCast.setGifResource(R.drawable.lightning_back);
                playerCast.setVisibility(View.VISIBLE);
                playerCast.play();
            } else if (spell.equals("Fog")) {
                fog.setVisibility(View.VISIBLE);
            } else if (spell.equals("Breeze")) {
                breeze.setGifResource(R.drawable.breeze_back);
                breeze.setVisibility(View.VISIBLE);
                breeze.play();
            } else if (spell.equals("Ices")) {
                ices.setGifResource(R.drawable.ices);
                ices.pause();
                ices.setVisibility(View.VISIBLE);
            } else if (spell.equals("Exhausting Sun")) {
                playerSun.setGifResource(R.drawable.exhausting_sun_back);
                playerSun.setVisibility(View.VISIBLE);
                playerSun.play();
            }
        }

        @Override
        public void showOpponentCast(String spell) {
            if (spell.equals("Fire Ball")) {
                opponentCast.setGifResource(R.drawable.fireball_front);
                opponentCast.setVisibility(View.VISIBLE);
                opponentCast.play();
            } else if (spell.equals("Lightning")) {
                opponentCast.setGifResource(R.drawable.lightning_front);
                opponentCast.setVisibility(View.VISIBLE);
                opponentCast.play();
            } else if (spell.equals("Fog")) {
                fog.setVisibility(View.VISIBLE);
            } else if (spell.equals("Breeze")) {
                breeze.setGifResource(R.drawable.breeze_front);
                breeze.setVisibility(View.VISIBLE);
                breeze.play();
            } else if (spell.equals("Ices")) {
                ices.setGifResource(R.drawable.ices);
                ices.pause();
                ices.setVisibility(View.VISIBLE);
            } else if (spell.equals("Exhausting Sun")) {
                opponentSun.setGifResource(R.drawable.exhausting_sun_front);
                opponentSun.setVisibility(View.VISIBLE);
                opponentSun.play();
            }
        }

        @Override
        public void hideOpponentCast(String spell) {
            if (spell.equals("Fog")) {
                fog.setVisibility(View.INVISIBLE);
            } else if (spell.equals("Breeze")) {
                breeze.pause();
                breeze.setVisibility(View.INVISIBLE);
            } else if (spell.equals("Ices")) {
                ices.play();
            } else if (spell.equals("Exhausting Sun")) {
                opponentSun.setVisibility(View.INVISIBLE);
                opponentSun.pause();
            } else if (spell.equals("Fire Ball")) {
                opponentCast.pause();
                opponentCast.setVisibility(View.INVISIBLE);
            } else if (spell.equals("Lightning")) {
                opponentCast.pause();
                opponentCast.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void hidePlayerCast(String spell) {
            if (spell.equals("Fog")) {
                fog.setVisibility(View.INVISIBLE);
            } else if (spell.equals("Breeze")) {
                breeze.pause();
                breeze.setVisibility(View.INVISIBLE);
            } else if (spell.equals("Ices")) {
                ices.play();
            } else if (spell.equals("Exhausting Sun")) {
                playerSun.setVisibility(View.INVISIBLE);
                playerSun.pause();
            } else if (spell.equals("Fire Ball")) {
                playerCast.pause();
                playerCast.setVisibility(View.INVISIBLE);
            } else if (spell.equals("Lightning")) {
                playerCast.pause();
                playerCast.setVisibility(View.INVISIBLE);
            }
        }

        synchronized public void sendNotification(String notification) {
            Toast.makeText(getApplicationContext(), notification, Toast.LENGTH_SHORT).show();
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

        fog = findViewById(R.id.fog);
        breeze = findViewById(R.id.breeze);
        ices = findViewById(R.id.ices);
        playerEffect = findViewById(R.id.playerEffect);

        playerCast = findViewById(R.id.playerCast);
        opponentCast = findViewById(R.id.opponentCast);
        playerSun = findViewById(R.id.playerSun);
        opponentSun = findViewById(R.id.opponentSun);

        controller = new Controller(this.new Painter());

        Caster caster = this.new Caster();

        opponentAvatar.setEnabled(false);
        gestureOverlayView.addOnGesturePerformedListener(new GestureListener(gestureLibrary, caster));
    }

    @Override
    public void onBackPressed() { /* Do nothing. */}
}
