package com.example.mindharbor.model;

import java.util.Date;

public class Terapia {
    private final TestPsicologico testPsicologico;
    private String terapiaStr;
    private final Date dataTerapia;

    public Terapia(TestPsicologico testPsicologico, String terapiaStr, Date dataTerapia ) {
        this.testPsicologico=testPsicologico;
        this.terapiaStr=terapiaStr;
        this.dataTerapia=dataTerapia;
    }

    public TestPsicologico getTestPsicologico() {
        return testPsicologico;
    }

    public String getTerapia() {
        return terapiaStr;
    }

    public void setTerapia(String terapiaStr) {
        this.terapiaStr = terapiaStr;
    }

    public Date getDataTerapia() {
        return dataTerapia;
    }
}
