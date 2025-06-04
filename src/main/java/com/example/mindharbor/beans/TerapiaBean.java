package com.example.mindharbor.beans;


public class TerapiaBean {
    private String psicologo;
    private String paziente;
    private final String terapia;
    private final String dataTerapia;
    private String dataTest;

    public TerapiaBean(String terapia, String dataTerapia){
        this.terapia=terapia;
        this.dataTerapia=dataTerapia;
    }
    public String getPaziente() {
        return paziente;
    }
    public String getTerapia() {
        return terapia;
    }
    public String getDataTerapia() {
        return dataTerapia;
    }
    public void setPsicologo(String psicologo) {this.psicologo=psicologo;}
    public void setDataTest(String dataTest) {this.dataTest = dataTest;}
    public void setPaziente(String paziente) {this.paziente=paziente;}
    public String getPsicologo() {return psicologo;}
    public String getDataTest() {return dataTest;}
}
