package com.example.mindharbor.controller_grafici.cli.paziente;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreControllerApplicativo;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreParametri;
import com.example.mindharbor.controller_grafici.cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.supporto_cli.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.supporto_cli.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ControllerGraficoInserisciInfoCLI extends AbsGestoreInput implements RicevitoreControllerApplicativo, RicevitoreParametri {
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoInserisciInfoCLI.class);
    private PrenotaAppuntamento prenotaAppuntamentoController;
    private InfoUtenteBean infoUtenteBean;
    private AppuntamentiBean appuntamentoBean;
    private final Scanner scanner = new Scanner(System.in);


    @Override
    public void start() {
        GestoreOutput.pulisciPagina();
        boolean esci = false;

        while(!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch (opzione)  {
                    case 1 -> richiestaAppuntamentoPresente();
                    case 2 -> tornaIndietro();
                    case 3 -> esci=true;
                    default -> throw new EccezioneFormatoNonValido("Scelta non valida");
                }

            }catch (EccezioneFormatoNonValido e) {
                logger.info("Scelta non valida ", e);
            }
        }
        tornaAllaHome();
    }

    @Override
    public int mostraMenu() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "INSERISCI INFORMAZIONI\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        GestoreOutput.stampaMessaggio("1) Inserisci informazioni personali");
        GestoreOutput.stampaMessaggio("2) Torna indietro");
        GestoreOutput.stampaMessaggio("3) Torna alla Home");

        return opzioneScelta(1,3);
    }


    private void inserisciDatiPersonali() {
        String nome;
        String cognome;
        String anni;

        GestoreOutput.stampaMessaggio("Inserisci il tuo nome o scrivi HOME per tornare alla Home o scrivi INDIETRO per tornarre indietro: ");
        nome = scanner.nextLine();

        controlloInserimento(nome);

        GestoreOutput.stampaMessaggio("Inserisci il tuo cognome o scrivi HOME per tornare alla Home o scrivi INDIETRO per tornarre indietro: ");
        cognome=scanner.nextLine();

        controlloInserimento(cognome);

        GestoreOutput.stampaMessaggio("Inserisci i tuoi anni o scrivi HOME per tornare alla Home o scrivi INDIETRO per tornarre indietro:");
        anni=scanner.nextLine();

        controlloInserimento(anni);

        validaDati(anni,nome,cognome);
    }


    private void controlloInserimento(String stringa) {
        if (stringa.equalsIgnoreCase("HOME")) {
            GestoreOutput.stampaMessaggio("Sei sicuro di voler tornare alla Home? Digita 1 per confermare e 0 per annullare");
            tornaAllaHome();
        }
        if (stringa.equalsIgnoreCase("INDIETRO")) {
            GestoreOutput.stampaMessaggio("Caricamento...");
            tornaIndietro();
        }
    }

    private void richiestaAppuntamentoPresente() {

        if (appuntamentoBean.getPaziente() != null && appuntamentoBean.getPaziente().getNome() != null && appuntamentoBean.getPaziente().getCognome() != null && appuntamentoBean.getPaziente().getAnni() != null) {
            GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "I dati da te inseriti sono:\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
            GestoreOutput.stampaMessaggio("NOME : " + appuntamentoBean.getPaziente().getNome());
            GestoreOutput.stampaMessaggio("COGNOME : " + appuntamentoBean.getPaziente().getCognome());
            GestoreOutput.stampaMessaggio("ANNI : " + appuntamentoBean.getPaziente().getAnni());
            GestoreOutput.stampaMessaggio("Vuoi modificarli?\n Digita 1 per confermare, 2 per andare avanti, 3 per tornare indietro, 4 per tornare alla Home.");
            try {
                switch (opzioneScelta(1, 4)) {
                    case 1 -> inserisciDatiPersonali();
                    case 2 -> caricaListaPsicologi();
                    case 3 -> tornaIndietro();
                    case 4 -> tornaAllaHome();
                    default -> throw new EccezioneFormatoNonValido("Scelta non valida");
                }
            } catch (EccezioneFormatoNonValido e) {
                logger.info("Scelta non valida ", e);
            }
        }else{
            inserisciDatiPersonali();
        }
    }

    private void validaDati(String anni, String nome, String cognome ) {
        PazienteBean pazienteBean;
        try {
            pazienteBean=new PazienteBean(nome, cognome, anni);
            if (prenotaAppuntamentoController.controlloInformazioniPaziente(pazienteBean)) {
                appuntamentoBean.setPaziente(pazienteBean);
                caricaListaPsicologi();
            }else {
                GestoreOutput.stampaMessaggio("Dati errati");
                inserisciDatiPersonali();
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore nel controllo dei dati del paziente");
        }catch (IllegalArgumentException e) {
            GestoreOutput.stampaMessaggio("Dati non validi");
        }
    }

    private void caricaListaPsicologi() {
        ControllerGraficoListaPsicologiCLI controllerGraficoListaPsicologiCLI=new ControllerGraficoListaPsicologiCLI();
        controllerGraficoListaPsicologiCLI.setControllerApplicativo(prenotaAppuntamentoController);
        controllerGraficoListaPsicologiCLI.setParametri(appuntamentoBean);
        controllerGraficoListaPsicologiCLI.start();
    }


    private void tornaIndietro() {
        ControllerGraficoSelezionaDataEOraCLI controllerGraficoSelezionaDataEOraCLI= new ControllerGraficoSelezionaDataEOraCLI();
        controllerGraficoSelezionaDataEOraCLI.setParametri(appuntamentoBean);
        controllerGraficoSelezionaDataEOraCLI.start();
    }

    private void tornaAllaHome() {
        if (opzioneScelta(0, 1) == 0) {
            richiestaAppuntamentoPresente();
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
    }

}





