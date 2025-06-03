package com.example.mindharbor.strumenti_utili.supporto_gui;

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SupportoCellFactory {

    private SupportoCellFactory(){/* costruttore vuoto */}
    public static Callback<ListView<Node>, ListCell<Node>> creaFactoryConGlow() {
        return lv -> new ListCell<>() {
            @Override
            protected void updateItem(Node item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setEffect(null);
                    setOnMouseEntered(null);
                    setOnMouseExited(null);
                    setStyle("");
                } else {
                    setGraphic(item);
                    EffettiGrafici.aggiungiEffettoGlow(this);
                    setStyle("-fx-background-color: transparent;");
                }
            }
        };
    }

}
