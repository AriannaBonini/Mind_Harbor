package com.example.mindharbor.controller_grafici.cli.psicologo;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.controller_grafici.cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.supporto_cli.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.supporto_cli.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class ControllerGraficoListaRichiesteAppuntamentiCLI extends AbsGestoreInput {
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoListaRichiesteAppuntamentiCLI.class);
    private final PrenotaAppuntamento prenotaAppuntamentoController = new PrenotaAppuntamento();
    private final InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();
    private List<AppuntamentiBean> listaRichieste;


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
        GestoreOutput.stampaMessaggio(GestoreOutput.NOME + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        GestoreOutput.stampaMessaggio("1) Lista richieste prenotazioni");
        GestoreOutput.stampaMessaggio("2) Torna alla Home");

        return opzioneScelta(1,2);

    }

    private void richiestaPrenotazioni() {
        try {
            if(listaRichieste==null) {
                listaRichieste = prenotaAppuntamentoController.getListaRichieste();
            }
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
                GestoreOutput.stampaMessaggio(GestoreOutput.messaggioConNotifica( "NUMERO RICHIESTA DI APPUNTAMENTO : " + indice, true));
                GestoreOutput.stampaMessaggio(GestoreOutput.messaggioConNotifica("NOME : " + appuntamentiBean.getPaziente().getNome(), true));
                GestoreOutput.stampaMessaggio(GestoreOutput.messaggioConNotifica("COGNOME : " + appuntamentiBean.getPaziente().getCognome(), true));
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
            ControllerGraficoVerificaDisponibilitaCLI controllerGraficoVerificaDisponibilitaCLI= new ControllerGraficoVerificaDisponibilitaCLI();
            controllerGraficoVerificaDisponibilitaCLI.setControllerApplicativo(prenotaAppuntamentoController);
            controllerGraficoVerificaDisponibilitaCLI.setParametri(listaRichieste.get(opzione));
            controllerGraficoVerificaDisponibilitaCLI.start();

        }

    }

    private void tornaAllaHome() { new ControllerGraficoHomePsicologoCLI().start();}
}
