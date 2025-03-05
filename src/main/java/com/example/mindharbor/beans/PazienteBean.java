package com.example.mindharbor.beans;

public class PazienteBean {
    private String nome;
    private String cognome;
    private String genere;
    private Integer anni;
    private String diagnosi;
    private String username;
    private Integer numTestSvolti;
    private Integer numNotifiche;

    public PazienteBean(String username, Integer numTestSvolti, String nome, String cognome, String genere) {
        this.username=username;
        this.numTestSvolti = numTestSvolti;
        this.nome=nome;
        this.cognome=cognome;
        this.genere=genere;
    }

    public PazienteBean(Integer numNotifiche) {
        this.numNotifiche=numNotifiche;
    }

    public PazienteBean(String nome, String cognome, Integer anni) {
        this.nome = nome;
        this.cognome = cognome;
        this.anni = anni;
    }

    public PazienteBean(String username, String nome, String cognome, String genere) {
        this.username=username;
        this.nome = nome;
        this.cognome = cognome;
        this.genere= genere;
    }

    public PazienteBean(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }


    public PazienteBean(String nome, String cognome, String genere, Integer anni, String diagnosi, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.genere= genere;
        this.anni = anni;
        this.diagnosi=diagnosi;
        this.username=username;
    }

    public void setAnni(Integer anni){this.anni=anni;}

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {return cognome;}
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public String getGenere() {
        return genere;
    }
    public void setGenere(String genere) {
        this.genere = genere;
    }
    public Integer getAnni() {
        return anni;
    }
    public String getDiagnosi() {
        return diagnosi;
    }
    public void setDiagnosi(String diagnosi){this.diagnosi=diagnosi;}
    public String getUsername() {return username;}
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getNumTestSvolti() {
        return numTestSvolti;
    }
    public void setNumTestSvolti(Integer numTestSvolti) {this.numTestSvolti = numTestSvolti;}
    public Integer getNumNotifiche() {return numNotifiche;}
}

