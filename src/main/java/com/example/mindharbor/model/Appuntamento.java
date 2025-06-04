package com.example.mindharbor.model;


import com.example.mindharbor.enumerazioni.TipoAppuntamento;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appuntamento {
    private Integer idAppuntamento;
    private LocalDate data;
    private LocalTime ora;
    private Paziente paziente;
    private Psicologo psicologo;
    private Integer statoAppuntamento;
    private Integer statoNotificaPsicologo;
    private Integer statoNotificaPaziente;
    private TipoAppuntamento tipoAppuntamento;

    public Appuntamento() {/*Costruttore vuoto*/}

    public Appuntamento(LocalDate data, LocalTime ora) {
        this.data=data;
        this.ora=ora;
    }

    public Appuntamento(LocalDate data, LocalTime ora, Paziente paziente, Psicologo psicologo) {
        this.data=data;
        this.ora=ora;
        this.paziente=paziente;
        this.psicologo=psicologo;
    }

    public Appuntamento(Integer idAppuntamento) {
        this.idAppuntamento = idAppuntamento;
    }

    public Appuntamento(Integer idAppuntamento, Psicologo psicologo, Paziente paziente) {
        this.idAppuntamento=idAppuntamento;
        this.psicologo=psicologo;
        this.paziente=paziente;
    }


    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public Integer getIdAppuntamento() {
        return idAppuntamento;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public Psicologo getPsicologo() {
        return psicologo;
    }

    public void setPsicologo(Psicologo psicologo) {
        this.psicologo = psicologo;
    }


    public Integer getStatoAppuntamento() {
        return statoAppuntamento;
    }

    public void setStatoAppuntamento(Integer statoAppuntamento) {
        this.statoAppuntamento = statoAppuntamento;
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

    public void setIdAppuntamento(Integer idAppuntamento) {
        this.idAppuntamento = idAppuntamento;
    }

    public TipoAppuntamento getTipoAppuntamento() {
        return tipoAppuntamento;
    }

    public void setTipoAppuntamento(TipoAppuntamento tipoAppuntamento) {
        this.tipoAppuntamento = tipoAppuntamento;
    }
}


