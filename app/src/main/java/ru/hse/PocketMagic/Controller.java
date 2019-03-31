package ru.hse.PocketMagic;

public class Controller {
    private Logic logic;
    Thread bot;
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
        //bot = new Thread(new Bot());
        //bot.start();
    }

    public void playerSpell(String spell) {
        logic.playerSpell(spell);
        gameActivity.setPlayerMP(logic.getPlayerMP());
        gameActivity.setOpponentHP(logic.getOpponentHP());
        if (logic.getOpponentHP() == 0) {
            gameActivity.endGame(GameResult.WIN);
            //bot.interrupt();
        }
    }

    public void opponentSpell(String spell) {

    }

    private class Bot implements Runnable {
        @Override
        public void run() {
            while (Thread.interrupted()) {
            }
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

        public void playerSpell(String spell) {
            if (true) {
                opponentHP = 0;
                playerMP -= 5;
            }
        }
    }
}
