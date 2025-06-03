package com.example.mindharbor.controller_grafici.cli.psicologo;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.TestBean;
import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreControllerApplicativo;
import com.example.mindharbor.controller_grafici.interfacce.RicevitoreParametri;
import com.example.mindharbor.controller_grafici.cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.supporto_cli.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.supporto_cli.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ControllerGraficoScegliTestCLI extends AbsGestoreInput implements RicevitoreControllerApplicativo, RicevitoreParametri {
    private PazienteBean pazienteSelezionato;
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoScegliTestCLI.class);
    private PrescriviTerapia prescriviTerapiaController;

    private InfoUtenteBean infoUtenteBean;


    @Override
    public void start(){
        GestoreOutput.pulisciPagina();

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
        new ControllerGraficoHomePsicologoCLI().start();
    }
    private void tornaIndietro() {
        ControllerGraficoSchedaPersonalePazienteCLI controllerGraficoSchedaPersonalePazienteCLI=new ControllerGraficoSchedaPersonalePazienteCLI();
        controllerGraficoSchedaPersonalePazienteCLI.setControllerApplicativo(prescriviTerapiaController);
        controllerGraficoSchedaPersonalePazienteCLI.setParametri(pazienteSelezionato);
        controllerGraficoSchedaPersonalePazienteCLI.start();
    }

    private void ottieniListaTest() {
        List<TestBean> listaTestPsicologiciBean = prescriviTerapiaController.getListaTest();

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
                TestBean testBeanSelezionato= new TestBean(listaTestPsicologiciBean.get(opzione).getNomeTest());
                testBeanSelezionato.setPaziente(pazienteSelezionato.getUsername());

                prescriviTerapiaController.assegnaNuovoTest(testBeanSelezionato);
                GestoreOutput.stampaMessaggio("Test assegnato con successo");
                tornaIndietro();
            } catch (EccezioneDAO e) {
                logger.error("Errore nell'assegnazione del test", e);
                GestoreOutput.stampaMessaggio("Errore nell'assegnazione del test");
            }
        }
    }

    @Override
    public void setControllerApplicativo(Object controllerApplicativo){
        this.prescriviTerapiaController=(PrescriviTerapia) controllerApplicativo;
        infoUtenteBean=prescriviTerapiaController.getInfoUtente();
    }

    @Override
    public void setParametri(Object pazienteSelezionato){
        this.pazienteSelezionato=(PazienteBean)pazienteSelezionato;
    }
}
