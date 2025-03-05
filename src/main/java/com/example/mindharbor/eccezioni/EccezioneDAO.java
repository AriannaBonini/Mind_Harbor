package com.example.mindharbor.eccezioni;

public class EccezioneDAO extends Exception {

    public EccezioneDAO() {
        super();
    }

    public EccezioneDAO(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }

    public EccezioneDAO(String messaggio) {
        super(messaggio);
    }
}

