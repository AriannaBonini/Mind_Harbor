package com.example.mindharbor.controller_grafici.gui.paziente;

import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.TerapiaBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

public class ControllerGraficoTerapiaPaziente {
    @FXML
    public Label listaVuota;
    @FXML
    public Label labelNomePaziente;
    @FXML
    public Label prescrizione;
    @FXML
    public Label dataPrescrizione;
    @FXML
    public Label labelNomePsicologo;
    @FXML
    public Label home;
    @FXML
    public ListView<Node> listViewTerapia;

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoTerapiaPaziente.class);
    private PrescriviTerapia prescriviTerapiaController;

    public void initialize() {
        prescriviTerapiaController= new PrescriviTerapia();

        InfoUtenteBean infoUtenteBean = prescriviTerapiaController.getInfoUtente();
        labelNomePaziente.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

        trovaPsicologo();
    }

    private void trovaPsicologo(){
        try{
            InfoUtenteBean infoUtenteBean= prescriviTerapiaController.infoPsicologo();
            if(infoUtenteBean==null) {
                listaVuota.setText("Non hai ancora uno psicologo");
            }else {
                labelNomePsicologo.setText("PSICOLOGO " + infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
                getListaTerapie();
            }

        }catch (EccezioneDAO e) {
            logger.info("Errore durante la ricerca dello psicologo: ", e);
        }
    }

    private void getListaTerapie() {
        try {
            List<TerapiaBean> terapieBean= prescriviTerapiaController.trovaTerapie();
            if (terapieBean.isEmpty()) {
                listaVuota.setText("Non ci sono terapie");
            } else {
                popolaListaTerapia(terapieBean);
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore nel caricamento della Lista delle Terapie ", e );
        }

    }

    private void popolaListaTerapia(List<TerapiaBean> terapieBean) {
        listViewTerapia.getItems().clear();

        ObservableList<Node> nodi = FXCollections.observableArrayList();

        for (TerapiaBean terapia : terapieBean) {
            VBox boxTerapia= new VBox();

            Label labelDataPrescrizione = new Label("DATA: " + terapia.getDataTerapia());
            Label labelPrescrizione = new Label("PRESCRIZIONE : \n " + terapia.getTerapia());


            boxTerapia.getChildren().addAll(labelDataPrescrizione, labelPrescrizione);
            boxTerapia.setSpacing(5);

            nodi.add(boxTerapia);


            boxTerapia.setUserData(terapia);
        }
        listViewTerapia.setFixedCellSize(150);
        listViewTerapia.getItems().addAll(nodi);
    }

    @FXML
    public void clickLabelHome() {
        try {
            NavigatorSingleton navigator= NavigatorSingleton.getInstance();
            navigator.closeStage(home);

            navigator.gotoPage("/com/example/mindharbor/HomePaziente.fxml");

        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia della Home del paziente", e);
        }
    }
}
