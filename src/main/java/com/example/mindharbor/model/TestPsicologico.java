package com.example.mindharbor.model;

import java.time.LocalDate;

public class TestPsicologico {
    private LocalDate data;
    private Integer risultato;
    private Psicologo psicologo;
    private Paziente paziente;
    private String test;
    private Integer svolto;
    private Integer statoNotificaPaziente;
    private Integer statoNotificaPsicologo;

    public TestPsicologico() {/*Costruttore vuoto*/}

    public TestPsicologico(LocalDate data, Psicologo psicologo, Paziente paziente) {
        this.data=data;
        this.psicologo=psicologo;
        this.paziente=paziente;
    }

    public TestPsicologico(Psicologo psicologo) {this.psicologo= psicologo;}

    public TestPsicologico(Psicologo psicologo, Paziente paziente) {
        this.psicologo=psicologo;
        this.paziente=paziente;
    }

    public TestPsicologico(LocalDate data, Integer risultato, String test) {
        this.data=data;
        this.risultato=risultato;
        this.test=test;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getRisultato() {
        return risultato;
    }

    public Psicologo getPsicologo() {
        return psicologo;
    }

    public void setPsicologo(Psicologo psicologo) {
        this.psicologo = psicologo;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Integer getSvolto() {
        return svolto;
    }

    public void setSvolto(Integer svolto) {
        this.svolto = svolto;
    }
    public Integer getStatoNotificaPsicologo() {
        return statoNotificaPsicologo;
    }

    public void setStatoNotificaPsicologo(Integer statoNotificaPsicologo) {
        this.statoNotificaPsicologo = statoNotificaPsicologo;
    }

    public Integer getStatoNotificaPaziente() {
        return statoNotificaPaziente;
    }

    public void setStatoNotificaPaziente(Integer statoNotificaPaziente) {
        this.statoNotificaPaziente = statoNotificaPaziente;

    }
    public void setRisultato(Integer risultato) {
        this.risultato = risultato;
    }


}
