package com.example.mindharbor.controller_grafici.gui.psicologo;


import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreControllerApplicativo;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreParametri;
import com.example.mindharbor.eccezioni.EccezionePazienteNonAutorizzato;
import com.example.mindharbor.strumenti_utili.supporto_gui.EffettiGrafici;
import com.example.mindharbor.strumenti_utili.costanti.Costanti;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.patterns.decorator.GenereDecorator;
import com.example.mindharbor.patterns.decorator.ImmagineDecorator;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


public class ControllerGraficoSchedaPersonalePaziente implements RicevitoreControllerApplicativo, RicevitoreParametri {
    @FXML
    private Label home;
    @FXML
    private Label nomePaziente;
    @FXML
    private Label cognomePaziente;
    @FXML
    private Label anniPaziente;
    @FXML
    private Label prescriviTerapia;
    @FXML
    private Label labelNomePsicologo;
    @FXML
    private Label notificaTest;
    @FXML
    private Text diagnosiPaziente;
    @FXML
    private ImageView immaginePaziente;
    @FXML
    private ImageView tornaIndietro;
    @FXML
    private Label scegliTest;

    private PazienteBean pazienteSelezionato;
    private PrescriviTerapia prescriviTerapiaController;
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoSchedaPersonalePaziente.class);
    private final NavigatorSingleton navigator=NavigatorSingleton.getInstance();

    public void initialize() {
        EffettiGrafici.aggiungiEffettoGlow(prescriviTerapia);
        EffettiGrafici.aggiungiEffettoGlow(scegliTest);
    }


    private void abilitaPrescriviTerapia() {
        try {
            if (prescriviTerapiaController.esistenzaTestSvoltiSenzaPrescrizione(pazienteSelezionato)) {
                prescriviTerapia.setDisable(false);
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei test svolti dal paziente senza prescrizione" , e);
        }
    }

    private void notificaStatoTest()  {
        try {
            pazienteSelezionato = prescriviTerapiaController.notificheTestSvolti(pazienteSelezionato);
            if (pazienteSelezionato.getNumTestSvolti() > 0) {
                notificaTest.setText(String.valueOf(pazienteSelezionato.getNumTestSvolti()));
            } else {
                notificaTest.setVisible(false);
            }
        }catch (EccezioneDAO e) {
            logger.info(e.getMessage());
        }
    }

    private void abilitaAssegnaTest() {
        try {
            if (prescriviTerapiaController.controlloAssegnazioneTestOdierno(pazienteSelezionato)) {
                scegliTest.setDisable(true);
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore durante il controllo della presenza di un test già assegnato al paziente nella giornata odierna" , e);

        }
    }


    private void popolaSchedaPersonale()  {
        try {
            pazienteSelezionato = prescriviTerapiaController.getSchedaPersonale(pazienteSelezionato);
            creaSchedaPersonale();

        } catch (EccezioneDAO e) {
            logger.info("Non esistono informazioni relative al paziente", e);
        }catch (EccezionePazienteNonAutorizzato e){
            logger.info("Errore",e);
        }

    }


    private void creaSchedaPersonale() {

        nomePaziente.setText(pazienteSelezionato.getNome());
        cognomePaziente.setText(pazienteSelezionato.getCognome());

        anniPaziente.setText(pazienteSelezionato.getAnni()+ " anni");

        if(pazienteSelezionato.getDiagnosi()==null || pazienteSelezionato.getDiagnosi().isEmpty()) {
            diagnosiPaziente.setText("Diagnosi Sconosciuta");
        } else {
            diagnosiPaziente.setText(pazienteSelezionato.getDiagnosi());
        }

        ImmagineDecorator immagineDecorator = new GenereDecorator(immaginePaziente,pazienteSelezionato.getGenere());
        immagineDecorator.caricaImmagine();
    }

    @FXML
    public void goToHome() {
        try {

            navigator.closeStage(home);

            navigator.gotoPage("/com/example/mindharbor/HomePsicologo.fxml");
        }catch(IOException e) {
            logger.error(Costanti.IMPOSSIBILE_CARICARE_INTERFACCIA, e);
        }

    }

    @FXML
    public void tornaIndietro() {
        try {

            navigator.closeStage(tornaIndietro);

            navigator.gotoPage("/com/example/mindharbor/ListaPazienti.fxml", prescriviTerapiaController);
        }catch(IOException e) {
            logger.error(Costanti.IMPOSSIBILE_CARICARE_INTERFACCIA, e);
        }

    }


    @FXML
    public void scegliTest() {
        try {

            navigator.closeStage(scegliTest);

            navigator.gotoPage("/com/example/mindharbor/ScegliTest.fxml", prescriviTerapiaController,pazienteSelezionato);
        }catch(IOException e) {
            logger.error(Costanti.IMPOSSIBILE_CARICARE_INTERFACCIA, e);
        }
    }
    @FXML
    public void prescriviTerapia() {
        try {

            navigator.closeStage(prescriviTerapia);

            navigator.gotoPage("/com/example/mindharbor/PrescrizioneTerapia.fxml", prescriviTerapiaController,pazienteSelezionato);
        }catch(IOException e) {
            logger.error(Costanti.IMPOSSIBILE_CARICARE_INTERFACCIA, e);
        }

    }

    @Override
    public void setControllerApplicativo(Object controllerApplicativo) {
        this.prescriviTerapiaController = (PrescriviTerapia) controllerApplicativo;
        inizializzazioneDati();
    }

    private void inizializzazioneDati() {
        InfoUtenteBean infoUtenteBean = prescriviTerapiaController.getInfoUtente();
        labelNomePsicologo.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

    }

    @Override
    public void setParametri(Object pazienteSelezionato) {
        this.pazienteSelezionato=(PazienteBean)pazienteSelezionato;
        inizializzazioneDatiBean();
    }

    private void inizializzazioneDatiBean(){
        notificaStatoTest();
        abilitaPrescriviTerapia();
        abilitaAssegnaTest();

        if (pazienteSelezionato.getAnni() == null && pazienteSelezionato.getDiagnosi() == null) {
            /*
              Questo controllo verifica se è già stato effettuato un accesso alla persistenza in precedenza.
              Serve a gestire il caso in cui lo psicologo torni indietro all'interfaccia precedente
              dopo aver navigato avanti, evitando così di eseguire nuovamente operazioni ridondanti sulla persistenza
             */
            popolaSchedaPersonale();
        } else {
            creaSchedaPersonale();
        }

    }

}


