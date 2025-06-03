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
        this.username=username;
        this.nome=nome;
        this.cognome=cognome;
        this.costoOrario=costoOrario;
        this.nomeStudio=nomeStudio;
        this.genere=genere;
    }
    public PsicologoBean(String username,String nome, String cognome,String genere) {this(username,nome,cognome,null,null,genere);}
    public PsicologoBean(String username, String nome, String cognome) {
        this(username,nome,cognome,null,null,null);
    }
    public PsicologoBean(String username, String nome, String cognome, String costoOrario, String nomeStudio,String genere, String password) {
        controlloUsername(username);
        controlloNome(nome);
        controlloCognome(cognome);
        controlloCostoOrario(costoOrario);
        controlloNomeStudio(nomeStudio);

        this.username=username;
        this.nome=nome;
        this.cognome=cognome;
        this.costoOrario= Integer.valueOf(costoOrario);
        this.nomeStudio=nomeStudio;
        this.genere=genere;
        this.password=password;
    }


    public String getUsername() {return username;}
    public void setUsername(String username) {this.username=username;}
    public String getNome() {return nome;}
    public void setNome(String nome)  {this.nome = nome;}
    public String getCognome() {return cognome;}
    public void setCognome(String cognome) {this.cognome = cognome;}
    public Integer getCostoOrario() {return costoOrario;}
    public void setCostoOrario(Integer costoOrario) {this.costoOrario = costoOrario;}
    public String getNomeStudio() {
        return nomeStudio;
    }
    public void setNomeStudio(String nomeStudio) {this.nomeStudio=nomeStudio;}
    public String getGenere() {
        return genere;
    }
    public Integer getNumNotifiche() {return numNotifiche;}
    public void setPassword(String password) {this.password = password;}
    public String getPassword(){return password;}
    public void controlloUsername(String username) {
        if (username.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("Lo username non può essere composto solo da numeri.");
        }
    }

    public void controlloNomeStudio(String nomeStudio) {
        if (nomeStudio.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("Il nome dello studio non può essere composto solo da numeri.");
        }
    }

    private void controlloCognome(String cognome) {
        if (cognome == null || !cognome.matches("\\p{L}+")) {
            throw new IllegalArgumentException("Il cognome deve contenere solo lettere e non può essere nullo");
        }
    }

    private void controlloCostoOrario(String costoOrario) {
        if (costoOrario == null || !costoOrario.matches("\\d+")) {
            throw new IllegalArgumentException("Il costo orario deve essere un numero intero positivo e non nullo");
        }
    }
    private void controlloNome(String nome) {
        if (nome == null || !nome.matches("\\p{L}+")) {
            throw new IllegalArgumentException("Il nome deve contenere solo lettere e non può essere nullo");
        }
    }
}
