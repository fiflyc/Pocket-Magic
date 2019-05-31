package ru.hse.PocketMagic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

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
    private GifImageView breeze;
    private GifImageView ices;
    private ImageView playerEffect;

    private GifImageView playerCast;
    private GifImageView playerSun;
    private GifImageView opponentCast;
    private GifImageView opponentSun;

    private GestureOverlayView gestureOverlayView;

    public class Caster {

        public void cast(String spell) {
            controller.playerSpell(spell, Target.BODY);
        }
    }

    private class Painter implements ru.hse.PocketMagic.Painter {

        private GifDrawable breezeBackAnim;
        private GifDrawable breezeFrontAnim;
        private GifDrawable icesAnim;
        private GifDrawable fireBallBackAnim;
        private GifDrawable fireBallFrontAnim;
        private GifDrawable lightningBackAnim;
        private GifDrawable lightningFrontAnim;
        private GifDrawable exhaustingSunBackAnim;
        private GifDrawable exhaustingSunFrontAnim;

        public Painter() {
            try {
                breezeBackAnim = new GifDrawable(getContext().getResources(), R.drawable.breeze_back);
                breezeFrontAnim = new GifDrawable(getContext().getResources(), R.drawable.breeze_front);
                icesAnim = new GifDrawable(getContext().getResources(), R.drawable.ices);
                fireBallBackAnim = new GifDrawable(getContext().getResources(), R.drawable.fireball_back);
                fireBallFrontAnim = new GifDrawable(getContext().getResources(), R.drawable.fireball_front);
                lightningBackAnim = new GifDrawable(getContext().getResources(), R.drawable.lightning_back);
                lightningFrontAnim = new GifDrawable(getContext().getResources(), R.drawable.lightning_front);
                exhaustingSunBackAnim = new GifDrawable(getContext().getResources(), R.drawable.exhausting_sun_back);
                exhaustingSunFrontAnim = new GifDrawable(getContext().getResources(), R.drawable.exhausting_sun_front);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setOpponentName(@NonNull String name) {
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
            finishGame(result);
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
            try {
                if (spell.equals("Fire Ball")) {
                    playerCast.setImageDrawable(fireBallBackAnim);
                    fireBallBackAnim.seekTo(0);
                    fireBallBackAnim.start();
                    playerCast.setVisibility(View.VISIBLE);

                    wait(fireBallBackAnim.getDuration());
                    playerCast.setVisibility(View.INVISIBLE);
                } else if (spell.equals("Lightning")) {
                    playerCast.setImageDrawable(lightningBackAnim);
                    lightningBackAnim.seekTo(0);
                    lightningBackAnim.start();
                    playerCast.setVisibility(View.VISIBLE);

                    wait(lightningBackAnim.getDuration());
                    playerCast.setVisibility(View.INVISIBLE);
                }
            } catch (InterruptedException e) {
                /* Do nothing. */
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void showOpponentCast(String spell) {
            try {
                if (spell.equals("Fire Ball")) {
                    opponentCast.setImageDrawable(fireBallFrontAnim);
                    fireBallFrontAnim.seekTo(0);
                    fireBallFrontAnim.start();
                    opponentCast.setVisibility(View.VISIBLE);

                    wait(fireBallFrontAnim.getDuration());
                    opponentCast.setVisibility(View.INVISIBLE);
                } else if (spell.equals("Lightning")) {
                    playerCast.setImageDrawable(lightningFrontAnim);
                    lightningFrontAnim.seekTo(0);
                    lightningFrontAnim.start();

                    wait(lightningFrontAnim.getDuration());
                    opponentCast.setVisibility(View.VISIBLE);
                }
            } catch (InterruptedException e) {
                /* Do nothing*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void showWeatherFront(String weather) {
            if (weather.equals("Fog")) {
                fog.setVisibility(View.VISIBLE);
            } else if (weather.equals("Breeze")) {
                breeze.setImageDrawable(breezeFrontAnim);
                breezeFrontAnim.seekTo(0);
                breezeFrontAnim.start();
                breeze.setVisibility(View.VISIBLE);
            } else if (weather.equals("Ices")) {
                ices.setImageDrawable(icesAnim);
                icesAnim.seekTo(0);
                icesAnim.stop();
                ices.setVisibility(View.VISIBLE);
            } else if (weather.equals("Exhausting Sun")) {
                opponentSun.setImageDrawable(exhaustingSunFrontAnim);
                exhaustingSunFrontAnim.start();
                opponentSun.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void hideWeatherFront(String weather) {
            if (weather.equals("Fog")) {
                fog.setVisibility(View.INVISIBLE);
            } else if (weather.equals("Breeze")) {
                breezeFrontAnim.stop();
                breeze.setVisibility(View.INVISIBLE);
            } else if (weather.equals("Ices")) {
                icesAnim.start();
                try {
                    wait(icesAnim.getDuration());
                } catch (InterruptedException e) {
                    /* Do nothing. */
                }
                ices.setVisibility(View.INVISIBLE);
            } else if (weather.equals("Exhausting Sun")) {
                exhaustingSunFrontAnim.stop();
                opponentSun.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void showWeatherBack(String weather) {
            if (weather.equals("Fog")) {
                fog.setVisibility(View.VISIBLE);
            } else if (weather.equals("Breeze")) {
                breeze.setImageDrawable(breezeBackAnim);
                breezeBackAnim.seekTo(0);
                breezeBackAnim.start();
                breeze.setVisibility(View.VISIBLE);
            } else if (weather.equals("Ices")) {
                ices.setImageDrawable(icesAnim);
                icesAnim.seekTo(0);
                icesAnim.stop();
                ices.setVisibility(View.VISIBLE);
            } else if (weather.equals("Exhausting Sun")) {
                playerSun.setImageDrawable(exhaustingSunBackAnim);
                exhaustingSunBackAnim.start();
                playerSun.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void hideWeatherBack(String weather) {
            if (weather.equals("Fog")) {
                fog.setVisibility(View.INVISIBLE);
            } else if (weather.equals("Breeze")) {
                breezeBackAnim.stop();
                breeze.setVisibility(View.INVISIBLE);
            } else if (weather.equals("Ices")) {
                icesAnim.start();
                try {
                    wait(icesAnim.getDuration());
                } catch (InterruptedException e) {
                    /* Do nothing. */
                }
                ices.setVisibility(View.INVISIBLE);
            } else if (weather.equals("Exhausting Sun")) {
                exhaustingSunBackAnim.stop();
                playerSun.setVisibility(View.INVISIBLE);
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

        breeze = findViewById(R.id.breeze);
        fog = findViewById(R.id.fog);
        ices = findViewById(R.id.ices);
        playerEffect = findViewById(R.id.playerEffect);

        playerCast = findViewById(R.id.playerCast);
        opponentCast = findViewById(R.id.opponentCast);
        playerSun = findViewById(R.id.playerSun);
        opponentSun = findViewById(R.id.opponentSun);

        if (getIntent().getSerializableExtra("GameType") == GameType.MULTIPLAYER) {
            NetworkController.setUI(this);
            controller = new Controller(this.new Painter(), GameType.MULTIPLAYER);
        } else {
            controller = new Controller(this.new Painter(), GameType.BOT);
        }

        Caster caster = this.new Caster();

        opponentAvatar.setEnabled(false);
        gestureOverlayView.addOnGesturePerformedListener(new GestureListener(gestureLibrary, caster));
    }

    @Override
    public void onBackPressed() { /* Do nothing. */}

    public void finishGame(GameResult result) {
        if (result == GameResult.ERROR) {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            intent.putExtra("ERROR", 1);
            startActivity(intent);
            finish();
        }

        Intent intent = new Intent(GameActivity.this, GameResultsActivity.class);;
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

        NetworkController.setUI(null);

        startActivity(intent);
        finish();
    }

    public Controller getController() {
        return controller;
    }
}
