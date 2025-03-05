package com.example.mindharbor.controller_applicativi;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.sessione.SessionManager;
import com.example.mindharbor.tipo_utente.UserType;
import com.example.mindharbor.beans.CredenzialiLoginBean;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.eccezioni.EccezioneSessioneUtente;
import com.example.mindharbor.model.Utente;

public class LoginController extends AbstractController {


    public InfoUtenteBean login(CredenzialiLoginBean credenziali) throws EccezioneDAO, EccezioneSessioneUtente {
        DAOFactoryFacade daoFactoryFacade=DAOFactoryFacade.getInstance();
        UtenteDAO utenteDAO= daoFactoryFacade.getUtenteDAO();
        PazienteDAO pazienteDAO= daoFactoryFacade.getPazienteDAO();

        Utente credenzialiUtenteLogin= new Utente(credenziali.getUsername(), credenziali.getPassword());
        InfoUtenteBean infoUtente=null;
        try {
            Utente utente = utenteDAO.trovaUtente(credenzialiUtenteLogin);
            if (utente != null) {
                infoUtente = new InfoUtenteBean(utente.getUserType());
                if (utente.getUserType().equals(UserType.PAZIENTE)) {
                    storeSessionUtente(utente.getUsername(), utente.getNome(), utente.getCognome(), utente.getUserType(), pazienteDAO.getUsernamePsicologo(utente));
                } else {
                    storeSessionUtente(utente.getUsername(), utente.getNome(), utente.getCognome(), utente.getUserType());
                }
            }
            return infoUtente;
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    protected void storeSessionUtente(String username, String nome, String cognome, UserType userType,String usernamePsicologo) throws EccezioneSessioneUtente {
        SessionManager sessionManager = SessionManager.getInstance();
        Utente currentUser = new Utente(username, nome, cognome, userType);
        sessionManager.login(currentUser,usernamePsicologo);
    }

    @Override
    protected void storeSessionUtente(String username, String nome, String cognome, UserType userType) throws EccezioneSessioneUtente {
        SessionManager sessionManager = SessionManager.getInstance();
        Utente currentUser = new Utente(username, nome, cognome, userType);
        sessionManager.login(currentUser,null); //sarebbe meglio cambiare nome al metodo in creaSessione
    }

}