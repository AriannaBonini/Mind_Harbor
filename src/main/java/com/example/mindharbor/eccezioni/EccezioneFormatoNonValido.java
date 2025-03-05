package com.example.mindharbor.eccezioni;

public class EccezioneFormatoNonValido extends Exception{
    private static final long serialVersionUID = 1L;

    public EccezioneFormatoNonValido(){super("Campi vuoti");}
    public EccezioneFormatoNonValido(Throwable cause){super(cause);}
    public EccezioneFormatoNonValido(String message) {super(message);}
}
