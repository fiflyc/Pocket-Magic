package ru.hse.PocketMagic;

import android.view.MotionEvent;
import android.view.View;

public class TouchListener implements View.OnTouchListener {

    private GameActivity.Caster caster;

    TouchListener(GameActivity.Caster caster) {
        this.caster = caster;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        caster.setPos(event.getX(), event.getY());
        return true;
    }
}
