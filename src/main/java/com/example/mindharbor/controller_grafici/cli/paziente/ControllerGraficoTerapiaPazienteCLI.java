package com.example.mindharbor.controller_grafici.cli.paziente;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.TerapiaBean;
import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.controller_grafici.cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.supporto_cli.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.supporto_cli.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ControllerGraficoTerapiaPazienteCLI extends AbsGestoreInput {

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoTerapiaPazienteCLI.class);
    private final PrescriviTerapia prescriviTerapiaController = new PrescriviTerapia();
    InfoUtenteBean infoUtenteBean = prescriviTerapiaController.getInfoUtente();

    @Override
    public void start() {

        GestoreOutput.pulisciPagina();

        boolean esci = false;
        while(!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                if (opzione == 1) {
                    esci = true;
                } else {
                    throw new EccezioneFormatoNonValido("Scelta non valida");
                }
            } catch (EccezioneFormatoNonValido e) {
                logger.info("Scelta non valida ", e);
            }
        }
        new ControllerGraficoHomePazienteCLI().start();
    }

    @Override
    public int mostraMenu() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "LISTA TERAPIE\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");

        if(prescriviTerapiaController.controllaEsistenzaPsicologo()) {
            getListaTerapie();
        } else {
            GestoreOutput.stampaMessaggio("Non hai ancora uno psicologo\n");
        }

        GestoreOutput.stampaMessaggio("1) Torna alla Home");

        return opzioneScelta(1,1);
    }

    private void getListaTerapie() {
        try {
            List<TerapiaBean> terapieBean= prescriviTerapiaController.trovaTerapie();
            if (terapieBean.isEmpty()) {
                GestoreOutput.stampaMessaggio("Non ci sono terapie");
            } else {
                popolaListaTerapia(terapieBean);
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore nel caricamento della Lista delle Terapie ", e );
        }
    }


    private void popolaListaTerapia(List<TerapiaBean> terapiaBean) {
        for (TerapiaBean terapia : terapiaBean) {
            GestoreOutput.stampaMessaggio("DATA : " + terapia.getDataTerapia());
            GestoreOutput.stampaMessaggio("PRESCRIZIONE : \n" + terapia.getTerapia());
            GestoreOutput.separatore();
        }
    }
}
