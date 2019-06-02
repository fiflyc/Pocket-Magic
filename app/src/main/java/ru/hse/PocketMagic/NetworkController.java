package ru.hse.PocketMagic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.nio.ByteBuffer;

public class NetworkController {

    private static AppCompatActivity currentActivity;
    private static Controller currentController;
    private static Network network;

    public static final int FIRE_BALL = 1;
    public static final int FREEZE = 2;
    public static final int LIGHTNING = 3;
    public static final int FOG = 4;
    public static final int HEAL = 5;
    public static final int BREEZE = 6;
    public static final int SUN_SHIELD = 7;
    public static final int EXHAUSTING_SUN = 8;

    public static Network createNetwork() {
        network = new Network();
        return network;
    }

    public static void startGame() {
        Intent intent = new Intent(currentActivity, LoadActivity.class);
        intent.putExtra("GameType", GameType.MULTIPLAYER);
        currentActivity.startActivity(intent);
        currentActivity.finish();
    }

    public static void finishGame() {
        if (currentActivity.getClass() == GameActivity.class) {
            ((GameActivity) currentActivity).finishGame(GameResult.ERROR);
        }
    }

    synchronized public static void setUI(AppCompatActivity activity) {
        currentActivity = activity;
        if (currentActivity != null && currentActivity.getClass() == GameActivity.class) {
            currentController = ((GameActivity) currentActivity).getController();
        } else {
            currentController = null;
        }
    }

    synchronized public static Context getContext() {
        return currentActivity.getApplicationContext();
    }

    public static void receiveSpell(int spellID) {
        /*switch (spellID) {
            case FIRE_BALL:
                currentController.opponentSpell("FireBall");
                break;
            case FREEZE:
                currentController.opponentSpell("Freeze");
                break;
            case LIGHTNING:
                currentController.opponentSpell("Lightning");
                break;
            case FOG:
                currentController.opponentSpell("Fog");
                break;
            case HEAL:
                currentController.opponentSpell("Heal");
                break;
            case BREEZE:
                currentController.opponentSpell("Breeze");
                break;
            case SUN_SHIELD:
                currentController.opponentSpell("SunShield");
                break;
            case EXHAUSTING_SUN:
                currentController.opponentSpell("ExhaustingSun");
                break;
        }
        */
        currentController.opponentSpell(spellID);
    }

    public static void sendSpell(int spellID) {
        /*
        switch (spell) {
            case "FireBall":
                network.sendMessage(ByteBuffer.allocate(4).putInt(FIRE_BALL).array());
                break;
            case "Freeze":
                network.sendMessage(ByteBuffer.allocate(4).putInt(FREEZE).array());
                break;
            case "Lightning":
                network.sendMessage(ByteBuffer.allocate(4).putInt(LIGHTNING).array());
                break;
            case "Fog":
                network.sendMessage(ByteBuffer.allocate(4).putInt(FOG).array());
                break;
            case "Heal":
                network.sendMessage(ByteBuffer.allocate(4).putInt(HEAL).array());
                break;
            case "Breeze":
                network.sendMessage(ByteBuffer.allocate(4).putInt(BREEZE).array());
                break;
            case "SunShield":
                network.sendMessage(ByteBuffer.allocate(4).putInt(SUN_SHIELD).array());
                break;
            case "ExhaustingSun":
                network.sendMessage(ByteBuffer.allocate(4).putInt(EXHAUSTING_SUN).array());
                break;
        }
        */
        network.sendMessage(ByteBuffer.allocate(4).putInt(spellID).array());
    }

}
