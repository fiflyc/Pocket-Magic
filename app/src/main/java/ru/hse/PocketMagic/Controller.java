package ru.hse.PocketMagic;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class Controller {
    private Logic logic;
    Bot bot;
    GameActivity gameActivity;

    public Controller(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        logic = new Logic();
        gameActivity.setOpponentName("Larry");
        gameActivity.setMaxHP(logic.getMaxHp());
        gameActivity.setMaxMP(logic.getMaxMp());
        gameActivity.setPlayerHP(logic.getPlayerHP());
        gameActivity.setPlayerMP(logic.getPlayerMP());
        gameActivity.setOpponentHP(logic.getOpponentHP());
        bot = new Bot();
        bot.execute();
    }

    public void playerSpell(String spell) {
        if (!logic.ableToThrowTheSpell(spell)) {
            gameActivity.showOutOfMP();
            return;
        }
        logic.playerSpell(spell);
        gameActivity.setPlayerMP(logic.getPlayerMP());
        gameActivity.setOpponentHP(logic.getOpponentHP());
        if (logic.getOpponentHP() == 0) {
            bot.stop();
            gameActivity.endGame(GameResult.WIN);
        }
    }

    public void opponentSpell(String spell) {
        logic.opponentSpell(spell);
        gameActivity.setPlayerHP(logic.getPlayerHP());
        if (logic.getPlayerHP() == 0) {
            bot.stop();
            gameActivity.endGame(GameResult.LOSE);
        }
    }

    private class Bot extends AsyncTask<Void, Void, Void> {
        private boolean isAlive = true;

        public void stop(){
            isAlive = false;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (isAlive) {
                publishProgress();
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            opponentSpell("");
        }
    }

    private class Logic {
        private static final int MAX_HP = 20;
        private static final int MAX_MP = 20;

        private int playerHP = MAX_HP ;
        private int opponentHP = MAX_HP;
        private int playerMP = MAX_MP;

        public int getPlayerHP() {
            return playerHP;
        }

        public int getOpponentHP() {
            return opponentHP;
        }

        public int getPlayerMP() {
            return playerMP;
        }

        public int getMaxHp() {
            return MAX_HP;
        }

        public int getMaxMp() {
            return MAX_MP;
        }

        synchronized public void playerSpell(String spell) {
            gameActivity.showPlayerSpell(spell);
            if (true) {
                //if (spell == "FireBall") {
                opponentHP -= 5;
                playerMP -= 4;
            }
        }

        synchronized public void opponentSpell(String spell) {
            if (true) {
                playerHP -= 5;
            }
        }

        public boolean ableToThrowTheSpell(String spell) {
            if (playerMP < 5) {
                return false;
            }
            return true;
        }
    }
}
