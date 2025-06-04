package com.example.mindharbor.beans;

public class AppuntamentiBean {
        private String data;
        private String ora;
        private PazienteBean paziente;
        private PsicologoBean psicologo;
        private Integer idAppuntamento;
        private Integer notificaRichiesta;
        private static final String FORMATO_ORA = "^\\d{2}:\\d{2}$";
        private static final String FORMATO_DATA = "^\\d{4}-\\d{2}-\\d{2}$";

    public AppuntamentiBean() {/*Costruttore vuoto*/}

    public AppuntamentiBean(String data, String ora, PazienteBean paziente) {
        this.data=data;
        this.ora=ora;
        this.paziente=paziente;
    }
    public AppuntamentiBean(String data, String ora, PsicologoBean psicologo) {
        this.data=data;
        this.ora=ora;
        this.psicologo=psicologo;
    }

    public AppuntamentiBean(PazienteBean paziente, Integer idAppuntamento, Integer notificaRichiesta) {
        this.paziente=paziente;
        this.idAppuntamento=idAppuntamento;
        this.notificaRichiesta=notificaRichiesta;
    }



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
        return data.matches(FORMATO_DATA);
    }

    private boolean controllaFormatoOrario(String ora) {
        return ora.matches(FORMATO_ORA);
    }

}
