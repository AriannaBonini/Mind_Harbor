package com.example.mindharbor.dao.csv.utente_dao_csv;

import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.tipo_utente.UserType;
import com.example.mindharbor.strumenti_utili.costanti.CostantiLetturaScrittura;
import com.example.mindharbor.strumenti_utili.UtilitiesCSV;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementazione dell'interfaccia {@link UtenteDAO} che utilizza un file CSV come fonte di dati per le operazioni di accesso utente.
 * <p>
 * Questa classe fornisce metodi per eseguire operazioni CRUD (Create, Read, Update, Delete) su utenti memorizzati in un file CSV.
 * I metodi inclusi permettono di verificare le credenziali di accesso degli utenti, cercare informazioni come nome e cognome,
 * e restituire una lista di utenti di tipo psicologo. Inoltre, consente di arricchire le informazioni dei pazienti associate
 * agli appuntamenti con dati aggiuntivi come nome, cognome e genere.
 * </p>
 * <p>
 * Il percorso del file CSV è definito dalla costante `FILE_PATH`. Gli errori nella lettura del file sono gestiti tramite
 * l'eccezione personalizzata {@link EccezioneDAO}.
 * </p>
 */
public class UtenteDAOCsv implements UtenteDAO {

    /**
     * Verifica le credenziali di accesso di un utente contro i dati presenti nel file CSV e,
     * se le credenziali corrispondono, restituisce un oggetto {@link Utente} con le informazioni dell'utente.
     * <p>
     * Questo metodo legge tutte le righe del file CSV specificato, che contiene informazioni sugli utenti.
     * Confronta le credenziali di accesso fornite (`credenzialiUtenteLogin`) con quelle memorizzate nel file.
     * Se viene trovata una corrispondenza tra l'username e la password, viene creato e restituito un oggetto
     * {@link Utente} contenente l'username, il nome, il cognome e il tipo di utente (paziente o psicologo).
     * Se non viene trovata alcuna corrispondenza, il metodo restituisce `null`.
     * </p>
     *
     * @param credenzialiUtenteLogin Un oggetto {@link Utente} contenente l'username e la password dell'utente che sta cercando di accedere.
     * @return Un oggetto {@link Utente} con le informazioni dell'utente se le credenziali corrispondono; `null` se non viene trovata alcuna corrispondenza.
     * @throws EccezioneDAO Se si verifica un errore durante la lettura del file CSV.
     */
    @Override
    public Utente trovaUtente(Utente credenzialiUtenteLogin) throws EccezioneDAO {
        Utente utente = null;
        List<String[]> righe;

        try {
            // Leggi tutte le righe del CSV utilizzando CSVReader
            righe = UtilitiesCSV.leggiRigheDaCsv(CostantiUtenteCvs.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(CostantiUtenteCvs.ERRORE_LETTURA + " " + e.getMessage());
        }

        // Itera attraverso le righe del file CSV
        for (String[] colonne : righe) {
            // Controlla se l'username e la password corrispondono
            if (colonne[CostantiUtenteCvs.INDICE_UTENTE_USERNAME].equals(credenzialiUtenteLogin.getUsername()) &&
                    colonne[CostantiUtenteCvs.INDICE_UTENTE_PASSWORD].equals(credenzialiUtenteLogin.getPassword())) {
                // Se il tipo di utente è "Paziente" o "Psicologo", crea l'oggetto Utente
                if(Objects.equals(colonne[CostantiUtenteCvs.INDICE_UTENTE_CATEGORIA], "Paziente")) {
                    utente = new Utente(colonne[CostantiUtenteCvs.INDICE_UTENTE_USERNAME], colonne[CostantiUtenteCvs.INDICE_UTENTE_NOME], colonne[CostantiUtenteCvs.INDICE_UTENTE_COGNOME], UserType.PAZIENTE);
                } else {
                    utente = new Utente(colonne[CostantiUtenteCvs.INDICE_UTENTE_USERNAME], colonne[CostantiUtenteCvs.INDICE_UTENTE_NOME], colonne[CostantiUtenteCvs.INDICE_UTENTE_COGNOME], UserType.PSICOLOGO);
                }
                break;  // Interrompe il ciclo se l'utente è stato trovato
            }
        }

        return utente;  // Ritorna l'utente trovato o null se non esiste
    }

    /**
     * Cerca e restituisce le informazioni di nome e cognome di un utente specificato, leggendo da un file CSV.
     * <p>
     * Questo metodo scorre tutte le righe del file CSV specificato e cerca l'utente corrispondente all'username
     * fornito nell'oggetto {@link Utente} passato come parametro. Se viene trovato un utente con lo stesso username,
     * viene creato un nuovo oggetto {@link Utente} contenente il nome e il cognome estratti dal file CSV.
     * </p>
     *
     * @param utente Un oggetto {@link Utente} contenente l'username dell'utente di cui si vogliono recuperare nome e cognome.
     * @return Un oggetto {@link Utente} contenente il nome e il cognome se l'utente è trovato; `null` se l'utente non è trovato.
     * @throws EccezioneDAO Se si verifica un errore durante la lettura del file CSV.
     */
    @Override
    public Utente trovaNomeCognome(Utente utente) throws EccezioneDAO {
        Utente infoUtente = null;
        List<String[]> righe;

        // Leggi le righe dal CSV utilizzando il metodo che usa CSVReader
        try {
            righe = UtilitiesCSV.leggiRigheDaCsv(CostantiUtenteCvs.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA); // Chiamata al metodo che usa CSVReader
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(CostantiUtenteCvs.ERRORE_LETTURA + " " + e.getMessage());
        }

        // Cerca l'utente nella lista delle righe lette
        for (String[] colonne : righe) {
            // Verifica se l'username corrisponde
            if (colonne[CostantiUtenteCvs.INDICE_UTENTE_USERNAME].equals(utente.getUsername())) {
                // Crea l'oggetto Utente con il nome e il cognome trovati
                infoUtente = new Utente("", colonne[CostantiUtenteCvs.INDICE_UTENTE_NOME], colonne[CostantiUtenteCvs.INDICE_UTENTE_COGNOME], "");
                break;
            }
        }

        return infoUtente;
    }


    /**
     * Restituisce una lista di oggetti {@link Psicologo} che rappresentano gli utenti di tipo psicologo.
     * <p>
     * Questo metodo viene utilizzato principalmente durante la prenotazione di un appuntamento, quando un paziente
     * deve visualizzare la lista completa degli psicologi disponibili o, se ha già uno psicologo assegnato,
     * visualizzare solo le informazioni di quest'ultimo. Il metodo legge i dati da un file CSV e restituisce una lista
     * di psicologi che includono il nome, il cognome, lo username e il genere.
     * </p>
     *
     * @param usernamePsicologo Lo username di uno psicologo specifico. Se è non null e corrisponde ad uno degli psicologi nel file,
     *                          verranno restituite solo le informazioni di quello psicologo. Se è null, verranno restituiti
     *                          tutti gli psicologi.
     * @return Una lista di oggetti {@link Psicologo} che rappresentano gli psicologi trovati nel file CSV. Se viene specificato
     *         uno username, la lista conterrà al massimo un elemento.
     * @throws EccezioneDAO Se si verifica un errore durante la lettura del file CSV.
     */
    @Override
    public List<Psicologo> listaUtentiDiTipoPsicologo(String usernamePsicologo) throws EccezioneDAO {
        List<Psicologo> listaPsicologi = new ArrayList<>();
        List<String[]> righe;

        try {
            // Leggi le righe dal CSV usando CSVReader
            righe = UtilitiesCSV.leggiRigheDaCsv(CostantiUtenteCvs.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);  // Metodo che usa CSVReader
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(CostantiUtenteCvs.ERRORE_LETTURA + " " + e.getMessage());
        }

        // Itera attraverso le righe lette
        for (String[] colonne : righe) {
            if (colonne[CostantiUtenteCvs.INDICE_UTENTE_CATEGORIA].equals("Psicologo")) { // Controlla se il ruolo è "Psicologo"
                if (usernamePsicologo != null && !colonne[CostantiUtenteCvs.INDICE_UTENTE_USERNAME].equals(usernamePsicologo)) {
                    continue; // Salta gli utenti che non corrispondono al nome utente richiesto
                }
                // Crea l'oggetto Psicologo e aggiungilo alla lista
                Psicologo psicologo = new Psicologo(colonne[CostantiUtenteCvs.INDICE_UTENTE_USERNAME], colonne[CostantiUtenteCvs.INDICE_UTENTE_NOME], colonne[CostantiUtenteCvs.INDICE_UTENTE_COGNOME], colonne[CostantiUtenteCvs.INDICE_UTENTE_GENERE]);
                listaPsicologi.add(psicologo);
            }
        }

        return listaPsicologi;
    }

    /**
     * Arricchisce le informazioni dei pazienti associati agli appuntamenti forniti.
     * <p>
     * Questo metodo prende una lista di appuntamenti (`richiesteAppuntamenti`) e arricchisce
     * le informazioni dei pazienti associati a ciascun appuntamento leggendo i dati da un file CSV.
     * Per ogni appuntamento nella lista, il metodo verifica se l'username del paziente
     * corrisponde a un record nel file CSV. Se viene trovata una corrispondenza, le informazioni
     * del paziente come il nome, il cognome e il genere vengono aggiornate con i valori
     * corrispondenti trovati nel CSV.
     * </p>
     *
     * @param richiesteAppuntamenti Una lista di oggetti {@link Appuntamento} contenente appuntamenti
     *                              per cui si desidera aggiornare le informazioni dei pazienti.
     * @return La lista di appuntamenti con le informazioni dei pazienti aggiornate.
     * @throws EccezioneDAO Se si verifica un errore durante la lettura del file CSV.
     */
    @Override
    public List<Appuntamento> richiestaAppuntamentiInfoPaziente(List<Appuntamento> richiesteAppuntamenti) throws EccezioneDAO {
        try {
            // Leggi le righe dal CSV usando CSVReader
            List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiUtenteCvs.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

            // Itera attraverso le righe lette
            for (String[] colonne : righe) {
                String username = colonne[CostantiUtenteCvs.INDICE_UTENTE_USERNAME];
                String nome = colonne[CostantiUtenteCvs.INDICE_UTENTE_NOME];
                String cognome = colonne[CostantiUtenteCvs.INDICE_UTENTE_COGNOME];
                String genere = colonne[CostantiUtenteCvs.INDICE_UTENTE_GENERE];

                // Per ogni appuntamento, controlla se l'utente corrisponde
                for (Appuntamento app : richiesteAppuntamenti) {
                    if (username.equals(app.getPaziente().getUsername())) {
                        // Aggiorna le informazioni del paziente nell'appuntamento
                        app.getPaziente().setNome(nome);
                        app.getPaziente().setCognome(cognome);
                        app.getPaziente().setGenere(genere);
                    }
                }
            }
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO("Errore nella lettura del file CSV: " + e.getMessage(), e);
        }
        return richiesteAppuntamenti;
    }

    /**
     * Restituisce un oggetto {@link Utente} contenente informazioni specifiche (nome, cognome e genere) di un paziente.
     * <p>
     * Questo metodo cerca nel file CSV un record corrispondente allo username del paziente passato come parametro.
     * Se trovato, restituisce un oggetto {@link Utente} contenente il nome, cognome e genere del paziente.
     * </p>
     *
     * @param paziente Un oggetto {@link Utente} contenente lo username del paziente di cui si vogliono ottenere le informazioni.
     * @return Un oggetto {@link Utente} con nome, cognome e genere del paziente. Restituisce null se non viene trovato nessun paziente con lo username specificato.
     * @throws EccezioneDAO Se si verifica un errore durante la lettura del file CSV.
     */
    @Override
    public Utente trovaInfoUtente(Utente paziente) throws EccezioneDAO {
        Utente infoUtente = null;

        try {
            // Leggi le righe dal CSV usando CSVReader
            List<String[]> righe = UtilitiesCSV.leggiRigheDaCsv(CostantiUtenteCvs.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

            // Itera attraverso le righe lette
            for (String[] colonne : righe) {
                // Assumendo che l'username si trovi nella colonna indicata
                if (colonne[CostantiUtenteCvs.INDICE_UTENTE_USERNAME].equals(paziente.getUsername())) {
                    // Crea l'oggetto Utente con le informazioni trovate
                    infoUtente = new Utente(paziente.getUsername(), colonne[CostantiUtenteCvs.INDICE_UTENTE_NOME], colonne[CostantiUtenteCvs.INDICE_UTENTE_COGNOME], colonne[CostantiUtenteCvs.INDICE_UTENTE_GENERE]); // Nome, Cognome, Genere
                    break; // Esci dal ciclo una volta trovata l'informazione
                }
            }
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO("Errore nella lettura del file CSV: " + e.getMessage(), e);
        }

        return infoUtente;
    }
}