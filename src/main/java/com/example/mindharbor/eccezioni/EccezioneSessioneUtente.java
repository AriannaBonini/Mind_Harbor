package com.example.mindharbor.eccezioni;

import java.io.Serial;

public class EccezioneSessioneUtente extends Exception{

    @Serial
    private static final long serialVersionUID = 3L;

    public EccezioneSessioneUtente(){
        super("The user hasn't been defined yet");
    }

    public EccezioneSessioneUtente(String messaggio) {
        super(messaggio);
    }
}