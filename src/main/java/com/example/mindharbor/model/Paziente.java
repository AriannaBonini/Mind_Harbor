package com.example.mindharbor.model;

import com.example.mindharbor.tipo_utente.UserType;
import java.util.List;

public class Paziente extends Utente{

    private String diagnosi;
    private Psicologo psicologo;
    private Integer anni;
    private Integer numeroTest;

    public Paziente() {/*Costruttore vuoto*/}

    public Paziente(String username, String nome, String cognome, UserType userType, String genere, String password, Integer anni) {
        super(username, nome, cognome, userType, genere,password);
        this.anni=anni;

    }

    public Paziente(String username, String nome, String cognome, UserType userType, Psicologo psicologo) {
        super(username, nome, cognome, userType, null,null);
        this.psicologo=psicologo;

    }

    public Paziente(String username, String nome, String cognome) {
        super(username,nome,cognome,UserType.PAZIENTE);
    }
    public Paziente(String username) { super(username); }
    public Paziente(Integer anni, String diagnosi){
        this.anni=anni;
        this.diagnosi=diagnosi;
    }
    public Integer getNumeroTest() {return numeroTest;}
    public String getDiagnosi() {return diagnosi;}
    public void setDiagnosi(String diagnosi) {this.diagnosi = diagnosi;}
    public Psicologo getPsicologo() {return psicologo;}
    public void setPsicologo(Psicologo psicologo) {this.psicologo = psicologo;}
    public Integer getAnni() {return anni;}
    public void setAnni(Integer anni) {this.anni=anni;}
    public void setNumeroTest(Integer numeroTest) {this.numeroTest=numeroTest;}
}