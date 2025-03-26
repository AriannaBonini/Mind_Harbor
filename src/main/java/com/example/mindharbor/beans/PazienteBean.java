package com.example.mindharbor.beans;

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

    public PazienteBean(String nome, String cognome, String genere, String username,Integer anni, String password) {
        setNome(nome);
        setCognome(cognome);
        this.genere=genere;
        setUsername(username);
        setAnni(anni);
        setPassword(password);
    }

    public void setAnni(Integer anni) {
        if (anni <= 0 || anni > 100 ) {
            throw new IllegalArgumentException("Età non valida: deve essere tra 1 e 100.");
        }
        this.anni = anni;

    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        for (int i = 0; i < nome.length(); i++) {
            if (!Character.isLetter(nome.charAt(i))) {
                throw new IllegalArgumentException("Il nome deve contenere solo lettere.");
            }
        }
        this.nome = nome;
    }
    public String getCognome() {return cognome;}
    public void setCognome(String cognome) {
        for (int i = 0; i < cognome.length(); i++) {
            if (!Character.isLetter(cognome.charAt(i))) {
                throw new IllegalArgumentException("Il cognome deve contenere solo lettere.");
            }
        }
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
        if (username.matches("[0-9]+")) {
            throw new IllegalArgumentException("Lo username non può essere composto solo da numeri.");
        }

        this.username = username;
    }
    public Integer getNumTestSvolti() {
        return numTestSvolti;
    }
    public void setNumTestSvolti(Integer numTestSvolti) {this.numTestSvolti = numTestSvolti;}
    public Integer getNumNotifiche() {return numNotifiche;}

    public void setPassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("La password deve contenere almeno 8 caratteri.");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La password deve contenere almeno una lettera maiuscola.");
        }

        if (!password.matches(".*[0-9].*") && !password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("La password deve contenere almeno un numero o un carattere speciale.");
        }
        this.password = password;
    }

    public String getPassword(){return password;}
}

