package ru.hse.PocketMagic;

import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class GestureListener implements GestureOverlayView.OnGesturePerformedListener, View.OnTouchListener {

    private GestureLibrary gestureLibrary;
    private GameActivity.Cast cast;

    private final Object mutex;
    private boolean isTouchListening;

    GestureListener(GestureLibrary gestureLibrary, GameActivity.Cast cast) {
        this.gestureLibrary = gestureLibrary;
        this.cast = cast;

        isTouchListening = false;
        mutex = new Object();
    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        synchronized (mutex) {
            if (isTouchListening) {
                return;
            }
        }

        ArrayList<Prediction> predictionList = gestureLibrary.recognize(gesture);

        if (predictionList.size() > 0) {
            Prediction firstPrediction = predictionList.get(0);

            if (firstPrediction.score > 3) {
                synchronized (mutex) {
                    isTouchListening = true;
                }
                cast.setSpell(firstPrediction.name);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (mutex) {
            if (!isTouchListening) {
                return false;
            } else {
                isTouchListening = false;
            }
        }

        cast.setPos(event.getX(), event.getY());

        return true;
    }
}
