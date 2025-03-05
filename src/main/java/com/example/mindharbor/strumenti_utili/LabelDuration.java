package com.example.mindharbor.strumenti_utili;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class LabelDuration {

    public void duration(Label label, String messagge) {
        Duration duration = Duration.seconds(5);
        FadeTransition fadeIn = new FadeTransition(duration, label);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(duration, label);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        label.setText(messagge);

        fadeIn.play();
        fadeIn.setOnFinished(event -> fadeOut.play());
    }
}