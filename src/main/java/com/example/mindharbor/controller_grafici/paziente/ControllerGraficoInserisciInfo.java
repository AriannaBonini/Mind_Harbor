package com.example.mindharbor.controller_grafici.paziente;

import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.AlertMessage;
import com.example.mindharbor.strumenti_utili.LabelDuration;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import com.example.mindharbor.strumenti_utili.PrenotaAppuntamentoSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ControllerGraficoInserisciInfo {
    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoCognome;
    @FXML
    private TextField campoAnni;
    @FXML
    private Label labelNomePaziente;
    @FXML
    private Label home;
    @FXML
    private Label conferma;
    @FXML
    private Label info;
    @FXML
    private ImageView tornaIndietro;

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoInserisciInfo.class);
    private final NavigatorSingleton navigator= NavigatorSingleton.getInstance();
    private final PrenotaAppuntamento prenotaAppuntamentoController = PrenotaAppuntamentoSingleton.getInstance();
    private AppuntamentiBean appuntamentoBean;
    private PazienteBean pazienteBean;

    public void initialize() {
        InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();
        labelNomePaziente.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

        appuntamentoBean = prenotaAppuntamentoController.getRichiestaAppuntamento();

        if(appuntamentoBean.getPaziente()!=null && appuntamentoBean.getPaziente().getNome()!=null && appuntamentoBean.getPaziente().getCognome()!=null && appuntamentoBean.getPaziente().getAnni()!=null) {
            campoNome.setText(appuntamentoBean.getPaziente().getNome());
            campoCognome.setText(appuntamentoBean.getPaziente().getCognome());
            campoAnni.setText(String.valueOf(appuntamentoBean.getPaziente().getAnni()));
        }
    }

    @FXML
    public void clickConferma() {
        if(campoNome.getText().isEmpty() || campoCognome.getText().isEmpty() || campoAnni.getText().isEmpty()) {
            new LabelDuration().duration(info,"Compila tutti i campi");
        } else {
            if(!prenotaAppuntamentoController.controlloFormatoAnni(campoAnni.getText())) {
                new LabelDuration().duration(info,"Formato anni errato");
                return;
            }
            try {
                pazienteBean=new PazienteBean(campoNome.getText(), campoCognome.getText(), Integer.valueOf(campoAnni.getText()));
                if (prenotaAppuntamentoController.controlloInformazioniPaziente(pazienteBean)) {
                    caricaListaPsicologi();
                }else {
                    new LabelDuration().duration(info,"Dati errati");
                }
            }catch (EccezioneDAO e) {
                logger.info("Errore nel controllo dei dati del paziente");
            }
        }
    }
    private void caricaListaPsicologi() {
        try {
            appuntamentoBean.setPaziente(pazienteBean);
            prenotaAppuntamentoController.setRichiestaAppuntamento(appuntamentoBean);

            Stage inserisciInfo = (Stage) conferma.getScene().getWindow();
            inserisciInfo.close();

            navigator.gotoPage("/com/example/mindharbor/ListaPsicologi.fxml");
        }catch (IOException e) {
            logger.info("Impossibile caricare l'interfaccia della lista di psicologi ", e);
        }
    }

    @FXML
    public void clickLabelHome() {
        try {
            Integer risposta= new AlertMessage().avvertenza("Sei sicuro di voler tornare indietro?");
            if(risposta!=0) {


                prenotaAppuntamentoController.eliminaRichiestaAppuntamento();

                Stage inserisciInfo = (Stage) home.getScene().getWindow();
                inserisciInfo.close();
                navigator.gotoPage("/com/example/mindharbor/HomePaziente.fxml");
            }
        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia della Home", e);
        }
    }

    @FXML
    public void clickLabelTornaIndietro() {
        try {
            Stage inserisciInfo = (Stage) tornaIndietro.getScene().getWindow();
            inserisciInfo.close();
            navigator.gotoPage("/com/example/mindharbor/SelezionaDataEOra.fxml");
        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia", e);
        }
    }
}
