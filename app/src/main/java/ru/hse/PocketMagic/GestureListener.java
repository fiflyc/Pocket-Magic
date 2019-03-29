package ru.hse.PocketMagic;

import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;

import java.util.ArrayList;

public class GestureListener implements GestureOverlayView.OnGesturePerformedListener {

    private GestureLibrary gestureLibrary = null;
    private GameActivity activity;
    private int keka = 0;

    public GestureListener(GestureLibrary gestureLibrary, GameActivity activity) {
        this.gestureLibrary = gestureLibrary;
        this.activity = activity;
    }

    /* When GestureOverlayView widget capture a user gesture it will run the code in this method.
       The first parameter is the GestureOverlayView object, the second parameter store user gesture information.*/
    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        // Recognize the gesture and return prediction list.
        ArrayList<Prediction> predictionList = gestureLibrary.recognize(gesture);

        int size = predictionList.size();

        if (size > 0) {
            StringBuilder stringBuilder = new StringBuilder();

            // Get the first prediction.
            Prediction firstPrediction = predictionList.get(0);

            /* Higher score higher gesture match. */
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
