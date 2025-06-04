package com.example.mindharbor.strumenti_utili.supporto_gui;


import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SupportoControllerGraficiListaUtenti {

    private SupportoControllerGraficiListaUtenti() {//costruttore privato//
    }

    public static HBox creaHBoxUtenti(ImageView imageView, String nome, String cognome, boolean evidenziaUtente){
        Label labelNome = new Label("\n     NOME: " + nome);
        Label labelCognome = new Label("     COGNOME: " + cognome);
        labelNome.setTextFill(Color.WHITE);
        labelCognome.setTextFill(Color.WHITE);

        if(evidenziaUtente) {
            labelNome.setStyle("-fx-font-weight: bold;");
            labelCognome.setStyle("-fx-font-weight: bold;");
        }

        VBox box = new VBox(labelNome, labelCognome);
        return new HBox(imageView, box);
    }
}
