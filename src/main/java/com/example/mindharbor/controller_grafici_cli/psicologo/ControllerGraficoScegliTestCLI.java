package com.example.mindharbor.controller_grafici_cli.psicologo;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.TestBean;
import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.controller_grafici_cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.PrescriviTerapiaSingleton;
import com.example.mindharbor.strumenti_utili.cli_helper.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.cli_helper.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ControllerGraficoScegliTestCLI extends AbsGestoreInput {
    private PazienteBean pazienteSelezionato;
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoScegliTestCLI.class);
    private List<TestBean> listaTestPsicologiciBean=null;
    private final PrescriviTerapia prescriviTerapiaController = PrescriviTerapiaSingleton.getInstance();

    private final InfoUtenteBean infoUtenteBean = prescriviTerapiaController.getInfoUtente();


    @Override
    public void start(){
        GestoreOutput.pulisciPagina();
        pazienteSelezionato= prescriviTerapiaController.getPazienteSelezionato();

        boolean esci = false;
        while(!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch(opzione) {
                    case 1 -> ottieniListaTest();
                    case 2 -> tornaIndietro();
                    case 3 -> esci = true;
                    default -> throw new EccezioneFormatoNonValido("Scelta non valida");
                }
            } catch (EccezioneFormatoNonValido e) {
                logger.info("Scelta non valida ", e);
            }
        }
        tornaAllaHome();
    }

    @Override
    public int mostraMenu(){
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "SCEGLI TEST\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");

        GestoreOutput.stampaMessaggio("\n\n");
        GestoreOutput.stampaMessaggio("NOME PAZIENTE : " + pazienteSelezionato.getNome());
        GestoreOutput.stampaMessaggio("COGNOME PAZIENTE : " + pazienteSelezionato.getCognome());
        GestoreOutput.stampaMessaggio("ANNI PAZIENTE : " + pazienteSelezionato.getAnni());
        GestoreOutput.stampaMessaggio("\n\n");

        GestoreOutput.stampaMessaggio("1) Visualizza la lista dei test");
        GestoreOutput.stampaMessaggio("2) Torna indietro");
        GestoreOutput.stampaMessaggio("3) Torna alla Home");

        return opzioneScelta(1,3);
    }

    private void tornaAllaHome() {
        prescriviTerapiaController.eliminaPazienteSelezionato();
        new ControllerGraficoHomePsicologoCLI().start();
    }
    private void tornaIndietro() {new ControllerGraficoSchedaPersonalePazienteCLI().start();}

    private void ottieniListaTest() {
        if(listaTestPsicologiciBean==null) {
            listaTestPsicologiciBean = prescriviTerapiaController.getListaTest();
        }

        int indice=0;
        for(TestBean test : listaTestPsicologiciBean) {
            GestoreOutput.stampaMessaggio(indice + ") " + test.getNomeTest());
            indice++;
        }
        GestoreOutput.stampaMessaggio("Scegli un test digitando il suo numero di riferimento,oppure digita " + indice + " per tornare al menu");
        int opzione = opzioneScelta(0, indice);
        if (opzione == indice) {
            start();
        } else {
            try {
                prescriviTerapiaController.notificaTest(listaTestPsicologiciBean.get(opzione).getNomeTest());
                GestoreOutput.stampaMessaggio("Test assegnato con successo");
                tornaIndietro();
            } catch (EccezioneDAO e) {
                logger.error("Errore nell'assegnazione del test", e);
                GestoreOutput.stampaMessaggio("Errore nell'assegnazione del test");
            }
        }


    }
}
