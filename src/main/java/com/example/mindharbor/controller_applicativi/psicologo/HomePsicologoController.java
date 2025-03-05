package com.example.mindharbor.controller_applicativi.psicologo;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.sessione.SessionManager;
import com.example.mindharbor.strumenti_utili.SetInfoUtente;

public class HomePsicologoController extends SetInfoUtente {
    private PsicologoBean psicologoBean;

    public InfoUtenteBean getInfoPsicologo() {
        return new SetInfoUtente().getInfo();
    }

    public PsicologoBean cercaNuoviTestSvolti() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        try {
             psicologoBean=new PsicologoBean(testPsicologicoDAO.getNumTestSvoltiDaNotificare(SessionManager.getInstance().getCurrentUser()));
        } catch (EccezioneDAO e ) {
            throw new EccezioneDAO(e.getMessage());
        }
        return psicologoBean;
    }

    public PsicologoBean cercaRichiesteAppuntamenti() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        try {
            psicologoBean= new PsicologoBean(appuntamentoDAO.getNumRicAppDaNotificare(SessionManager.getInstance().getCurrentUser()));
        } catch (EccezioneDAO e ) {
            throw new EccezioneDAO(e.getMessage());
        }
        return psicologoBean;
    }

    public void logout() {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.logout();
    }
}
