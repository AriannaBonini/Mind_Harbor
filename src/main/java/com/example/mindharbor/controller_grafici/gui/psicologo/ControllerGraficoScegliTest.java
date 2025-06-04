package com.example.mindharbor.controller_grafici.gui.psicologo;

import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.TestBean;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

public class ControllerGraficoScegliTest implements RicevitoreControllerApplicativo, RicevitoreParametri {

    @FXML
    private Label nomePaziente;
    @FXML
    private Label cognomePaziente;
    @FXML
    private Label anniPaziente;
    @FXML
    private Label labelNomePsicologo;
    @FXML
    private Label home;
    @FXML
    private ImageView immaginePaziente;
    @FXML
    private ImageView tornaIndietro;
    @FXML
    private CheckBox test1;
    @FXML
    private CheckBox test2;
    @FXML
    private CheckBox test3;
    @FXML
    private CheckBox test4;
    @FXML
    private Label assegnaTest;

    private PazienteBean pazienteSelezionato;
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoScegliTest.class);
    private List<TestBean> listaTestPsicologiciBean;
    private PrescriviTerapia prescriviTerapiaController;
    private final  NavigatorSingleton navigator= NavigatorSingleton.getInstance();


    public void initialize() {
        EffettiGrafici.aggiungiEffettoGlow(test1);
        EffettiGrafici.aggiungiEffettoGlow(test2);
        EffettiGrafici.aggiungiEffettoGlow(test3);
        EffettiGrafici.aggiungiEffettoGlow(test4);
        EffettiGrafici.aggiungiEffettoGlow(assegnaTest);
    }


    private void aggiungiInformazioni() {
        nomePaziente.setText(pazienteSelezionato.getNome());
        cognomePaziente.setText(pazienteSelezionato.getCognome());

        anniPaziente.setText(pazienteSelezionato.getAnni()+ " anni");

        ComponenteNodo base = new NodoBase(immaginePaziente);

        NodoDecorator nodoDecorator = new GenereDecorator(base, pazienteSelezionato.getGenere());

        nodoDecorator.applica();
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
    public void clickLabelTornaIndietro() {
        try {
            navigator.closeStage(tornaIndietro);

            navigator.gotoPage("/com/example/mindharbor/SchedaPersonalePaziente.fxml",prescriviTerapiaController,pazienteSelezionato);

        }catch(IOException e) {
            logger.error("Impossibile caricare l'interfaccia della scheda personale del paziente", e);
        }

    }

    @FXML
    public void ottieniListaTest() {
         listaTestPsicologiciBean = prescriviTerapiaController.getListaTest();

        if (listaTestPsicologiciBean != null) {
            CheckBox[] checkBoxes = {test1, test2, test3, test4};
            int numCheckBoxes = Math.min(listaTestPsicologiciBean.size(), checkBoxes.length);
            for (int i = 0; i < numCheckBoxes; i++) {
                checkBoxes[i].setText(String.valueOf(listaTestPsicologiciBean.get(i).getNomeTest()));
                checkBoxes[i].setVisible(true);
            }
            for (int i = numCheckBoxes; i < checkBoxes.length; i++) {
                checkBoxes[i].setVisible(false);
            }
        }
    }

    @FXML
    public void assegnaTest() {
        CheckBox[] checkBoxes = {test1, test2, test3, test4};
        int numCheckBoxes = Math.min(listaTestPsicologiciBean.size(), checkBoxes.length);
        int contatore = 0;
        String nomeTest = null;

        for (int i = 0; i < numCheckBoxes; i++) {
            if (checkBoxes[i].isSelected()) {
                contatore++;
                nomeTest = checkBoxes[i].getText();
            }
        }
        TestBean testBean= new TestBean(nomeTest);
        testBean.setPaziente(pazienteSelezionato.getUsername());

        try {
            if(!prescriviTerapiaController.controlloNumeroTestSelezionati(contatore,testBean)){
                controlloFallito();
            }else {
                controlloSuperato();
            }
        } catch (EccezioneDAO e) {
            logger.error("Errore nell'assegnazione del test", e);
        }

    }

    private void controlloFallito(){
        Alert alert = MessaggioDiAlert.errore("Selezionare uno ed un solo test");
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> alert.close()));
        timeline.play();
    }

    private void controlloSuperato() {
        Alert alert = MessaggioDiAlert.informazione("Operazione Completata", "Esito Positivo", "Test assegnato con successo");
        alert.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            alert.close();
            clickLabelTornaIndietro();
        }));
        timeline.play();
    }

    @Override
    public void setControllerApplicativo(Object controllerApplicativo) {
        this.prescriviTerapiaController = (PrescriviTerapia) controllerApplicativo;
        inizializzazioneDati();
    }

    private void inizializzazioneDati(){
        InfoUtenteBean infoUtenteBean = prescriviTerapiaController.getInfoUtente();
        labelNomePsicologo.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
    }

    @Override
    public void setParametri(Object pazienteSelezionato){
        this.pazienteSelezionato=(PazienteBean) pazienteSelezionato;

        aggiungiInformazioni();
        ottieniListaTest();
    }
}

