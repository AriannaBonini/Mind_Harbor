package com.example.mindharbor.patterns.decorator;

import javafx.scene.Node;

public class NodoBase implements ComponenteNodo {
    private final Node nodo;

    public NodoBase(Node nodo) {
        this.nodo = nodo;
    }

    @Override
    public void applica() {/*Comportamento base: nessuna decorazione*/}

    @Override
    public Node getNodo() {
        return nodo;
    }
}
