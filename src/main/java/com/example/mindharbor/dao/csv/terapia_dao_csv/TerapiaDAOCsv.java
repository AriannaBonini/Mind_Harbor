package com.example.mindharbor.dao.csv.terapia_dao_csv;

import com.example.mindharbor.dao.TerapiaDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.*;
import com.example.mindharbor.strumenti_utili.costanti.CostantiLetturaScrittura;
import com.example.mindharbor.strumenti_utili.UtilitiesCSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static com.example.mindharbor.strumenti_utili.UtilitiesCSV.leggiRigheDaCsv;
import static com.example.mindharbor.strumenti_utili.UtilitiesCSV.scriviRigheAggiornate;

public class TerapiaDAOCsv implements TerapiaDAO {

    private static final Logger logger = LoggerFactory.getLogger(TerapiaDAOCsv.class);

    @Override
    public void aggiungiTerapia(Terapia terapia) throws EccezioneDAO {
        String[] nuovaRiga = {
                terapia.getTestPsicologico().getPsicologo().getUsername(),
                terapia.getTestPsicologico().getPaziente().getUsername(),
                terapia.getTerapia(),
                String.valueOf(terapia.getDataTerapia()),
                String.valueOf(terapia.getTestPsicologico().getData()),
                CostantiTerapiaCsv.NOTIFICA_PAZIENTE_DA_CONSEGNARE
        };

        List<String[]> righeCSV = leggiRigheDaCsv(CostantiTerapiaCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);

        righeCSV.add(nuovaRiga);

        scriviRigheAggiornate(CostantiTerapiaCsv.FILE_PATH, righeCSV);
    }

    @Override
    public List<Terapia> getTerapie(Utente paziente) throws EccezioneDAO {
        List<Terapia> terapie = new ArrayList<>();

        List<String[]> righeCsv = leggiRigheDaCsv(CostantiTerapiaCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        for (String[] colonne : righeCsv) {
            if (colonne.length > Math.max(
                    CostantiTerapiaCsv.INDICE_PAZIENTE,
                    Math.max(CostantiTerapiaCsv.INDICE_PSICOLOGO,
                            Math.max(CostantiTerapiaCsv.INDICE_TERAPIA, CostantiTerapiaCsv.INDICE_DATA_TERAPIA)))) {

                if (colonne[CostantiTerapiaCsv.INDICE_PAZIENTE].equals(paziente.getUsername())) {
                    Terapia terapia = new Terapia(
                            new TestPsicologico(new Psicologo(colonne[CostantiTerapiaCsv.INDICE_PSICOLOGO])),
                            colonne[CostantiTerapiaCsv.INDICE_TERAPIA],
                            LocalDate.parse(colonne[CostantiTerapiaCsv.INDICE_DATA_TERAPIA])
                    );
                    terapie.add(terapia);
                }
            } else {
                String rigaMalformata = (colonne.length > 0) ? String.join(",", colonne) : "Riga vuota o nulla";
                logger.warn("Riga malformata: {}", rigaMalformata);
            }
        }

        aggiornaStatoNotificaPaziente(paziente);
        return terapie;
    }

    private void aggiornaStatoNotificaPaziente(Utente utente) throws EccezioneDAO {
        List<String[]> righeCSV = leggiRigheDaCsv(CostantiTerapiaCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);
        List<String[]> righeAggiornate = new ArrayList<>();

        for (String[] colonne : righeCSV) {
            if (colonne[CostantiTerapiaCsv.INDICE_PAZIENTE].equals(utente.getUsername())) {
                colonne[CostantiTerapiaCsv.INDICE_NOTIFICA_PAZIENTE] = CostantiTerapiaCsv.NOTIFICA_PAZIENTE_CONSEGNATA; // Supponiamo che 0 significhi "non notificato"
            }

            righeAggiornate.add(colonne);
        }

        scriviRigheAggiornate(CostantiTerapiaCsv.FILE_PATH, righeAggiornate);
    }

    @Override
    public Terapia getNuoveTerapie(Utente paziente) throws EccezioneDAO {
        Terapia terapia= new Terapia();
        int nuoveTerapie= UtilitiesCSV.contaNotifichePaziente(CostantiTerapiaCsv.FILE_PATH, paziente.getUsername(), CostantiTerapiaCsv.INDICE_PAZIENTE, CostantiTerapiaCsv.INDICE_NOTIFICA_PAZIENTE);
        terapia.setNotificaPaziente(nuoveTerapie);

        return terapia;
    }


    @Override
    public boolean esistenzaTerapiaPerUnTest(TestPsicologico testPsicologico) throws EccezioneDAO {
        List<String[]> righeCSV = leggiRigheDaCsv(CostantiTerapiaCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);
        for (String[] colonna : righeCSV) {
            if (colonna[CostantiTerapiaCsv.INDICE_PSICOLOGO].equals(testPsicologico.getPsicologo().getUsername()) && colonna[CostantiTerapiaCsv.INDICE_PAZIENTE].equals(testPsicologico.getPaziente().getUsername()) && LocalDate.parse(colonna[CostantiTerapiaCsv.INDICE_DATA_TEST]).equals(testPsicologico.getData())) {
                return false;
            }
        }
        return true;
    }
}
