package com.example.mindharbor.dao.csv.paziente_dao_csv;

import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.strumenti_utili.costanti.CostantiLetturaScrittura;
import com.example.mindharbor.strumenti_utili.UtilitiesCSV;
import java.util.ArrayList;
import java.util.List;

public class PazienteDAOCsv implements PazienteDAO {

    public List<Paziente> trovaPazienti(Utente psicologo) throws EccezioneDAO {
        List<Paziente> listaPazienti = new ArrayList<>();

        try {
            List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiPazienteCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

            for (String[] colonne : righeCSV) {
                if (colonne[CostantiPazienteCsv.INDICE_PSICOLOGO_USERNAME].equals(psicologo.getUsername())) {
                    Paziente paziente= new Paziente(colonne[CostantiPazienteCsv.INDICE_PAZIENTE_USERNAME]);

                    listaPazienti.add(paziente);
                }
            }
            return listaPazienti;
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO("Errore nella lettura del file CSV: " + e.getMessage(), e);
        }
    }

    @Override
    public Paziente getInfoSchedaPersonale(Paziente pazienteSelezionato) throws EccezioneDAO {

        List<String[]> righeCSV;
        try {
            righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiPazienteCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA); // Usa il metodo leggiRigheDaCsv per leggere con CSVReader
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(CostantiPazienteCsv.ERRORE_LETTURA + " " + e.getMessage(), e);
        }

        for (String[] colonne : righeCSV) {
            if (colonne[CostantiPazienteCsv.INDICE_PAZIENTE_USERNAME].equals(pazienteSelezionato.getUsername())) {
                return new Paziente(Integer.parseInt(colonne[CostantiPazienteCsv.INDICE_ANNI]),colonne[CostantiPazienteCsv.INDICE_DIAGNOSI]);
            }
        }

        return pazienteSelezionato;
    }

    @Override
    public Paziente checkAnniPaziente(Paziente pazienteCorrente) throws EccezioneDAO {
        Paziente paziente= new Paziente();
        List<String[]> righeCSV;
        try {
            righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiPazienteCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA); // Usa CSVReader tramite leggiRigheDaCsv
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(CostantiPazienteCsv.ERRORE_LETTURA + " " + e.getMessage(), e);
        }

        for (String[] colonne : righeCSV) {
            if (colonne[CostantiPazienteCsv.INDICE_PAZIENTE_USERNAME].equals(pazienteCorrente.getUsername())) {
                paziente.setAnni(Integer.parseInt(colonne[CostantiPazienteCsv.INDICE_ANNI]));
            }
        }
        return paziente;
    }

    @Override
    public String getUsernamePsicologo(Utente paziente) throws EccezioneDAO {
        String usernamePsicologo = null;

        List<String[]> righeCSV;
        try {
            righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiPazienteCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(CostantiPazienteCsv.ERRORE_LETTURA + " " + e.getMessage(), e);
        }

        for (String[] colonne : righeCSV) {
            if (colonne[CostantiPazienteCsv.INDICE_PAZIENTE_USERNAME].equals(paziente.getUsername())) {
                if (!colonne[CostantiPazienteCsv.INDICE_PSICOLOGO_USERNAME].isEmpty()) {
                    usernamePsicologo = colonne[CostantiPazienteCsv.INDICE_PSICOLOGO_USERNAME];
                }
                break;
            }
        }
        return usernamePsicologo;
    }



    @Override
    public void aggiungiPsicologoAlPaziente(Appuntamento appuntamento) throws EccezioneDAO {
        // Leggi tutte le righe del file CSV utilizzando CSVReader
        List<String[]> righeCSV;
        try {
            righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiPazienteCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA); // Usa il metodo leggiRigheDaCsv
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(CostantiPazienteCsv.ERRORE_LETTURA + " " + e.getMessage(), e);
        }

        // Variabile per sapere se il record è stato aggiornato
        boolean recordUpdated = false;

        // Aggiorna il nome utente dello psicologo nel CSV
        for (String[] colonne : righeCSV) {
            // Verifica se lo username del paziente è quello cercato
            if (colonne[CostantiPazienteCsv.INDICE_PAZIENTE_USERNAME].equals(appuntamento.getPaziente().getUsername())) {
                // Imposta il nome utente dello psicologo
                colonne[CostantiPazienteCsv.INDICE_PSICOLOGO_USERNAME] = appuntamento.getPsicologo().getUsername(); // Imposta lo psicologo
                recordUpdated = true;
                break; // Esci non appena il record viene aggiornato
            }
        }

        if (!recordUpdated) {
            throw new EccezioneDAO("Paziente con username " + appuntamento.getPaziente().getUsername() + " non trovato nel file CSV.");
        }

        try {
            UtilitiesCSV.scriviRigheAggiornate(CostantiPazienteCsv.FILE_PATH, righeCSV);
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(CostantiPazienteCsv.ERRORE_SCRITTURA + " " + e.getMessage(), e);
        }
    }

    @Override
    public void inserisciDatiPaziente(Paziente paziente) throws EccezioneDAO {
        List<String[]> righeCSV;
        try {
            righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiPazienteCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(CostantiPazienteCsv.ERRORE_LETTURA + " " + e.getMessage(), e);
        }

        String[] nuovaRiga = new String[CostantiPazienteCsv.NUMERO_COLONNE];
        nuovaRiga[CostantiPazienteCsv.INDICE_PAZIENTE_USERNAME] = paziente.getUsername();
        nuovaRiga[CostantiPazienteCsv.INDICE_ANNI] = String.valueOf(paziente.getAnni());

        righeCSV.add(nuovaRiga);

        try {
            UtilitiesCSV.scriviRigheAggiornate(CostantiPazienteCsv.FILE_PATH, righeCSV);

        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(CostantiPazienteCsv.ERRORE_SCRITTURA + " " + e.getMessage(), e);
        }
    }
}


