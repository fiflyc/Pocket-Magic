package ru.hse.PocketMagic;

import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;

import java.util.ArrayList;

public class GestureListener implements GestureOverlayView.OnGesturePerformedListener {

    private GestureLibrary gestureLibrary;
    private GameActivity activity;

    public GestureListener(GestureLibrary gestureLibrary, GameActivity activity) {
        this.gestureLibrary = gestureLibrary;
        this.activity = activity;
    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        ArrayList<Prediction> predictionList = gestureLibrary.recognize(gesture);
        int size = predictionList.size();

        if (size > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            Prediction firstPrediction = predictionList.get(0);

            if(firstPrediction.score > 3) {
                String action = firstPrediction.name;

                stringBuilder.append("You've casted ");
                stringBuilder.append(action);
            } else {
                stringBuilder.append("Try to cast neatly.");
            }

            activity.showPlayerSpell(stringBuilder.toString());
        } else {
            activity.showPlayerSpell("OMG go learn spells!");
        }
    }
}
