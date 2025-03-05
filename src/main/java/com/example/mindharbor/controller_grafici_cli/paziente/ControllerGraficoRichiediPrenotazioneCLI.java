package com.example.mindharbor.controller_grafici_cli.paziente;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.controller_grafici_cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.PrenotaAppuntamentoSingleton;
import com.example.mindharbor.strumenti_utili.cli_helper.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.cli_helper.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerGraficoRichiediPrenotazioneCLI extends AbsGestoreInput {
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoRichiediPrenotazioneCLI.class);
    private final PrenotaAppuntamento prenotaAppuntamentoController = PrenotaAppuntamentoSingleton.getInstance();
    private final InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();

    private PsicologoBean psicologoSelezionato;

    @Override
    public void start() {
        GestoreOutput.pulisciPagina();
        boolean esci = false;

        while (!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch (opzione) {
                    case 1 -> infoPsicologoSelezionato();
                    case 2 -> tornaIndietro();
                    case 3 -> esci = true;
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
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "SCHEDA PERSONALE PSICOLOGO\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        GestoreOutput.stampaMessaggio("1) Informazioni psicologo");
        GestoreOutput.stampaMessaggio("2) Torna indietro");
        GestoreOutput.stampaMessaggio("3) Torna alla Home");

        return opzioneScelta(1, 3);
    }


    private void infoPsicologoSelezionato()  {
        try {
            psicologoSelezionato = prenotaAppuntamentoController.getPsicologoSelezionato();
            psicologoSelezionato = prenotaAppuntamentoController.getInfoPsicologo(psicologoSelezionato);

            stampaInfoPsicologo();

        }catch (EccezioneDAO e) {
            logger.info("Non esistono ulteriori informazioni relative allo psicologo " ,e);
        }
    }

    private void stampaInfoPsicologo() {
        GestoreOutput.stampaMessaggio("NOME : " + psicologoSelezionato.getNome());
        GestoreOutput.stampaMessaggio("COGNOME : " + psicologoSelezionato.getCognome());
        GestoreOutput.stampaMessaggio("COSTO ORARIO : " + psicologoSelezionato.getCostoOrario());
        GestoreOutput.stampaMessaggio("NOME STUDIO : " + psicologoSelezionato.getNomeStudio());
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "digita 1 per richiedere la prenotazione e 0 per tornare al menù\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);

        int opzione=opzioneScelta(0,1);
        if (opzione==0) {
            start();
        }else {
            richiediAppuntamento();
        }

    }

    private void richiediAppuntamento() {
        try {
            AppuntamentiBean appuntamentoBean = prenotaAppuntamentoController.getRichiestaAppuntamento();
            appuntamentoBean.setPsicologo(psicologoSelezionato);

            prenotaAppuntamentoController.salvaRichiestaAppuntamento(appuntamentoBean);

            GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + " OPERAZIONE COMPLETATA\nRichiesta inviata" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
            new ControllerGraficoHomePazienteCLI().start();
        }catch (EccezioneDAO e ) {
            logger.info("Errore nel salvataggio della richiesta di appuntamento", e);
        }
    }



    private void tornaIndietro() {
        prenotaAppuntamentoController.eliminaPsicologoSelezionato();
        new ControllerGraficoListaPsicologiCLI().start();
    }

    private void tornaAllaHome(){
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "Sei sicuro di voler tornare alla Home?" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("Tornando indietro perderai tutti i tuoi progressi");
        GestoreOutput.stampaMessaggio("Digita 1 per confermare, 0 altrimenti");

        if (opzioneScelta(0, 1) == 0) {
            stampaInfoPsicologo();
        } else {
            prenotaAppuntamentoController.eliminaAppuntamentoSelezionato();
            new ControllerGraficoHomePazienteCLI().start();
        }
    }
}
