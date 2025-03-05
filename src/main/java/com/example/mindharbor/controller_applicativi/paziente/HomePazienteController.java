package com.example.mindharbor.controller_applicativi.paziente;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.dao.TerapiaDAO;
import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.sessione.SessionManager;
import com.example.mindharbor.strumenti_utili.SetInfoUtente;


public class HomePazienteController {

    private PazienteBean pazienteBean;

    public InfoUtenteBean getInfoPaziente() {return new SetInfoUtente().getInfo();}

    public PazienteBean cercaNuoviTestDaSvolgere() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        try {
            pazienteBean = new PazienteBean(testPsicologicoDAO.getNotificaPazientePerTestAssegnato(SessionManager.getInstance().getCurrentUser()));

        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return pazienteBean;
    }

    public PazienteBean cercaNuoveTerapie() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TerapiaDAO terapiaDAO= daoFactoryFacade.getTerapiaDAO();
        try {
            pazienteBean =new PazienteBean(terapiaDAO.getNuoveTerapie(SessionManager.getInstance().getCurrentUser()));
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return pazienteBean;
    }

    public PazienteBean cercaNuoviAppuntamenti() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        try {
            pazienteBean =new PazienteBean(appuntamentoDAO.getNumRicAppDaNotificare(SessionManager.getInstance().getCurrentUser()));
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return pazienteBean;
    }

    public boolean getPsicologo() {return SessionManager.getInstance().getUsernamePsicologo() !=null; }

    public void logout() {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.logout();
    }
}
