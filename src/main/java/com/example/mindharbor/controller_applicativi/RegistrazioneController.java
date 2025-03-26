package com.example.mindharbor.controller_applicativi;

import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.beans.PsicologoBean;
import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.dao.PsicologoDAO;
import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.tipo_utente.UserType;

public class RegistrazioneController {

    public void registrazionePaziente(PazienteBean pazienteBean) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade = DAOFactoryFacade.getInstance();
        UtenteDAO utenteDAO = daoFactoryFacade.getUtenteDAO();
        PazienteDAO pazienteDAO = daoFactoryFacade.getPazienteDAO();

        Paziente paziente = new Paziente(
                pazienteBean.getUsername(),
                pazienteBean.getNome(),
                pazienteBean.getCognome(),
                UserType.PAZIENTE,
                pazienteBean.getGenere(),
                pazienteBean.getPassword(),
                pazienteBean.getAnni()
        );

        try {
            if (utenteDAO.controllaUsernameERegistraNuovoUtente(paziente)) {
                pazienteDAO.inserisciDatiPaziente(paziente);
            } else {
                throw new EccezioneDAO("Username già in uso");
            }

        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public void registrazionePsicologo(PsicologoBean psicologoBean) throws EccezioneDAO {
        DAOFactoryFacade daoFactoryFacade = DAOFactoryFacade.getInstance();
        UtenteDAO utenteDAO = daoFactoryFacade.getUtenteDAO();
        PsicologoDAO psicologoDAO = daoFactoryFacade.getPsicologoDAO();

        Psicologo psicologo = new Psicologo(
                psicologoBean.getUsername(),
                psicologoBean.getNome(),
                psicologoBean.getCognome(),
                UserType.PSICOLOGO,
                psicologoBean.getGenere(),
                psicologoBean.getPassword(),
                psicologoBean.getNomeStudio(),
                psicologoBean.getCostoOrario()
        );

        try {
            if (utenteDAO.controllaUsernameERegistraNuovoUtente(psicologo)) {
                psicologoDAO.inserisciDatiPsicologo(psicologo);
            } else {
                throw new EccezioneDAO("Username già in uso");
            }

        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }


}
