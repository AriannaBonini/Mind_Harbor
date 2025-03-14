package com.example.mindharbor.controller_grafici.psicologo;

import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.TerapiaBean;
import com.example.mindharbor.beans.TestBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.sessione.SessionManager;
import com.example.mindharbor.strumenti_utili.AlertMessage;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import com.example.mindharbor.strumenti_utili.PrescriviTerapiaSingleton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ControllerGraficoPrescriviTerapia {
    @FXML
    private Label campoNome;
    @FXML
    private Label campoCognome;
    @FXML
    private Label campoData;
    @FXML
    private Label home;
    @FXML
    private Label labelNomePsicologo;
    @FXML
    private TextArea campoPrescrizione;
    @FXML
    private ImageView tornaIndietro;
    @FXML
    private ListView<Node> listViewTest;
    @FXML
    private Text campoInfoTest;
    @FXML
    private Button prescrivi;


    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoPrescriviTerapia.class);
    private final   NavigatorSingleton navigator= NavigatorSingleton.getInstance();
    private PazienteBean pazienteSelezionato;
    private TestBean testbean;
    private final PrescriviTerapia prescriviTerapiaController = PrescriviTerapiaSingleton.getInstance();


    public void initialize() {
        InfoUtenteBean infoUtenteBean = prescriviTerapiaController.getInfoUtente();
        labelNomePsicologo.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

        pazienteSelezionato = prescriviTerapiaController.getPazienteSelezionato();

        modificaStatoNotifica();
        popolaListaTestSvoltiSenzaPrescrizione();
    }

    private void popolaListaTestSvoltiSenzaPrescrizione() {
        try {
            List<TestBean> testSvoltiBean = prescriviTerapiaController.getTestSvoltiSenzaPrescrizione(pazienteSelezionato);
            popolaAreaTest(testSvoltiBean);
        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei test svolti senza ancora prescrizioni ", e);
        }
    }

    private void popolaAreaTest(List<TestBean> testSvoltiBean) {
        listViewTest.getItems().clear();

        ObservableList<Node> nodi = FXCollections.observableArrayList();
        for (TestBean testBean : testSvoltiBean) {
            VBox areaTest = new VBox();

            Label dataTest= new Label("DATA: " + testBean.getData());
            Label nomeTest= new Label("TEST: " + testBean.getNomeTest());
            Label risultatoTest= new Label("RISULTATO: "+ testBean.getRisultato());

            areaTest.getChildren().addAll(dataTest,nomeTest,risultatoTest);
            areaTest.setSpacing(5);

            nodi.add(areaTest);

            areaTest.setUserData(testBean);
        }
        listViewTest.setFixedCellSize(100);
        listViewTest.getItems().addAll(nodi);

    }


    private void modificaStatoNotifica() {
        try {
            prescriviTerapiaController.aggiornaStatoNotificaTest(pazienteSelezionato);
        } catch (EccezioneDAO e ) {
            logger.info("Errore durante la modifica dello stato dei test psicologici", e);
        }
    }

    @FXML
    public void clickLabelHome() {
        try {
            prescriviTerapiaController.eliminaPazienteSelezionato();

            Stage prescriviTerapia = (Stage) home.getScene().getWindow();
            prescriviTerapia.close();

            navigator.gotoPage("/com/example/mindharbor/HomePsicologo.fxml");

        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia Home dello psicologo", e);
        }
    }

    @FXML
    public void clickLabelTornaIndietro() {
        try {
            prescriviTerapiaController.azzeraIlNumeroDiTestSvolti();

            Stage prescriviTerapia = (Stage) tornaIndietro.getScene().getWindow();
            prescriviTerapia.close();

            navigator.gotoPage("/com/example/mindharbor/SchedaPersonalePaziente.fxml");

        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia della scheda personale del paziente", e);
        }
    }

    @FXML
    public void nodoSelezionato() {
        Node nodo= listViewTest.getSelectionModel().getSelectedItem();
        if (nodo != null) {
            testbean = (TestBean) nodo.getUserData();
            campoNome.setText(pazienteSelezionato.getNome());
            campoCognome.setText(pazienteSelezionato.getCognome());
            campoData.setText(String.valueOf(LocalDate.now()));
            campoInfoTest.setText("Risultato Test: " + testbean.getRisultato());
            prescrivi.setDisable(false);
        }
    }

    @FXML
    public void clickLabelPrescrivi() {
        String testoInserito= campoPrescrizione.getText();
        if (testoInserito.isEmpty()) {
            Alert alert= new AlertMessage().errore("Inserisci la prescrizione");
            alert.show();
            new Timeline(new KeyFrame(Duration.seconds(3), event -> alert.close()));
        } else {
            try {
            Date currentDate= new Date();
            prescriviTerapiaController.aggiungiTerapia(new TerapiaBean(SessionManager.getInstance().getCurrentUser().getUsername(), pazienteSelezionato.getUsername(), testoInserito,currentDate,testbean.getData()));
            Alert alert= new AlertMessage().informazione("ESITO POSITIVO", "Operazione completata", "Terapia assegnata con successo");
            new Timeline(new KeyFrame(Duration.seconds(3), event -> alert.close()));
            alert.showAndWait();

            clickLabelTornaIndietro();

            } catch (EccezioneDAO e) {
                logger.info("Errore durante il salvataggio della terapia ", e );
                Alert alert= new AlertMessage().errore("Una sola prescrizione per test");
                alert.show();

                new Timeline(new KeyFrame(Duration.seconds(3), event -> alert.close()));
            }
        }
    }

}

