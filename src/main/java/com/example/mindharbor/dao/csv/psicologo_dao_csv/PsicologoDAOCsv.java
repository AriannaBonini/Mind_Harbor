package com.example.mindharbor.dao.csv.psicologo_dao_csv;

import com.example.mindharbor.dao.PsicologoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.strumenti_utili.costanti.CostantiLetturaScrittura;
import com.example.mindharbor.strumenti_utili.UtilitiesCSV;
import java.util.List;

public class PsicologoDAOCsv implements PsicologoDAO {

    public Psicologo getInfoPsicologo(Psicologo psicologo) throws EccezioneDAO {
        try {
            List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiPsicologoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

            for (String[] colonne : righeCSV) {

                if (colonne[CostantiPsicologoCsv.INDICE_PSICOLOGO_USERNAME].equals(psicologo.getUsername())) {
                    return new Psicologo(Integer.parseInt(colonne[CostantiPsicologoCsv.INDICE_COSTO_ORARIO]),colonne[CostantiPsicologoCsv.INDICE_NOME_STUDIO]);

                }
            }
        } catch (EccezioneDAO e) {
            throw new EccezioneDAO("Errore nella lettura del file CSV: " + e.getMessage(), e);
        }

        return psicologo;
    }


    @Override
    public void inserisciDatiPsicologo(Psicologo psicologo) throws EccezioneDAO {
        try {
            List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiPsicologoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);

            String[] nuovaRiga = new String[3];
            nuovaRiga[CostantiPsicologoCsv.INDICE_PSICOLOGO_USERNAME] = psicologo.getUsername();
            nuovaRiga[CostantiPsicologoCsv.INDICE_COSTO_ORARIO] = String.valueOf(psicologo.getCostoOrario());
            nuovaRiga[CostantiPsicologoCsv.INDICE_NOME_STUDIO] = psicologo.getNomeStudio();

            righeCSV.add(nuovaRiga);

            UtilitiesCSV.scriviRigheAggiornate(CostantiPsicologoCsv.FILE_PATH, righeCSV);

        } catch (EccezioneDAO e) {
            throw new EccezioneDAO("Errore nell'inserimento dei dati dello psicologo nel file CSV: " + e.getMessage(), e);
        }
    }

}
