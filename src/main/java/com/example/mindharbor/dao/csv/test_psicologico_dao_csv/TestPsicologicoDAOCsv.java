package com.example.mindharbor.dao.csv.test_psicologico_dao_csv;

import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.TestPsicologico;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.enumerazioni.TipoUtente;
import com.example.mindharbor.strumenti_utili.costanti.CostantiLetturaScrittura;
import com.example.mindharbor.strumenti_utili.UtilitiesCSV;
import java.time.LocalDate;
import java.util.*;

public class TestPsicologicoDAOCsv implements TestPsicologicoDAO {

    @Override
    public void assegnaTest(TestPsicologico test) throws EccezioneDAO {
        List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiTestPsicologicoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);

        String[] nuovoTest = new String[8];
        nuovoTest[0] = new java.sql.Date(System.currentTimeMillis()).toString();
        nuovoTest[1] = CostantiTestPsicologicoCsv.RISULTATO_TEST_NON_SVOLTO;
        nuovoTest[2] = test.getPsicologo().getUsername();
        nuovoTest[3] = test.getPaziente().getUsername();
        nuovoTest[4] = test.getTest(); // Test
        nuovoTest[5] = CostantiTestPsicologicoCsv.NOTIFICA_PAZIENTE_DA_CONSEGNARE;
        nuovoTest[6] = CostantiTestPsicologicoCsv.TEST_DA_SVOLGERE;
        nuovoTest[7] = CostantiTestPsicologicoCsv.NOTIFICA_PSICOLOGO_DA_NON_CONSEGNARE;

        righeCSV.add(nuovoTest);

