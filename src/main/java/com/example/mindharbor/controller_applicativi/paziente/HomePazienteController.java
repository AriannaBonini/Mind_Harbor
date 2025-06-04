package com.example.mindharbor.controller_applicativi.paziente;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.dao.TerapiaDAO;
import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.TestPsicologico;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.sessione.SessionManager;
import com.example.mindharbor.strumenti_utili.SetInfoUtente;


public class HomePazienteController {

    public InfoUtenteBean getInfoPaziente() {return new SetInfoUtente().getInfo();}

    public PazienteBean cercaNuoviTestDaSvolgere() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();

        try {
            TestPsicologico testPsicologico = testPsicologicoDAO.getNotificaPazientePerTestAssegnato(SessionManager.getInstance().getPazienteCorrente());
            return new PazienteBean(testPsicologico.getStatoNotificaPaziente());
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public PazienteBean cercaNuoveTerapie() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TerapiaDAO terapiaDAO= daoFactoryFacade.getTerapiaDAO();
        try {
            return new PazienteBean((terapiaDAO.getNuoveTerapie(SessionManager.getInstance().getPazienteCorrente())).getNotificaPaziente());
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public PazienteBean cercaNuoviAppuntamenti() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        try {
            Appuntamento appuntamento= appuntamentoDAO.notificheNuoviAppuntamentiPaziente(SessionManager.getInstance().getPazienteCorrente());
            return new PazienteBean(appuntamento.getStatoNotificaPaziente());
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }
    public boolean getPsicologo() {
        Psicologo psicologo = SessionManager.getInstance().getPazienteCorrente().getPsicologo();
        return psicologo != null && psicologo.getUsername() != null;
    }

    public void logout() {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.logout();
    }
}
