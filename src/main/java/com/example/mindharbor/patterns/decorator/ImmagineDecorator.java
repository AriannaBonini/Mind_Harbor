package com.example.mindharbor.patterns.decorator;

import javafx.scene.image.ImageView;

public abstract class ImmagineDecorator {
     protected ImageView imageView;

     protected ImmagineDecorator(ImageView imageView) {
         this.imageView = imageView;
     }

     public abstract void caricaImmagine();
}

