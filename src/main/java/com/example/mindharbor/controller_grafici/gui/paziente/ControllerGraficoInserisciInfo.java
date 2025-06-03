package com.example.mindharbor.controller_grafici.gui.paziente;

import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreControllerApplicativo;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreParametri;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.supporto_gui.MessaggioDiAlert;
import com.example.mindharbor.strumenti_utili.supporto_gui.EffettiGrafici;
import com.example.mindharbor.strumenti_utili.supporto_gui.LabelTemporanea;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ControllerGraficoInserisciInfo implements RicevitoreControllerApplicativo, RicevitoreParametri {
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
    private PrenotaAppuntamento prenotaAppuntamentoController;
    private AppuntamentiBean appuntamentoBean;
    private PazienteBean pazienteBean;

    public void initialize() {
        EffettiGrafici.aggiungiEffettoGlow(conferma);
    }

    @FXML
    public void clickConferma(){
        try {
            pazienteBean = new PazienteBean(campoNome.getText(), campoCognome.getText(), campoAnni.getText());
            if(prenotaAppuntamentoController.controlloInformazioniPaziente(pazienteBean)) {
                caricaListaPsicologi();
            }else {
                LabelTemporanea.duration(info, "dati errati");
            }
        }catch (IllegalArgumentException e) {
            LabelTemporanea.duration(info,"Dati inseriti errati o mancanti");
        }catch (EccezioneDAO e) {
            logger.info("Errore nel controllo dei dati del paziente");
        }

    }

    private void caricaListaPsicologi() {
        try {
            appuntamentoBean.setPaziente(pazienteBean);

            navigator.closeStage(conferma);

            navigator.gotoPage("/com/example/mindharbor/ListaPsicologi.fxml",prenotaAppuntamentoController,appuntamentoBean);
        }catch (IOException e) {
            logger.info("Impossibile caricare l'interfaccia della lista di psicologi ", e);
        }
    }

    @FXML
    public void clickLabelHome() {
        try {
            Integer risposta= MessaggioDiAlert.avvertenza("Sei sicuro di voler tornare indietro?");
            if(risposta!=0) {

                navigator.closeStage(home);

                navigator.gotoPage("/com/example/mindharbor/HomePaziente.fxml");
            }
        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia della Home", e);
        }
    }

    @FXML
    public void clickLabelTornaIndietro() {
        try {
            navigator.closeStage(tornaIndietro);

            navigator.gotoPage("/com/example/mindharbor/SelezionaDataEOra.fxml", prenotaAppuntamentoController,appuntamentoBean);
        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia", e);
        }
    }

    @Override
    public void setControllerApplicativo(Object controllerApplicativo) {
        this.prenotaAppuntamentoController = (PrenotaAppuntamento) controllerApplicativo;
        inizializzazioneDati();
    }

    private void inizializzazioneDati() {
        InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();
        labelNomePaziente.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
    }

    @Override
    public void setParametri(Object appuntamentoBean){
        this.appuntamentoBean=(AppuntamentiBean) appuntamentoBean;
        inizializzazioneDatiBean();
    }

    private void inizializzazioneDatiBean(){
        if (appuntamentoBean.getPaziente() != null && appuntamentoBean.getPaziente().getNome() != null && appuntamentoBean.getPaziente().getCognome() != null && appuntamentoBean.getPaziente().getAnni() != null) {
            campoNome.setText(appuntamentoBean.getPaziente().getNome());
            campoCognome.setText(appuntamentoBean.getPaziente().getCognome());
            campoAnni.setText(String.valueOf(appuntamentoBean.getPaziente().getAnni()));
        }
    }
}
