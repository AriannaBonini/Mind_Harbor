package com.example.mindharbor.controller_grafici.paziente;

import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.strumenti_utili.costanti.Costanti;
import com.example.mindharbor.strumenti_utili.PrenotaAppuntamentoSingleton;
import com.example.mindharbor.strumenti_utili.LabelDuration;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;



public class ControllerGraficoSelezionaDataEOra {
    @FXML
    private Label labelNomePaziente;
    @FXML
    private Label avanti;
    @FXML
    private Label home;
    @FXML
    private Label errore;
    @FXML
    private TextField orario;
    @FXML
    private DatePicker data;
    @FXML
    private Label infoOra;
    @FXML
    private Label infoData;

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoSelezionaDataEOra.class);
    private final NavigatorSingleton navigator = NavigatorSingleton.getInstance();
    private final PrenotaAppuntamento prenotaAppuntamentoController = PrenotaAppuntamentoSingleton.getInstance();
    private AppuntamentiBean appuntamentoBean;


    public void initialize() {
        InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();
        labelNomePaziente.setText(infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());

        regoleData();
        inserisciInfo();

        if((appuntamentoBean = prenotaAppuntamentoController.getRichiestaAppuntamento())!=null) {
            orario.setText(appuntamentoBean.getOra());
            this.data.setValue(LocalDate.parse(appuntamentoBean.getData()));
        }else {
            appuntamentoBean =new AppuntamentiBean();}
    }

    private void inserisciInfo(){
        Tooltip tooltipOra=new Tooltip("Le fasce orarie valide sono: " + prenotaAppuntamentoController.tooltipOra());
        tooltipOra.setStyle("-fx-background-color: white; -fx-text-fill: #22433a; -fx-font-size: 14px;");
        infoOra.setTooltip(tooltipOra);

        Tooltip tooltipData= new Tooltip(prenotaAppuntamentoController.tooltipData());
        tooltipData.setStyle("-fx-background-color: white; -fx-text-fill: #22433a; -fx-font-size: 14px;");
        infoData.setTooltip(tooltipData);

    }



    private void regoleData() {
        data.getEditor().setDisable(false);
        data.getEditor().setFocusTraversable(false);

        data.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                return data.getValue();
                /**
                 * Togliamo il parsing automatico.
                 * Il parsing verrà fatto SOLO dopo che l'utente avrà selezionato la Label "Avanti".
                 */
            }
        });


        data.valueProperty().addListener((observable, dataPrimaDellaModifica, nuovaData) -> {
            if (nuovaData != null) {
                data.getEditor().setText(nuovaData.toString());

                /**
                 * Queste listener si attiva ogni volta che viene modificato il campo data, sia manualmente che tramite il calendario.
                 * Mi prende il nuovo valore inserito facendo si che la modifica sia visibile subito; sia se quest'ultima avviene manualmente, sia tramite il calendario.
                 */

            }
        });

        data.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate data, boolean vuoto) {
                super.updateItem(data, vuoto);

                setDisable(dataPassataOnelWeekend(data));
            }
        });
    }

    private boolean dataPassataOnelWeekend(LocalDate data) {
        return data.isBefore(LocalDate.now()) || weekend(data);
    }

    private boolean weekend(LocalDate data) {
        DayOfWeek dayOfWeek = data.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }


    @FXML
    public void goToHome() {
        try {
            prenotaAppuntamentoController.eliminaRichiestaAppuntamento();
            Stage selezionaDataEOra = (Stage) home.getScene().getWindow();
            selezionaDataEOra.close();

            navigator.gotoPage("/com/example/mindharbor/HomePaziente.fxml");

        } catch (IOException e) {
            logger.error(Costanti.IMPOSSIBILE_CARICARE_INTERFACCIA, e);
        }
    }


    @FXML
    public void clickAvanti() {
        try {
            appuntamentoBean.setData(data.getEditor().getText().trim());
            appuntamentoBean.setOra(orario.getText());

            if(Boolean.FALSE.equals(prenotaAppuntamentoController.controlloDataEOra(appuntamentoBean))) {
                new LabelDuration().duration(errore,"Dati non validi");
            } else {
                prossimaInterfaccia(appuntamentoBean);
            }
        }catch (IllegalArgumentException e) {
            new LabelDuration().duration(errore, "Il formato deve essere: AAAA-MM-GG, HH:mm");
        }
    }

    private void prossimaInterfaccia(AppuntamentiBean appuntamento) {
        try {
            Stage selezionaDataEOra = (Stage) avanti.getScene().getWindow();
            selezionaDataEOra.close();

            prenotaAppuntamentoController.setRichiestaAppuntamento(appuntamento);

            navigator.gotoPage("/com/example/mindharbor/InserisciInfo.fxml");
        }catch (IOException e) {
            logger.info(Costanti.IMPOSSIBILE_CARICARE_INTERFACCIA, e);
        }
    }
}

