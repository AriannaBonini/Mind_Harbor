package com.example.mindharbor.beans;

public class PsicologoBean {
    private String username;
    private String nome;
    private String cognome;
    private Integer costoOrario;
    private String nomeStudio;
    private String genere;
    private Integer numNotifiche;
    private String password;

    public PsicologoBean(){}

    public PsicologoBean(Integer numNotifiche) {this.numNotifiche=numNotifiche;}


    public PsicologoBean(String username, String nome, String cognome, Integer costoOrario, String nomeStudio,String genere) {
        setUsername(username);
        setNome(nome);
        setCognome(cognome);
        setCostoOrario(costoOrario);
        setNomeStudio(nomeStudio);
        this.genere=genere;
    }

    public PsicologoBean(String username, String nome, String cognome) {
        this.username=username;
        this.nome=nome;
        this.cognome=cognome;
    }


    public PsicologoBean(String username, String nome, String cognome, Integer costoOrario, String nomeStudio,String genere, String password) {
        this(username,nome,cognome,costoOrario,nomeStudio,genere);
        setPassword(password);
    }


    public String getUsername() {return username;}
    public void setUsername(String username) {
        if (username.matches("[0-9]+")) {
            throw new IllegalArgumentException("Lo username non può essere composto solo da numeri.");
        }

        this.username = username;
    }
    public String getNome() {return nome;}
    public void setNome(String nome)  {
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
    public Integer getCostoOrario() {return costoOrario;}
    public void setCostoOrario(Integer costoOrario) {
        if (costoOrario <= 0) {
            throw new IllegalArgumentException("Il costo orario deve essere maggiore di zero.");
        }

        this.costoOrario = costoOrario;
    }
    public String getNomeStudio() {
        return nomeStudio;
    }
    public void setNomeStudio(String nomeStudio) {
        if (nomeStudio.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("Il nome dello studio non può essere composto solo da numeri. ");
        }
        this.nomeStudio=nomeStudio;
    }
    public String getGenere() {
        return genere;
    }
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
