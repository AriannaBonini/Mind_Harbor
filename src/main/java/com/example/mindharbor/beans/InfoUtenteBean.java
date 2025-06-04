package com.example.mindharbor.beans;

import com.example.mindharbor.enumerazioni.TipoUtente;

public class InfoUtenteBean {
    private String nome;
    private String cognome;
    private TipoUtente tipoUtente;
    public InfoUtenteBean() {}
    public InfoUtenteBean(String nome, String cognome) {
        this.nome = nome;
        this.cognome= cognome;
    }
    public InfoUtenteBean(TipoUtente tipoUtente) {this.tipoUtente = tipoUtente;}
    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public TipoUtente getTipoUtente() {return tipoUtente;}
}
