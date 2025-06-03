package com.example.mindharbor.controller_grafici.cli.psicologo;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.TerapiaBean;
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
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ControllerGraficoPrescriviTerapiaCLI extends AbsGestoreInput implements RicevitoreControllerApplicativo, RicevitoreParametri {

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoPrescriviTerapiaCLI.class);
    private List<TestBean> listaTest=null;
    private PrescriviTerapia prescriviTerapiaController;
    private PazienteBean pazienteSelezionato;


     @Override
    public void start() {
         GestoreOutput.pulisciPagina();

         modificaStatoNotifica();

         boolean esci = false;
         while(!esci) {
             int opzione;
             try {
                 opzione = mostraMenu();
                 switch(opzione) {
                     case 1 -> popolaListaTestSvoltiSenzaPrescrizione();
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
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "PRESCRIVI TERAPIA\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        GestoreOutput.stampaMessaggio("1) Prescrivi una terapia");
        GestoreOutput.stampaMessaggio("2) Torna indietro");
        GestoreOutput.stampaMessaggio("3) Torna alla Home");

        return opzioneScelta(1,3);
    }

    private void popolaListaTestSvoltiSenzaPrescrizione() {
        try {
            if(listaTest==null) {
                listaTest = prescriviTerapiaController.getTestSvoltiSenzaPrescrizione(pazienteSelezionato);
            }
            stampaTest();
        } catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei test svolti senza ancora prescrizioni ", e);
        }
    }

    private void stampaTest() {
         int indice=0;
         for(TestBean testBean : listaTest) {
             GestoreOutput.stampaMessaggio("NUMERO TEST: " + indice);
             GestoreOutput.stampaMessaggio("DATA : " + testBean.getData());
             GestoreOutput.stampaMessaggio("NOME TEST : " + testBean.getNomeTest());
             GestoreOutput.stampaMessaggio("RISULTATO : " + testBean.getRisultato());
             indice++;

             GestoreOutput.separatore();
         }

        GestoreOutput.stampaMessaggio("Scegli il test per fare una prescrizione digitando il suo numero di riferimento, oppure digita " + indice + " per tornare al menù");
        int opzione = opzioneScelta(0, indice);
        if (opzione == indice) {
            start();
        } else {
            prescrivi(opzione);
        }
    }

    private void prescrivi(int indiceTest) {
        Scanner input = new Scanner(System.in);
        GestoreOutput.stampaMessaggio("Scrivi la prescrizione oppure digita 1 per tornare alla lista dei test o digita 2 per tornare al menu");
        String prescrizione = input.nextLine();

        if(prescrizione.equals("1")) {
            stampaTest();
        }
        if(prescrizione.equals("2")) {
            start();
        }
        try {
            Date currentDate = new Date();
            prescriviTerapiaController.aggiungiNuovaTerapia(new TerapiaBean(pazienteSelezionato.getUsername(), prescrizione, currentDate, listaTest.get(indiceTest).getData()));
            GestoreOutput.stampaMessaggio("OPERAZIONE COMPLETATA: Terapia assegnata con successo");
        }catch (EccezioneDAO e) {
            logger.info("Errore durante il salvataggio della terapia ", e );
        }
        tornaIndietro();
    }


    private void modificaStatoNotifica() {
        try {
            prescriviTerapiaController.aggiornaStatoNotificaTest(pazienteSelezionato);
        } catch (EccezioneDAO e ) {
            logger.info("Errore durante la modifica dello stato dei test psicologici", e);
        }
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

    @Override
    public void setControllerApplicativo(Object controllerApplicativo){
        this.prescriviTerapiaController=(PrescriviTerapia) controllerApplicativo;
    }

    @Override
    public void setParametri(Object pazienteSelezionato){
        this.pazienteSelezionato=(PazienteBean)pazienteSelezionato;
    }

}
