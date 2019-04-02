package ru.hse.PocketMagic;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.min;

public class Controller {
    private Logic logic;
    Bot bot;
    GameActivity gameActivity;
    ManaGeneration generation;
    boolean theEnd = false;

    public Controller(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        logic = new Logic();
        gameActivity.setOpponentName("Kappa, the Twitch meme");
        gameActivity.setMaxHP(logic.getMaxHp());
        gameActivity.setMaxMP(logic.getMaxMp());
        gameActivity.setPlayerHP(logic.getPlayerHP());
        gameActivity.setPlayerMP(logic.getPlayerMP());
        gameActivity.setOpponentHP(logic.getOpponentHP());
        bot = new Bot();
        //bot.execute();
        bot.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        generation = new ManaGeneration();
        //generation.execute();
        generation.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void endGame() {
        theEnd = true;
        bot.stop();
        generation.stop();
    }

    public void playerSpell(String spell, Target target) {
        if (!logic.ableToThrowTheSpell(spell)) {
            gameActivity.sendNotification("Not enough mana");
            return;
        }
        logic.playerSpell(spell, target);
        gameActivity.setPlayerMP(logic.getPlayerMP());
        gameActivity.setOpponentHP(logic.getOpponentHP());
        if (logic.getOpponentHP() == 0) {
            endGame();
            gameActivity.endGame(GameResult.WIN);
        }
    }

    public void opponentSpell(String spell) {
        gameActivity.showOpponentSpell(spell);
        HideOpponentSpell hide = new HideOpponentSpell();
        hide.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, spell);
    }

    private void generateMana(int mana) {
        logic.generateMana(mana);
        gameActivity.setPlayerMP(logic.getPlayerMP());
    }

    private void throwOpponentSpell(String spell) {
        if (theEnd) {
            return;
        }
        gameActivity.hideOpponentSpell();
        logic.opponentSpell(spell);
        gameActivity.setPlayerHP(logic.getPlayerHP());
        gameActivity.sendNotification("You've got a damage!");
        if (logic.getPlayerHP() == 0) {
            endGame();
            gameActivity.endGame(GameResult.LOSE);
        }
    }

    private class HideOpponentSpell extends AsyncTask<String, String, Void> {
        @Override
        protected Void doInBackground(String... spell) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(spell);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... spell) {
            throwOpponentSpell(spell[0]);
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
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            opponentSpell("FireBall");
        }
    }

    private class ManaGeneration extends AsyncTask<Void, Void, Void> {
        private boolean isAlive = true;

        public void stop() {
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
            generateMana(1);
        }
    }

    private class Logic {
        private static final int MAX_HP = 20;
        private static final int MAX_MP = 20;

        volatile private int playerHP = MAX_HP ;
        volatile private int opponentHP = MAX_HP;
        volatile private int playerMP = MAX_MP;

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

        synchronized public void playerSpell(String spell, Target target) {
            //gameActivity.showPlayerSpell(spell);
            if (true) { //if (spell == "FireBall") {
                playerMP -= 4;
                if (target == Target.BODY) {
                    opponentHP -= 5;
                }
            }
        }

        synchronized public void opponentSpell(String spell) {
            //gameActivity.showOpponentSpell(spell);
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

        synchronized public void generateMana(int mana) {
            playerMP += mana;
            playerMP = min(playerMP, MAX_MP);
        }
    }
}
