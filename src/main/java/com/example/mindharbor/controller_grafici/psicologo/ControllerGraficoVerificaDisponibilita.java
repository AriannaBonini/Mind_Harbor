package com.example.mindharbor.controller_grafici.psicologo;

import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.strumenti_utili.costanti.Costanti;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.patterns.decorator.DisponibilitaDecorator;
import com.example.mindharbor.patterns.decorator.GenereDecorator;
import com.example.mindharbor.patterns.decorator.ImmagineDecorator;
import com.example.mindharbor.strumenti_utili.AlertMessage;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import com.example.mindharbor.strumenti_utili.PrenotaAppuntamentoSingleton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class ControllerGraficoVerificaDisponibilita {
    @FXML
    private Label labelData;
    @FXML
    private Label labelOra;
    @FXML
    private Label labelNome;
    @FXML
    private Label labelCognome;
    @FXML
    private Label labelNomePsicologo;
    @FXML
    private Label home;
    @FXML
    private Label accetta;
    @FXML
    private Label rifiuta;
    @FXML
    private Label verificaDisp;

    @FXML
    private ImageView immaginePaziente;
    @FXML
    private ImageView tornaIndietro;
    @FXML
    private ImageView immagineDisp;

    @FXML
    private Text disp;

    private final PrenotaAppuntamento prenotaAppuntamentoController = PrenotaAppuntamentoSingleton.getInstance();
    private final NavigatorSingleton navigator= NavigatorSingleton.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoVerificaDisponibilita.class);
    private AppuntamentiBean richiestaAppuntamentoSelezionato;

    public void initialize() {
        InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();
        labelNomePsicologo.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

        richiestaAppuntamentoSelezionato = prenotaAppuntamentoController.getRichiestaAppuntamento();

        modificaStatoNotifica();
        schedaAppuntamento();
    }

    private void modificaStatoNotifica() {
        try {
            prenotaAppuntamentoController.modificaStatoNotifica(richiestaAppuntamentoSelezionato);
        }catch (EccezioneDAO e) {
            logger.info("Errore nella modifica dello stato della notifica della richiesta ", e);
        }
    }

    private void schedaAppuntamento() {
        try {
            richiestaAppuntamentoSelezionato = prenotaAppuntamentoController.aggiungiInfoRichiestaAppuntamento(richiestaAppuntamentoSelezionato);
            popolaScheda();
        }catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca delle informazioni della richiesta ", e);
        }
    }

    private void popolaScheda() {
        labelNome.setText(richiestaAppuntamentoSelezionato.getPaziente().getNome());
        labelCognome.setText(richiestaAppuntamentoSelezionato.getPaziente().getCognome());
        labelData.setText(richiestaAppuntamentoSelezionato.getData());
        labelOra.setText(richiestaAppuntamentoSelezionato.getOra());

        ImmagineDecorator immagineDecorator = new GenereDecorator(immaginePaziente,richiestaAppuntamentoSelezionato.getPaziente().getGenere());
        immagineDecorator.caricaImmagine();
    }


    @FXML
    public void clickLabelHome() {
        try {
            prenotaAppuntamentoController.eliminaRichiestaAppuntamento();

            Stage verificaDispStage = (Stage) home.getScene().getWindow();
            verificaDispStage.close();

            navigator.gotoPage("/com/example/mindharbor/HomePsicologo.fxml");
        }catch(IOException e) {
            logger.error(Costanti.IMPOSSIBILE_CARICARE_INTERFACCIA, e);
        }
    }

    @FXML
    public void clickLabelTornaIndietro() {
        try {
            prenotaAppuntamentoController.eliminaRichiestaAppuntamento();

            Stage verificaDispStage = (Stage) tornaIndietro.getScene().getWindow();
            verificaDispStage.close();

            navigator.gotoPage("/com/example/mindharbor/ListaRichiesteAppuntamenti.fxml");
        }catch(IOException e) {
            logger.error(Costanti.IMPOSSIBILE_CARICARE_INTERFACCIA, e);
        }
    }


    @FXML
    public void verificaDisp() {
        verificaDisp.setVisible(false);
        verificaDisp.setDisable(true);

        accetta.setVisible(true);
        rifiuta.setVisible(true);
        disp.setVisible(true);
        immagineDisp.setVisible(true);

        rifiuta.setDisable(false);

        try {
            if(!prenotaAppuntamentoController.verificaDisponibilita(richiestaAppuntamentoSelezionato.getIdAppuntamento())) {
                ImmagineDecorator immagineDecorator = new DisponibilitaDecorator(immagineDisp,false);
                immagineDecorator.caricaImmagine();

            } else {
                ImmagineDecorator immagineDecorator = new DisponibilitaDecorator(immagineDisp,true);
                immagineDecorator.caricaImmagine();
                accetta.setDisable(false);
            }

        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca delle disponibilità dello psicologo ", e);
        }
    }


    @FXML
    public void richiestaAccettata() {
        try {
            prenotaAppuntamentoController.richiestaAccettata(richiestaAppuntamentoSelezionato);
            Alert alert=new AlertMessage().informazione("SUCCESSO", "Richiesta accettata","Hai un nuovo appuntamento");

            new Timeline(new KeyFrame(Duration.seconds(3), event -> alert.close()));
            alert.showAndWait();

            clickLabelTornaIndietro();
        } catch (EccezioneDAO e) {
            logger.info(Costanti.ELIMINA_RICHIESTA, e);
        }
    }

    @FXML
    public void richiestaRifiutata() {
        try {
            prenotaAppuntamentoController.richiestaRifiutata(richiestaAppuntamentoSelezionato);
            Alert alert=new AlertMessage().informazione("SUCCESSO", "Richiesta rifiutata","appuntamento rifiutato");

            new Timeline(new KeyFrame(Duration.seconds(3), event -> alert.close()));
            alert.showAndWait();
            clickLabelTornaIndietro();
        } catch (EccezioneDAO e) {
            logger.info(Costanti.ELIMINA_RICHIESTA, e);
        }
    }
}
