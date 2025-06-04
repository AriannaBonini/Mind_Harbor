package com.example.mindharbor.controller_grafici.gui.paziente;

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
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

public class ControllerGraficoAppuntamentiPaziente {
    @FXML
    private Text listaVuotaInProgramma;
    @FXML
    private Text listaVuotaPassati;
    @FXML
    private Label labelNomePazienteTab2;
    @FXML
    private Label labelNomePazienteTab1;
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
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoAppuntamentiPaziente.class);
    private InfoUtenteBean infoUtenteBean;

    public void initialize() {
        appuntamentiController = new AppuntamentiController();
        infoUtenteBean = appuntamentiController.getInfoUtente();

        labelNomePazienteTab1.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
        labelNomePazienteTab2.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

        if(appuntamentiController.getPsicologo()) {
            modificaStatoNotifica();
            tab1Selezionato();
        } else {
            assenzaPsicologo();
        }

        SupportoTab.aggiuntoListenerTab(tabPane, tab1, tab2, this::tab1Selezionato, this::tab2Selezionato);

    }

    private void assenzaPsicologo() {
        listaVuotaPassati.setText("Non hai ancora uno psicologo");
        listaVuotaInProgramma.setText("Non hai ancora uno psicologo");
    }

    private void modificaStatoNotifica() {
        try {
            appuntamentiController.modificaStatoNotificaAppuntamenti();
        } catch (EccezioneDAO e ) {
            logger.info("Errore durante la modifica dello stato di notifica dei test psicologici", e);
        }
    }


    private void tab1Selezionato(){ricercaAppuntamentiPaziente("IN PROGRAMMA", listaVuotaInProgramma, listViewInProgramma);}
    private void tab2Selezionato() {ricercaAppuntamentiPaziente("PASSATI", listaVuotaPassati, listViewPassati);}

    private void ricercaAppuntamentiPaziente(String selectedTabName, Text text, ListView<Node> listView) {
        if(appuntamentiController.getPsicologo()) {
               try {
                   List<AppuntamentiBean> appuntamenti = appuntamentiController.getAppuntamentiPaziente(selectedTabName);
                   if (appuntamenti.isEmpty()) {
                       text.setText("Non ci sono appuntamenti");
                   } else {
                       creaVBoxAppuntamentiPaziente(appuntamenti, listView);
                   }
               } catch (EccezioneDAO e) {
                   logger.info("Errore nella ricerca degli appuntamenti", e);
               }
           }
    }

    private void creaVBoxAppuntamentiPaziente(List<AppuntamentiBean> appuntamenti, ListView<Node> listView) {
        listView.getItems().clear();

        ObservableList<Node> nodi = FXCollections.observableArrayList();

        for (AppuntamentiBean app : appuntamenti) {
            VBox vBox = new VBox();

            Label dataAppuntamento = new Label("DATA:" + " " + app.getData());
            Label oraAppuntamento = new Label("ORA:" + " " + app.getOra());
            Label nomePsicologo = new Label("PSICOLOGO:" + " " + app.getPsicologo().getNome() + " " + app.getPsicologo().getCognome());
            Label nomePaziente = new Label("PAZIENTE:" + " " + infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

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
    public void clickLabelHomeDaTab1() { caricaHome(homeTab1);}
    @FXML
    public void clickLabelHomeDaTab2() { caricaHome(homeTab2);}

    private void caricaHome(Label label) {
        try {
            NavigatorSingleton navigator= NavigatorSingleton.getInstance();

            navigator.closeStage(label);

            navigator.gotoPage("/com/example/mindharbor/HomePaziente.fxml");

        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia Home del paziente", e);
        }
    }

}
