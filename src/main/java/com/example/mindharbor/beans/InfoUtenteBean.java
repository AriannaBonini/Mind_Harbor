package com.example.mindharbor.beans;

import com.example.mindharbor.tipo_utente.UserType;

public class InfoUtenteBean {
    private String nome;
    private String cognome;
    private UserType userType;
    public InfoUtenteBean() {}
    public InfoUtenteBean(String nome, String cognome) {
        this.nome = nome;
        this.cognome= cognome;
    }
    public InfoUtenteBean(UserType userType) {this.userType=userType;}
    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public UserType getUserType() {return userType;}
    public void setUserType(UserType userType) {this.userType = userType;}
}
