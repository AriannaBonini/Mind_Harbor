package com.example.mindharbor.beans;

public class RisultatiTestBean {
    private Integer risultatoUltimoTest;
    private Double risultatoTestPrecedente;

    public RisultatiTestBean() {/*Costruttore Vuoto*/}


    public Integer getRisultatoUltimoTest() {
        return risultatoUltimoTest;
    }
    public void setRisultatoUltimoTest(Integer risultatoUltimoTest) {
        this.risultatoUltimoTest = risultatoUltimoTest;
    }
    public Double getRisultatoTestPrecedente() {
        return risultatoTestPrecedente;
    }
    public void setRisultatoTestPrecedente(Double risultatoTestPrecedente) {this.risultatoTestPrecedente = risultatoTestPrecedente;}
}
