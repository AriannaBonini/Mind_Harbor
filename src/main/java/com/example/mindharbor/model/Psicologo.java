package com.example.mindharbor.model;

import com.example.mindharbor.tipo_utente.UserType;


public class Psicologo extends Utente {
    private String nomeStudio;
    private Integer costoOrario;


    public Psicologo(String username, String nome, String cognome, UserType userType, String genere, String password) {
        super(username, nome, cognome, userType, genere, password);
    }

    public Psicologo(String username, String nome, String cognome, String genere) {
        super(username, nome, cognome,genere);
    }

    public Psicologo(String username, String nome, String cognome) {
        super(username,nome,cognome,UserType.PSICOLOGO);
    }
    public Psicologo(String username) { super(username);}

    public String getNomeStudio() {return nomeStudio;}
    public void setNomeStudio(String nomeStudio) {
        if (nomeStudio.length() > 45) {
            throw new IllegalArgumentException("Username non può superare i 45 caratteri.");
        }
        this.nomeStudio=nomeStudio;
    }

    public Integer getCostoOrario() {return costoOrario;}
    public void setCostoOrario(Integer costoOrario) {this.costoOrario = costoOrario;}
}
