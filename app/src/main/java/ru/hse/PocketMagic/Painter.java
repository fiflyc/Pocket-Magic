package ru.hse.PocketMagic;

import android.content.Context;

public interface Painter {

    void setOpponentName(String name);

    void setMaxHP(int value);

    void setMaxMP(int value);

    void setPlayerHP(int value);

    void setPlayerMP(int value);

    void setOpponentHP(int value);

    void endGame(GameResult result);

    void showOpponentSpell(String spell);

    void hideOpponentSpell();

    void showOpponentBuff(String buff);

    void hideOpponentBuff(String buff);

    void setPlayerBuff(String buff);

    void hidePlayerBuff(String buff);

    void setPlayerEffect(String effect);

    void hidePlayerEffect(String effect);

    void setOpponentEffect(String effect);

    void hideOpponentEffect(String effect);

    void sendNotification(String notification);

    Context getContext();
}
