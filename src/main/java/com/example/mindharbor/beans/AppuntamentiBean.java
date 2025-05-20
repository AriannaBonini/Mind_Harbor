package com.example.mindharbor.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalTime;

public class AppuntamentiBean {
        private String data;
        private String ora;
        private PazienteBean paziente;
        private PsicologoBean psicologo;
        private Integer idAppuntamento;
        private Integer notificaRichiesta;
        private static final DateTimeFormatter FORMATO_ORA = DateTimeFormatter.ofPattern("HH:mm");
        private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AppuntamentiBean() {}

    public AppuntamentiBean(String data, String ora, PazienteBean paziente,PsicologoBean psicologo,Integer idAppuntamento,Integer notificaRichiesta) {
        this.data=data;
        this.ora=ora;
        this.paziente=paziente;
        this.psicologo=psicologo;
        this.idAppuntamento=idAppuntamento;
        this.notificaRichiesta=notificaRichiesta;
    }
    public AppuntamentiBean(String data, String ora, PazienteBean paziente) {this(data,ora,paziente,null,null,null);}
    public AppuntamentiBean(String data, String ora, PsicologoBean psicologo) {this(data,ora,null,psicologo,null,null);}
    public AppuntamentiBean(PazienteBean paziente, Integer idAppuntamento, Integer notificaRichiesta) {this(null,null,paziente,null,idAppuntamento,notificaRichiesta);}

    public void setOra(String ora) {
        if (!ora.isEmpty() && controllaFormatoOrario(ora)) {
            this.ora = ora;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setData(String data) {
        if (!data.isEmpty() && controllaFormatoData(data)) {
            this.data = data;
        } else {
            throw new IllegalArgumentException();
        }
    }
    public String getData() {return data;}
    public String getOra() {return ora;}
    public Integer getNotificaRichiesta() {return notificaRichiesta;}
    public PazienteBean getPaziente() {return paziente;}
    public void setPaziente(PazienteBean paziente) {this.paziente = paziente;}
    public PsicologoBean getPsicologo() {return psicologo;}
    public void setPsicologo(PsicologoBean psicologo) {this.psicologo = psicologo;}
    public Integer getIdAppuntamento() {return idAppuntamento;}
    private boolean controllaFormatoData(String data) {
        try {
            LocalDate.parse(data, FORMATO_DATA);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean controllaFormatoOrario(String ora) {
        try {
            LocalTime.parse(ora, FORMATO_ORA);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
