package com.example.mindharbor.eccezioni;

public class EccezionePazienteNonAutorizzato extends RuntimeException {
    public EccezionePazienteNonAutorizzato(String message) {
        super(message);
    }
}
