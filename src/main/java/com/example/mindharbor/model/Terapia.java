package com.example.mindharbor.model;

import java.time.LocalDate;

public class Terapia {
    private TestPsicologico testPsicologico;
    private String prescrizione;
    private LocalDate dataTerapia;
    private Integer notificaPaziente;

    public Terapia(TestPsicologico testPsicologico, String prescrizione, LocalDate dataTerapia ) {
        this.testPsicologico=testPsicologico;
        this.prescrizione = prescrizione;
        this.dataTerapia=dataTerapia;
    }

    public Terapia(){/*Costruttore vuoto*/}




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

    public LocalDate getDataTerapia() {
        return dataTerapia;
    }

    public void setDataTerapia(LocalDate dataTerapia) {
        this.dataTerapia = dataTerapia;
    }

    public Integer getNotificaPaziente() {
        return notificaPaziente;
    }

    public void setNotificaPaziente(Integer notificaPaziente) {
        this.notificaPaziente = notificaPaziente;
    }
}
