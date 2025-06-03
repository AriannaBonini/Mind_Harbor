package com.example.mindharbor.model;

import java.util.Date;

public class Terapia {
    private TestPsicologico testPsicologico;
    private String prescrizione;
    private Date dataTerapia;
    private Integer notificaPaziente;

    public Terapia(TestPsicologico testPsicologico, String prescrizione, Date dataTerapia ) {
        this.testPsicologico=testPsicologico;
        this.prescrizione = prescrizione;
        this.dataTerapia=dataTerapia;
    }

    public Terapia(){}

    public TestPsicologico getTestPsicologico() {
        return testPsicologico;
    }

    public void setTestPsicologico(TestPsicologico testPsicologico) {
        this.testPsicologico = testPsicologico;
    }

    public String getTerapia() {
        return prescrizione;
    }

    public void setTerapia(String terapiaStr) {
        this.prescrizione = terapiaStr;
    }

    public Date getDataTerapia() {
        return dataTerapia;
    }

    public void setDataTerapia(Date dataTerapia) {
        this.dataTerapia = dataTerapia;
    }

    public Integer getNotificaPaziente() {
        return notificaPaziente;
    }

    public void setNotificaPaziente(Integer notificaPaziente) {
        this.notificaPaziente = notificaPaziente;
    }
}
