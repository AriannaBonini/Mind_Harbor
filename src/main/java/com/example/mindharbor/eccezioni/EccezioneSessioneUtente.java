package com.example.mindharbor.eccezioni;


public class EccezioneSessioneUtente extends RuntimeException{
    private final String username;

    public EccezioneSessioneUtente(String messaggio, String username) {
        super(messaggio);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}