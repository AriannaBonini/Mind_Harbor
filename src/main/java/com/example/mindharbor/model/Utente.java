package com.example.mindharbor.model;

import com.example.mindharbor.enumerazioni.TipoUtente;

    public class Utente {
        private String username;
        private String nome;
        private String cognome;
        private TipoUtente tipoUtente;
        private String genere;
        private String password;

        public Utente() {/*Costruttore vuoto*/}

        public Utente(String username, String nome, String cognome, TipoUtente tipoUtente, String genere, String password ) {
            this.username=username;
            this.nome=nome;
            this.cognome=cognome;
            this.tipoUtente = tipoUtente;
            this.genere=genere;
            this.password=password;
        }
        public Utente(String username) {this.username=username;}
        public Utente(String username, String nome, String cognome, TipoUtente tipoUtente) {
            this.username=username;
            this.nome=nome;
            this.cognome=cognome;
            this.tipoUtente = tipoUtente;
        }

        public Utente(String username, String password) {
            this.username=username;
            this.password=password;
        }

        public Utente(String username, String nome, String cognome, String genere) {
            this.username=username;
            this.nome=nome;
            this.cognome=cognome;
            this.genere=genere;
        }




        public String getGenere() {return genere;}
        public void setGenere(String genere) {this.genere = genere;}
        public String getPassword() {return password;}
        public String getUsername() {return username;}
        public void setUsername(String username) {this.username = username;}
        public String getNome() {return nome;}
        public void setNome(String nome) {this.nome = nome;}
        public String getCognome() {return cognome;}
        public void setCognome(String cognome) {this.cognome = cognome;}
        public TipoUtente getUserType() {return tipoUtente;}
        public void setPassword(String password) {this.password=password;}
    }

