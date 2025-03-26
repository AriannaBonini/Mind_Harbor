package com.example.mindharbor.model;

import com.example.mindharbor.tipo_utente.UserType;

    public class Utente {
        private String username;
        private String nome;
        private String cognome;
        private UserType userType;
        private String genere;
        private String password;

        public Utente() {}

        public Utente(String username, String nome, String cognome, UserType userType, String genere, String password ) {
            setUsername(username);
            setNome(nome);
            setCognome(cognome);
            this.userType=userType;
            setGenere(genere);
            setPassword(password);
        }

        public Utente(String username, String nome, String cognome, UserType userType) {
            this.username=username;
            this.nome=nome;
            this.cognome=cognome;
            this.userType=userType;
        }

        public Utente(String username, String password) {
            this.username=username;
            this.password=password;
        }

        public Utente(String username, String nome, String cognome, UserType userType, String genere) {
            this.username=username;
            this.nome=nome;
            this.cognome=cognome;
            this.userType=userType;
            this.genere=genere;
        }

        public Utente(String username) {
            this.username=username;
        }

        public Utente(String username, String nome, String cognome, String genere) {
            this.username=username;
            this.nome=nome;
            this.cognome=cognome;
            this.genere=genere;
        }



        public String getGenere() {return genere;}
        public void setGenere(String genere) {
            if (genere.length() != 1 || (!genere.equals("M") && !genere.equals("F"))) {
                throw new IllegalArgumentException("Genere non valido. Deve essere 'M' o 'F'.");
            }
            this.genere = genere;
        }
        public String getPassword() {return password;}
        public String getUsername() {return username;}
        public void setUsername(String username) {
            if (username.length() > 45) {
                throw new IllegalArgumentException("Username non può superare i 45 caratteri.");
            }
            this.username = username;
        }
        public String getNome() {return nome;}
        public void setNome(String nome) {
            if (nome.length() > 45) {
                throw new IllegalArgumentException("Il nome non può superare i 45 caratteri.");
            }
            this.nome = nome;
        }
        public String getCognome() {return cognome;}
        public void setCognome(String cognome) {
            if (cognome.length() > 45) {
                throw new IllegalArgumentException("Il cognome non può superare i 45 caratteri.");
            }
            this.cognome = cognome;
        }
        public UserType getUserType() {return userType;}

        public void setPassword(String password) {
            if (password.length() > 45) {
                throw new IllegalArgumentException("La password non può superare i 45 caratteri.");
            }
            this.password=password;
        }
    }

