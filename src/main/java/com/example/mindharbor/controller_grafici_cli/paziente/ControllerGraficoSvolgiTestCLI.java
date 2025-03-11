package com.example.mindharbor.controller_grafici_cli.paziente;

import com.example.mindharbor.beans.DomandeTestBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.RisultatiTestBean;
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
import java.util.ArrayList;
import java.util.List;

public class ControllerGraficoSvolgiTestCLI extends AbsGestoreInput {

    private final PrescriviTerapia prescriviTerapiaController = PrescriviTerapiaSingleton.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoSvolgiTestCLI.class);
    private final TestBean testSelezionato=prescriviTerapiaController.getTestSelezionato();


    @Override
    public void start(){
        GestoreOutput.pulisciPagina();
        boolean esci = false;

        while (!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch (opzione) {
                    case 1 -> svolgiTest();
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
        InfoUtenteBean infoUtenteBean = prescriviTerapiaController.getInfoUtente();

        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "SVOLGI TEST\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");

        GestoreOutput.stampaMessaggio("1) Inizia test");
        GestoreOutput.stampaMessaggio("2) Torna alla Home");

        return opzioneScelta(1, 2);
    }

    private void svolgiTest() {
        DomandeTestBean domandeTestBean = prescriviTerapiaController.cercaDomande(testSelezionato);
        List<Integer> punteggi=new ArrayList<>();
        DomandeTestBean punteggiBean=new DomandeTestBean();
        GestoreOutput.stampaMessaggio("Ripondi alle domande digitando il numero dell'emozione che meglio ti rappresenta per ciascuna domanda oppure digita 4 per tornare al menù\n\n");

        int domandaCorrente=0;

        while (domandaCorrente < domandeTestBean.getDomande().size()) {
            GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + domandeTestBean.getDomande().get(domandaCorrente) + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
            GestoreOutput.stampaMessaggio("1) " + CodiciAnsi.EMOJI_FELICE + "    2) " + CodiciAnsi.EMOJI_TRISTE + "    3) " + CodiciAnsi.EMOJI_ARRABBIATA);

            int opzione = opzioneScelta(1, 4);
            if (opzione == 4) {
                tornaAlMenu();
            } else {
                punteggi.add(domandeTestBean.getPunteggi().get(opzione - 1));
                domandaCorrente++;
                GestoreOutput.separatore();
            }
        }

        punteggiBean.setPunteggi(punteggi);
        calcolaRisultato(punteggiBean);
        tornaIndietro();
    }

    private void calcolaRisultato(DomandeTestBean punteggiBean) {
        try {
            RisultatiTestBean risultatoTest = prescriviTerapiaController.calcolaRisultato(punteggiBean, testSelezionato);
            if (risultatoTest.getRisultatoTestPrecedente() == null) {
                GestoreOutput.stampaMessaggio("Risultato test: " + risultatoTest.getRisultatoUltimoTest() + "\nComplimenti! Hai svolto il tuo primo test");

            } else {
                if (risultatoTest.getRisultatoTestPrecedente() > 0) {
                    GestoreOutput.stampaMessaggio("Progresso: " + risultatoTest.getRisultatoTestPrecedente() + "%" + "\nComplimenti!");
                } else if (risultatoTest.getRisultatoTestPrecedente()<0){
                    GestoreOutput.stampaMessaggio("Regresso: " + risultatoTest.getRisultatoTestPrecedente() + "%" +  "\nMi dispiace!");
                }else {
                    GestoreOutput.stampaMessaggio("Hai ottenuto lo stesso punteggio del tuo ultimo " + testSelezionato.getNomeTest() + "\nPunteggio ottenuto : " + risultatoTest.getRisultatoUltimoTest());
                }
            }

        }catch (EccezioneDAO e) {
            logger.info("Errore nel calcolo del risultato del test ", e);
        }
    }


    private void tornaAllaHome() {
        prescriviTerapiaController.eliminaTestSelezionato();
        new ControllerGraficoHomePazienteCLI().start();
    }

    private void tornaAlMenu() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "Sei sicuro di voler tornare al menu?" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("Tornando indietro perderai tutti i tuoi progressi");
        GestoreOutput.stampaMessaggio("Digita 1 per confermare, 0 altrimenti");

        if(opzioneScelta(0,1)==1) {
            start();
        }

    }

    private void tornaIndietro(){
        prescriviTerapiaController.eliminaTestSelezionato();
        new ControllerGraficoListaTestCLI().start();
    }

}
