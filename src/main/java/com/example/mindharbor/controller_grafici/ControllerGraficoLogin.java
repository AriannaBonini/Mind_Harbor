package com.example.mindharbor.controller_grafici;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.tipo_utente.UserType;
import com.example.mindharbor.beans.CredenzialiLoginBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneSessioneUtente;
import com.example.mindharbor.strumenti_utili.LabelDuration;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.example.mindharbor.controller_applicativi.LoginController;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ControllerGraficoLogin {
    @FXML
    private Label msgLbl;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField enterPasswordField;
    @FXML
    private Button accediButton;

    private final LoginController loginController = new LoginController();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoLogin.class);
    private final NavigatorSingleton navigator = NavigatorSingleton.getInstance();


    public void initialize() {
        //possiamo aggiungere la registrazione
        msgLbl.setText("Benvenuto");
    }


    @FXML
    public void onLoginClick() {
        String username = usernameTextField.getText();
        String password = enterPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            msgLbl.setText("Inserisci username e/o password");
            return;
        }
        try {
            CredenzialiLoginBean credenziali = new CredenzialiLoginBean(username, password);
            InfoUtenteBean infoUtenteLoggato = loginController.login(credenziali);
            if (infoUtenteLoggato == null) {
                new LabelDuration().duration(msgLbl, "Credenziali errate");
            } else {
                if (infoUtenteLoggato.getUserType().equals(UserType.PAZIENTE)) {
                    homePaziente();
                } else {
                    homePsicologo();
                }
            }
        } catch (EccezioneDAO e) {
            logger.error("Errore durante la ricerca dell'utente {}", username, e);
            new LabelDuration().duration(msgLbl, "Credenziali errate");
        } catch (EccezioneSessioneUtente e) {
            logger.info("{} già loggato", username, e);
            new LabelDuration().duration(msgLbl, "Utente già loggato");
        }

    }

    public void homePaziente() {
        try {
            Stage loginstage = (Stage) accediButton.getScene().getWindow();
            loginstage.close();
            navigator.gotoPage("/com/example/mindharbor/HomePaziente.fxml");
        } catch (IOException e) {
            logger.error("Impossibile caricare la Home del paziente", e);
        }

    }

    public void homePsicologo() {
        try {
            Stage loginstage = (Stage) accediButton.getScene().getWindow();
            loginstage.close();
            navigator.gotoPage("/com/example/mindharbor/HomePsicologo.fxml");
        } catch (IOException e) {
            logger.error("Impossibile caricare la Home dello psicologo", e);
        }

    }

}
