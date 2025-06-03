package com.example.mindharbor.controller_grafici.cli.psicologo;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.controller_grafici.cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.supporto_cli.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.supporto_cli.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ControllerGraficoListaPazientiCLI extends AbsGestoreInput {

    private final PrescriviTerapia prescriviTerapiaController = new PrescriviTerapia();
    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoListaPazientiCLI.class);
    private List<PazienteBean> listaPazienti=null;


    @Override
    public void start() {
        GestoreOutput.pulisciPagina();

        boolean esci = false;
        while(!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch(opzione) {
                    case 1 -> listaPazienti();
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
        InfoUtenteBean infoUtenteBean = prescriviTerapiaController.getInfoUtente();
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "LISTA PAZIENTI\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio(GestoreOutput.NOME + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        GestoreOutput.stampaMessaggio("1) Lista pazienti");
        GestoreOutput.stampaMessaggio("2) Torna alla Home");

        return opzioneScelta(1,2);
    }

    private void listaPazienti() {
        try {
        if(listaPazienti==null) {
            listaPazienti = prescriviTerapiaController.getListaPazienti();
        }
            stampaLista();
        }catch (EccezioneDAO e) {
            logger.info("Non non ci sono pazienti", e);
        }
    }

    private void stampaLista() {
        int indice=0;
        for (PazienteBean pazienteBean : listaPazienti) {
            if (pazienteBean.getNumTestSvolti()>0) {
                GestoreOutput.stampaMessaggio(GestoreOutput.messaggioConNotifica( "NUMERO PAZIENTE : " + indice, true));
                GestoreOutput.stampaMessaggio(GestoreOutput.messaggioConNotifica(GestoreOutput.NOME + pazienteBean.getNome(), true));
                GestoreOutput.stampaMessaggio(GestoreOutput.messaggioConNotifica("COGNOME : " + pazienteBean.getCognome(), true));
            } else {
                GestoreOutput.stampaMessaggio("NUMERO PAZIENTE : " +indice);
                GestoreOutput.stampaMessaggio(GestoreOutput.NOME + pazienteBean.getNome());
                GestoreOutput.stampaMessaggio("COGNOME : " + pazienteBean.getCognome());
            }

            GestoreOutput.separatore();
            indice++;
        }
        GestoreOutput.stampaMessaggio("Scegli un paziente digitando il suo numero di riferimento, oppure digita " + indice + " per tornare al menù");
        int opzione = opzioneScelta(0, indice);
        if (opzione == indice) {
            start();
        } else {
            ControllerGraficoSchedaPersonalePazienteCLI controllerGraficoSchedaPersonalePazienteCLI= new ControllerGraficoSchedaPersonalePazienteCLI();
            controllerGraficoSchedaPersonalePazienteCLI.setControllerApplicativo(prescriviTerapiaController);
            controllerGraficoSchedaPersonalePazienteCLI.setParametri(listaPazienti.get(opzione));
            controllerGraficoSchedaPersonalePazienteCLI.start();
        }
    }
    private void tornaAllaHome() {new ControllerGraficoHomePsicologoCLI().start();}
}
