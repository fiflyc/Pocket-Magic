package ru.hse.PocketMagic;

import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;

import java.util.ArrayList;

public class GestureListener implements GestureOverlayView.OnGesturePerformedListener {

    private GestureLibrary gestureLibrary;
    private Controller controller;

    public GestureListener(GestureLibrary gestureLibrary, Controller controller) {
        this.gestureLibrary = gestureLibrary;
        this.controller = controller;
    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        ArrayList<Prediction> predictionList = gestureLibrary.recognize(gesture);

        if (predictionList.size() > 0) {
            Prediction firstPrediction = predictionList.get(0);

            if(firstPrediction.score > 3) {
                controller.playerSpell(firstPrediction.name);
            }
        }
    }
}
