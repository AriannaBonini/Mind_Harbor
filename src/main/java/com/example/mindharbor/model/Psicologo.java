package com.example.mindharbor.model;

import com.example.mindharbor.enumerazioni.TipoUtente;

import java.util.List;


public class Psicologo extends Utente {
    private List<Paziente> pazienti;
    private String nomeStudio;
    private Integer costoOrario;

    public Psicologo(){/*Costruttore vuoto*/}

    public Psicologo(String username, String nome, String cognome, TipoUtente tipoUtente, String genere, String password) {
        super(username, nome, cognome, tipoUtente, genere, password);
    }

    public Psicologo(String username) {
        super(username);
    }

    public Psicologo(Integer costoOrario,String nomeStudio){
        this.costoOrario=costoOrario;
        this.nomeStudio=nomeStudio;
    }

    public Psicologo(String username, String nome, String cognome, TipoUtente tipoUtente) {
        super(username,nome,cognome,tipoUtente);
    }


    public Psicologo(String username, String nome, String cognome, String genere) {
        super(username,nome,cognome,genere);
    }






    public String getNomeStudio() {return nomeStudio;}
    public void setNomeStudio(String nomeStudio) {this.nomeStudio=nomeStudio;}
    public Integer getCostoOrario() {return costoOrario;}
    public void setCostoOrario(Integer costoOrario) {this.costoOrario = costoOrario;}
    public List<Paziente> getPazienti() {return pazienti;}
    public void setPazienti(List<Paziente> pazienti) {this.pazienti = pazienti;}
}
