package com.example.mindharbor.controller_grafici.gui;

import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.controller_applicativi.RegistrazioneController;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.supporto_gui.MessaggioDiAlert;
import com.example.mindharbor.strumenti_utili.supporto_gui.LabelTemporanea;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import com.example.mindharbor.strumenti_utili.costanti.Costanti;
import com.example.mindharbor.enumerazioni.TipoUtente;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ControllerGraficoRegistrazione {

    /**
     * DA CONTROLLARE
     */

    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoCognome;
    @FXML
    private TextField campoUsername;
    @FXML
    private TextField campoPassword;
    @FXML
    private TextField campoNomeStudio;
    @FXML
    private TextField campoCostoOrario;
    @FXML
    private TextField campoAnni;
    @FXML
    private ComboBox<String> campoSesso;
    @FXML
    private ComboBox<String> campoTipoUtente;
    @FXML
    private ImageView tornaIndietro;
    @FXML
    private Label info;
    @FXML
    private Label datiStudio;
    @FXML
    private Label datiAnagrafici;

    private RegistrazioneController registrazioneController;
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoRegistrazione.class);
    private final NavigatorSingleton navigator = NavigatorSingleton.getInstance();
    private String tipoUtenteScelto;

    public void initialize() {
        registrazioneController = new RegistrazioneController();
        campoTipoUtente.getItems().addAll(TipoUtente.PAZIENTE.getId(), TipoUtente.PSICOLOGO.getId());
    }


    @FXML
    public void selezionatoTipoUtente() {
        tipoUtenteScelto = campoTipoUtente.getValue();
        campoNome.setDisable(false);
        campoCognome.setDisable(false);
        campoUsername.setDisable(false);
        campoPassword.setDisable(false);
        campoSesso.setDisable(false);
        campoSesso.getItems().addAll("F", "M");
        datiAnagrafici.setDisable(false);

        if (tipoUtenteScelto.equals("Paziente")) {
            campoAnni.setDisable(false);
            campoCostoOrario.setDisable(true);
            campoNomeStudio.setDisable(true);
            campoCostoOrario.setText("");
            campoNomeStudio.setText("");
            datiStudio.setDisable(true);

        } else if (tipoUtenteScelto.equals("Psicologo")) {
            campoCostoOrario.setDisable(false);
            campoNomeStudio.setDisable(false);
            campoAnni.setDisable(true);
            datiStudio.setDisable(false);
            campoAnni.setText("");
        }
    }

    @FXML
    public void tornaIndietro() {
        try {
            Integer risposta= MessaggioDiAlert.avvertenza("Sei sicuro di voler tornare indietro?");
            if (risposta!=0) {

                navigator.closeStage(tornaIndietro);

                navigator.gotoPage("/com/example/mindharbor/Login.fxml");
            }
        } catch (IOException e) {
            logger.info(Costanti.IMPOSSIBILE_CARICARE_INTERFACCIA, e);
        }
    }

    @FXML
    public void registrati() {
        if (campoNome.getText().isEmpty() || campoCognome.getText().isEmpty() || campoUsername.getText().isEmpty() || campoPassword.getText().isEmpty() || campoSesso.getValue() == null) {
            LabelTemporanea.duration(info,"Compilare tutti i campi");
        } else {
            try {
                if (tipoUtenteScelto.equals("Paziente") && !campoAnni.getText().isEmpty()) {
                    PazienteBean pazienteBean = new PazienteBean(campoNome.getText(), campoCognome.getText(), campoSesso.getValue(), campoUsername.getText(), campoAnni.getText(), campoPassword.getText());
                    registrazioneController.registrazionePaziente(pazienteBean);
                    tornaAllaPaginaLogin();


                } else if (tipoUtenteScelto.equals("Psicologo") && !campoNomeStudio.getText().isEmpty() && !campoCostoOrario.getText().isEmpty()) {
                    PsicologoBean psicologoBean = new PsicologoBean(campoUsername.getText(), campoNome.getText(), campoCognome.getText(), campoCostoOrario.getText(), campoNomeStudio.getText(), campoSesso.getValue(), campoPassword.getText());
                    registrazioneController.registrazionePsicologo(psicologoBean);
                    tornaAllaPaginaLogin();
                }


            } catch (IllegalArgumentException e) {
                logger.info("Errore durante la compilazione dei campi ");
                LabelTemporanea.duration(info,e.getMessage());
            } catch (EccezioneDAO e) {
                logger.info("Errore durante la verifica dello username ");
                LabelTemporanea.duration(info,"username già in uso");
            }
        }
    }

    @FXML
    public void tornaAllaPaginaLogin() {
        try {
        navigator.closeStage(tornaIndietro);

        navigator.gotoPage("/com/example/mindharbor/Login.fxml");

        } catch (IOException e) {
        logger.info(Costanti.IMPOSSIBILE_CARICARE_INTERFACCIA, e);
        }
    }
}
