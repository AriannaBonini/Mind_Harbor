package com.example.mindharbor.patterns.decorator;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class DisponibilitaDecorator extends NodoDecorator {
    private final boolean disp;

    public DisponibilitaDecorator(ComponenteNodo componenteDecorato, boolean disp) {
        super(componenteDecorato);
        this.disp = disp;
    }

    @Override
    public void applica() {
        super.applica();

        ImageView imageView = estraiImageView();

        if (imageView != null) {
            String path = disp
                    ? "/com/example/mindharbor/Img/casellaVerde.png"
                    : "/com/example/mindharbor/Img/casellaRossa.png";
            Image immagine = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            imageView.setImage(immagine);
        }
    }
}
