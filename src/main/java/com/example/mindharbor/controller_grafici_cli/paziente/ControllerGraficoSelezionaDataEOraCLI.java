package com.example.mindharbor.controller_grafici_cli.paziente;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.controller_grafici_cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.PrenotaAppuntamentoSingleton;
import com.example.mindharbor.strumenti_utili.cli_helper.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.cli_helper.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

public class ControllerGraficoSelezionaDataEOraCLI extends AbsGestoreInput {

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoSelezionaDataEOraCLI.class);
    private final PrenotaAppuntamento prenotaAppuntamentoController = PrenotaAppuntamentoSingleton.getInstance();
    private final InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();
    private AppuntamentiBean appuntamentoBean;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void start() {
        GestoreOutput.pulisciPagina();
        boolean esci = false;

        while (!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch (opzione) {
                    case 1 -> richiestaAppuntamentoPresente();
                    case 2 -> esci = true;
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
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "SELEZIONA DATA E ORA\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        GestoreOutput.stampaMessaggio("1) Inserisci data e ora");
        GestoreOutput.stampaMessaggio("2) Torna alla Home");

        return opzioneScelta(1, 2);
    }

    private void richiestaAppuntamentoPresente() {
        if ((appuntamentoBean = prenotaAppuntamentoController.getRichiestaAppuntamento()) != null) {
            GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "I dati da te inseriti sono:\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
            GestoreOutput.stampaMessaggio("DATA : " + appuntamentoBean.getData());
            GestoreOutput.stampaMessaggio("ORA : " + appuntamentoBean.getOra());
            GestoreOutput.stampaMessaggio("Vuoi modificarli?\n Digita 1 per confermare, 2 per andare avanti, 3 per tornare alla Home");
            try {
                switch (opzioneScelta(1, 4)) {
                    case 1 -> inserisciData();
                    case 2 -> prossimaInterfaccia();
                    case 3 -> tornaAllaHome();
                    default -> throw new EccezioneFormatoNonValido("Scelta non valida");
                }
            } catch (EccezioneFormatoNonValido e) {
                logger.info("Scelta non valida ", e);
            }
        } else {
            appuntamentoBean= new AppuntamentiBean();
            inserisciData();
        }
    }

    private void inserisciData() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + prenotaAppuntamentoController.tooltipData() + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);

        String data;
        boolean dataValida = false;

        do {
            try {
                GestoreOutput.stampaMessaggio("Inserisci una data (AAAA-MM-GG) o scrivi 'HOME' per tornare indietro: ");
                data = scanner.nextLine();

                if (data.equalsIgnoreCase("HOME")) {
                    GestoreOutput.stampaMessaggio("Caricamento della Home...");
                    new ControllerGraficoHomePazienteCLI().start();
                }

                appuntamentoBean.setData(data);

                dataValida = true;
            } catch (IllegalArgumentException e) {
                GestoreOutput.stampaMessaggio("Errore: il formato della data non è corretto! Riprova.");
            }
        } while (!dataValida);

        inserisciOra();
    }

    private void inserisciOra() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + prenotaAppuntamentoController.tooltipOra() + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        String ora;
        boolean oraValida = false;

        do {
            try {
                GestoreOutput.stampaMessaggio("Inserisci un orario (HH:mm) o scrivi 'HOME' per tornare indietro: ");
                ora = scanner.nextLine();

                if (ora.equalsIgnoreCase("HOME")) {
                    GestoreOutput.stampaMessaggio("Caricamento della Home...");
                    new ControllerGraficoHomePazienteCLI().start();
                }

                appuntamentoBean.setOra(ora);

                oraValida = true;
            } catch (IllegalArgumentException e) {
                GestoreOutput.stampaMessaggio("Errore: il formato dell'ora non è corretto! Riprova.");
            }
        } while (!oraValida);

        controlloFinale();
    }

        private void controlloFinale() {
        if (Boolean.FALSE.equals(prenotaAppuntamentoController.controlloDataEOra(appuntamentoBean))) {
            GestoreOutput.stampaMessaggio("Dati non validi");
            inserisciData();
        } else {
            prenotaAppuntamentoController.setRichiestaAppuntamento(appuntamentoBean);
            prossimaInterfaccia();
        }
    }

    private void prossimaInterfaccia() {
        new ControllerGraficoInserisciInfoCLI().start();
    }

    private void tornaAllaHome() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "Sei sicuro di voler tornare alla Home?" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("Tornando indietro perderai tutti i tuoi progressi");
        GestoreOutput.stampaMessaggio("Digita 1 per confermare, 0 altrimenti");

        if (opzioneScelta(0, 1) == 0) {
            richiestaAppuntamentoPresente();
        } else {
            prenotaAppuntamentoController.eliminaRichiestaAppuntamento();
            new ControllerGraficoHomePazienteCLI().start();
        }
    }

}

