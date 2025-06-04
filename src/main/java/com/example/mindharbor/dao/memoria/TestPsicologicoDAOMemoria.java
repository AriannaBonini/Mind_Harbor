package com.example.mindharbor.dao.memoria;

import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.TestPsicologico;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.enumerazioni.TipoUtente;
import java.time.LocalDate;
import java.util.*;

public class TestPsicologicoDAOMemoria implements TestPsicologicoDAO {

    private final List<TestPsicologico> testPsicologiciInMemoria = new ArrayList<>();

    @Override
    public void assegnaTest(TestPsicologico test) throws EccezioneDAO {
        try {
            TestPsicologico testDaInserire = new TestPsicologico(LocalDate.now(), test.getPsicologo(), test.getPaziente());

            testDaInserire.setTest(test.getTest());
            testDaInserire.setStatoNotificaPaziente(1);
            testDaInserire.setStatoNotificaPsicologo(0);

            testPsicologiciInMemoria.add(testDaInserire);
        }catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'inserimento del nuovo test: " + e.getMessage());
        }
    }

    @Override
    public TestPsicologico getNotificaPazientePerTestAssegnato(Utente paziente) throws EccezioneDAO {
        TestPsicologico testPsicologico= new TestPsicologico();
        int numeroNuoviTestAssegnati = 0;
        try {
            for (TestPsicologico t : testPsicologiciInMemoria) {
                if (t.getPaziente().getUsername().equals(paziente.getUsername()) && t.getStatoNotificaPaziente() == 1) {
                    numeroNuoviTestAssegnati++;
                }
            }
            testPsicologico.setStatoNotificaPaziente(numeroNuoviTestAssegnati);
            return testPsicologico;
        }catch (Exception e) {
            throw new EccezioneDAO("Errore durante il conteggio delle notifiche del paziente: " + e.getMessage());
        }
    }

    @Override
    public void modificaStatoNotificaTest(Utente utente, Paziente pazienteSelezionato) throws EccezioneDAO{
        try {
            if (utente.getUserType().equals(TipoUtente.PAZIENTE)) {
                for (TestPsicologico t : testPsicologiciInMemoria) {
                    if (t.getPaziente().getUsername().equals(utente.getUsername()) && t.getStatoNotificaPaziente() == 1) {
                        t.setStatoNotificaPaziente(0);
                    }
                }
            } else {
                for (TestPsicologico t : testPsicologiciInMemoria) {
                    if (t.getPsicologo().getUsername().equals(utente.getUsername())
                            && t.getPaziente().getUsername().equals(pazienteSelezionato.getUsername())
                            && t.getStatoNotificaPsicologo() == 1) {
                        t.setStatoNotificaPsicologo(0);
                    }
                }
            }
        }catch (Exception e) {
            throw new EccezioneDAO("Errore durante la modifica dello stato delle notifiche del paziente o dello psicologo " + e.getMessage());
        }
    }

    @Override
    public List<TestPsicologico> trovaListaTest(Utente paziente) throws EccezioneDAO{
        List<TestPsicologico> listaTest = new ArrayList<>();
        try {
            for (TestPsicologico t : testPsicologiciInMemoria) {
                if (t.getPaziente().getUsername().equals(paziente.getUsername())) {
                    TestPsicologico test = new TestPsicologico(
                            t.getData(),
                            t.getRisultato(),
                            t.getTest());

                    test.setSvolto(t.getSvolto());

                    listaTest.add(test);
                }
            }
            return listaTest;
        }catch (Exception e) {
            throw new EccezioneDAO("Errore durante la ricerca dei test: " + e.getMessage());
        }
    }

    @Override
    public TestPsicologico trovaTestPassati(TestPsicologico testDaAggiungere) throws EccezioneDAO {
        TestPsicologico ultimoTestPsicologicoSvolto = new TestPsicologico();
        LocalDate data = null;
        try {
            for (TestPsicologico t : testPsicologiciInMemoria) {
                if (t.getPaziente().getUsername().equals(testDaAggiungere.getPaziente().getUsername())
                        && t.getTest().equals(testDaAggiungere.getTest())
                        && t.getSvolto() == 1) {
                    LocalDate dataTest = t.getData();
                    if (dataTest.isAfter(data)) {
                        data = dataTest;
                        ultimoTestPsicologicoSvolto.setRisultato(t.getRisultato());
                    }
                }
            }

            aggiornaTestAppenaSvolto(testDaAggiungere);
            return ultimoTestPsicologicoSvolto;
        }catch (Exception e) {
            throw new EccezioneDAO("Errore durante la ricerca dei test passati: " + e.getMessage());
        }
    }

    public void aggiornaTestAppenaSvolto(TestPsicologico testDaAggiungere) throws EccezioneDAO{
        try {
            for (TestPsicologico t : testPsicologiciInMemoria) {
                if (t.getPaziente().getUsername().equals(testDaAggiungere.getPaziente().getUsername())
                        && t.getData().equals(testDaAggiungere.getData())) {
                    t.setRisultato(testDaAggiungere.getRisultato());
                    t.setSvolto(1);
                    t.setStatoNotificaPsicologo(1);
                    break;
                }
            }
        }catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'aggiornamento del test appena svolto: " + e.getMessage());
        }
    }

    @Override
    public TestPsicologico getNumTestSvoltiDaNotificare(Utente psicologo) throws EccezioneDAO {
        TestPsicologico testPsicologico= new TestPsicologico();
        int nuoviTestSvolti = 0;
        try {
        for (TestPsicologico t : testPsicologiciInMemoria) {
            if (t.getPsicologo().getUsername().equals(psicologo.getUsername()) && t.getStatoNotificaPsicologo() == 1) {
                nuoviTestSvolti++;
            }
        }
        testPsicologico.setStatoNotificaPsicologo(nuoviTestSvolti);
        return testPsicologico;
    }catch (Exception e) {
            throw new EccezioneDAO("Errore durante la ricerca dei test svolti da notificare: " + e.getMessage());
        }
    }

    @Override
    public List<TestPsicologico> listaTestSvolti(TestPsicologico testPsicologico) throws EccezioneDAO {
        List<TestPsicologico> listaTestPsicologico= new ArrayList<>();

        try {
            for (TestPsicologico test : testPsicologiciInMemoria) {
                if (test.getSvolto() == 1
                        && test.getPsicologo().getUsername().equals(testPsicologico.getPsicologo().getUsername())
                        && test.getPaziente().getUsername().equals(testPsicologico.getPaziente().getUsername())) {

                    TestPsicologico testPsicologicoTrovato=new TestPsicologico(test.getPsicologo(),test.getPaziente());

                    testPsicologicoTrovato.setData(test.getData());
                    listaTestPsicologico.add(testPsicologicoTrovato);
                }
            }
            return listaTestPsicologico;
        }catch (Exception e) {
                throw new EccezioneDAO("Errore nel conteggio del numeri to test senza prescrizione: " + e.getMessage());
            }

    }


    @Override
    public List<TestPsicologico> listaTestSvoltiSenzaPrescrizione(TestPsicologico testPsicologico) throws EccezioneDAO{
        List<TestPsicologico> testSvolti = new ArrayList<>();

        try{
            for (TestPsicologico test : testPsicologiciInMemoria) {
                if (test.getSvolto() == 1
                        && test.getPaziente().getUsername().equals(testPsicologico.getPaziente().getUsername())
                        && test.getPsicologo().getUsername().equals(testPsicologico.getPsicologo().getUsername())) {

                    TestPsicologico testSvolto = new TestPsicologico(
                                test.getData(),
                                test.getRisultato(),
                                test.getTest()
                    );

                    testSvolti.add(testSvolto);
                }
            }
        }catch (Exception e) {
            throw new EccezioneDAO("Errore nella ricerca dei test senza prescrizione: " + e.getMessage());
        }
        return testSvolti;
    }

    @Override
    public Paziente numTestSvoltiPerPaziente(Utente paziente) throws EccezioneDAO{
        int count = 0;
        Paziente pazienteDaRitornare= new Paziente();
        try {
            for (TestPsicologico testPsicologico : testPsicologiciInMemoria) {
                if (testPsicologico.getPaziente().getUsername().equals(paziente.getUsername())) {
                    count += testPsicologico.getStatoNotificaPsicologo();
                }
            }
            pazienteDaRitornare.setNumeroTest(count);
            return pazienteDaRitornare;
        }catch (Exception e) {
            throw new EccezioneDAO("Errore durante la ricerca dei test svolti per singolo paziente: " + e.getMessage());

        }
    }

    @Override
    public boolean getNumTestAssegnato(Utente paziente) throws EccezioneDAO {
        LocalDate oggi = LocalDate.now();

        try {
            for (TestPsicologico t : testPsicologiciInMemoria) {
                if (t.getPaziente().getUsername().equals(paziente.getUsername())) {

                    LocalDate dataTest = t.getData();
                    if (dataTest.equals(oggi)) {
                        return true;
                    }
                }
            }

            return false;
        }catch (Exception e) {
            throw new EccezioneDAO("Errore durante la ricerca dei test assegnati: " + e.getMessage());

        }
    }

}

