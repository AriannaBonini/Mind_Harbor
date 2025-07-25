package com.example.mindharbor.controller_grafici.gui.psicologo;

import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.patterns.decorator.*;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;



public class ControllerGraficoListaRichiesteAppuntamenti {
    @FXML
    private ListView<Node> listViewPazienti;
    @FXML
    private Label labelNomePsicologo;
    @FXML
    private Label home;
    @FXML
    private Text listaVuota;

    private PrenotaAppuntamento prenotaAppuntamentoController;
    private final NavigatorSingleton navigator= NavigatorSingleton.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoListaRichiesteAppuntamenti.class);

    public void initialize() {
        prenotaAppuntamentoController = new PrenotaAppuntamento();

        InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();
        labelNomePsicologo.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

        popolaLista();
    }

    private void popolaLista() {
        try {
            List<AppuntamentiBean> listaRichieste = prenotaAppuntamentoController.getListaRichieste();
            if(listaRichieste.isEmpty()) {
                listaVuota.setText("Non ci sono richieste di appuntamento");
            }else {
                creaVBoxListaRichieste(listaRichieste);
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca delle richieste di appuntamento", e);
        }
    }

    private void creaVBoxListaRichieste(List<AppuntamentiBean> listaRichieste) {
        listViewPazienti.getItems().clear();

        ObservableList<Node> nodi = FXCollections.observableArrayList();

        for (AppuntamentiBean appBean : listaRichieste) {

            VBox boxPaziente = new VBox();
            HBox hBoxPaziente = new HBox();
            ImageView immaginePaziente = new ImageView();

            Label nomePaziente = new Label("\n     NOME:" + " " + appBean.getPaziente().getNome());
            Label cognomePaziente = new Label("     COGNOME:" + " " + appBean.getPaziente().getCognome());

            if((appBean.getNotificaRichiesta())==1) {
                nomePaziente.setStyle("-fx-font-weight: bold;");
                cognomePaziente.setStyle("-fx-font-weight: bold;");
            }

            nomePaziente.setTextFill(Color.WHITE);
            cognomePaziente.setTextFill(Color.WHITE);

            boxPaziente.getChildren().addAll(nomePaziente, cognomePaziente);
            hBoxPaziente.getChildren().addAll(immaginePaziente, boxPaziente);

            ComponenteNodo base = new NodoBase(hBoxPaziente);
            NodoDecorator immagineDecorator = new GenereDecorator(base, appBean.getPaziente().getGenere());
            NodoDecorator glowDecorator = new GlowDecorator(immagineDecorator);

            glowDecorator.applica();

            nodi.add(hBoxPaziente);

            hBoxPaziente.setUserData(appBean);

        }

        listViewPazienti.setFixedCellSize(100);
        listViewPazienti.getItems().addAll(nodi);
    }


    @FXML
    public void clickLabelHome() {
        try {
            navigator.closeStage(home);

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

                AppuntamentiBean richiestaAppuntamentoSelezionato = (AppuntamentiBean) nodo.getUserData();

                navigator.closeStage(listViewPazienti);

                navigator.gotoPage("/com/example/mindharbor/VerificaDisponibilita.fxml", prenotaAppuntamentoController,richiestaAppuntamentoSelezionato);
            }
        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia", e);
        }
    }
}