        UtilitiesCSV.scriviRigheAggiornate(CostantiTestPsicologicoCsv.FILE_PATH, righeCSV);
    }

    @Override
    public TestPsicologico getNotificaPazientePerTestAssegnato(Utente paziente) throws EccezioneDAO {
        TestPsicologico testPsicologico= new TestPsicologico();

        int numeroNuoviTestAssegnait= UtilitiesCSV.contaNotifichePaziente(CostantiTestPsicologicoCsv.FILE_PATH, paziente.getUsername(), CostantiTestPsicologicoCsv.INDICE_PAZIENTE, CostantiTestPsicologicoCsv.INDICE_STATO_NOTIFICA_PAZIENTE);
        testPsicologico.setStatoNotificaPaziente(numeroNuoviTestAssegnait);
        return testPsicologico;
    }


    @Override
    public void modificaStatoNotificaTest(Utente utente, Paziente pazienteSelezionato) throws EccezioneDAO {
        List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiTestPsicologicoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);

        for (String[] colonne : righeCSV) {

            if (utente.getUserType().equals(TipoUtente.PAZIENTE)) {
                if (colonne[CostantiTestPsicologicoCsv.INDICE_PAZIENTE].equals(utente.getUsername()) && colonne[CostantiTestPsicologicoCsv.INDICE_STATO_NOTIFICA_PAZIENTE].equals(CostantiTestPsicologicoCsv.NOTIFICA_PAZIENTE_DA_CONSEGNARE)) {
                    colonne[CostantiTestPsicologicoCsv.INDICE_STATO_NOTIFICA_PAZIENTE] = CostantiTestPsicologicoCsv.NOTIFICA_PAZIENTE_CONSEGNATA; // Modifica lo stato
                }
            } else {
                if (colonne[CostantiTestPsicologicoCsv.INDICE_PSICOLOGO].equals(utente.getUsername()) &&
                        colonne[CostantiTestPsicologicoCsv.INDICE_PAZIENTE].equals(pazienteSelezionato.getUsername()) &&
                        colonne[CostantiTestPsicologicoCsv.INDICE_STATO_NOTIFICA_PSICOLOGO].equals(CostantiTestPsicologicoCsv.NOTIFICA_PSICOLOGO_DA_CONSEGNARE)) {
                    colonne[CostantiTestPsicologicoCsv.INDICE_STATO_NOTIFICA_PSICOLOGO] = CostantiTestPsicologicoCsv.NOTIFICA_PSICOLOGO_CONSEGNATA; // Modifica lo stato notifica dello psicologo
                }
            }
        }

        UtilitiesCSV.scriviRigheAggiornate(CostantiTestPsicologicoCsv.FILE_PATH, righeCSV);
    }

    @Override
    public List<TestPsicologico> trovaListaTest(Utente paziente) throws EccezioneDAO {

        List<TestPsicologico> testPsicologicoList = new ArrayList<>();

        List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiTestPsicologicoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        for (String[] colonne : righeCSV) {
            if (colonne[CostantiTestPsicologicoCsv.INDICE_PAZIENTE].equals(paziente.getUsername())) {
                LocalDate data = LocalDate.parse(colonne[CostantiTestPsicologicoCsv.INDICE_DATA]);

                TestPsicologico test = new TestPsicologico(
                        data,
                        Integer.parseInt(colonne[CostantiTestPsicologicoCsv.INDICE_RISULTATO]),
                        colonne[CostantiTestPsicologicoCsv.INDICE_TEST]);

                test.setSvolto(Integer.parseInt(colonne[CostantiTestPsicologicoCsv.INDICE_SVOLTO]));

                testPsicologicoList.add(test);
            }
        }
        return testPsicologicoList;
    }

    @Override
    public TestPsicologico trovaTestPassati(TestPsicologico testDaAggiungere) throws EccezioneDAO {
        TestPsicologico ultimoTestPsicologicoSvolto = new TestPsicologico();

        List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiTestPsicologicoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        LocalDate maxData = null;

        for (String[] colonne : righeCSV) {
            if (colonne[CostantiTestPsicologicoCsv.INDICE_PAZIENTE].equals(testDaAggiungere.getPaziente().getUsername())
                    && colonne[CostantiTestPsicologicoCsv.INDICE_TEST].equals(testDaAggiungere.getTest())
                    && Objects.equals(colonne[CostantiTestPsicologicoCsv.INDICE_SVOLTO], CostantiTestPsicologicoCsv.TEST_SVOLTO)) {

                LocalDate dataTest = LocalDate.parse(colonne[CostantiTestPsicologicoCsv.INDICE_DATA]);

                if (maxData == null || dataTest.isAfter(maxData)) {
                    maxData = dataTest;
                    ultimoTestPsicologicoSvolto.setRisultato(Integer.parseInt(colonne[CostantiTestPsicologicoCsv.INDICE_RISULTATO]));
                }
            }
        }

        aggiornaTestAppenaSvolto(testDaAggiungere);

        return ultimoTestPsicologicoSvolto;
    }


    private void aggiornaTestAppenaSvolto(TestPsicologico testDaAggiungere) throws EccezioneDAO {
        List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiTestPsicologicoCsv.FILE_PATH, CostantiLetturaScrittura.LETTURA_SCRITTURA);
        List<String[]> righeAggiornate = new ArrayList<>();


        for (String[] colonne : righeCSV) {
            if (colonne[CostantiTestPsicologicoCsv.INDICE_PAZIENTE].equals(testDaAggiungere.getPaziente().getUsername())
                    && LocalDate.parse(colonne[CostantiTestPsicologicoCsv.INDICE_DATA]).equals(testDaAggiungere.getData())) {

                colonne[CostantiTestPsicologicoCsv.INDICE_RISULTATO] = String.valueOf(testDaAggiungere.getRisultato());
                colonne[CostantiTestPsicologicoCsv.INDICE_SVOLTO] = CostantiTestPsicologicoCsv.TEST_SVOLTO;
                colonne[CostantiTestPsicologicoCsv.INDICE_STATO_NOTIFICA_PSICOLOGO] = CostantiTestPsicologicoCsv.NOTIFICA_PSICOLOGO_DA_CONSEGNARE;
            }

            righeAggiornate.add(colonne);
        }

        UtilitiesCSV.scriviRigheAggiornate(CostantiTestPsicologicoCsv.FILE_PATH, righeAggiornate);
    }

    @Override
    public TestPsicologico getNumTestSvoltiDaNotificare(Utente psicologo) throws EccezioneDAO {
        TestPsicologico testPsicologico= new TestPsicologico();
        int nuoviTestSvolti = 0;

        List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiTestPsicologicoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        for (String[] colonne : righeCSV) {
            if (CostantiTestPsicologicoCsv.NOTIFICA_PSICOLOGO_DA_CONSEGNARE.equals(colonne[CostantiTestPsicologicoCsv.INDICE_STATO_NOTIFICA_PSICOLOGO]) && psicologo.getUsername().equals(colonne[CostantiTestPsicologicoCsv.INDICE_PSICOLOGO])) {
                nuoviTestSvolti++;
            }
        }

        testPsicologico.setStatoNotificaPsicologo(nuoviTestSvolti);
        return testPsicologico;
    }

    @Override
    public List<TestPsicologico> listaTestSvolti(TestPsicologico testPsicologico) throws EccezioneDAO {
        List<TestPsicologico> listaTestPsicologico=new ArrayList<>();
        List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiTestPsicologicoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        for (String[] colonna : righeCSV) {
            if (colonna[CostantiTestPsicologicoCsv.INDICE_PSICOLOGO].equals(testPsicologico.getPsicologo().getUsername()) && colonna[CostantiTestPsicologicoCsv.INDICE_PAZIENTE].equals(testPsicologico.getPaziente().getUsername()) && colonna[CostantiTestPsicologicoCsv.INDICE_SVOLTO].equals(CostantiTestPsicologicoCsv.TEST_SVOLTO)) {
                TestPsicologico testPsicologicoTrovato= new TestPsicologico(new Psicologo(colonna[CostantiTestPsicologicoCsv.INDICE_PSICOLOGO]), new Paziente(colonna[CostantiTestPsicologicoCsv.INDICE_PAZIENTE]));

                testPsicologicoTrovato.setData(LocalDate.parse(colonna[CostantiTestPsicologicoCsv.INDICE_DATA]));

                listaTestPsicologico.add(testPsicologicoTrovato);
            }
        }

        return listaTestPsicologico;
    }


    public List<TestPsicologico> listaTestSvoltiSenzaPrescrizione(TestPsicologico testPsicologico) throws EccezioneDAO {
        List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiTestPsicologicoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);
        List<TestPsicologico> listaTest=new ArrayList<>();

        for (String[] colonna : righeCSV) {
            if (colonna[CostantiTestPsicologicoCsv.INDICE_PSICOLOGO].equals(testPsicologico.getPsicologo().getUsername()) && colonna[CostantiTestPsicologicoCsv.INDICE_PAZIENTE].equals(testPsicologico.getPaziente().getUsername()) && colonna[CostantiTestPsicologicoCsv.INDICE_SVOLTO].equals(CostantiTestPsicologicoCsv.TEST_SVOLTO)) {
                TestPsicologico testPsicologicoTrovato= new TestPsicologico(
                        LocalDate.parse(colonna[CostantiTestPsicologicoCsv.INDICE_DATA]),
                        Integer.parseInt(colonna[CostantiTestPsicologicoCsv.INDICE_RISULTATO]),
                        colonna[CostantiTestPsicologicoCsv.INDICE_TEST]);

                listaTest.add(testPsicologicoTrovato);
            }
        }
        return listaTest;
    }

    @Override
    public Paziente numTestSvoltiPerPaziente(Utente paziente) throws EccezioneDAO {
        List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiTestPsicologicoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);
        int numeroTestSvolti = 0;
        Paziente pazienteConNumTest=new Paziente();

        for (String[] colonne : righeCSV) {
            if (colonne[CostantiTestPsicologicoCsv.INDICE_PAZIENTE].equals(paziente.getUsername())) {
                numeroTestSvolti += Integer.parseInt(colonne[CostantiTestPsicologicoCsv.INDICE_STATO_NOTIFICA_PSICOLOGO]); // Incrementa il conteggio
            }
        }
        pazienteConNumTest.setNumeroTest(numeroTestSvolti);
        return pazienteConNumTest;
    }

    @Override
    public boolean getNumTestAssegnato(Utente paziente) throws EccezioneDAO {

        List<String[]> righeCSV = UtilitiesCSV.leggiRigheDaCsv(CostantiTestPsicologicoCsv.FILE_PATH, CostantiLetturaScrittura.SOLO_LETTURA);

        String dataOdierna = java.time.LocalDate.now().toString();

        for (String[] colonne : righeCSV) {
            if (colonne[CostantiTestPsicologicoCsv.INDICE_DATA].equals(dataOdierna) && paziente.getUsername().equals(colonne[CostantiTestPsicologicoCsv.INDICE_PAZIENTE])) {
                return true;
            }
        }

        return false;
    }
}

