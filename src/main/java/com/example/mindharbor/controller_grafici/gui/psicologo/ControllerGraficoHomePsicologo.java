package com.example.mindharbor.controller_grafici.gui.psicologo;

import com.example.mindharbor.controller_applicativi.psicologo.HomePsicologoController;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.supporto_gui.EffettiGrafici;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private HomePsicologoController homePsicologoController;
    private final NavigatorSingleton navigator = NavigatorSingleton.getInstance();
    private PsicologoBean psicologoBean;

    public void initialize() {
        EffettiGrafici.aggiungiEffettoGlow(logout);
        EffettiGrafici.aggiungiEffettoGlow(prescriviTerapia);
        EffettiGrafici.aggiungiEffettoGlow(richiestaPrenotazione);
        EffettiGrafici.aggiungiEffettoGlow(visualizzaAppuntamenti);

        homePsicologoController = new HomePsicologoController();

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
            navigator.closeStage(visualizzaAppuntamenti);

            navigator.gotoPage("/com/example/mindharbor/ListaAppuntamenti2.fxml");
        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia degli appuntamenti", e);
        }

    }

    @FXML
    public void clickLabelPrescriviTerapia() {
        try {
            navigator.closeStage(prescriviTerapia);

            navigator.gotoPage("/com/example/mindharbor/ListaPazienti.fxml");
        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia per la prescrizione delle terapie", e);
        }
    }

    @FXML
    public void clickLabelRichiestePrenotazioni() {
        try {
            navigator.closeStage(richiestaPrenotazione);

            navigator.gotoPage("/com/example/mindharbor/ListaRichiesteAppuntamenti.fxml");
        } catch (IOException e ) {
            logger.error("Impossibile caricare l'interfaccia delle richieste di appuntamento", e);
        }

    }

    @FXML
    public void clickLabelLogout() {
        try {
            homePsicologoController.logout();

            navigator.closeStage(logout);

            navigator.gotoPage("/com/example/mindharbor/Login.fxml");
        } catch (IOException e ) {
            logger.error("Impossibile caricare l'interfaccia del Login", e);
        }
    }
}
