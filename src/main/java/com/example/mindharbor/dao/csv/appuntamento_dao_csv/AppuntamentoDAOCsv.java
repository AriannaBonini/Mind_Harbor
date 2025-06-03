package com.example.mindharbor.dao.csv.appuntamento_dao_csv;

import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.strumenti_utili.costanti.CostantiLetturaScrittura;
import com.example.mindharbor.strumenti_utili.UtilitiesCSV;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AppuntamentoDAOCsv implements AppuntamentoDAO {

    @Override
    public List<Appuntamento> trovaAppuntamentiPaziente(Utente paziente, String selectedTabName) throws EccezioneDAO {
        List<Appuntamento> appuntamentoPsicologoList;
        LocalDate dataCorrente = LocalDate.now();

        appuntamentoPsicologoList = leggiAppuntamentiDaCsv(paziente, selectedTabName, dataCorrente, false);
        return appuntamentoPsicologoList;
    }

    @Override
    public List<Appuntamento> trovaAppuntamentiPsicologo(Utente psicologo, String selectedTabName) throws EccezioneDAO {
        List<Appuntamento> appuntamentoPsicologoList;
        LocalDate dataCorrente = LocalDate.now();

        appuntamentoPsicologoList = leggiAppuntamentiDaCsv(psicologo, selectedTabName, dataCorrente, true);
        return appuntamentoPsicologoList;
    }
    private List<Appuntamento> leggiAppuntamentiDaCsv(Utente utente, String tabSelezionato, LocalDate dataCorrente, boolean tipo) throws EccezioneDAO {
        List<Appuntamento> appuntamenti = new ArrayList<>();

        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        for (String[] colonne : righe) {

            if (Objects.equals(colonne[CostantiAppuntamentoCsv.INDICE_STATO_APPUNTAMENTO], CostantiAppuntamentoCsv.APPUNTAMENTO_ACCETTATO) && tipoUtente(utente, colonne, tipo)) {
                LocalDate dataAppuntamento = LocalDate.parse(colonne[CostantiAppuntamentoCsv.INDICE_DATA]);

                if (appuntamentoValido(dataAppuntamento, dataCorrente, tabSelezionato)) {
                    Appuntamento appuntamento = creaAppuntamento(colonne, tipo);
                    appuntamenti.add(appuntamento);
                }
            }
        }

        return appuntamenti;
    }

    /**
     * Verifica se l'utente specificato corrisponde al paziente o allo psicologo nel record CSV fornito.
     * <p>
     * Questo metodo controlla se l'username dell'utente corrisponde a quello del paziente o dello psicologo
     * all'interno del record CSV, in base al tipo di utente specificato. Se il parametro `tipo` è `true`,
     * il metodo confronta l'username dello psicologo; se `false`, confronta l'username del paziente.
     * </p>
     *
     * @param utente   L'oggetto {@link Utente} di cui si vuole verificare la corrispondenza dell'username.
     * @param colonne Un array di stringhe contenente i valori delle colonne di un record CSV.
     * @param tipo   Un booleano che indica il tipo di utente. Se `true`, confronta lo psicologo; se `false`, confronta il paziente.
     * @return `true` se l'username dell'utente corrisponde a quello specificato nel CSV, `false` altrimenti.
     */
    private boolean tipoUtente(Utente utente, String[] colonne, boolean tipo) {
        String usernamePaziente = colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PAZIENTE];
        String usernamePsicologo = colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PSICOLOGO];
        return tipo ? usernamePsicologo.equals(utente.getUsername()) : usernamePaziente.equals(utente.getUsername());
    }

    private boolean appuntamentoValido(LocalDate dataAppuntamento, LocalDate dataCorrente, String selectedTabName) {
        return (selectedTabName.equals(UtilitiesCSV.IN_PROGRAMMA) && dataAppuntamento.isAfter(dataCorrente)) ||
                (selectedTabName.equals(UtilitiesCSV.PASSATI) && dataAppuntamento.isBefore(dataCorrente));
    }

    private Appuntamento creaAppuntamento(String[] colonne, boolean tipo) {
        String data = colonne[CostantiAppuntamentoCsv.INDICE_DATA];
        String ora = colonne[CostantiAppuntamentoCsv.INDICE_ORA];
        if (tipo) {
            String usernamePaziente = colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PAZIENTE];
            return new Appuntamento(data, ora, new Paziente(usernamePaziente));
        } else {
            return new Appuntamento(data, ora);
        }
    }

    @Override
    public void insertRichiestaAppuntamento(Appuntamento appuntamento) throws EccezioneDAO {

        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);

        String[] nuovoRecord = new String[] {
                String.valueOf(calcolaIDAppuntamento()),
                appuntamento.getData(),
                appuntamento.getOra(),
                appuntamento.getPaziente().getUsername(),
                appuntamento.getPsicologo().getUsername(),
                CostantiAppuntamentoCsv.RICHIESTA_IN_ATTESA,
                CostantiAppuntamentoCsv.NOTIFICA_PSICOLOGO_ATTIVA,
                CostantiAppuntamentoCsv.NOTIFICA_PAZIENTE_NON_ATTIVA
        };

        righe.add(nuovoRecord);

        UtilitiesCSV.scriviRigheAggiornate(CostantiAppuntamentoCsv.FILE_PATH, righe);
    }

    private Integer calcolaIDAppuntamento() throws EccezioneDAO {
        int maxId = 0;
        try {
            List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);
            for (String[] riga : righe) {
                int id = Integer.parseInt(riga[CostantiAppuntamentoCsv.INDICE_ID_APPUNTAMENTO]);
                if (id > maxId) {
                    maxId = id;
                }
            }
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return maxId + 1;
    }

    @Override
    public Appuntamento notificheNuoviAppuntamentiPaziente(Paziente paziente) throws EccezioneDAO {
        Appuntamento appuntamento = new Appuntamento();
        int notifiche = 0;

        try {
            List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

            for (String[] colonne : righe) {
                if (colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PAZIENTE].equals(paziente.getUsername()) &&
                        colonne[CostantiAppuntamentoCsv.INDICE_STATO_NOTIFICA_PAZIENTE].equals(CostantiAppuntamentoCsv.NOTIFICA_PAZIENTE_DA_CONSEGNARE)) {
                    notifiche++;
                }
            }

            if (notifiche > 0) {
                appuntamento.setStatoNotificaPaziente(notifiche);
            }

        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante la lettura delle notifiche da CSV per il paziente: " + e.getMessage());
        }

        return appuntamento;
    }

    @Override
    public Appuntamento notificheNuoviAppuntamentiPsicologo(Psicologo psicologo) throws EccezioneDAO {
        Appuntamento appuntamento = new Appuntamento();
        int notifiche = 0;

        try {
            List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

            for (String[] colonne : righe) {
                if (colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PSICOLOGO].equals(psicologo.getUsername()) &&
                        colonne[CostantiAppuntamentoCsv.INDICE_STATO_NOTIFICA_PSICOLOGO].equals(CostantiAppuntamentoCsv.NOTIFICA_PSICOLOGO_DA_CONSEGNARE)) {
                    notifiche++;
                }
            }

            if (notifiche > 0) {
                appuntamento.setStatoNotificaPsicologo(notifiche);
            }

        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante la lettura delle notifiche da CSV per lo psicologo: " + e.getMessage());
        }

        return appuntamento;
    }



    @Override
    public List<Appuntamento> trovaRichiesteAppuntamento(Psicologo psicologo) throws EccezioneDAO {
            List<Appuntamento> richiesteAppuntamento = new ArrayList<>();

            List<String[]> risultati = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

            for(String[] colonne: risultati) {
                if (colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PSICOLOGO].equals(psicologo.getUsername()) && Integer.parseInt(colonne[CostantiAppuntamentoCsv.INDICE_STATO_APPUNTAMENTO]) == CostantiAppuntamentoCsv.APPUNTAMENTO_NON_ACCETTATO) {
                    Appuntamento richiesta = new Appuntamento(
                            Integer.parseInt(colonne[CostantiAppuntamentoCsv.INDICE_ID_APPUNTAMENTO]),
                            new Paziente(colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PAZIENTE]),
                            Integer.valueOf(colonne[CostantiAppuntamentoCsv.INDICE_STATO_NOTIFICA_PSICOLOGO])
                    );
                    richiesteAppuntamento.add(richiesta);
                }
            }

            return richiesteAppuntamento;
    }

    @Override
    public void aggiornaStatoNotifica(Appuntamento richiestaAppuntamento) throws EccezioneDAO {
        List<String[]> risultati = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);
        List<String[]> recordAggiornati = new ArrayList<>();
        boolean saltaIntestazione=true;

        for(String[] colonne: risultati) {
            if (saltaIntestazione) {
                // Aggiungi l'intestazione senza fare il controllo nell'if
                recordAggiornati.add(colonne);
                saltaIntestazione = false;
                continue;
            }
            if (Integer.parseInt(colonne[CostantiAppuntamentoCsv.INDICE_ID_APPUNTAMENTO]) == richiestaAppuntamento.getIdAppuntamento()) {
                colonne[CostantiAppuntamentoCsv.INDICE_STATO_NOTIFICA_PSICOLOGO] = CostantiAppuntamentoCsv.NOTIFICA_PSICOLOGO_CONSEGNATA;
            }
            recordAggiornati.add(colonne);
        }

        UtilitiesCSV.scriviRigheAggiornate(CostantiAppuntamentoCsv.FILE_PATH, recordAggiornati);
    }

    @Override
    public Appuntamento getInfoRichiesta(Appuntamento richiestaAppuntamento) throws EccezioneDAO {
        Appuntamento richiesta = null;

        List<String[]> recordLetti = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        for(String[] colonne : recordLetti) {
            if(colonne[CostantiAppuntamentoCsv.INDICE_ID_APPUNTAMENTO].equals(String.valueOf(richiestaAppuntamento.getIdAppuntamento()))) {
                String data = colonne[CostantiAppuntamentoCsv.INDICE_DATA];
                String ora = colonne[CostantiAppuntamentoCsv.INDICE_ORA];
                richiesta = new Appuntamento(data, ora);
                break;
            }
        }

        return richiesta;
    }

    @Override
    public void accettaRichiesta(Appuntamento appuntamento) throws EccezioneDAO {
        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);
        List<String[]> righeAggiornate = new ArrayList<>();
        boolean saltaIntestazione=true;

        for (String[] colonne : righe) {
            if (saltaIntestazione) {
                righeAggiornate.add(colonne);
                saltaIntestazione = false;
                continue;
            }

            if (Integer.parseInt(colonne[CostantiAppuntamentoCsv.INDICE_ID_APPUNTAMENTO]) == appuntamento.getIdAppuntamento()) {
                colonne[CostantiAppuntamentoCsv.INDICE_STATO_APPUNTAMENTO] = CostantiAppuntamentoCsv.APPUNTAMENTO_ACCETTATO; // Stato appuntamento accettato
                colonne[CostantiAppuntamentoCsv.INDICE_STATO_NOTIFICA_PAZIENTE] = CostantiAppuntamentoCsv.NOTIFICA_PAZIENTE_DA_CONSEGNARE; // Notifica paziente aggiornata
            }

            righeAggiornate.add(colonne);
        }

        UtilitiesCSV.scriviRigheAggiornate(CostantiAppuntamentoCsv.FILE_PATH, righeAggiornate);

    }

    @Override
    public void eliminaRichiesteDiAppuntamentoPerAltriPsicologi(Appuntamento appuntamento) throws EccezioneDAO {
        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);


        List<String[]> righeAggiornate = righe.stream().filter(colonne ->
                !colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PAZIENTE].equals(appuntamento.getPaziente().getUsername())
                        || colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PSICOLOGO].equals(appuntamento.getPsicologo().getUsername())
        ).toList();

        UtilitiesCSV.scriviRigheAggiornate(CostantiAppuntamentoCsv.FILE_PATH, righeAggiornate);
    }

    @Override
    public void eliminaRichiesta(Appuntamento appuntamento) throws EccezioneDAO {

        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);
        List<String[]> righeAggiornate = new ArrayList<>();
        boolean saltaIntestazione=true;

        for (String[] colonne : righe) {
            if (saltaIntestazione) {

                righeAggiornate.add(colonne);
                saltaIntestazione = false;
                continue;
            }

            if(Integer.parseInt(colonne[CostantiAppuntamentoCsv.INDICE_ID_APPUNTAMENTO]) != appuntamento.getIdAppuntamento()) {
                righeAggiornate.add(colonne);
            }
        }
        UtilitiesCSV.scriviRigheAggiornate(CostantiAppuntamentoCsv.FILE_PATH, righeAggiornate);
    }

    @Override
    public boolean getDisp(Integer idAppuntamento, Psicologo psicologo) throws EccezioneDAO {

        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        for (String[] colonne : righe) {

            if (Integer.parseInt(colonne[CostantiAppuntamentoCsv.INDICE_ID_APPUNTAMENTO]) == idAppuntamento && colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PSICOLOGO].equals(psicologo.getUsername())) {

                boolean conflitto = righe.stream()
                        .anyMatch(innerColonne ->
                                        Objects.equals(innerColonne[CostantiAppuntamentoCsv.INDICE_STATO_APPUNTAMENTO], CostantiAppuntamentoCsv.APPUNTAMENTO_ACCETTATO) &&
                                        innerColonne[CostantiAppuntamentoCsv.INDICE_DATA].equals(colonne[CostantiAppuntamentoCsv.INDICE_DATA]) &&
                                        innerColonne[CostantiAppuntamentoCsv.INDICE_ORA].equals(colonne[CostantiAppuntamentoCsv.INDICE_ORA]) &&
                                        innerColonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PSICOLOGO].equals(colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PSICOLOGO]));

                return !conflitto;
            }
        }

        return false;
    }

    @Override
    public void aggiornaStatoNotificaPaziente(Utente utente) throws EccezioneDAO {

        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);

        List<String[]> righeAggiornate = new ArrayList<>();
        for (String[] colonne : righe) {
            if (colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PAZIENTE].equals(utente.getUsername()) && colonne[CostantiAppuntamentoCsv.INDICE_STATO_NOTIFICA_PAZIENTE].equals(CostantiAppuntamentoCsv.NOTIFICA_PAZIENTE_DA_CONSEGNARE)) {
                colonne[CostantiAppuntamentoCsv.INDICE_STATO_NOTIFICA_PAZIENTE] = CostantiAppuntamentoCsv.NOTIFICA_PAZIENTE_CONSEGNATA; // Aggiorna lo stato di notifica del paziente a 0
            }
            righeAggiornate.add(colonne);
        }

        UtilitiesCSV.scriviRigheAggiornate(CostantiAppuntamentoCsv.FILE_PATH, righeAggiornate);
    }
}
