package com.example.mindharbor.patterns.decorator;



import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class GenereDecorator extends NodoDecorator {
    private final String genere;

    public GenereDecorator(ComponenteNodo componenteDecorato, String genere) {
        super(componenteDecorato);
        this.genere = genere;
    }

    @Override
    public void applica() {
        super.applica();

        ImageView imageView = estraiImageView();

        if (imageView != null) {
            String path = genere.equalsIgnoreCase("M")
                    ? "/com/example/mindharbor/Img/IconaMaschio.png"
                    : "/com/example/mindharbor/Img/IconaFemmina.png";
            Image immagine = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            imageView.setImage(immagine);
        }
    }



}

