package com.example.mindharbor.beans;

public class RisultatiTestBean {
    private Integer risultatoUltimoTest;
    private Integer risultatoTestPrecedente;
    private Double progresso;

    public RisultatiTestBean() {/*Costruttore Vuoto*/}


    public Integer getRisultatoUltimoTest() {
        return risultatoUltimoTest;
    }
    public void setRisultatoUltimoTest(Integer risultatoUltimoTest) {
        this.risultatoUltimoTest = risultatoUltimoTest;
    }
    public Integer getRisultatoTestPrecedente() {
        return risultatoTestPrecedente;
    }
    public void setRisultatoTestPrecedente(Integer risultatoTestPrecedente) {this.risultatoTestPrecedente = risultatoTestPrecedente;}
    public Double getProgresso() {return progresso;}
    public void setProgresso(Double progresso) {this.progresso = progresso;}
}
