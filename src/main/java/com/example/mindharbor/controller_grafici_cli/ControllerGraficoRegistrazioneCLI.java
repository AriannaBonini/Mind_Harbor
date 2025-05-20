package com.example.mindharbor.controller_grafici_cli;

import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.controller_applicativi.RegistrazioneController;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneFormatoNonValido;
import com.example.mindharbor.strumenti_utili.cli_helper.CodiciAnsi;
import com.example.mindharbor.strumenti_utili.cli_helper.GestoreOutput;
import com.example.mindharbor.tipo_utente.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ControllerGraficoRegistrazioneCLI extends AbsGestoreInput{

    private static final Logger logger = LoggerFactory.getLogger(ControllerGraficoRegistrazioneCLI.class);
    private final Scanner scanner = new Scanner(System.in);
    private final RegistrazioneController registrazioneController= new RegistrazioneController();

    @Override
    public void start() {
        GestoreOutput.pulisciPagina();

        boolean esci = false;
        while(!esci) {
            int opzione;
            try {
                opzione = mostraMenu();
                switch (opzione) {
                    case 1 -> datiGenericiUtente(UserType.PAZIENTE.getId());
                    case 2 -> datiGenericiUtente(UserType.PSICOLOGO.getId());
                    case 3 -> esci = true;
                    default -> throw new EccezioneFormatoNonValido("Scelta non valida");
                }
            } catch (EccezioneFormatoNonValido e) {
                logger.info("Scelta non valida ", e);
            }
        }
        tornaAlLogin();
    }


    @Override
    public int mostraMenu() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "*** REGISTRATI! ***\n" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("1) Registrati come paziente");
        GestoreOutput.stampaMessaggio("2) Registrati come psicologo");
        GestoreOutput.stampaMessaggio("3) Torna alla schermata del Login");

        return opzioneScelta(1,3);
    }



    private void datiGenericiUtente(String tipoUtente) {
        String nome;
        String cognome;
        String username;
        String password;
        String sesso;

        GestoreOutput.stampaMessaggio("Inserisci il tuo nome o scrivi LOGIN per tornare alla schermata del Login: ");
        nome = scanner.nextLine();

        controlloInserimento(nome);

        GestoreOutput.stampaMessaggio("Inserisci il tuo cognome o scrivi LOGIN per tornare alla schermata del Login: ");
        cognome = scanner.nextLine();

        controlloInserimento(cognome);

        GestoreOutput.stampaMessaggio("Inserisci il tuo username o scrivi LOGIN per tornare alla schermata del Login: ");
        username = scanner.nextLine();

        controlloInserimento(username);

        GestoreOutput.stampaMessaggio("Inserisci la tua password o scrivi LOGIN per tornare alla schermata del Login: ");
        password = scanner.nextLine();

        controlloInserimento(password);

        GestoreOutput.stampaMessaggio("Digita M o F, a seconda che tu sia rispettivamente maschio o femmina, oppure scrivi LOGIN per tornare alla schermata del Login: ");
        sesso = scanner.nextLine();

        controlloInserimento(sesso);

        try {
            if (tipoUtente.equals("Paziente")) {
                String anni;
                GestoreOutput.stampaMessaggio("Inserisci la tua età o scrivi LOGIN per tornare alla schermata del Login: ");
                anni = scanner.nextLine();
                controlloInserimento(anni);

                PazienteBean pazienteBean = new PazienteBean(nome, cognome, sesso, username, anni, password);
                registrazioneController.registrazionePaziente(pazienteBean);

            } else if (tipoUtente.equals("Psicologo")) {
                String nomeStudio;
                String costoOrario;

                GestoreOutput.stampaMessaggio("Inserisci il nome del tuo studio o scrivi LOGIN per tornare alla schermata del Login: ");
                nomeStudio = scanner.nextLine();
                controlloInserimento(nomeStudio);

                GestoreOutput.stampaMessaggio("Inserisci la tua tariffa oraria o scrivi LOGIN per tornare alla schermata del Login: ");
                costoOrario = scanner.nextLine();
                controlloInserimento(costoOrario);

                PsicologoBean psicologoBean = new PsicologoBean(username, nome, cognome, costoOrario, nomeStudio, sesso, password);
                registrazioneController.registrazionePsicologo(psicologoBean);
            }
            new ControllerGraficoLoginCLI().start();
        } catch (IllegalArgumentException e) {
            logger.info("Errore durante la compilazione dei campi ");
        } catch (EccezioneDAO e) {
            logger.info("Errore durante la verifica dello username ");
        }
    }

    private void controlloInserimento(String stringa) {
        if (stringa.equalsIgnoreCase("LOGIN")) {
            tornaAlLogin();
        }
    }

    private void tornaAlLogin() {
        GestoreOutput.stampaMessaggio(CodiciAnsi.ANSI_GRASSETTO + "Sei sicuro di voler tornare all'interfaccia del Login?" + CodiciAnsi.ANSI_RIPRISTINA_GRASSETTO);
        GestoreOutput.stampaMessaggio("Tornando indietro perderai tutti i tuoi progressi");
        GestoreOutput.stampaMessaggio("Digita 1 per confermare, 0 altrimenti");

        if (opzioneScelta(0, 1) == 1) {
            new ControllerGraficoLoginCLI().start();
        }
    }

}
