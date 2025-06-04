package com.example.mindharbor.patterns.decorator;

import com.example.mindharbor.strumenti_utili.supporto_gui.EffettiGrafici;
import javafx.scene.Node;


public class GlowDecorator extends NodoDecorator {
    public GlowDecorator(ComponenteNodo componenteDecorato) {
        super(componenteDecorato);
    }

    @Override
    public void applica() {
        super.applica();

        Node nodo = getNodo();
        EffettiGrafici.aggiungiEffettoGlow(nodo);
    }
}
