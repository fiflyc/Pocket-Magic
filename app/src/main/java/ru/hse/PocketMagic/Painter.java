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

    void setPlayerState(String state);

    void hidePlayerState();

    void setOpponentState(String state);

    void hideOpponentState();

    void showPlayerCast(String spell);

    void hidePlayerCast(String spell);

    void showOpponentCast(String spell);

    void hideOpponentCast(String spell);

    void sendNotification(String notification);

    Context getContext();
}
