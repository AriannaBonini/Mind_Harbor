package com.example.mindharbor.controller_applicativi;

import com.example.mindharbor.beans.InfoUtenteBean;
import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
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
                    assegnaSessione(utente.getUsername(), utente.getNome(), utente.getCognome(), utente.getUserType(), pazienteDAO.getUsernamePsicologo(utente));
                } else {
                    assegnaSessione(utente.getUsername(), utente.getNome(), utente.getCognome(), utente.getUserType());
                }
            }
            return infoUtente;
        }catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }
    }

    @Override
    protected void assegnaSessione(String username, String nome, String cognome, UserType userType,String usernamePsicologo) throws EccezioneSessioneUtente {
        SessionManager sessionManager = SessionManager.getInstance();
        Paziente utenteCorrente = new Paziente(username, nome, cognome, userType,new Psicologo(usernamePsicologo));
        sessionManager.login(utenteCorrente);
    }

    @Override
    protected void assegnaSessione(String username, String nome, String cognome, UserType userType) throws EccezioneSessioneUtente {
        SessionManager sessionManager = SessionManager.getInstance();
        Psicologo utenteCorrente = new Psicologo(username, nome, cognome, userType);
        sessionManager.login(utenteCorrente);
    }

}