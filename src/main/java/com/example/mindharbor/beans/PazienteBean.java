package com.example.mindharbor.beans;

import static com.example.mindharbor.strumenti_utili.costanti.Costanti.SOLO_LETTERE;

public class PazienteBean {
    private String nome;
    private String cognome;
    private String genere;
    private Integer anni;
    private String diagnosi;
    private String username;
    private String password;
    private Integer numTestSvolti;
    private Integer numNotifiche;
    /*
    Questo attributo viene utilizzato per le notifiche dei nuovi appuntamenti, delle nuove terapie e dei nuovi test
     */

    public PazienteBean(){
        //Costruttore vuoto//
    }

    public PazienteBean(String nome, String cognome, String genere, Integer anni, String diagnosi, String username,Integer numTestSvolti) {
        this.nome = nome;
        this.cognome = cognome;
        this.genere= genere;
        this.anni = anni;
        this.diagnosi=diagnosi;
        this.username=username;
        this.numTestSvolti=numTestSvolti;
    }
    public PazienteBean(String nome, String cognome, String genere, Integer anni, String diagnosi, String username) {
        this(username,nome,cognome,genere);
        this.anni=anni;
        this.diagnosi=diagnosi;
    }

    public PazienteBean(String username, Integer numTestSvolti, String nome, String cognome, String genere) {
        this(username,nome,cognome,genere);
        this.numTestSvolti=numTestSvolti;
    }
    public PazienteBean(Integer numNotifiche) {
        this.numNotifiche=numNotifiche;
    }
    public PazienteBean(String username, String nome, String cognome, String genere) {
        this(nome,cognome);
        this.username=username;
        this.genere=genere;
    }
    public PazienteBean(String nome, String cognome) {
        this.nome=nome;
        this.cognome=cognome;
    }




    public PazienteBean(String nome, String cognome, String anni) {
        controlloFormatoNome(nome);
        controlloFormatoCognome(cognome);
        controlloFormatoAnni(anni);

        this.nome=nome;
        this.cognome=cognome;
        this.anni=Integer.parseInt(anni);
    }

    public PazienteBean(String nome, String cognome, String genere, String username,String anni, String password) {
        controlloFormatoAnni(anni);
        controlloFormatoNome(nome);
        controlloFormatoCognome(cognome);
        controlloFormatoUsername(username);
        controlloFormatoGenere(genere);

        this.nome=nome;
        this.cognome=cognome;
        this.genere=genere;
        this.username=username;
        this.anni=Integer.parseInt(anni);
        this.password=password;
    }
    public void setAnni(Integer anni) {this.anni = anni;}
    public String getNome() {return nome;}
    public String getCognome() {return cognome;}
    public String getGenere() {return genere;}
    public Integer getAnni() {return anni;}
    public String getDiagnosi() {return diagnosi;}
    public void setDiagnosi(String diagnosi){this.diagnosi=diagnosi;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public Integer getNumTestSvolti() {return numTestSvolti;}
    public void setNumTestSvolti(Integer numTestSvolti) {this.numTestSvolti = numTestSvolti;}
    public Integer getNumNotifiche() {return numNotifiche;}
    public String getPassword(){return password;}




    private void controlloFormatoNome(String nome){
        if (nome == null || !nome.matches(SOLO_LETTERE)){
            throw new IllegalArgumentException("Il nome deve contenere solo lettere e non può essere vuoto.");
        }
    }

    private void controlloFormatoCognome(String cognome){
        if (cognome == null || !cognome.matches(SOLO_LETTERE)) {
            throw new IllegalArgumentException("Il cognome deve contenere solo lettere e non può essere vuoto.");
        }
    }
    private void controlloFormatoUsername(String username) {
        if (username.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("Lo username non può essere composto solo da numeri.");
        }
    }
    private void controlloFormatoAnni(String anni) {
        if(anni == null || !anni.matches("\\d+")) {
            throw new IllegalArgumentException("L'età deve contenere solo numeri.");
        }
    }

    private void controlloFormatoGenere(String genere){
        if (genere == null || !genere.matches("\\p{L}+")) {
            throw new IllegalArgumentException("Il genere deve contenere solo lettere e non può essere vuoto.");
        }
    }
}

