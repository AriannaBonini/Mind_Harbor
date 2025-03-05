package com.example.mindharbor.beans;

import java.util.Date;

public class TestBean {
    private String nomeTest;
    private String psicologo;
    private String paziente;
    private Integer risultato;
    private Date data;
    private Integer svolto;

    public TestBean() {}

    public TestBean(String nomeTest) {this.nomeTest=nomeTest;}

    public TestBean(String nomeTest, String psicologo, String paziente, Integer risultato, Date data, Integer svolto) {
        this.nomeTest=nomeTest;
        this.psicologo=psicologo;
        this.paziente=paziente;
        this.risultato=risultato;
        this.data=data;
        this.svolto=svolto;

    }

    public TestBean(String nomeTest, Integer risultato, Date data, Integer svolto) {
        this.nomeTest=nomeTest;
        this.risultato=risultato;
        this.data=data;
        this.svolto=svolto;
    }

    public String getNomeTest() {
        return nomeTest;
    }

    public String getPsicologo() {
        return psicologo;
    }

    public void setPsicologo(String psicologo) {
        this.psicologo = psicologo;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {this.data = data;}

    public Integer getSvolto() {
        return svolto;
    }
}
