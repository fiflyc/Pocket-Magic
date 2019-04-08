package ru.hse.PocketMagic;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.min;

public class Controller {
    /* inner logic of the game */
    private Logic logic;
    private Bot bot;
    /* Parent GameActivity */
    private GameActivity gameActivity;
    /* Special AsyncTask for increasing playerMP every x seconds */
    private ManaGeneration generation;
    /* there are 3 cases when the game should be stopped
    theEnd == true iff one of them happened
    uses for communication between theese 3 cases () */
    private boolean isStopped = false;

    /**  */
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
        bot.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        generation = new ManaGeneration();
        generation.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void endGame() {
        isStopped = true;
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
        if (isStopped) {
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

        private DatabaseHelper mDBHelper;
        private SQLiteDatabase mDb;

        public Logic() {
            mDBHelper = new DatabaseHelper(gameActivity);
            try {
                mDBHelper.updateDataBase();
            } catch (IOException mIOException) {
                throw new Error("UnableToUpdateDatabase");
            }
            try {
                mDb = mDBHelper.getWritableDatabase();
            } catch (SQLException mSQLException) {
                throw mSQLException;
            }

            //Отправляем запрос в БД
            Cursor cursor = mDb.rawQuery("SELECT cost FROM spells WHERE name='FireBall'", null);
            cursor.moveToFirst();
            //Пробегаем по всем клиентам
            while (!cursor.isAfterLast()) {
                //client = new HashMap<String, Object>();

                //Заполняем клиента
                //client.put("name",  cursor.getString(1));
                //client.put("age",  cursor.getString(2));

                gameActivity.sendAlert(cursor.getString( 0));
                //Переходим к следующему клиенту
                cursor.moveToNext();
            }
            cursor.close();
        }

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
            //if (true) {//if (spell == "FireBall") {
            playerMP -= getSpellCost(spell);
            if (target == Target.BODY) {
                opponentHP -= getSpellDamage(spell);
            }
            //}
        }

        synchronized public void opponentSpell(String spell) {
            //gameActivity.showOpponentSpell(spell);
            playerHP -= getSpellDamage(spell); //5;
        }

        public boolean ableToThrowTheSpell(String spell) {
            if (playerMP < getSpellCost(spell) ) {
                return false;
            }
            return true;
        }

        synchronized public void generateMana(int mana) {
            playerMP += mana;
            playerMP = min(playerMP, MAX_MP);
        }

        public int getSpellCost(String spell) {
            Cursor cursor = mDb.rawQuery("SELECT cost FROM spells WHERE name='" + spell + "'", null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        }

        public int getSpellDamage(String spell) {
            Cursor cursor = mDb.rawQuery("SELECT damage FROM spells WHERE name='" + spell + "'", null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        }


        public ArrayList<Spell> getAllSpells() {
            ArrayList<Spell> result = new ArrayList<Spell>();
            Cursor cursor = mDb.rawQuery("SELECT * FROM spells", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                result.add( new Spell( cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4)) );
                cursor.moveToNext();
            }
            return result;
        }

        public ArrayList<String> getAllSpellNames() {
            ArrayList<String> result = new ArrayList<String>();
            Cursor cursor = mDb.rawQuery("SELECT name FROM spells", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                result.add(cursor.getString(0));
                cursor.moveToNext();
            }
            return result;
        }
    }
}
