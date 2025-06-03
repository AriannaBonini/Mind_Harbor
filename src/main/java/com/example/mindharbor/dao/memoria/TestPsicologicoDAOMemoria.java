package com.example.mindharbor.dao.memoria;

import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.TestPsicologico;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.tipo_utente.UserType;
import java.time.LocalDate;
import java.util.*;

public class TestPsicologicoDAOMemoria implements TestPsicologicoDAO {

    private final List<TestPsicologico> testPsicologiciInMemoria = new ArrayList<>();

    @Override
    public void assegnaTest(TestPsicologico test) throws EccezioneDAO {
        try {
            TestPsicologico testDaInserire = new TestPsicologico(new java.sql.Date(System.currentTimeMillis()), 0, test.getPsicologo(), test.getPaziente(), test.getTest(), 0, 1, 0);
            testPsicologiciInMemoria.add(testDaInserire);
        }catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'inserimento del nuovo test: " + e.getMessage());
        }
    }

    @Override
    public TestPsicologico getNotificaPazientePerTestAssegnato(Paziente paziente) throws EccezioneDAO {
        TestPsicologico testPsicologico= new TestPsicologico();
        int numeroNuoviTestAssegnati = 0;
        try {
            for (TestPsicologico t : testPsicologiciInMemoria) {
                if (t.getPaziente().getUsername().equals(paziente.getUsername()) && t.getStatoNotificaPaziente() == 1) {
                    numeroNuoviTestAssegnati++;
                }
            }
            testPsicologico.setStatoNotificaPsicologo(numeroNuoviTestAssegnati);
            return testPsicologico;
        }catch (Exception e) {
            throw new EccezioneDAO("Errore durante il conteggio delle notifiche del paziente: " + e.getMessage());
        }
    }

    @Override
    public void modificaStatoNotificaTest(Utente utente, Paziente pazienteSelezionato) throws EccezioneDAO{
        try {
            if (utente.getUserType().equals(UserType.PAZIENTE)) {
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
    public List<TestPsicologico> trovaListaTest(Paziente paziente) throws EccezioneDAO{
        List<TestPsicologico> listaTest = new ArrayList<>();
        try {
            for (TestPsicologico t : testPsicologiciInMemoria) {
                if (t.getPaziente().getUsername().equals(paziente.getUsername())) {
                    TestPsicologico test = new TestPsicologico(
                            t.getData(),
                            t.getRisultato(),
                            t.getTest(),
                            t.getSvolto()
                    );

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
        Date data = null;
        try {
            for (TestPsicologico t : testPsicologiciInMemoria) {
                if (t.getPaziente().getUsername().equals(testDaAggiungere.getPaziente().getUsername())
                        && t.getTest().equals(testDaAggiungere.getTest())
                        && t.getSvolto() == 1) {
                    Date dataTest = t.getData();
                    if (dataTest.after(data)) {
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
    public Integer getNumTestSvoltiDaNotificare(Utente psicologo) throws EccezioneDAO {
        int count = 0;
        try {
        for (TestPsicologico t : testPsicologiciInMemoria) {
            if (t.getPsicologo().getUsername().equals(psicologo.getUsername()) && t.getStatoNotificaPsicologo() == 1) {
                count++;
            }
        }
        return count;
    }catch (Exception e) {
            throw new EccezioneDAO("Errore durante la ricerca dei test svolti da notificare: " + e.getMessage());
        }
    }

    @Override
    public List<TestPsicologico> listaTestSvolti(Utente utentePsicologo, Paziente paziente) throws EccezioneDAO {
        List<TestPsicologico> listaTestPsicologico= new ArrayList<>();

        try {
            for (TestPsicologico test : testPsicologiciInMemoria) {
                if (test.getSvolto() == 1
                        && test.getPsicologo().getUsername().equals(utentePsicologo.getUsername())
                        && test.getPaziente().getUsername().equals(paziente.getUsername())) {

                    TestPsicologico testPsicologico=new TestPsicologico(test.getData(),test.getPsicologo(),test.getPaziente());
                    listaTestPsicologico.add(testPsicologico);
                }
            }
            return listaTestPsicologico;
        }catch (Exception e) {
                throw new EccezioneDAO("Errore nel conteggio del numeri to test senza prescrizione: " + e.getMessage());
            }

    }


    @Override
    public List<TestPsicologico> listaTestSvoltiSenzaPrescrizione(String usernamePaziente, String usernamePsicologo) throws EccezioneDAO{
        List<TestPsicologico> testSvolti = new ArrayList<>();

        try{
            for (TestPsicologico test : testPsicologiciInMemoria) {
                if (test.getSvolto() == 1
                        && test.getPaziente().getUsername().equals(usernamePaziente)
                        && test.getPsicologo().getUsername().equals(usernamePsicologo)) {

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
    public boolean getNumTestAssegnato(Paziente paziente) throws EccezioneDAO {
        LocalDate oggi = LocalDate.now();

        try {
            for (TestPsicologico t : testPsicologiciInMemoria) {
                if (t.getPaziente().getUsername().equals(paziente.getUsername())) {

                    LocalDate dataTest = ((java.sql.Date) t.getData()).toLocalDate();
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

