package com.example.mindharbor.controller_applicativi.psicologo;

import com.example.mindharbor.beans.*;
import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.dao.TerapiaDAO;
import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.mockapi.BoundaryMockAPI;
import com.example.mindharbor.model.*;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.sessione.SessionManager;
import com.example.mindharbor.strumenti_utili.NavigatorSingleton;
import com.example.mindharbor.strumenti_utili.SetInfoUtente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescriviTerapia {

    private final NavigatorSingleton navigator = NavigatorSingleton.getInstance();

    public InfoUtenteBean getInfoUtente() {return new SetInfoUtente().getInfo();}

    public List<PazienteBean> getListaPazienti() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade = DAOFactoryFacade.getInstance();
        PazienteDAO pazienteDAO = daoFactoryFacade.getPazienteDAO();
        try {

            List<Paziente> listaPazienti = pazienteDAO.trovaPazienti(
                    SessionManager.getInstance().getCurrentUser());
            List<PazienteBean> pazientiNumTestBeanList = new ArrayList<>();

            for (Paziente paz : listaPazienti) {
                PazienteBean pazientiTestBean = new PazienteBean(
                        paz.getUsername(),
                        paz.getNumeroTest(),
                        paz.getNome(),
                        paz.getCognome(),
                        paz.getGenere());

                pazientiNumTestBeanList.add(pazientiTestBean);
            }
            return pazientiNumTestBeanList;

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public Integer numeroTestSvoltiSenzaPrescrizione(PazienteBean pazienteSelezionato) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        try {
            return testPsicologicoDAO.getNumTestSvoltiSenzaPrescrizione(SessionManager.getInstance().getCurrentUser(), new Paziente(pazienteSelezionato.getUsername()));
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public Integer getNumeroTestOdiernoAssegnato(PazienteBean pazienteSelezionato) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        try {
            return testPsicologicoDAO.getNumTestAssegnato(new Paziente(pazienteSelezionato.getUsername()));
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public PazienteBean getSchedaPersonale(PazienteBean pazienteSelezionato) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        PazienteDAO pazienteDAO = daoFactoryFacade.getPazienteDAO();
        try {
            Paziente paziente = pazienteDAO.getInfoSchedaPersonale(new Paziente(pazienteSelezionato.getUsername()));

            pazienteSelezionato.setAnni(paziente.getAnni());
            pazienteSelezionato.setDiagnosi(paziente.getDiagnosi());
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return pazienteSelezionato;
    }

    public List<TestBean> getListaTest() {return BoundaryMockAPI.testPiscologici();}

    public List<TestBean> getListaTestAssegnati() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        List<TestBean> testBeanList = new ArrayList<>();

        if (SessionManager.getInstance().getUsernamePsicologo() != null) {
            try {
                List<TestPsicologico> listaTest = testPsicologicoDAO.trovaListaTest(SessionManager.getInstance().getCurrentUser());

                for (TestPsicologico test : listaTest) {
                    TestBean testBean = new TestBean(
                            test.getTest(),
                            test.getRisultato(),
                            test.getData(),
                            test.getSvolto());

                    testBeanList.add(testBean);
                }
            } catch (EccezioneDAO e) {
                throw new EccezioneDAO(e.getMessage());
            }
        }
        return testBeanList;
    }

    public boolean controlloNumeroTestSelezionati(Integer contatore, String nomeTest) throws EccezioneDAO {
        if (contatore !=1) {
            return false;
        }else {
            try {
                //Se il controllo va a buon fine, il controller applicativo chiede di inserire il nuovo test in persistenza
                //inoltre, elimina la bean pazienteSelezionato dal Navigator Singleton visto che da questo momento non servirà più.
                notificaTest(nomeTest);
                return true;
            }catch (EccezioneDAO e) {
                throw new EccezioneDAO(e.getMessage());
            }
        }
    }

    private void notificaTest(String nomeTest) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        try {
            testPsicologicoDAO.assegnaTest(new TestPsicologico((new Psicologo(SessionManager.getInstance().getCurrentUser().getUsername())), new Paziente(getPazienteSelezionato().getUsername()), nomeTest));
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public List<TestBean> getTestSvoltiSenzaPrescrizione(PazienteBean pazienteSelezionato) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();

        List<TestBean> testSvoltiBean= new ArrayList<>();
        try {
            List<TestPsicologico> testSvolti = testPsicologicoDAO.listaTestSvoltiSenzaPrescrizione(pazienteSelezionato.getUsername(), SessionManager.getInstance().getCurrentUser().getUsername());

            for (TestPsicologico test : testSvolti) {
                TestBean testSvoltoBean = new TestBean(test.getTest(),
                        "",
                        "",
                        test.getRisultato(),
                        test.getData(),
                        null);

                testSvoltiBean.add(testSvoltoBean);
            }
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return testSvoltiBean;
    }

    public void aggiornaStatoNotificaTest(PazienteBean pazienteSelezionato) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        try {
            if(pazienteSelezionato==null) {
                testPsicologicoDAO.modificaStatoNotificaTest(SessionManager.getInstance().getCurrentUser(),null);
            }else {
                testPsicologicoDAO.modificaStatoNotificaTest(SessionManager.getInstance().getCurrentUser(), new Paziente(pazienteSelezionato.getUsername()));
            }
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public void aggiungiTerapia(TerapiaBean terapiaBean) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TerapiaDAO terapiaDAO= daoFactoryFacade.getTerapiaDAO();
        try {
            Terapia terapia = new Terapia(new TestPsicologico(terapiaBean.getDataTest(), null, new Psicologo(terapiaBean.getPsicologo()), new Paziente(terapiaBean.getPaziente()), "", null), terapiaBean.getTerapia(), terapiaBean.getDataTerapia());
            terapiaDAO.insertTerapia(terapia);
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public InfoUtenteBean infoPsicologo() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        UtenteDAO utenteDAO=daoFactoryFacade.getUtenteDAO();

        if(SessionManager.getInstance().getUsernamePsicologo()==null) {
            return null;
        }
        try {
            Utente utente= utenteDAO.trovaNomeCognome(new Utente(SessionManager.getInstance().getUsernamePsicologo()));
            return new InfoUtenteBean(utente.getNome(),utente.getCognome());
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public DomandeTestBean cercaDomande(TestBean testSelezionato) {return BoundaryMockAPI.domandeTest(testSelezionato.getNomeTest());}

    public RisultatiTestBean calcolaRisultato(DomandeTestBean punteggiBean, TestBean testSelezionato) throws EccezioneDAO {
        RisultatiTestBean risultatoTest= new RisultatiTestBean();
        try {
            Integer risultato = 0;

            for (Integer punteggio : punteggiBean.getPunteggi()) {
                risultato += punteggio;
            }
            risultatoTest.setRisultatoUltimoTest(risultato);
            Double progresso = calcolaProgresso(risultatoTest,testSelezionato.getData(),testSelezionato.getNomeTest());

            risultatoTest.setRisultatoTestPrecedente(progresso);
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return risultatoTest;
    }

    public Double calcolaProgresso(RisultatiTestBean risultatoTest, Date dataTest, String nomeTest) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();

        double progressi;
        try {

            Integer punteggioPassato = testPsicologicoDAO.trovaTestPassati(new TestPsicologico(dataTest, risultatoTest.getRisultatoUltimoTest(), null, new Paziente(SessionManager.getInstance().getCurrentUser().getUsername()),nomeTest, null));

            if (punteggioPassato == null) {
                return null;

            } else {
                if (punteggioPassato == 0) {
                    progressi = (double) risultatoTest.getRisultatoUltimoTest() * 10;
                    progressi = Math.round(progressi * 100.0) / 100.0;
                    return progressi;

                }
                if (risultatoTest.getRisultatoUltimoTest() == 0) {
                    progressi = (double) (-punteggioPassato) * 10;
                    progressi = Math.round(progressi * 100.0) / 100.0;
                    return progressi;

                }

                progressi = (risultatoTest.getRisultatoUltimoTest() - punteggioPassato);
                progressi = (progressi / Math.abs(punteggioPassato)) * 100;
                progressi = Math.round(progressi * 100.0) / 100.0;

            }
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return progressi;
    }

    public List<TerapiaBean> trovaTerapie() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TerapiaDAO terapiaDAO= daoFactoryFacade.getTerapiaDAO();

        List<TerapiaBean> terapieBean=new ArrayList<>();
        try {

            List<Terapia> terapie = terapiaDAO.getTerapie(SessionManager.getInstance().getCurrentUser());

            for (Terapia terapia : terapie) {
                TerapiaBean terapiaBean = new TerapiaBean(terapia.getTestPsicologico().getPsicologo().getUsername(),
                        " ",
                        terapia.getTerapia(),
                        terapia.getDataTerapia(),
                        null);

                terapieBean.add(terapiaBean);
            }
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }

        return terapieBean;
    }

    public boolean controllaEsistenzaPsicologo() {return SessionManager.getInstance().getUsernamePsicologo()!=null;}


    public void setPazienteSelezionato(PazienteBean pazienteSelezionato) {navigator.setPazienteBean(pazienteSelezionato);}
    public PazienteBean getPazienteSelezionato(){return navigator.getPazienteBean();}
    public void eliminaPazienteSelezionato() {navigator.eliminaPazienteBean();}
    public void azzeraIlNumeroDiTestSvolti() {navigator.getPazienteBean().setNumTestSvolti(0);}
    public void setTestSelezionato(TestBean testSelezionato) {navigator.setTestBean(testSelezionato);}
    public TestBean getTestSelezionato(){return navigator.getTestBean();}
    public void eliminaTestSelezionato(){navigator.eliminaTestBean();}

}
