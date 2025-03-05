package com.example.mindharbor.patterns.decorator;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class GenereDecorator extends ImmagineDecorator {
    private final String genere;

    public GenereDecorator(ImageView imageView, String genere) {
        super(imageView);
        this.genere = genere;
    }

    public void caricaImmagine() {
        Image immagine;
        if (genere.equalsIgnoreCase("M")) {
            immagine = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/mindharbor/Img/IconaMaschio.png")));

        } else {
            immagine = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/mindharbor/Img/IconaFemmina.png")));
        }
        imageView.setImage(immagine);
    }
}
