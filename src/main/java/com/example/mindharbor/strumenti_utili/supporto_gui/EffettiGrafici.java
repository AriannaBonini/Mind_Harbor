package com.example.mindharbor.strumenti_utili.supporto_gui;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public final class EffettiGrafici {

    private EffettiGrafici() {}

    public static void aggiungiEffettoListaNodi(ListView<Node> listaNodi) {
        for (Node nodo : listaNodi.getItems()) {
            EffettiGrafici.aggiungiEffettoGlow(nodo);
        }
    }

    public static void aggiungiEffettoGlow(Node nodo) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#7fbf00", 0.8));
        glow.setRadius(12);
        glow.setSpread(0.5);
        glow.setOffsetX(0);
        glow.setOffsetY(0);

        nodo.setOnMouseEntered(e -> nodo.setEffect(glow));
        nodo.setOnMouseExited(e -> nodo.setEffect(null));
    }
}
