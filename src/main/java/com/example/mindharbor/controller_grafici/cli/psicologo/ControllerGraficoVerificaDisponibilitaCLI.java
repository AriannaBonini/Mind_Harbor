package com.example.mindharbor.controller_grafici.cli.psicologo;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreControllerApplicativo;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreParametri;
import com.example.mindharbor.controller_grafici.cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.supporto_cli.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.supporto_cli.GestoreOutput;
import com.example.mindharbor.strumenti_utili.costanti.Costanti;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerGraficoVerificaDisponibilitaCLI extends AbsGestoreInput implements RicevitoreControllerApplicativo, RicevitoreParametri {

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoVerificaDisponibilitaCLI.class);
    private PrenotaAppuntamento prenotaAppuntamentoController;
    private InfoUtenteBean infoUtenteBean;
    private AppuntamentiBean richiestaAppuntamentoSelezionato;


    @Override
    public void start(){
        GestoreOutput.pulisciPagina();
        modificaStatoNotifica();
        boolean esci = false;

        while (!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch (opzione) {
                    case 1 -> verificaDisponibilita();
                    case 2 -> tornaIndietro();
                    case 3 -> esci = true;
                    default -> throw new EccezioneFormatoNonValido(Costanti.SCELTA_NON_VALIDA);
                }

            } catch (EccezioneFormatoNonValido e) {
                logger.info(Costanti.SCELTA_NON_VALIDA, e);
            }
        }
        tornaAllaHome();
    }

    @Override
    public int mostraMenu(){
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "VERIFICA DISPONIBILITA'\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        stampaInformazioniRichiestaAppuntamentoSelezionato();
        GestoreOutput.stampaMessaggio("1) Verifica disponibilità");
        GestoreOutput.stampaMessaggio("2) Torna indietro");
        GestoreOutput.stampaMessaggio("3) Torna alla Home");

        return opzioneScelta(1,3);
    }


    private void tornaIndietro() {
        new ControllerGraficoListaRichiesteAppuntamentiCLI().start();
    }
    private void tornaAllaHome() {
        new ControllerGraficoHomePsicologoCLI().start();
    }

    private void stampaInformazioniRichiestaAppuntamentoSelezionato() {
        try {
            richiestaAppuntamentoSelezionato = prenotaAppuntamentoController.aggiungiInfoRichiestaAppuntamento(richiestaAppuntamentoSelezionato);
            popolaScheda();
        }catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca delle informazioni della richiesta ", e);
        }
    }

    private void popolaScheda() {
        GestoreOutput.stampaMessaggio(richiestaAppuntamentoSelezionato.getPaziente().getNome());
        GestoreOutput.stampaMessaggio(richiestaAppuntamentoSelezionato.getPaziente().getCognome());
        GestoreOutput.stampaMessaggio(richiestaAppuntamentoSelezionato.getData());
        GestoreOutput.stampaMessaggio(richiestaAppuntamentoSelezionato.getOra());
    }


    private void modificaStatoNotifica() {
        try {
            prenotaAppuntamentoController.modificaStatoNotifica(richiestaAppuntamentoSelezionato);
        }catch (EccezioneDAO e) {
            logger.info("Errore nella modifica dello stato della notifica della richiesta ", e);
        }
    }

    private void verificaDisponibilita() {
        try {
            if (prenotaAppuntamentoController.nonDisponibile(richiestaAppuntamentoSelezionato)) {
                GestoreOutput.disponibile();
                GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "VUOI ACCETTARE O RIFIUTARE LA RICHIESTA DI APPUNTAMENTO?" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
                GestoreOutput.stampaMessaggio("Per accettare digita 2, 1 per rifiutare e digita 0 per tornare al menu");

                richiestaAccettataORifiutata(opzioneScelta(0,2));

            } else {
                GestoreOutput.nonDisponibile();
                GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "VUOI RIFIUTARE LA RICHIESTA DI APPUNTAMENTO?" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
                GestoreOutput.stampaMessaggio("Per rifiutarla digita 1, altrimenti 0 per tornare al menu");

                richiestaAccettataORifiutata(opzioneScelta(0,1));

            }
        }catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca delle disponibilità dello psicologo ", e);
        }

    }

    private void richiestaAccettataORifiutata(int opzione) {
        try {
            switch (opzione) {
                case 0 -> start();
                case 1 -> richiestaRifiutata();
                case 2 -> richiestaAccettata();
                default -> throw new EccezioneFormatoNonValido("Scelta non valida");
            }
        }catch (EccezioneFormatoNonValido e) {
            logger.info("Scelta non valida ", e);

        }
    }

    private void richiestaAccettata() {
        try {
            prenotaAppuntamentoController.richiestaAccettata(richiestaAppuntamentoSelezionato);
            GestoreOutput.stampaMessaggio(GestoreOutput.messaggioConNotifica("SUCCESSO",true));
            GestoreOutput.stampaMessaggio("Richiesta accettata. Hai un nuovo appuntamento!");
            tornaIndietro();
        }catch (EccezioneDAO e) {
            logger.info(Costanti.ELIMINA_RICHIESTA, e);
        }
    }

    private void richiestaRifiutata(){
        try {
            prenotaAppuntamentoController.richiestaRifiutata(richiestaAppuntamentoSelezionato);
            GestoreOutput.stampaMessaggio(GestoreOutput.messaggioConNotifica("SUCCESSO",true));
            GestoreOutput.stampaMessaggio("Richiesta rifiutata.");
            tornaIndietro();
        }catch (EccezioneDAO e) {
            logger.info(Costanti.ELIMINA_RICHIESTA, e);
        }
    }

    @Override
    public void setControllerApplicativo(Object controllerApplicativo){
        this.prenotaAppuntamentoController=(PrenotaAppuntamento) controllerApplicativo;
        infoUtenteBean=prenotaAppuntamentoController.getInfoUtente();
    }

    @Override
    public void setParametri(Object appuntamentoSelezionato){
        this.richiestaAppuntamentoSelezionato=(AppuntamentiBean)appuntamentoSelezionato;
    }


}
