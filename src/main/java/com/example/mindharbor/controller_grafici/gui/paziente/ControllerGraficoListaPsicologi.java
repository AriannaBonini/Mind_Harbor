package com.example.mindharbor.controller_grafici.gui.paziente;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreControllerApplicativo;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreParametri;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.supporto_gui.MessaggioDiAlert;
import com.example.mindharbor.strumenti_utili.supporto_gui.SupportoCellFactory;
import com.example.mindharbor.strumenti_utili.supporto_gui.SupportoControllerGraficiListaUtenti;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

public class ControllerGraficoListaPsicologi implements RicevitoreControllerApplicativo, RicevitoreParametri {

    @FXML
    private ListView<Node> listViewPsicologo;
    @FXML
    private ImageView tornaIndietro;
    @FXML
    private Label labelNomePaziente;
    @FXML
    private Label home;
    @FXML
    private Text listaVuota;

    private final NavigatorSingleton navigator= NavigatorSingleton.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoListaPsicologi.class);
    private PrenotaAppuntamento prenotaAppuntamentoController;
    private AppuntamentiBean appuntamentoBean;

    public void initialize() {
        listViewPsicologo.setCellFactory(SupportoCellFactory.creaFactoryConGlow());
    }
    private void popolaLista() {
        try {
            List<PsicologoBean> listaPsicologiBean = prenotaAppuntamentoController.getListaPsicologi();
            if(listaPsicologiBean.isEmpty()) {
                listaVuota.setText("Non esistono psicologi");
            }else {
                creaVBoxListaPsicologi(listaPsicologiBean);
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei psicologi", e);
        }
    }
    private void creaVBoxListaPsicologi(List<PsicologoBean> listaPsicologiBean) {
        listViewPsicologo.getItems().clear();
        ObservableList<Node> nodi = FXCollections.observableArrayList();

        for (PsicologoBean psiBean : listaPsicologiBean) {
            ImageView immaginePsicologo = new ImageView();
            HBox hBoxPsicologo = SupportoControllerGraficiListaUtenti.creaHBoxUtenti(immaginePsicologo, psiBean.getNome(), psiBean.getCognome(), psiBean.getGenere(),false);
            nodi.add(hBoxPsicologo);
            hBoxPsicologo.setUserData(psiBean);
        }

        listViewPsicologo.setFixedCellSize(100);
        listViewPsicologo.getItems().addAll(nodi);
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

            navigator.gotoPage("/com/example/mindharbor/InserisciInfo.fxml",prenotaAppuntamentoController,appuntamentoBean);

        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia", e);
        }
    }

    @FXML
    public void nodoSelezionato() {
        try {
            Node nodo = listViewPsicologo.getSelectionModel().getSelectedItem();
            if(nodo==null) {
                return;
            }

            PsicologoBean psicologoSelezionato =(PsicologoBean) nodo.getUserData();
            appuntamentoBean.setPsicologo(psicologoSelezionato);

            navigator.closeStage(listViewPsicologo);

            navigator.gotoPage("/com/example/mindharbor/RichiediPrenotazione.fxml",prenotaAppuntamentoController,appuntamentoBean);
        } catch (IOException e) {
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
        popolaLista();
    }

    @Override
    public void setParametri(Object appuntamentoBean) {
        this.appuntamentoBean=(AppuntamentiBean)appuntamentoBean;
    }
}
