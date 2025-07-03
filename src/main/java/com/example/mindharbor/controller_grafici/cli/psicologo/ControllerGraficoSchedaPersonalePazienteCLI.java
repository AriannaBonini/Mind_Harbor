package com.example.mindharbor.controller_grafici.cli.psicologo;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreControllerApplicativo;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreParametri;
import com.example.mindharbor.controller_grafici.cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.eccezioni.EccezionePazienteNonAutorizzato;
import com.example.mindharbor.strumenti_utili.supporto_cli.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.supporto_cli.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ControllerGraficoSchedaPersonalePazienteCLI extends AbsGestoreInput implements RicevitoreControllerApplicativo, RicevitoreParametri {
    private PrescriviTerapia prescriviTerapiaController;
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoSchedaPersonalePazienteCLI.class);
    private InfoUtenteBean infoUtenteBean;
    private PazienteBean pazienteSelezionato;

    private boolean assegnaTestAbilitato = false;
    private boolean prescriviTerapiaAbilitato = false;

    @Override
    public void start() {
        GestoreOutput.pulisciPagina();

        boolean esci = false;
        while (!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch (opzione) {
                    case 1 -> {
                        if (assegnaTestAbilitato) {
                            scegliTest();
                        } else {
                            GestoreOutput.stampaMessaggio("Operazione non disponibile");
                        }
                    }
                    case 2 -> {
                        if (prescriviTerapiaAbilitato) {
                            prescriviTerapia();
                        } else {
                            GestoreOutput.stampaMessaggio("Operazione non disponibile");
                        }
                    }
                    case 3 -> tornaIndietro();
                    case 4 -> esci = true;
                    default -> throw new EccezioneFormatoNonValido("Scelta non valida");
                }
            } catch (EccezioneFormatoNonValido e) {
                logger.info("Scelta non valida ", e);
            }
        }
        tornaAllaHome();
    }

    @Override
    public int mostraMenu() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "SCHEDA PERSONALE\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");

        schedaPersonale();

        if (!abilitaAssegnaTest()) {
            assegnaTestAbilitato = true;
            GestoreOutput.stampaMessaggio("1) Assegna Test");
        } else {
            GestoreOutput.stampaMessaggio("1) " + CodiciAnsi.ANSI_GRIGIO + "Assegna Test" + CodiciAnsi.ANSI_RIPRISTINA_GRIGIO);
        }

        if (abilitaPrescriviTerapia()) {
            prescriviTerapiaAbilitato = true;
            GestoreOutput.stampaMessaggio("2) " + GestoreOutput.messaggioConNotifica("Prescrivi Terapia", notificaStatoTest()));
        } else {
            GestoreOutput.stampaMessaggio("2) " + CodiciAnsi.ANSI_GRIGIO + "Prescrivi Terapia" + CodiciAnsi.ANSI_RIPRISTINA_GRIGIO);
        }

        GestoreOutput.stampaMessaggio("3) Torna indietro");
        GestoreOutput.stampaMessaggio("4) Torna alla Home");

        return opzioneScelta(1, 4);
    }


    private void tornaIndietro() {
        new ControllerGraficoListaPazientiCLI().start();
    }

    private void tornaAllaHome() {
        new ControllerGraficoHomePsicologoCLI().start();
    }


    private void schedaPersonale() {
        if (pazienteSelezionato.getAnni() == null && pazienteSelezionato.getDiagnosi() == null) {
            popolaSchedaPersonale();
        }
        creaSchedaPersonale();
    }

    private void popolaSchedaPersonale() {
        try {
            pazienteSelezionato = prescriviTerapiaController.getSchedaPersonale(pazienteSelezionato);
        } catch (EccezioneDAO e) {
            logger.info("Non esistono informazioni relative al paziente", e);
        }catch (EccezionePazienteNonAutorizzato e) {
            String username= e.getUsernamePaziente();
            String messaggio= "Tentativo non autorizzato per il paziente: " + username;

            GestoreOutput.stampaMessaggio(messaggio);
            tornaIndietro();
        }

    }


    private void creaSchedaPersonale() {
        GestoreOutput.stampaMessaggio("\n\n");
        GestoreOutput.stampaMessaggio("NOME PAZIENTE : " + pazienteSelezionato.getNome());
        GestoreOutput.stampaMessaggio("COGNOME PAZIENTE : " + pazienteSelezionato.getCognome());
        GestoreOutput.stampaMessaggio("ANNI PAZIENTE : " + pazienteSelezionato.getAnni());

        if (pazienteSelezionato.getDiagnosi() == null || pazienteSelezionato.getDiagnosi().isEmpty()) {
            GestoreOutput.stampaMessaggio("Diagnosi Sconosciuta");
        } else {
            GestoreOutput.stampaMessaggio("DIAGNOSI PAZIENTE : " + pazienteSelezionato.getDiagnosi());
        }

        GestoreOutput.stampaMessaggio("\n\n\n");

    }

    public void scegliTest() {
        ControllerGraficoScegliTestCLI controllerGraficoScegliTestCLI= new ControllerGraficoScegliTestCLI();
        controllerGraficoScegliTestCLI.setControllerApplicativo(prescriviTerapiaController);
        controllerGraficoScegliTestCLI.setParametri(pazienteSelezionato);
        controllerGraficoScegliTestCLI.start();
    }

    public void prescriviTerapia() {
        ControllerGraficoPrescriviTerapiaCLI controllerGraficoPrescriviTerapiaCLI= new ControllerGraficoPrescriviTerapiaCLI();
        controllerGraficoPrescriviTerapiaCLI.setControllerApplicativo(prescriviTerapiaController);
        controllerGraficoPrescriviTerapiaCLI.setParametri(pazienteSelezionato);
        controllerGraficoPrescriviTerapiaCLI.start();
    }

    private boolean abilitaPrescriviTerapia() {
        try {
            return prescriviTerapiaController.esistenzaTestSvoltiSenzaPrescrizione(pazienteSelezionato);
        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei test svolti dal paziente senza prescrizione", e);
        }
        return false;
    }

    private boolean abilitaAssegnaTest() {
        try {
            return prescriviTerapiaController.controlloAssegnazioneTestOdierno(pazienteSelezionato);
        } catch (EccezioneDAO e) {
            logger.info("Errore durante il controllo della presenza di un test già assegnato al paziente nella giornata odierna", e);

        }
        return false;
    }

    private boolean notificaStatoTest() {
        return pazienteSelezionato.getNumTestSvolti() > 0;
    }

    @Override
    public void setControllerApplicativo(Object controllerApplicativo){
        this.prescriviTerapiaController=(PrescriviTerapia) controllerApplicativo;
        infoUtenteBean=prescriviTerapiaController.getInfoUtente();
    }

    @Override
    public void setParametri(Object pazienteSelezionato){
        this.pazienteSelezionato=(PazienteBean)pazienteSelezionato;
    }

}