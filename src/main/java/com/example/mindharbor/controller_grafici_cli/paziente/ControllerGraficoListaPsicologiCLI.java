package com.example.mindharbor.controller_grafici_cli.paziente;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;
import com.example.mindharbor.controller_grafici_cli.AbsGestoreInput;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.PrenotaAppuntamentoSingleton;
import com.example.mindharbor.strumenti_utili.cli_helper.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.cli_helper.GestoreOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ControllerGraficoListaPsicologiCLI extends AbsGestoreInput {

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoListaPsicologiCLI.class);
    private final PrenotaAppuntamento prenotaAppuntamentoController = PrenotaAppuntamentoSingleton.getInstance();
    private final InfoUtenteBean infoUtenteBean = prenotaAppuntamentoController.getInfoUtente();
    private  List<PsicologoBean> psicologiBean=null;

    @Override
    public void start() {
        GestoreOutput.pulisciPagina();
        boolean esci = false;

        while (!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch (opzione) {
                    case 1 -> stampaListaPsicologi();
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
    public int mostraMenu() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "LISTA PSICOLOGI\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("NOME : " + infoUtenteBean.getNome() + "\n" + "COGNOME : " + infoUtenteBean.getCognome() + "\n");
        GestoreOutput.stampaMessaggio("1) Lista Psicologi");
        GestoreOutput.stampaMessaggio("2) Torna indietro");
        GestoreOutput.stampaMessaggio("3) Torna alla Home");

        return opzioneScelta(1, 3);
    }

    private void stampaListaPsicologi() {
        int indice = 0;
        if (psicologiBean == null) {
            psicologiBean = listaPsicologi();
        }
        for (PsicologoBean psicologoBean : psicologiBean) {
            GestoreOutput.stampaMessaggio(indice + ") NOME: " + psicologoBean.getNome() + ", COGNOME: " + psicologoBean.getCognome());
            indice++;
        }
        GestoreOutput.stampaMessaggio("Scegli uno psicologo digitando il suo numero di riferimento, oppure digita " + indice + " per tornare al menù");
        int opzione = opzioneScelta(0, indice);
        if (opzione == indice) {
            start();
        } else {
            prenotaAppuntamentoController.setPsicologoSelezionato(psicologiBean.get(opzione));
            new ControllerGraficoRichiediPrenotazioneCLI().start();

        }
    }

    private List<PsicologoBean> listaPsicologi() {
        List<PsicologoBean> listaPsicologiBean=new ArrayList<>();
        try {
            listaPsicologiBean = prenotaAppuntamentoController.getListaPsicologi();
            if(listaPsicologiBean.isEmpty()) {
                GestoreOutput.stampaMessaggio("Non esistono psicologi");
            }
        }catch (EccezioneDAO e) {
            logger.info("Errore nella ricerca dei psicologi", e);
        }
        return listaPsicologiBean;
    }

    private void tornaAllaHome() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "Sei sicuro di voler tornare alla Home?" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("Tornando indietro perderai tutti i tuoi progressi");
        GestoreOutput.stampaMessaggio("Digita 1 per confermare, 0 altrimenti");

        if (opzioneScelta(0, 1) == 0) {
            stampaListaPsicologi();
        } else {
            prenotaAppuntamentoController.eliminaRichiestaAppuntamento();
            new ControllerGraficoHomePazienteCLI().start();
        }
    }
    private void tornaIndietro() {
        new ControllerGraficoInserisciInfoCLI().start();
    }

}



