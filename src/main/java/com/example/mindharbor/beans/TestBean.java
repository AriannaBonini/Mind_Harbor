package com.example.mindharbor.beans;


public class TestBean {
    private String nomeTest;
    private String paziente;
    private Integer risultato;
    private String data;
    private Integer svolto;

    public TestBean() {}

    public TestBean(String nomeTest) {this.nomeTest=nomeTest;}

    public TestBean(String nomeTest, Integer risultato, String data) {
        this.nomeTest=nomeTest;
        this.risultato=risultato;
        this.data=data;
    }

    public TestBean(String nomeTest, Integer risultato, String data, Integer svolto) {
        this.nomeTest=nomeTest;
        this.risultato=risultato;
        this.data=data;
        this.svolto=svolto;
    }



    public String getNomeTest() {
        return nomeTest;
    }
    public String getPaziente() {
        return paziente;
    }
    public void setPaziente(String paziente) {
        this.paziente = paziente;
    }
    public Integer getRisultato() {
        return risultato;
    }
    public String getData() {
        return data;
    }
    public Integer getSvolto() {return svolto;}
}
