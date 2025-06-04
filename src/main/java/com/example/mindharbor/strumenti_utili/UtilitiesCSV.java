package com.example.mindharbor.strumenti_utili;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.strumenti_utili.costanti.CostantiLetturaScrittura;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UtilitiesCSV {
    private static final Logger logger = LoggerFactory.getLogger(UtilitiesCSV.class);
    private UtilitiesCSV(){}



    /**
     * Legge tutte le righe da un file CSV specificato e le restituisce come una lista di stringhe.
     * <p>
     * Questo metodo legge il contenuto di un file CSV situato nel percorso specificato
     * e restituisce una lista contenente tutte le righe del file come stringhe.
     * Se si verifica un errore durante la lettura del file, viene lanciata un'eccezione {@link EccezioneDAO}.
     * </p>
     *
     * @param filePath Il percorso del file CSV da cui leggere le righe.
     * @return Una lista di stringhe, dove ciascuna stringa rappresenta una riga del file CSV.
     * @throws EccezioneDAO Se si verifica un errore durante la lettura del file CSV.
     */
    public static List<String[]> leggiRigheDaCsv(String filePath, int x) throws EccezioneDAO {
        List<String[]> righe = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            if(x==0) {
                reader.readNext();
            }
            String[] colonne;
            while ((colonne = reader.readNext()) != null) {

                righe.add(colonne);
            }
        } catch (IOException | CsvValidationException e) {
            throw new EccezioneDAO("Errore nella lettura del file CSV: " + e.getMessage(), e);
        }
        return righe;
    }

    /**
     * Scrive una lista di righe aggiornate in un file CSV specificato.
     * <p>
     * Questo metodo sovrascrive il contenuto del file CSV situato nel percorso specificato
     * con le righe fornite nella lista. Se si verifica un errore durante la scrittura del file,
     * viene lanciata un'eccezione {@link EccezioneDAO}.
     * </p>
     *
     * @param filePath       Il percorso del file CSV in cui scrivere le righe aggiornate.
     * @param righeAggiornate Una lista di stringhe, dove ciascuna stringa rappresenta una riga da scrivere nel file CSV.
     * @throws EccezioneDAO Se si verifica un errore durante la scrittura nel file CSV.
     */
    public static void scriviRigheAggiornate(String filePath, List<String[]> righeAggiornate) throws EccezioneDAO {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {

            for (String[] riga : righeAggiornate) {
                writer.writeNext(riga);
            }
        } catch (IOException e) {
            throw new EccezioneDAO("Errore nella scrittura nel file CSV: " + e.getMessage(), e);
        }
    }

    /**
     * Conta il numero di notifiche attive per un paziente specifico in un file CSV.
     * <p>
     * Questo metodo legge tutte le righe di un file CSV e conta quante righe corrispondono
     * a un determinato paziente e hanno una notifica attiva. Le colonne del CSV vengono
     * specificate tramite gli indici forniti come parametri.
     * </p>
     *
     * @param filePath Il percorso del file CSV da leggere.
     * @param username Lo username del paziente di cui contare le notifiche.
     * @param indicePaziente L'indice della colonna che contiene lo username del paziente.
     * @param indiceNotifica L'indice della colonna che contiene lo stato della notifica.
     * @return Il numero di notifiche attive trovate per il paziente specificato.
     * @throws EccezioneDAO Se si verifica un errore durante la lettura del file CSV.
     */
    public static int contaNotifichePaziente(String filePath, String username, int indicePaziente, int indiceNotifica) throws EccezioneDAO {
        int contatore = 0;

        List<String[]> righeCSV = leggiRigheDaCsv(filePath, CostantiLetturaScrittura.SOLO_LETTURA);

        for (String[] colonne : righeCSV) {
            if (colonne.length > Math.max(indicePaziente, indiceNotifica)) {

                if (colonne[indicePaziente].equals(username) && colonne[indiceNotifica].equals("1")) {
                    contatore++;
                }
            } else {
                String rigaMalformata = (colonne.length > 0) ? String.join(",", colonne) : "Riga vuota o nulla";
                logger.warn("Riga malformata: {}", rigaMalformata);
            }
        }

        return contatore;
    }
}
