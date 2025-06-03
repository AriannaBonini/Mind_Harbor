package com.example.mindharbor.model;

import com.example.mindharbor.tipo_utente.UserType;

import java.util.List;


public class Psicologo extends Utente {
    private List<Paziente> pazienti;
    private String nomeStudio;
    private Integer costoOrario;

    public Psicologo(String username, String nome, String cognome, UserType userType, String genere, String password) {
        super(username, nome, cognome, userType, genere, password);
    }
    public Psicologo(String username, String nome, String cognome, String genere) {this(username, nome, cognome,null,genere,null);}
    public Psicologo(String username, String nome, String cognome, UserType userType) {this(username,nome,cognome,userType,null,null);}

    public Psicologo(Integer costoOrario,String nomeStudio){
        this.costoOrario=costoOrario;
        this.nomeStudio=nomeStudio;
    }
    public Psicologo(String username) { super(username);}
    public String getNomeStudio() {return nomeStudio;}
    public void setNomeStudio(String nomeStudio) {this.nomeStudio=nomeStudio;}
    public Integer getCostoOrario() {return costoOrario;}
    public void setCostoOrario(Integer costoOrario) {this.costoOrario = costoOrario;}
    public List<Paziente> getPazienti() {return pazienti;}
    public void setPazienti(List<Paziente> pazienti) {this.pazienti = pazienti;}
}
