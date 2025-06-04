package com.example.mindharbor.enumerazioni;

public enum TipoAppuntamento {
        IN_PROGRAMMA("IN PROGRAMMA"),
        PASSATO("PASSATI");

    private final String id;

    TipoAppuntamento(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

