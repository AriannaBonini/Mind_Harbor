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
    private Label registrati;
    @FXML
    private Label info;
    @FXML
    private TextField campoUsername;
    @FXML
    private TextField campoPassword;
    @FXML
    private Button accedi;

    private final LoginController loginController = new LoginController();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoLogin.class);
    private final NavigatorSingleton navigator = NavigatorSingleton.getInstance();


    public void initialize() {
        info.setText("Benvenuto");
    }

    @FXML
    public void clickLabelRegistrati() {
        try {
            Stage login = (Stage) registrati.getScene().getWindow();
            login.close();
            navigator.gotoPage("/com/example/mindharbor/Registrazione.fxml");
        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia per la registrazione", e);
        }
    }


    @FXML
    public void clickBottoneAccedi() {
        String username = campoUsername.getText();
        String password = campoPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            info.setText("Inserisci username e/o password");
            return;
        }
        try {
            CredenzialiLoginBean credenziali = new CredenzialiLoginBean(username, password);
            InfoUtenteBean infoUtenteLoggato = loginController.login(credenziali);
            if (infoUtenteLoggato == null) {
                new LabelDuration().duration(info, "Credenziali errate");
            } else {
                if (infoUtenteLoggato.getUserType().equals(UserType.PAZIENTE)) {
                    homePaziente();
                } else {
                    homePsicologo();
                }
            }
        } catch (EccezioneDAO e) {
            logger.error("Errore durante la ricerca dell'utente {}", username, e);
            new LabelDuration().duration(info, "Credenziali errate");
        } catch (EccezioneSessioneUtente e) {
            logger.info("{} già loggato", username, e);
            new LabelDuration().duration(info, "Utente già loggato");
        }

    }

    private void homePaziente() {
        try {
            Stage login = (Stage) accedi.getScene().getWindow();
            login.close();
            navigator.gotoPage("/com/example/mindharbor/HomePaziente.fxml");
        } catch (IOException e) {
            logger.error("Impossibile caricare la Home del paziente", e);
        }

    }

    private void homePsicologo() {
        try {
            Stage login = (Stage) accedi.getScene().getWindow();
            login.close();
            navigator.gotoPage("/com/example/mindharbor/HomePsicologo.fxml");
        } catch (IOException e) {
            logger.error("Impossibile caricare la Home dello psicologo", e);
        }

    }

}
