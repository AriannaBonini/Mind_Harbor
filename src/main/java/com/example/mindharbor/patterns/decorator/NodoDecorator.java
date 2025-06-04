package com.example.mindharbor.patterns.decorator;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public abstract class NodoDecorator implements ComponenteNodo {
    protected final ComponenteNodo componenteDecorato;

    protected NodoDecorator(ComponenteNodo componenteDecorato) {
        this.componenteDecorato = componenteDecorato;
    }

    @Override
    public void applica() {
        componenteDecorato.applica();
    }

    @Override
    public Node getNodo() {
        return componenteDecorato.getNodo();
    }


    protected ImageView estraiImageView() {
        Node nodo = getNodo();

        if (nodo instanceof ImageView iv) {
            return iv;
        } else if (nodo instanceof HBox hbox) {
            for (Node child : hbox.getChildren()) {
                if (child instanceof ImageView ivChild) {
                    return ivChild;
                }
            }
        }
        return null;
    }
}


