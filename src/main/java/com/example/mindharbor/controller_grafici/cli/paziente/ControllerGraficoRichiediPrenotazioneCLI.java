package com.example.mindharbor.controller_grafici.cli.paziente;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.controller_grafici.cli.AbsGestoreInput;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreControllerApplicativo;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreParametri;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.supporto_cli.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.supporto_cli.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerGraficoRichiediPrenotazioneCLI extends AbsGestoreInput implements RicevitoreControllerApplicativo, RicevitoreParametri {
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoRichiediPrenotazioneCLI.class);
    private PrenotaAppuntamento prenotaAppuntamentoController;
    private InfoUtenteBean infoUtenteBean;
    private AppuntamentiBean appuntamentoBean;

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
            appuntamentoBean.setPsicologo(psicologoSelezionato);

            prenotaAppuntamentoController.salvaRichiestaAppuntamento(appuntamentoBean);

            GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + " OPERAZIONE COMPLETATA\nRichiesta inviata" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
            new ControllerGraficoHomePazienteCLI().start();
        }catch (EccezioneDAO e ) {
            logger.info("Errore nel salvataggio della richiesta di appuntamento", e);
        }
    }



    private void tornaIndietro() {
        ControllerGraficoListaPsicologiCLI controllerGraficoListaPsicologiCLI= new ControllerGraficoListaPsicologiCLI();
        controllerGraficoListaPsicologiCLI.setControllerApplicativo(prenotaAppuntamentoController);
        controllerGraficoListaPsicologiCLI.setParametri(appuntamentoBean);
        controllerGraficoListaPsicologiCLI.start();
    }

    private void tornaAllaHome(){
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "Sei sicuro di voler tornare alla Home?" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("Tornando indietro perderai tutti i tuoi progressi");
        GestoreOutput.stampaMessaggio("Digita 1 per confermare, 0 altrimenti");

        if (opzioneScelta(0, 1) == 0) {
            stampaInfoPsicologo();
        } else {
            new ControllerGraficoHomePazienteCLI().start();
        }
    }

    @Override
    public void setControllerApplicativo(Object controllerApplicativo) {
        this.prenotaAppuntamentoController = (PrenotaAppuntamento) controllerApplicativo;
        infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();
    }


    @Override
    public void setParametri(Object appuntamentoBean){
        this.appuntamentoBean=(AppuntamentiBean) appuntamentoBean;
        psicologoSelezionato=(this.appuntamentoBean).getPsicologo();
    }
}
