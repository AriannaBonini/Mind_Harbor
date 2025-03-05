package com.example.mindharbor.controller_grafici.psicologo;

import com.example.mindharbor.controller_applicativi.psicologo.HomePsicologoController;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


public class ControllerGraficoHomePsicologo {
    @FXML
    private Label logout;
    @FXML
    private Label prescriviTerapia;
    @FXML
    private Label richiestaPrenotazione;
    @FXML
    private Label visualizzaAppuntamenti;
    @FXML
    private Label labelNomePsicologo;
    @FXML
    private Label notificaTest;
    @FXML
    private Label notificaRichieste;
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoHomePsicologo.class);
    private final HomePsicologoController homePsicologoController = new HomePsicologoController();
    private final NavigatorSingleton navigator = NavigatorSingleton.getInstance();
    private PsicologoBean psicologoBean;

    public void initialize() {
        InfoUtenteBean infoUtenteBean = homePsicologoController.getInfoPsicologo();
        labelNomePsicologo.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
        notificaTestEffettuati();
        notificaRichiesteAppuntamenti();
    }

    private void notificaRichiesteAppuntamenti() {
        try {
            psicologoBean= homePsicologoController.cercaRichiesteAppuntamenti();
            if (psicologoBean.getNumNotifiche() > 0) {
                notificaRichieste.setText(String.valueOf(psicologoBean.getNumNotifiche()));
            }
        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei test ", e);
        }

    }

    private void notificaTestEffettuati() {
        try {
            psicologoBean = homePsicologoController.cercaNuoviTestSvolti();
            if (psicologoBean.getNumNotifiche() > 0) {
                notificaTest.setText(String.valueOf(psicologoBean.getNumNotifiche()));
            }
        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei test ", e);
        }

    }

    @FXML
    public void clickLabelAppuntamenti() {
        try {
            Stage homePsicologo = (Stage) visualizzaAppuntamenti.getScene().getWindow();
            homePsicologo.close();

            navigator.gotoPage("/com/example/mindharbor/ListaAppuntamenti2.fxml");
        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia degli appuntamenti", e);
        }

    }

    @FXML
    public void clickLabelPrescriviTerapia() {
        try {
            Stage homePsicologo = (Stage) prescriviTerapia.getScene().getWindow();
            homePsicologo.close();
            navigator.gotoPage("/com/example/mindharbor/ListaPazienti.fxml");
        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia per la prescrizione delle terapie", e);
        }
    }

    @FXML
    public void clickLabelRichiestePrenotazioni() {
        try {
            Stage homePsicologo = (Stage) richiestaPrenotazione.getScene().getWindow();
            homePsicologo.close();

            navigator.gotoPage("/com/example/mindharbor/ListaRichiesteAppuntamenti.fxml");
        } catch (IOException e ) {
            logger.error("Impossibile caricare l'interfaccia delle richieste di appuntamento", e);
        }

    }

    @FXML
    public void clickLabelLogout() {
        try {
            homePsicologoController.logout();

            Stage homePsicologo = (Stage) logout.getScene().getWindow();
            homePsicologo.close();

            navigator.gotoPage("/com/example/mindharbor/Login.fxml");
        } catch (IOException e ) {
            logger.error("Impossibile caricare l'interfaccia del Login", e);
        }
    }
}
