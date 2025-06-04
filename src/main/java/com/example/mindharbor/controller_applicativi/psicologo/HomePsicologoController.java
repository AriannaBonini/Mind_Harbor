package com.example.mindharbor.controller_applicativi.psicologo;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.dao.TestPsicologicoDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.TestPsicologico;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.sessione.SessionManager;
import com.example.mindharbor.strumenti_utili.SetInfoUtente;

public class HomePsicologoController extends SetInfoUtente {

    public InfoUtenteBean getInfoPsicologo() {
        return new SetInfoUtente().getInfo();
    }

    public PsicologoBean cercaNuoviTestSvolti() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        TestPsicologicoDAO testPsicologicoDAO= daoFactoryFacade.getTestPsicologicoDAO();
        PsicologoBean psicologoBean= new PsicologoBean();
        try {
             TestPsicologico testPsicologico= testPsicologicoDAO.getNumTestSvoltiDaNotificare(SessionManager.getInstance().getPsicologoCorrente());
            psicologoBean.setNumNotifiche(testPsicologico.getStatoNotificaPsicologo());

        } catch (EccezioneDAO e ) {
            throw new EccezioneDAO(e.getMessage());
        }
        return  psicologoBean;
    }

    public PsicologoBean cercaRichiesteAppuntamenti() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        try {
            Appuntamento appuntamento= appuntamentoDAO.notificheNuoviAppuntamentiPsicologo(SessionManager.getInstance().getPsicologoCorrente());
            return new PsicologoBean(appuntamento.getStatoNotificaPsicologo());
        } catch (EccezioneDAO e ) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public void logout() {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.logout();
    }
}
