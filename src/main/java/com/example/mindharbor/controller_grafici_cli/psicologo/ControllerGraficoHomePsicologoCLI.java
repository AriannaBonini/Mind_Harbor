package com.example.mindharbor.controller_grafici_cli.psicologo;

import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.controller_applicativi.psicologo.HomePsicologoController;
import com.example.mindharbor.controller_grafici_cli.AbsGestoreInput;
import com.example.mindharbor.controller_grafici_cli.ControllerGraficoLoginCLI;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.cli_helper.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.cli_helper.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerGraficoHomePsicologoCLI extends AbsGestoreInput {

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoHomePsicologoCLI.class);
    private final HomePsicologoController homePsicologoController = new HomePsicologoController();
    private boolean notificaRichiestaPrenotazione;
    private boolean notificaPrescriviTerapia;
    private PsicologoBean psicologoBean;
    @Override
    public void start() {

        GestoreOutput.pulisciPagina();

        notificaTestEffettuati();
        notificaRichiesteAppuntamenti();

        boolean esci = false;
        while(!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch(opzione) {
                    case 1 -> visualizzaAppuntamenti();
                    case 2 -> richiestaAppuntamenti();
                    case 3 -> prescriviTerapia();
                    case 4 -> esci = true;
                    default -> throw new EccezioneFormatoNonValido("Scelta non valida");
                }
            } catch (EccezioneFormatoNonValido e) {
                logger.info("Scelta non valida ", e);
            }
        }
        homePsicologoController.logout();
        new ControllerGraficoLoginCLI().start();
    }


    @Override
    public int mostraMenu() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "HOME PAGE\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("1) Visualizza Appuntamenti");
        GestoreOutput.stampaMessaggio("2) " + GestoreOutput.messaggioConNotifica("Richiesta Prenotazione",notificaRichiestaPrenotazione));
        GestoreOutput.stampaMessaggio("3) " + GestoreOutput.messaggioConNotifica("Prescrivi Terapia",notificaPrescriviTerapia));
        GestoreOutput.stampaMessaggio("4) Logout");

        return opzioneScelta(1,4);
    }

    private void notificaRichiesteAppuntamenti() {
        try {
            psicologoBean= homePsicologoController.cercaRichiesteAppuntamenti();
            notificaRichiestaPrenotazione= psicologoBean.getNumNotifiche() > 0;
        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei test ", e);
        }

    }

    private void notificaTestEffettuati() {
        try {
            psicologoBean = homePsicologoController.cercaNuoviTestSvolti();
            notificaPrescriviTerapia= psicologoBean.getNumNotifiche() > 0;
        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei test ", e);
        }

    }

    private void visualizzaAppuntamenti() {new ControllerGraficoAppuntamentiPsicologoCLI().start();}

    private void richiestaAppuntamenti() {new ControllerGraficoListaRichiesteAppuntamentiCLI().start();}

    private void prescriviTerapia() {new ControllerGraficoListaPazientiCLI().start();}

}
