package com.example.mindharbor.patterns.decorator;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class TestDecorator extends NodoDecorator {
    private final Integer testSvolto;

    public TestDecorator(ComponenteNodo componenteDecorato, Integer testSvolto) {
        super(componenteDecorato);
        this.testSvolto = testSvolto;
    }

    @Override
    public void applica() {
        super.applica();

        ImageView imageView = estraiImageView();

        if (imageView != null) {
            String path = (testSvolto == 0)
                    ? "/com/example/mindharbor/Img/NonCompletato.png"
                    : "/com/example/mindharbor/Img/Completato.png";
            Image immagine = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            imageView.setImage(immagine);
        }
    }

}
