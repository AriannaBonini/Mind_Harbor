package com.example.mindharbor.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.example.mindharbor.strumenti_utili.costanti.OrariEDataValidiRichiestaAppuntamento.*;

public class Appuntamento {
    private Integer idAppuntamento;
    private String data;
    private String ora;
    private Paziente paziente;
    private Psicologo psicologo;
    private Integer notificaRichiesta;

    public Appuntamento(){}


    public Appuntamento(String data, String ora, Integer idAppuntamento, Paziente paziente, Psicologo psicologo, Integer notificaRichiesta) {
        this.idAppuntamento = idAppuntamento;
        this.data = data;
        setOra(ora);
        this.paziente = paziente;
        this.psicologo = psicologo;
        this.notificaRichiesta=notificaRichiesta;
    }

    public Appuntamento(String data, String ora, Paziente paziente) {
        this.data = data;
        setOra(ora);
        this.paziente = paziente;
    }

    public Appuntamento(String data, String ora) {
        this.data = data;
        setOra(ora);
    }

    public Appuntamento(Integer idAppuntamento, Paziente paziente, Integer notificaRichiesta) {
        this.idAppuntamento = idAppuntamento;
        this.paziente = paziente;
        this.notificaRichiesta=notificaRichiesta;
    }

    public Appuntamento(String data, String ora, Integer idAppuntamento, Paziente paziente, Psicologo psicologo) {
        this(data, ora, idAppuntamento, paziente, psicologo, null);
    }

    public Appuntamento(Paziente paziente, Psicologo psicologo) {
        this.paziente=paziente;
        this.psicologo=psicologo;
    }

    public Appuntamento(Integer idAppuntamento) {
        this.idAppuntamento=idAppuntamento;
    }
    public Appuntamento(Integer idAppuntamento, Psicologo psicologo, Paziente paziente) {
        this.idAppuntamento=idAppuntamento;
        this.psicologo=psicologo;
        this.paziente=paziente;
    }

    public boolean controllaOrario(String ora) {
        try {
            LocalTime time = LocalTime.parse(ora);

            boolean inFasciaMattutina = !time.isAfter(INIZIO_PAUSA) && !time.isBefore(ORARIO_APERTURA);
            boolean inFasciaPomeridiana = !time.isAfter(ORARIO_CHIUSURA) &&  !time.isBefore(FINE_PAUSA);

            return inFasciaMattutina || inFasciaPomeridiana;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setData(String data) {
        if (controllaData(data)) {
            this.data = data;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean controllaData(String data) {
        try {
            LocalDate localDate = LocalDate.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (localDate.isBefore(LocalDate.now())) {
                return false;
            }
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            return !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
        }catch(DateTimeParseException e) {
            throw new IllegalArgumentException();
        }
    }


    public void setOra(String ora) {
        if (controllaOrario(ora)) {
            this.ora = ora;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Integer getIdAppuntamento() {
        return idAppuntamento;
    }

    public String getData() {
        return data;
    }

    public String getOra() {return ora;}

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

    public Integer getNotificaRichiesta() {return notificaRichiesta;}
}

