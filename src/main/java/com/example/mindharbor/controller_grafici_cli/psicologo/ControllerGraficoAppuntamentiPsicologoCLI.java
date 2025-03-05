package com.example.mindharbor.controller_grafici_cli.psicologo;

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

public class ControllerGraficoAppuntamentiPsicologoCLI extends AbsGestoreInput {

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoAppuntamentiPsicologoCLI.class);
    private final AppuntamentiController appuntamentiController = new AppuntamentiController();
    private final InfoUtenteBean infoUtenteBean = appuntamentiController.getInfoUtente();

    @Override
    public void start() {
        GestoreOutput.pulisciPagina();

        boolean esci = false;
            while (!esci) {
                int opzione;
                try {
                    opzione = mostraMenu();
                    switch (opzione) {
                        case 1 -> ricercaAppuntamentiPsicologo("IN PROGRAMMA");
                        case 2 -> ricercaAppuntamentiPsicologo("PASSATI");
                        case 3 -> esci = true;
                        default -> throw new EccezioneFormatoNonValido("Scelta non valida");
                    }
                } catch (EccezioneFormatoNonValido e) {
                    logger.info("Scelta non valida ", e);
                }
            }
            new ControllerGraficoHomePsicologoCLI().start();
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

    private void ricercaAppuntamentiPsicologo(String tipologiaAppuntamenti) {
        try {
            List<AppuntamentiBean> appuntamenti = appuntamentiController.getAppuntamentiPsicologo(tipologiaAppuntamenti);
            if (appuntamenti.isEmpty()) {
                GestoreOutput.stampaMessaggio("Non ci sono appuntamenti\n\n");
            } else {
                stampaAppuntamenti(appuntamenti, tipologiaAppuntamenti);
            }
        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca degli appuntamenti", e);
        }
    }

    private void stampaAppuntamenti(List<AppuntamentiBean> appuntamenti, String tipologiaAppuntamenti) {
        GestoreOutput.pulisciPagina();
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + tipologiaAppuntamenti+ "\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        for (AppuntamentiBean app : appuntamenti) {
            GestoreOutput.stampaMessaggio("DATA:" + " " + app.getData());
            GestoreOutput.stampaMessaggio("ORA:" + " " + app.getOra());
            GestoreOutput.stampaMessaggio("PSICOLOGO:" + " " + infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
            GestoreOutput.stampaMessaggio("PAZIENTE:" + " " + app.getPaziente().getNome()+ " " + app.getPaziente().getCognome());
            GestoreOutput.separatore();
        }
    }






}
