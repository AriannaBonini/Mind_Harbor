package com.example.mindharbor.controller_grafici.paziente;

import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.TestBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.patterns.decorator.ImmagineDecorator;
import com.example.mindharbor.patterns.decorator.TestDecorator;
import com.example.mindharbor.strumenti_utili.LabelDuration;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

public class ControllerGraficoListaTest {

    @FXML
    private ListView<Node> listViewTest;
    @FXML
    private Label labelPsicologo;
    @FXML
    private Label labelNomePaziente;
    @FXML
    private Label info;
    @FXML
    private Label home;
    private final PrescriviTerapia prescriviTerapiaController = PrescriviTerapiaSingleton.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoListaTest.class);
    private final NavigatorSingleton navigator = NavigatorSingleton.getInstance();
    private InfoUtenteBean infoUtenteBean;

    public void initialize() {
        infoUtenteBean = prescriviTerapiaController.getInfoUtente();
        labelNomePaziente.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

        trovaPsicologo();

    }

    private void trovaPsicologo() {
        try {
            infoUtenteBean = prescriviTerapiaController.infoPsicologo();

            if (infoUtenteBean != null) {
                labelPsicologo.setText("PSICOLOGO " + infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
                modificaStatoNotifica();
                popolaLista();

            }else {
                info.setText("Non hai ancora uno psicologo");
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore durante la ricerca dello psicologo: ", e);
        }
    }


    private void modificaStatoNotifica()  {
        try {
            prescriviTerapiaController.aggiornaStatoNotificaTest(null);
        } catch (EccezioneDAO e ) {
            logger.info("Errore durante la modifica dello stato dei test psicologici", e);
        }
    }

    private void popolaLista() throws EccezioneDAO {
        List<TestBean> listaTest = prescriviTerapiaController.getListaTestAssegnati();
        if (listaTest.isEmpty()) {
            info.setText("Non hai test assegnati");
        } else {
            creaVBoxListaTest(listaTest);
        }
    }

    private void creaVBoxListaTest(List<TestBean> listaTest) {
        listViewTest.getItems().clear();

        ObservableList<Node> nodi = FXCollections.observableArrayList();

        for (TestBean test : listaTest){

            VBox boxTest = new VBox();
            HBox hBoxTest = new HBox();
            ImageView immagineStato = new ImageView();

            Label nomeTest =new Label("\n     NOME:" + " " + test.getNomeTest());
            Label dataTest =new Label("\n     DATA:" + " " + test.getData());

            Label risultatoTest;
            if(test.getRisultato()==null) {
                risultatoTest =new Label("\n     RISULTATO:" + "non presente" );

            } else {
                risultatoTest =new Label("\n     RISULTATO:" + " " + test.getRisultato());
            }

            nomeTest.setTextFill(Color.WHITE);
            risultatoTest.setTextFill(Color.WHITE);

            boxTest.getChildren().addAll(dataTest, nomeTest, risultatoTest);

            ImmagineDecorator immagineDecorator = new TestDecorator(immagineStato,test.getSvolto());
            immagineDecorator.caricaImmagine();

            immagineStato.setFitWidth(25);
            immagineStato.setFitHeight(25);

            hBoxTest.getChildren().addAll(immagineStato, boxTest);
            hBoxTest.setSpacing(10);

            nodi.add(hBoxTest);

            hBoxTest.setUserData(test);
        }
        listViewTest.setFixedCellSize(100);
        listViewTest.getItems().addAll(nodi);
    }

    @FXML
    public void clickLabelHome() {
        try {
            Stage listaTest = (Stage) home.getScene().getWindow();
            listaTest.close();

            navigator.gotoPage("/com/example/mindharbor/HomePaziente.fxml");

        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia Home del paziente", e);
        }
    }

    @FXML
    public void testSelezionato() {
        try {
            Node nodo = listViewTest.getSelectionModel().getSelectedItem();
            if (nodo == null) {
                return;
            }
            TestBean test = (TestBean) nodo.getUserData();
            if(test.getSvolto()==1) {
                new LabelDuration().duration(info,"test già effettuato");
                return;
            }
            prescriviTerapiaController.setTestSelezionato(test);

            Stage listaTest = (Stage) listViewTest.getScene().getWindow();
            listaTest.close();

            navigator.gotoPage("/com/example/mindharbor/SvolgiTest.fxml");

        } catch (IOException e) {
            logger.error("Impossibile caricare l'interfaccia Svolgi Test ", e);
        }

    }
}


