package com.example.mindharbor.controller_grafici_cli.psicologo;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.controller_grafici_cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.PrenotaAppuntamentoSingleton;
import com.example.mindharbor.strumenti_utili.cli_helper.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.cli_helper.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class ControllerGraficoListaRichiesteAppuntamentiCLI extends AbsGestoreInput {
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoListaRichiesteAppuntamentiCLI.class);
    private final PrenotaAppuntamento prenotaAppuntamentoController = PrenotaAppuntamentoSingleton.getInstance();
    private final InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();


    @Override
    public void start() {
        GestoreOutput.pulisciPagina();
        boolean esci = false;

        while (!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch (opzione) {
                    case 1 -> richiestaPrenotazioni();
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
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "RICHIESTE PRENOTAZIONE\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        GestoreOutput.stampaMessaggio("1) Lista richieste prenotazioni");
        GestoreOutput.stampaMessaggio("2) Torna alla Home");

        return opzioneScelta(1,2);

    }

    private void richiestaPrenotazioni() {
        try {
            List<AppuntamentiBean> listaRichieste = prenotaAppuntamentoController.getListaRichieste();
            if(listaRichieste.isEmpty()) {
                GestoreOutput.stampaMessaggio("Non ci sono richieste di appuntamento");
            }else {
                stampaLista(listaRichieste);
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca delle richieste di appuntamento", e);
        }
    }

    private void stampaLista(List<AppuntamentiBean> listaRichieste) {
        int indice=0;
        for (AppuntamentiBean appuntamentiBean : listaRichieste) {
            if (appuntamentiBean.getNotificaRichiesta() == 1) {
                GestoreOutput.stampaMessaggio(GestoreOutput.stampaMessaggioConNotifica( "NUMERO RICHIESTA DI APPUNTAMENTO : " + indice, true));
                GestoreOutput.stampaMessaggio(GestoreOutput.stampaMessaggioConNotifica(" NOME : " + appuntamentiBean.getPaziente().getNome(), true));
                GestoreOutput.stampaMessaggio(GestoreOutput.stampaMessaggioConNotifica(" COGNOME : " + appuntamentiBean.getPaziente().getCognome(), true));
            } else {
                GestoreOutput.stampaMessaggio("NUMERO RICHIESTA DI APPUNTAMENTO : " +indice);
                GestoreOutput.stampaMessaggio("NOME : " + appuntamentiBean.getPaziente().getNome());
                GestoreOutput.stampaMessaggio("COGNOME : " + appuntamentiBean.getPaziente().getCognome());
            }

            GestoreOutput.separatore();
            indice++;
        }
        GestoreOutput.stampaMessaggio("Scegli una richiesta di appuntamento digitando il suo numero di riferimento, oppure digita " + indice + " per tornare al menù");
        int opzione = opzioneScelta(0, indice);
        if (opzione == indice) {
            start();
        } else {
            prenotaAppuntamentoController.setRichiestaAppuntamento(listaRichieste.get(opzione));
            new ControllerGraficoVerificaDisponibilitaCLI().start();

        }

    }

    private void tornaAllaHome() { new ControllerGraficoHomePsicologoCLI().start();}
}
