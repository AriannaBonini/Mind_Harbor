package com.example.mindharbor.controller_applicativi;

import com.example.mindharbor.beans.AppuntamentiBean;
import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.dao.AppuntamentoDAO;
import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.sessione.SessionManager;
import com.example.mindharbor.strumenti_utili.SetInfoUtente;
import java.util.ArrayList;
import java.util.List;

public class AppuntamentiController {

    public InfoUtenteBean getInfoUtente() {return new SetInfoUtente().getInfo();}

    public void modificaStatoNotificaAppuntamenti() throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();
        try {
            appuntamentoDAO.aggiornaStatoNotificaPaziente(SessionManager.getInstance().getPazienteCorrente());
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    public List<AppuntamentiBean> getAppuntamentiPaziente(String selectedTabName ) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        UtenteDAO utenteDAO= daoFactoryFacade.getUtenteDAO();
        AppuntamentoDAO appuntamentoDAO= daoFactoryFacade.getAppuntamentoDAO();

        List<AppuntamentiBean> appuntamentiPazienteBeanList = new ArrayList<>();
        try {
            Utente infoPsicologo=utenteDAO.trovaNomeCognome(new Psicologo(SessionManager.getInstance().getPazienteCorrente().getPsicologo().getUsername()));
            List<Appuntamento> appuntamentoPazienteList = appuntamentoDAO.trovaAppuntamentiPaziente(SessionManager.getInstance().getPazienteCorrente(), selectedTabName);

            for (Appuntamento app : appuntamentoPazienteList) {
                AppuntamentiBean appuntamentiPazienteBean = new AppuntamentiBean(
                        app.getData(),
                        app.getOra(),
                        new PsicologoBean(infoPsicologo.getUsername(),infoPsicologo.getNome(),infoPsicologo.getCognome())
                );

                appuntamentiPazienteBeanList.add(appuntamentiPazienteBean);
            }
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return appuntamentiPazienteBeanList;
    }

    public List<AppuntamentiBean> getAppuntamentiPsicologo(String selectedTabName) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade = DAOFactoryFacade.getInstance();
        AppuntamentoDAO appuntamentoDAO = daoFactoryFacade.getAppuntamentoDAO();
        UtenteDAO utenteDAO= daoFactoryFacade.getUtenteDAO();

        List<AppuntamentiBean> appuntamentiPsicologoBeanList = new ArrayList<>();

        try {
            List<Appuntamento> appuntamentoPsicologoList = appuntamentoDAO.trovaAppuntamentiPsicologo(SessionManager.getInstance().getPsicologoCorrente(),selectedTabName);

            for (Appuntamento app : appuntamentoPsicologoList) {
                Utente utentePaziente=utenteDAO.trovaNomeCognome(app.getPaziente());

                AppuntamentiBean appuntamentiPsicologoBean = new AppuntamentiBean(
                        app.getData(),
                        app.getOra(),
                        new PazienteBean(utentePaziente.getNome(),utentePaziente.getCognome())
                );
                appuntamentiPsicologoBeanList.add(appuntamentiPsicologoBean);
            }

        }catch(EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
        return appuntamentiPsicologoBeanList;
    }

    public boolean getPsicologo() {
        /*
          Restituisce lo psicologo assegnato al paziente.
          Questo metodo viene utilizzato per evitare di cercare appuntamenti nella persistenza
          nel caso in cui il paziente non abbia ancora uno psicologo assegnato.
         */
        return SessionManager.getInstance().getPazienteCorrente().getPsicologo().getUsername() != null;}
}
