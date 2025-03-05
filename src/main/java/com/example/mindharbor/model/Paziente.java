package com.example.mindharbor.model;

import com.example.mindharbor.tipo_utente.UserType;

public class Paziente extends Utente{

    private String diagnosi;
    private String usernamePsicologo;
    private Integer anni;
    private Integer numeroTest;

    public Paziente(String username, String nome, String cognome, String genere, Integer numTest) {
        super(username, nome, cognome, genere);
        this.numeroTest=numTest;
    }

    public Paziente(Integer numTest) {this.numeroTest=numTest;}

    public Paziente(String username, String nome, String cognome, UserType userType, String genere) {
        super(username, nome, cognome, userType, genere);
    }

    public Paziente(String nome, String cognome, UserType userType, String genere, String diagnosi, Integer anni) {
        super("",nome, cognome, userType, genere);
        this.diagnosi=diagnosi;
        this.anni=anni;
    }

    public Paziente(String diagnosi, Integer anni) {
        super();
        this.diagnosi=diagnosi;
        this.anni=anni;
    }


    public Paziente(String username, String nome, String cognome, UserType userType, Integer anni) {
        super(username, nome, cognome, userType);
        this.anni=anni;
    }

    public Paziente(String username, String nome, String cognome) {
        super(username,nome,cognome,UserType.PAZIENTE);
    }
    public Paziente(String username) { super(username); }
    public Integer getNumeroTest() {return numeroTest;}
    public void setNumeroTest(Integer numeroTest) {this.numeroTest = numeroTest;}
    public String getDiagnosi() {return diagnosi;}
    public void setDiagnosi(String diagnosi) {this.diagnosi = diagnosi;}
    public String getUsernamePsicologo() {return usernamePsicologo;}
    public void setUsernamePsicologo(String usernamePsicologo) {this.usernamePsicologo = usernamePsicologo;}
    public Integer getAnni() {return anni;}
    public void setAnni(Integer anni) {this.anni = anni;}
}