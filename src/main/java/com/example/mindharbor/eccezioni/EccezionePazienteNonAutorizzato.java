package com.example.mindharbor.eccezioni;

public class EccezionePazienteNonAutorizzato extends RuntimeException {
    private final String usernamePaziente;

    public EccezionePazienteNonAutorizzato(String message, String usernamePaziente) {
        super(message);
        this.usernamePaziente = usernamePaziente;
    }

    public String getUsernamePaziente() {
        return usernamePaziente;
    }
}
