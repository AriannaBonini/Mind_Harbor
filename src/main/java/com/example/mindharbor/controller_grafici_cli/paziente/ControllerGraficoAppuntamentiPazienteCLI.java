package com.example.mindharbor.controller_grafici_cli.paziente;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.controller_applicativi.AppuntamentiController;
import com.example.mindharbor.controller_grafici_cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.cli_helper.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.cli_helper.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class ControllerGraficoAppuntamentiPazienteCLI extends AbsGestoreInput {

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoAppuntamentiPazienteCLI.class);
    private final AppuntamentiController appuntamentiController = new AppuntamentiController();
    private final InfoUtenteBean infoUtenteBean = appuntamentiController.getInfoUtente();

    @Override
    public void start() {
        GestoreOutput.pulisciPagina();
        if (appuntamentiController.getPsicologo()) {
            modificaStatoNotifica();
            boolean esci = false;
            while (!esci) {
                int opzione;
                try {
                    opzione = mostraMenu();
                    switch (opzione) {
                        case 1 -> ricercaAppuntamentiPaziente("IN PROGRAMMA");
                        case 2 -> ricercaAppuntamentiPaziente("PASSATI");
                        case 3 -> esci = true;
                        default -> throw new EccezioneFormatoNonValido("Scelta non valida");
                    }
                } catch (EccezioneFormatoNonValido e) {
                    logger.info("Scelta non valida ", e);
                }
            }
            new ControllerGraficoHomePazienteCLI().start();
        }else {
            assenzaPsicologo();
        }
    }

    private void assenzaPsicologo() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "LISTA APPUNTAMENTI \n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        GestoreOutput.stampaMessaggio("Non hai ancora uno psicologo");
        boolean esci = false;
        while (!esci) {
            try {
                GestoreOutput.stampaMessaggio("Digita 1 per tornare alla Home");
                if (opzioneScelta(1, 1) == 1) {
                    esci = true;
                } else {
                    throw new EccezioneFormatoNonValido("Scelta non valida");
                }
            } catch (EccezioneFormatoNonValido e) {
                logger.info("Scelta non valida ", e);
            }
            new ControllerGraficoHomePazienteCLI().start();
        }
    }

    @Override
    public int mostraMenu() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "LISTA APPUNTAMENTI \n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        GestoreOutput.stampaMessaggio("1) In Programma");
        GestoreOutput.stampaMessaggio("2) Passati");
        GestoreOutput.stampaMessaggio("3) Torna alla Home");

        return opzioneScelta(1,3);
    }

    private void ricercaAppuntamentiPaziente(String tipologiaAppuntamenti) {
        if (appuntamentiController.getPsicologo()) {
            try {
                List<AppuntamentiBean> appuntamenti = appuntamentiController.getAppuntamentiPaziente(tipologiaAppuntamenti);
                if (appuntamenti.isEmpty()) {
                    GestoreOutput.stampaMessaggio("Non ci sono appuntamenti\n\n");
                } else {
                    stampaAppuntamenti(appuntamenti, tipologiaAppuntamenti);
                }
            } catch (EccezioneDAO e) {
                logger.info("Errore nella ricerca degli appuntamenti", e);
            }
        }
    }

    private void stampaAppuntamenti(List<AppuntamentiBean> appuntamenti, String tipologiaAppuntamenti) {
        GestoreOutput.pulisciPagina();
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + tipologiaAppuntamenti+ "\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        for (AppuntamentiBean app : appuntamenti) {
            GestoreOutput.stampaMessaggio("DATA:" + " " + app.getData());
            GestoreOutput.stampaMessaggio("ORA:" + " " + app.getOra());
            GestoreOutput.stampaMessaggio("PSICOLOGO:" + " " + app.getPsicologo().getNome() + " " + app.getPsicologo().getCognome());
            GestoreOutput.stampaMessaggio("PAZIENTE:" + " " + infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
            GestoreOutput.separatore();
        }
    }

    private void modificaStatoNotifica() {
        try {
            appuntamentiController.modificaStatoNotificaAppuntamenti();
        } catch (EccezioneDAO e ) {
            logger.info("Errore durante la modifica dello stato di notifica dei test psicologici", e);
        }
    }

}
