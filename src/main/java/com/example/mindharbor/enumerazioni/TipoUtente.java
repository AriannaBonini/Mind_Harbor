package com.example.mindharbor.enumerazioni;

public enum TipoUtente {
    PAZIENTE("Paziente"),
    PSICOLOGO("Psicologo");

    private final String id;

    TipoUtente(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
