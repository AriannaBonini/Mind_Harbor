package com.example.mindharbor.strumenti_utili;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertMessage {

    public Alert errore(String messaggio) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Messaggio d'errore");
        alert.setHeaderText("Attenzione");
        alert.setContentText(messaggio);

        alert.getDialogPane().setPrefWidth(300);
        alert.getDialogPane().setPrefHeight(100);
        alert.getDialogPane().setGraphic(null);

        return alert;
    }

    public Alert informazione(String titolo, String intestazione, String messaggio) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(intestazione);
        alert.setContentText(messaggio);

        alert.getDialogPane().setPrefWidth(300);
        alert.getDialogPane().setPrefHeight(100);
        alert.getDialogPane().setGraphic(null);

        return alert;
    }

    public Integer avvertenza(String messaggio) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AVVERTENZA");
        alert.setHeaderText("Tornando indietro perderai tutti i tuoi progessi");
        alert.setContentText(messaggio);

        alert.getDialogPane().setPrefWidth(300);
        alert.getDialogPane().setPrefHeight(200);
        alert.getDialogPane().setGraphic(null);

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        ButtonType risultato = alert.showAndWait().orElse(ButtonType.NO);

        if (risultato == ButtonType.YES) {
            return 1;
        } else {
            return 0;
        }
    }
}
