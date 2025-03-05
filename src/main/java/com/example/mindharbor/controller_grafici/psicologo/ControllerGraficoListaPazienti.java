package com.example.mindharbor.controller_grafici.psicologo;

import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.SupportoControllerGraficiListaUtenti;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import com.example.mindharbor.strumenti_utili.PrescriviTerapiaSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

public class ControllerGraficoListaPazienti {
    @FXML
    private Label labelNomePsicologo;
    @FXML
    private Label home;
    @FXML
    private Text listaVuota;
    @FXML
    private ListView<Node> listViewPazienti;
    private final PrescriviTerapia prescriviTerapiaController = PrescriviTerapiaSingleton.getInstance();
    private final NavigatorSingleton navigator= NavigatorSingleton.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoListaPazienti.class);

    public void initialize() {
        InfoUtenteBean infoUtenteBean = prescriviTerapiaController.getInfoUtente();
        labelNomePsicologo.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
        popolaLista();
    }

    private void popolaLista() {
        try {
            List<PazienteBean> listaPazienti = prescriviTerapiaController.getListaPazienti();
            creaVBoxListaPazienti(listaPazienti);
        }catch (EccezioneDAO e) {
            logger.info("Non non ci sono pazienti", e);
            listaVuota.setText("Non esistono pazienti");
        }
    }

    private void creaVBoxListaPazienti(List<PazienteBean> listaPazienti) {
        listViewPazienti.getItems().clear();
        ObservableList<Node> nodi = FXCollections.observableArrayList();
        boolean grassetto;

        for (PazienteBean paz : listaPazienti) {
            grassetto=false;
            ImageView immaginePaziente = new ImageView();
            if(paz.getNumTestSvolti()>0) {
                grassetto=true;
            }
            HBox hBoxPaziente = SupportoControllerGraficiListaUtenti.creaHBoxUtenti(immaginePaziente, paz.getNome(), paz.getCognome(), paz.getGenere(),grassetto);
            nodi.add(hBoxPaziente);
            hBoxPaziente.setUserData(paz);
        }

        listViewPazienti.setFixedCellSize(100);
        listViewPazienti.getItems().addAll(nodi);
    }

    @FXML
    public void clickLabelHome() {
        try {
            Stage listaPazienti = (Stage) home.getScene().getWindow();
            listaPazienti.close();

            navigator.gotoPage("/com/example/mindharbor/HomePsicologo.fxml");
        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia Home dello psicologo", e);
        }

    }
    @FXML
    public void nodoSelezionato() {
        try {
            Node nodo = listViewPazienti.getSelectionModel().getSelectedItem();
            if(nodo!=null) {
                PazienteBean pazienteSelezionato = (PazienteBean) nodo.getUserData();
                prescriviTerapiaController.setPazienteSelezionato(pazienteSelezionato);

                Stage listaPazienti = (Stage) listViewPazienti.getScene().getWindow();
                listaPazienti.close();

                navigator.gotoPage("/com/example/mindharbor/SchedaPersonalePaziente.fxml");
            }
        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia della scheda personale del paziente", e);
        }
    }
}
