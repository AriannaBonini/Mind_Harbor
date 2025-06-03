package com.example.mindharbor.controller_applicativi.psicologo;

import com.example.mindharbor.beans.*;
import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.dao.TerapiaDAO;
import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezionePazienteNonAutorizzato;
import com.example.mindharbor.mockapi.BoundaryMockAPI;
import com.example.mindharbor.model.*;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.sessione.SessionManager;
import com.example.mindharbor.strumenti_utili.SetInfoUtente;
import com.example.mindharbor.strumenti_utili.ValidatoreSessione;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrescriviTerapia {

    public InfoUtenteBean getInfoUtente() {return new SetInfoUtente().getInfo();}

    public List<PazienteBean> getListaPazienti() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade = DAOFactoryFacade.getInstance();
        PazienteDAO pazienteDAO = daoFactoryFacade.getPazienteDAO();
        UtenteDAO utenteDAO=daoFactoryFacade.getUtenteDAO();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();

        Psicologo psicologoCorrente = SessionManager.getInstance().getPsicologoCorrente();

        try {
            if (ValidatoreSessione.controllaPresenzaPazientiDelloPsicologoCorrente(psicologoCorrente.getPazienti())) {
                Paziente numeroTestSvoltiPaziente;
                for(Paziente paziente: psicologoCorrente.getPazienti()) {
                    numeroTestSvoltiPaziente = testPsicologicoDAO.numTestSvoltiPerPaziente(paziente);
                    paziente.setNumeroTest(numeroTestSvoltiPaziente.getNumeroTest());
                }

                return creaListaPazientiBean(psicologoCorrente.getPazienti());
            }

            List<Paziente> listaPazienti = pazienteDAO.trovaPazienti(psicologoCorrente);
            for(Paziente paziente : listaPazienti) {
                Utente utente= utenteDAO.trovaInfoUtente(paziente);

                paziente.setNome(utente.getNome());
                paziente.setCognome(utente.getCognome());
                paziente.setGenere(utente.getGenere());

                Paziente numeroTestPaziente= testPsicologicoDAO.numTestSvoltiPerPaziente(paziente);
                paziente.setNumeroTest(numeroTestPaziente.getNumeroTest());

            }

            psicologoCorrente.setPazienti(listaPazienti);

            return creaListaPazientiBean(listaPazienti);

        } catch (EccezioneDAO e) {
            throw new EccezioneDAO("Errore durante il recupero della lista pazienti: " + e.getMessage());
        }
    }

    private List<PazienteBean> creaListaPazientiBean(List<Paziente> listaPazienti) {
        List<PazienteBean> listaPazientiBean = new ArrayList<>();

        for (Paziente p : listaPazienti) {
            PazienteBean bean = new PazienteBean(
                    p.getUsername(),
                    p.getNumeroTest(),
                    p.getNome(),
                    p.getCognome(),
                    p.getGenere()
            );
            listaPazientiBean.add(bean);
        }

        return listaPazientiBean;
    }

    public boolean esistenzaTestSvoltiSenzaPrescrizione(PazienteBean pazienteSelezionato) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        TerapiaDAO terapiaDAO= daoFactoryFacade.getTerapiaDAO();

        try {
            List<TestPsicologico> listaTestsvolti = testPsicologicoDAO.listaTestSvolti(SessionManager.getInstance().getPsicologoCorrente(), new Paziente(pazienteSelezionato.getUsername()));
            for(TestPsicologico testPsicologico: listaTestsvolti) {
                if(!terapiaDAO.controlloEsistenzaTerapiaPerUnTest(testPsicologico)) {
                    return true;
                }
            }
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return false;
    }

    public boolean controlloAssegnazioneTestOdierno(PazienteBean pazienteSelezionato) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        try {
            return testPsicologicoDAO.getNumTestAssegnato(new Paziente(pazienteSelezionato.getUsername()));
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public PazienteBean notificheTestSvolti(PazienteBean pazienteSelezionato) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();

        try {
            Paziente paziente= testPsicologicoDAO.numTestSvoltiPerPaziente(new Paziente(pazienteSelezionato.getUsername()));
            pazienteSelezionato.setNumTestSvolti(paziente.getNumeroTest());
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO("Errore durante il conteggio delle notifiche dei test svolti dal paziente " + e );
        }
        return pazienteSelezionato;
    }

    public PazienteBean getSchedaPersonale(PazienteBean pazienteSelezionato) throws EccezioneDAO,EccezionePazienteNonAutorizzato {
        Psicologo psicologo=SessionManager.getInstance().getPsicologoCorrente();
        List<Paziente> listaPazienti=psicologo.getPazienti();

        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        PazienteDAO pazienteDAO = daoFactoryFacade.getPazienteDAO();
        try {
            int indice = ValidatoreSessione.controllaPresenzaPazienteNellaListaPazienti(SessionManager.getInstance().getPsicologoCorrente().getPazienti(),pazienteSelezionato.getUsername());
            if(indice!=-1) {
                if(ValidatoreSessione.controllaPresenzaAnniEDiagnosiDelPaziente(indice, SessionManager.getInstance().getPsicologoCorrente().getPazienti())){
                    pazienteSelezionato.setAnni(listaPazienti.get(indice).getAnni());
                    pazienteSelezionato.setDiagnosi(listaPazienti.get(indice).getDiagnosi());
                }else {
                    Paziente paziente=pazienteDAO.getInfoSchedaPersonale(listaPazienti.get(indice));
                    aggiornaSessione(indice,paziente);
                    pazienteSelezionato.setAnni(paziente.getAnni());
                    pazienteSelezionato.setDiagnosi(paziente.getDiagnosi());
                }
            }else {
                /*
                Se entro in questo else vuol dire che il paziente non è presente nella lista del mio psicologo, questo comporta l'impossibilità di andare avanti
                poiché lo psicologo può visualizzare SOLO i suoi pazienti.
                 */
                throw new EccezionePazienteNonAutorizzato("Il paziente non appartiene allo psicologo");
            }
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return pazienteSelezionato;
    }

    private void aggiornaSessione(Integer indice, Paziente paziente) {
        Paziente pazienteDaAggiornare= SessionManager.getInstance().getPsicologoCorrente().getPazienti().get(indice);
        pazienteDaAggiornare.setAnni(paziente.getAnni());
        pazienteDaAggiornare.setDiagnosi(paziente.getDiagnosi());

    }


    public List<TestBean> getListaTest() {return BoundaryMockAPI.testPiscologici();}

    public List<TestBean> getListaTestAssegnati() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        List<TestBean> testBeanList = new ArrayList<>();

        if (SessionManager.getInstance().getPazienteCorrente().getPsicologo().getUsername() != null) {
            try {
                List<TestPsicologico> listaTest = testPsicologicoDAO.trovaListaTest(SessionManager.getInstance().getPazienteCorrente());

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

    public boolean controlloNumeroTestSelezionati(Integer contatore, TestBean testBean) throws EccezioneDAO {
        if (contatore !=1) {
            return false;
        }else {
            try {
                /*Se il controllo va a buon fine, il controller applicativo chiede di inserire il nuovo test in persistenza*/
                assegnaNuovoTest(testBean);
                return true;
            }catch (EccezioneDAO e) {
                throw new EccezioneDAO(e.getMessage());
            }
        }
    }

    public void assegnaNuovoTest(TestBean testBean) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        try {
            testPsicologicoDAO.assegnaTest(new TestPsicologico(SessionManager.getInstance().getPsicologoCorrente(), new Paziente(testBean.getPaziente()), testBean.getNomeTest()));
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public List<TestBean> getTestSvoltiSenzaPrescrizione(PazienteBean pazienteSelezionato) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        TerapiaDAO terapiaDAO= daoFactoryFacade.getTerapiaDAO();

        List<TestBean> testSvoltiBean= new ArrayList<>();
        try {
            List<TestPsicologico> testSvolti = testPsicologicoDAO.listaTestSvoltiSenzaPrescrizione(pazienteSelezionato.getUsername(), SessionManager.getInstance().getPsicologoCorrente().getUsername());
            for(TestPsicologico test : testSvolti) {

                test.setPaziente(new Paziente(pazienteSelezionato.getUsername()));
                test.setPsicologo(SessionManager.getInstance().getPsicologoCorrente());

                if(!terapiaDAO.controlloEsistenzaTerapiaPerUnTest(test)) {
                    TestBean testSvoltoBean = new TestBean(test.getTest(),
                            "",
                            "",
                            test.getRisultato(),
                            test.getData(),
                            null);

                    testSvoltiBean.add(testSvoltoBean);
                }
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
                testPsicologicoDAO.modificaStatoNotificaTest(SessionManager.getInstance().getPazienteCorrente(),null);
            }else {
                testPsicologicoDAO.modificaStatoNotificaTest(SessionManager.getInstance().getPsicologoCorrente(), new Paziente(pazienteSelezionato.getUsername()));
            }
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public void aggiungiNuovaTerapia(TerapiaBean terapiaBean) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TerapiaDAO terapiaDAO= daoFactoryFacade.getTerapiaDAO();
        try {
            Terapia terapia = new Terapia(new TestPsicologico(terapiaBean.getDataTest(), null, SessionManager.getInstance().getPsicologoCorrente(), new Paziente(terapiaBean.getPaziente()), "", null), terapiaBean.getTerapia(), terapiaBean.getDataTerapia());
            terapiaDAO.aggiungiTerapia(terapia);
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public InfoUtenteBean infoPsicologo() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade = DAOFactoryFacade.getInstance();
        UtenteDAO utenteDAO = daoFactoryFacade.getUtenteDAO();

        Psicologo psicologoCorrente = SessionManager.getInstance().getPazienteCorrente().getPsicologo();
        if (!ValidatoreSessione.psicologoDelPazienteCorrente(SessionManager.getInstance().getPazienteCorrente().getPsicologo())) {
            return null;
        }

        if (ValidatoreSessione.controllaPresenzaNomeECognomePsicologo(SessionManager.getInstance().getPazienteCorrente().getPsicologo())) {
            return new InfoUtenteBean(psicologoCorrente.getNome(), psicologoCorrente.getCognome());
        }
        try {
            Utente psicologo = utenteDAO.trovaNomeCognome(psicologoCorrente);

            psicologoCorrente.setNome(psicologo.getNome());
            psicologoCorrente.setCognome(psicologo.getCognome());

            return new InfoUtenteBean(psicologo.getNome(), psicologo.getCognome());

        } catch (EccezioneDAO e) {
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

    private Double calcolaProgresso(RisultatiTestBean risultatoTest, Date dataTest, String nomeTest) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();

        double progressi;
        try {

            TestPsicologico ultimoTestPsicologico = testPsicologicoDAO.trovaTestPassati(new TestPsicologico(dataTest, risultatoTest.getRisultatoUltimoTest(), null, SessionManager.getInstance().getPazienteCorrente(),nomeTest, null));

            if (ultimoTestPsicologico == null) {
                return null;

            } else {
                if (ultimoTestPsicologico.getRisultato() == 0) {
                    progressi = (double) risultatoTest.getRisultatoUltimoTest() * 10;
                    progressi = Math.round(progressi * 100.0) / 100.0;
                    return progressi;

                }
                if (risultatoTest.getRisultatoUltimoTest() == 0) {
                    progressi = (double) (-ultimoTestPsicologico.getRisultato()) * 10;
                    progressi = Math.round(progressi * 100.0) / 100.0;
                    return progressi;

                }

                progressi = (risultatoTest.getRisultatoUltimoTest() - ultimoTestPsicologico.getRisultato());
                progressi = (progressi / Math.abs(ultimoTestPsicologico.getRisultato())) * 100;
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

            List<Terapia> terapie = terapiaDAO.getTerapie(SessionManager.getInstance().getPazienteCorrente());

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


    public boolean controllaEsistenzaPsicologo() {return SessionManager.getInstance().getPazienteCorrente().getPsicologo()!=null &&   SessionManager.getInstance().getPazienteCorrente().getPsicologo().getUsername() !=null;}


}
