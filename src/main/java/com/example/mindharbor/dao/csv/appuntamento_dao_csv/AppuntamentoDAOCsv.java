package com.example.mindharbor.dao.csv.appuntamento_dao_csv;

import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.enumerazioni.TipoAppuntamento;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.strumenti_utili.costanti.CostantiLetturaScrittura;
import com.example.mindharbor.strumenti_utili.UtilitiesCSV;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AppuntamentoDAOCsv implements AppuntamentoDAO {

    @Override
    public List<Appuntamento> trovaAppuntamentiPaziente(Appuntamento appuntamento) throws EccezioneDAO {
        List<Appuntamento> listaAppuntamentiPaziente = new ArrayList<>();
        LocalDate dataCorrente = LocalDate.now();
        String usernamePaziente = appuntamento.getPaziente().getUsername();
        boolean cercaFuturi = appuntamento.getTipoAppuntamento() == TipoAppuntamento.IN_PROGRAMMA;

        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(
                CostantiAppuntamentoCsv.FILE_PATH,
                CostantiLetturaScrittura.SOLO_LETTURA);

        for (String[] colonne : righe) {
            String username = colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PAZIENTE];
            LocalDate dataAppuntamento = LocalDate.parse(colonne[CostantiAppuntamentoCsv.INDICE_DATA]);

            boolean isAccettato = Objects.equals(colonne[CostantiAppuntamentoCsv.INDICE_STATO_APPUNTAMENTO], CostantiAppuntamentoCsv.APPUNTAMENTO_ACCETTATO);
            boolean isStessoPaziente = Objects.equals(username, usernamePaziente);
            boolean isDataValida = cercaFuturi ? dataAppuntamento.isAfter(dataCorrente) : dataAppuntamento.isBefore(dataCorrente);

            if (isAccettato && isStessoPaziente && isDataValida) {
                String ora = colonne[CostantiAppuntamentoCsv.INDICE_ORA];
                listaAppuntamentiPaziente.add(new Appuntamento(dataAppuntamento, LocalTime.parse(ora)));
            }
        }

        return listaAppuntamentiPaziente;
    }

    @Override
    public List<Appuntamento> trovaAppuntamentiPsicologo(Appuntamento appuntamento) throws EccezioneDAO {
        List<Appuntamento> listaAppuntamentiPsicologo = new ArrayList<>();
        LocalDate dataCorrente = LocalDate.now();
        String usernamePsicologo = appuntamento.getPsicologo().getUsername();
        boolean cercaFuturi = appuntamento.getTipoAppuntamento() == TipoAppuntamento.IN_PROGRAMMA;

        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(
                CostantiAppuntamentoCsv.FILE_PATH,
                CostantiLetturaScrittura.SOLO_LETTURA
        );

        for (String[] colonne : righe) {
            String stato = colonne[CostantiAppuntamentoCsv.INDICE_STATO_APPUNTAMENTO];
            String username = colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PSICOLOGO];
            String usernamePaziente = colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PAZIENTE];
            LocalDate dataAppuntamento = LocalDate.parse(colonne[CostantiAppuntamentoCsv.INDICE_DATA]);

            boolean isAccettato = Objects.equals(stato, CostantiAppuntamentoCsv.APPUNTAMENTO_ACCETTATO);
            boolean isStessoPsicologo = Objects.equals(username, usernamePsicologo);
            boolean isDataValida = cercaFuturi ? dataAppuntamento.isAfter(dataCorrente) : dataAppuntamento.isBefore(dataCorrente);

            if (isAccettato && isStessoPsicologo && isDataValida) {
                String ora = colonne[CostantiAppuntamentoCsv.INDICE_ORA];
                Appuntamento appuntamentoTrovato = new Appuntamento(dataAppuntamento, LocalTime.parse(ora));
                appuntamentoTrovato.setPaziente(new Paziente(usernamePaziente));
                listaAppuntamentiPsicologo.add(appuntamentoTrovato);
            }
        }

        return listaAppuntamentiPsicologo;
    }


    @Override
    public void insertRichiestaAppuntamento(Appuntamento appuntamento) throws EccezioneDAO {

        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);

        String[] nuovoRecord = new String[] {
                String.valueOf(calcolaIDAppuntamento()),
                String.valueOf(appuntamento.getData()),
                String.valueOf(appuntamento.getOra()),
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
    public Appuntamento notificheNuoviAppuntamentiPaziente(Utente paziente) throws EccezioneDAO {
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

            appuntamento.setStatoNotificaPaziente(notifiche);

        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante la lettura delle notifiche da CSV per il paziente: " + e.getMessage());
        }

        return appuntamento;
    }

    @Override
    public Appuntamento notificheNuoviAppuntamentiPsicologo(Utente psicologo) throws EccezioneDAO {
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

            appuntamento.setStatoNotificaPsicologo(notifiche);

        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante la lettura delle notifiche da CSV per lo psicologo: " + e.getMessage());
        }

        return appuntamento;
    }



    @Override
    public List<Appuntamento> trovaRichiesteAppuntamento(Utente psicologo) throws EccezioneDAO {
            List<Appuntamento> richiesteAppuntamento = new ArrayList<>();

            List<String[]> risultati = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

            for(String[] colonne: risultati) {
                if (colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PSICOLOGO].equals(psicologo.getUsername()) && Integer.parseInt(colonne[CostantiAppuntamentoCsv.INDICE_STATO_APPUNTAMENTO]) == CostantiAppuntamentoCsv.APPUNTAMENTO_NON_ACCETTATO) {
                    Appuntamento richiesta = new Appuntamento(
                            Integer.parseInt(colonne[CostantiAppuntamentoCsv.INDICE_ID_APPUNTAMENTO]));

                    richiesta.setPaziente(new Paziente(colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PAZIENTE]));
                    richiesta.setStatoNotificaPsicologo(Integer.valueOf(colonne[CostantiAppuntamentoCsv.INDICE_STATO_NOTIFICA_PSICOLOGO]));

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
        Appuntamento richiesta = new Appuntamento();

        List<String[]> recordLetti = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        for(String[] colonne : recordLetti) {
            if(colonne[CostantiAppuntamentoCsv.INDICE_ID_APPUNTAMENTO].equals(String.valueOf(richiestaAppuntamento.getIdAppuntamento()))) {
                String data = colonne[CostantiAppuntamentoCsv.INDICE_DATA];
                String ora = colonne[CostantiAppuntamentoCsv.INDICE_ORA];
                richiesta = new Appuntamento(LocalDate.parse(data), LocalTime.parse(ora));
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
    public boolean getDisp(Appuntamento appuntamento) throws EccezioneDAO {

        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        for (String[] colonne : righe) {

            if (Integer.parseInt(colonne[CostantiAppuntamentoCsv.INDICE_ID_APPUNTAMENTO]) == appuntamento.getIdAppuntamento() && colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PSICOLOGO].equals(appuntamento.getPsicologo().getUsername())) {

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
    public void aggiornaStatoNotificaPaziente(Utente paziente) throws EccezioneDAO {

        List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiAppuntamentoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);

        List<String[]> righeAggiornate = new ArrayList<>();
        for (String[] colonne : righe) {
            if (colonne[CostantiAppuntamentoCsv.INDICE_USERNAME_PAZIENTE].equals(paziente.getUsername()) && colonne[CostantiAppuntamentoCsv.INDICE_STATO_NOTIFICA_PAZIENTE].equals(CostantiAppuntamentoCsv.NOTIFICA_PAZIENTE_DA_CONSEGNARE)) {
                colonne[CostantiAppuntamentoCsv.INDICE_STATO_NOTIFICA_PAZIENTE] = CostantiAppuntamentoCsv.NOTIFICA_PAZIENTE_CONSEGNATA; // Aggiorna lo stato di notifica del paziente a 0
            }
            righeAggiornate.add(colonne);
        }

        UtilitiesCSV.scriviRigheAggiornate(CostantiAppuntamentoCsv.FILE_PATH, righeAggiornate);
    }
}
