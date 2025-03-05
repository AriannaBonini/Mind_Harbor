package com.example.mindharbor.controller_grafici.paziente;

import com.example.mindharbor.controller_applicativi.paziente.HomePazienteController;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class ControllerGraficoHomePaziente {
    @FXML
    private Label listaAppuntamenti;
    @FXML
    private Label listaTest;
    @FXML
    private Label terapia;
    @FXML
    private Label prenotaAppuntamento;
    @FXML
    private Label labelNomePaziente;
    @FXML
    private Label notificaTest;
    @FXML
    private Label logout;
    @FXML
    private Label notificaTerapie;
    @FXML
    private Label notificaAppuntamenti;
    private final HomePazienteController homePazienteController = new HomePazienteController();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoHomePaziente.class);
    private final NavigatorSingleton navigator=NavigatorSingleton.getInstance();
    private PazienteBean pazienteBean;

    public void initialize() {
        InfoUtenteBean infoUtenteBean = homePazienteController.getInfoPaziente();
        labelNomePaziente.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

        if(homePazienteController.getPsicologo()) {
            notificaNuoviTest();
            notificaNuoveTerapie();
            notificaNuoviAppuntamenti();
        }
    }

    private void notificaNuoviAppuntamenti() {
        try {
            pazienteBean = homePazienteController.cercaNuoviAppuntamenti();
            if (pazienteBean.getNumNotifiche()>0) {
                notificaAppuntamenti.setVisible(true);
                notificaAppuntamenti.setText(String.valueOf(pazienteBean.getNumNotifiche()));
            }

        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei nuovi appuntamenti ", e);
        }
    }

    private void notificaNuoveTerapie() {
        try {
            pazienteBean = homePazienteController.cercaNuoveTerapie();
            if (pazienteBean.getNumNotifiche()>0) {
                notificaTerapie.setVisible(true);
                notificaTerapie.setText(String.valueOf(pazienteBean.getNumNotifiche()));
            }
        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca delle nuove terapie assegnate al paziente ", e);
        }
    }

    private void notificaNuoviTest() {
        try {
            pazienteBean =homePazienteController.cercaNuoviTestDaSvolgere();
            if (pazienteBean.getNumNotifiche()>0) {
                notificaTest.setVisible(true);
                notificaTest.setText(String.valueOf(pazienteBean.getNumNotifiche()));
            }
        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei nuovi test assegnati al paziente ", e);
        }
    }

    @FXML
    public void clickLabelAppuntamenti() {
        try {
            Stage homePaziente = (Stage) listaAppuntamenti.getScene().getWindow();
            homePaziente.close();
            navigator.gotoPage("/com/example/mindharbor/ListaAppuntamentiPaziente.fxml");

        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia appuntamenti", e);
        }

    }

    @FXML
    public void clickLabelTest() {
        try {
            Stage homePaziente = (Stage) listaTest.getScene().getWindow();
            homePaziente.close();
            navigator.gotoPage("/com/example/mindharbor/ListaTest.fxml");

        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia dei test", e);
        }
    }


    @FXML
    public void clickLabelPrenotaAppuntamento() {
        try {
            Stage homePaziente = (Stage) prenotaAppuntamento.getScene().getWindow();
            homePaziente.close();
            navigator.gotoPage("/com/example/mindharbor/SelezionaDataEOra.fxml");

        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia di prenotazione", e);
        }

    }

    @FXML
    public void clickLabelterapia() {
        try {
            Stage homePaziente = (Stage) terapia.getScene().getWindow();
            homePaziente.close();

            navigator.gotoPage("/com/example/mindharbor/PrescrizioniPaziente.fxml");

        } catch (IOException e ) {
            logger.error("Impossibile caricare l'interfaccia delle terapie", e);
        }
    }


    @FXML
    public void clickLabelLogout() {
        try {
            homePazienteController.logout();

            Stage homePaziente = (Stage) logout.getScene().getWindow();
            homePaziente.close();

            navigator.gotoPage("/com/example/mindharbor/Login.fxml");

        } catch (IOException e ) {
            logger.error("Impossibile caricare l'interfaccia del login", e);
        }
    }

}


