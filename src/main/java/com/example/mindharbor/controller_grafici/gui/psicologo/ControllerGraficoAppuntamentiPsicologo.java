package com.example.mindharbor.controller_grafici.gui.psicologo;

import com.example.mindharbor.controller_applicativi.AppuntamentiController;
import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import com.example.mindharbor.strumenti_utili.supporto_gui.SupportoTab;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class ControllerGraficoAppuntamentiPsicologo {
    @FXML
    private Text listaVuotaInProgramma;
    @FXML
    private Text listaVuotaPassati;
    @FXML
    private Label labelNomePsicologoTab2;
    @FXML
    private Label labelNomePsicologoTab1;
    @FXML
    private ListView<Node> listViewInProgramma;
    @FXML
    private ListView<Node> listViewPassati;
    @FXML
    private Label homeTab1;
    @FXML
    private Label homeTab2;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;

    private AppuntamentiController appuntamentiController;
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoAppuntamentiPsicologo.class);
    private InfoUtenteBean infoUtenteBean;
    private final NavigatorSingleton navigator= NavigatorSingleton.getInstance();

    public void initialize() {
        appuntamentiController= new AppuntamentiController();

        infoUtenteBean=appuntamentiController.getInfoUtente();

        labelNomePsicologoTab1.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
        labelNomePsicologoTab2.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

        tab1Selezionato();

        SupportoTab.aggiuntoListenerTab(tabPane, tab1, tab2, this::tab1Selezionato, this::tab2Selezionato);

    }

    private void tab1Selezionato()  {
        ricercaAppuntamenti("IN PROGRAMMA", listaVuotaInProgramma, listViewInProgramma);
    }

    private void tab2Selezionato() {
        ricercaAppuntamenti("PASSATI", listaVuotaPassati, listViewPassati);
    }

    private void ricercaAppuntamenti(String selectedTabName, Text text, ListView<Node> listView){
        try {
            List<AppuntamentiBean> appuntamenti = appuntamentiController.getAppuntamentiPsicologo(selectedTabName);
            if (appuntamenti.isEmpty()) {
                text.setText("Non ci sono appuntamenti");
            }else {
                creaVBoxAppuntamenti(appuntamenti, listView);
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca degli appuntamenti", e);
        }
    }

    private void creaVBoxAppuntamenti(List<AppuntamentiBean> appuntamenti, ListView<Node> listView) {
        listView.getItems().clear();

        ObservableList<Node> nodi = FXCollections.observableArrayList();

        for (AppuntamentiBean app : appuntamenti) {
            VBox vBox = new VBox();

            Label dataAppuntamento = new Label("DATA:" + " " + app.getData());
            Label oraAppuntamento = new Label("ORA:" + " " + app.getOra());
            Label nomePsicologo = new Label("PSICOLOGO:" + " " + infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
            Label nomePaziente = new Label("PAZIENTE:" + " " + app.getPaziente().getNome()+ " " + app.getPaziente().getCognome());

            dataAppuntamento.setTextFill(Color.WHITE);
            oraAppuntamento.setTextFill(Color.WHITE);
            nomePsicologo.setTextFill(Color.WHITE);
            nomePaziente.setTextFill(Color.WHITE);

            vBox.getChildren().addAll(dataAppuntamento, oraAppuntamento, nomePsicologo, nomePaziente);
            nodi.add(vBox);
        }

        listView.setFixedCellSize(100);
        listView.getItems().addAll(nodi);
    }

    @FXML
    public void vaiAllaHomeDaTab1() {
        clickLabelHome(homeTab1);
    }
    @FXML
    public void vaiAllaHomeDaTab2() {
        clickLabelHome(homeTab2);
    }
    private void clickLabelHome(Label label) {
        try {

            navigator.closeStage(label);

            navigator.gotoPage("/com/example/mindharbor/HomePsicologo.fxml");

        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia Home dello psicologo", e);
        }
    }
}
