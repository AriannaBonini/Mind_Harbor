package com.example.mindharbor.controller_grafici.gui.paziente;

import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreControllerApplicativo;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreParametri;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.patterns.decorator.ComponenteNodo;
import com.example.mindharbor.patterns.decorator.GenereDecorator;
import com.example.mindharbor.patterns.decorator.NodoBase;
import com.example.mindharbor.patterns.decorator.NodoDecorator;
import com.example.mindharbor.strumenti_utili.supporto_gui.MessaggioDiAlert;
import com.example.mindharbor.strumenti_utili.supporto_gui.EffettiGrafici;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class ControllerGraficoRichiediPrenotazione implements RicevitoreControllerApplicativo, RicevitoreParametri {

    @FXML
    private ImageView immaginePsicologo;
    @FXML
    private ImageView tornaIndietro;
    @FXML
    private Label nomePsicologo;
    @FXML
    private Label cognomePsicologo;
    @FXML
    private Label costoOrario;
    @FXML
    private Label nomeStudio;
    @FXML
    private Label labelNomePaziente;
    @FXML
    private Label home;
    @FXML
    private Label richiediPrenotazione;

    NavigatorSingleton navigator= NavigatorSingleton.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoRichiediPrenotazione.class);
    private PrenotaAppuntamento prenotaAppuntamentoController;
    private PsicologoBean psicologoSelezionato;
    private AppuntamentiBean appuntamentoBean;

    public void initialize() {
        EffettiGrafici.aggiungiEffettoGlow(richiediPrenotazione);
    }

    private void popolaSchedaPsicologo() {
        try {
            psicologoSelezionato = prenotaAppuntamentoController.getInfoPsicologo(psicologoSelezionato);
            creaSchedaPsicologo();

        }catch (EccezioneDAO e) {
            logger.info("Non esistono ulteriori informazioni relative allo psicologo " ,e);
        }

    }

    private void creaSchedaPsicologo() {
        nomePsicologo.setText(psicologoSelezionato.getNome());
        cognomePsicologo.setText(psicologoSelezionato.getCognome());
        costoOrario.setText(psicologoSelezionato.getCostoOrario() + " €/h");
        nomeStudio.setText(psicologoSelezionato.getNomeStudio());

        ComponenteNodo base = new NodoBase(immaginePsicologo);

        NodoDecorator nodoDecorator = new GenereDecorator(base,psicologoSelezionato.getGenere());
        nodoDecorator.applica();

    }

    @FXML
    public void clickLabelHome() {
        try {
            Integer risposta= MessaggioDiAlert.avvertenza("Sei sicuro di voler tornare indietro?");
            if(risposta!=0) {

                navigator.closeStage(home);

                navigator.gotoPage("/com/example/mindharbor/HomePaziente.fxml");
            }

        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia Home del paziente", e);
        }
    }

    @FXML
    public void clickLabelTornaIndietro() {
        try {
            navigator.closeStage(tornaIndietro);

            navigator.gotoPage("/com/example/mindharbor/ListaPsicologi.fxml",prenotaAppuntamentoController,appuntamentoBean);

        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia della lista degli psicolog", e);
        }
    }

    @FXML
    public void richiediPrenotazione() {
        try {
            prenotaAppuntamentoController.salvaRichiestaAppuntamento(appuntamentoBean);

            Alert alert=MessaggioDiAlert.informazione("OPERAZIONE COMPLETATA","SUCCESSO", "Richiesta inviata");

            new Timeline(new KeyFrame(Duration.seconds(3), event -> alert.close()));
            alert.showAndWait();

            navigator.closeStage(home);

            navigator.gotoPage("/com/example/mindharbor/HomePaziente.fxml");

        }catch (EccezioneDAO e) {
            logger.info("Errore nel salvataggio della richiesta di appuntamento", e);

        }catch (IOException e) {
            logger.info("Errore nel caricamento dell'interfaccia");
        }
    }

    @Override
    public void setControllerApplicativo(Object controllerApplicativo) {
        this.prenotaAppuntamentoController = (PrenotaAppuntamento) controllerApplicativo;
        aggiornaDatiUtente();
    }

    private void aggiornaDatiUtente() {
        InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();
        if (infoUtenteBean != null) {
            String nome = infoUtenteBean.getNome() != null ? infoUtenteBean.getNome() : "";
            String cognome = infoUtenteBean.getCognome() != null ? infoUtenteBean.getCognome() : "";
            labelNomePaziente.setText((nome + " " + cognome).trim());
        }
    }

    @Override
    public void setParametri(Object appuntamentoBean) {
        this.appuntamentoBean = (AppuntamentiBean) appuntamentoBean;
        aggiornaDatiAppuntamento();
    }

    private void aggiornaDatiAppuntamento() {
        if (appuntamentoBean != null) {
            psicologoSelezionato = appuntamentoBean.getPsicologo();
            popolaSchedaPsicologo();
        }
    }

}
