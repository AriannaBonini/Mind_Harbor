package com.example.mindharbor.controller_grafici_cli.paziente;

import com.example.mindharbor.beans.InfoUtenteBean;
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

public class ControllerGraficoListaTestCLI extends AbsGestoreInput {

    private final PrescriviTerapia prescriviTerapiaController = PrescriviTerapiaSingleton.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoListaTestCLI.class);
    private InfoUtenteBean infoUtenteBean;
    private List<TestBean> listaTest=null;

    @Override
    public void start() {
        GestoreOutput.pulisciPagina();
        boolean esci = false;

        while (!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch (opzione) {
                    case 1 -> trovaPsicologo();
                    case 2 -> esci = true;
                    default -> throw new EccezioneFormatoNonValido("Scelta non valida");
                }

            } catch (EccezioneFormatoNonValido e) {
                logger.info("Scelta non valida ", e);
            }
        }
        tornaAllaHome();
    }

    @Override
    public int mostraMenu() {
        infoUtenteBean = prescriviTerapiaController.getInfoUtente();

        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "LISTA TEST\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");

        GestoreOutput.stampaMessaggio("1) Lista test");
        GestoreOutput.stampaMessaggio("2) Torna alla Home");

        return opzioneScelta(1, 2);
    }

    private void trovaPsicologo() {
        try {
            infoUtenteBean = prescriviTerapiaController.infoPsicologo();

            if (infoUtenteBean != null) {
                GestoreOutput.stampaMessaggio("PSICOLOGO : " + infoUtenteBean.getNome() + " " + infoUtenteBean.getCognome());
                GestoreOutput.stampaMessaggio("\n\n");
                popolaLista();
            }else {
                GestoreOutput.stampaMessaggio("Non hai ancora uno psicologo");
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
        if(listaTest==null) {
            listaTest = prescriviTerapiaController.getListaTestAssegnati();
        }
        if (listaTest.isEmpty()) {
            GestoreOutput.stampaMessaggio("Non hai test assegnati");
        } else {
            stampaListaTest();
        }
    }

    private void stampaListaTest() {
        int indice=0;
        for(TestBean testBean : listaTest) {

            if(testBean.getSvolto()==1) {
                GestoreOutput.stampaMessaggio(" " +CodiciAnsi.ANSI_QUADRATO_VERDE_CON_TICCHETTA + " INDICE TEST : " + indice);
            } else {
                GestoreOutput.stampaMessaggio(" " + CodiciAnsi.ANSI_QUADRATO_VERDE + " INDICE TEST : " + indice);
            }
            GestoreOutput.stampaMessaggio("NOME TEST : " + testBean.getNomeTest());
            GestoreOutput.stampaMessaggio("DATA TEST : " + testBean.getData());

            if(testBean.getRisultato()==null) {
                GestoreOutput.stampaMessaggio("RISULTATO TEST : non presente");
            }else {
                GestoreOutput.stampaMessaggio("RISULTATO TEST : " + testBean.getRisultato());
            }
            GestoreOutput.separatore();
            indice++;
        }
        scegliTest(indice);
    }

    private void scegliTest(int indice) {
        int opzione;

        do {
            GestoreOutput.stampaMessaggio("Scegli un test digitando il suo numero di riferimento, oppure digita " + indice + " per tornare al menù");
            opzione = opzioneScelta(0, indice);

            if (opzione == indice) {
                start();
            }

            if (listaTest.get(opzione).getSvolto() == 1) {
                GestoreOutput.stampaMessaggio("Test già effettuato. Scegli un altro test.");
            }
        } while (listaTest.get(opzione).getSvolto() == 1);

        prescriviTerapiaController.setTestSelezionato(listaTest.get(opzione));
        new ControllerGraficoSvolgiTestCLI().start();
    }

    private void tornaAllaHome() {
        modificaStatoNotifica();
        new ControllerGraficoHomePazienteCLI().start();
    }
}
