package com.example.mindharbor.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TestPsicologico {
    private Date data;
    private Integer risultato;
    private Psicologo psicologo;
    private Paziente paziente;
    private String test;
    private Integer svolto;

    public TestPsicologico(Date data, Integer risultato, Psicologo psicologo, Paziente paziente, String test, Integer svolto) {
        this.data = data;
        this.risultato = risultato;
        this.psicologo= psicologo;
        this.paziente=paziente;
        this.test=test;
        this.svolto=svolto;
    }
    public TestPsicologico(Psicologo psicologo, Paziente paziente, String test) {this(null,null,psicologo,paziente,test,null);}
    public TestPsicologico(Psicologo psicologo) {
        this.psicologo= psicologo;
    }
    public TestPsicologico(Date data, Integer risultato, String test, Integer svolto) {this(data,risultato,null,null,test,svolto);}
    public TestPsicologico(Date data, Integer risultato, String test) {this(data,risultato,null,null,test,null);}

    public LocalDate convertiInLocalDate(Date date) {
        // Converti java.util.Date a LocalDate
        if (!(date instanceof java.sql.Date)) {
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        // Converte la java.sql.Date in java.util.Date
        return ((java.sql.Date) date).toLocalDate();
    }


    public Date getData() {
        return data;
    }

    public void setData(Date data) {
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
}
