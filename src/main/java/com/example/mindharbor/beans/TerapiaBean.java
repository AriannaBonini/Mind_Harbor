package com.example.mindharbor.beans;

import java.util.Date;

public class TerapiaBean {
    private String psicologo;
    private String paziente;
    private String terapia;
    private final Date dataTerapia;
    private final Date dataTest;

    public TerapiaBean(String psicologo, String paziente, String terapia, Date dataTerapia, Date dataTest) {
        this.psicologo=psicologo;
        this.paziente=paziente;
        this.terapia=terapia;
        this.dataTerapia=dataTerapia;
        this.dataTest=dataTest;
    }

    public TerapiaBean(String paziente, String terapia, Date dataTerapia, Date dataTest) {
        this(null,paziente,terapia,dataTerapia,dataTest);
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

    public String getTerapia() {
        return terapia;
    }

    public void setTerapia(String terapia) {
        this.terapia = terapia;
    }

    public Date getDataTerapia() {
        return dataTerapia;
    }

    public Date getDataTest() {
        return dataTest;
    }

}
