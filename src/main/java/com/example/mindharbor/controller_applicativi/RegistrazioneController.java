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
        controlloDatiPaziente(pazienteBean);

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
            if (Boolean.TRUE.equals(utenteDAO.controllaUsernameERegistraNuovoUtente(paziente))) {
                pazienteDAO.inserisciDatiPaziente(paziente);
            } else {
                throw new EccezioneDAO("Username già in uso");
            }

        } catch (EccezioneDAO e) {
            throw new EccezioneDAO(e.getMessage());
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    public void registrazionePsicologo(PsicologoBean psicologoBean) throws EccezioneDAO {
        controlloDatiPsicologo(psicologoBean);

        DAOFactoryFacade daoFactoryFacade = DAOFactoryFacade.getInstance();
        UtenteDAO utenteDAO = daoFactoryFacade.getUtenteDAO();
        PsicologoDAO psicologoDAO = daoFactoryFacade.getPsicologoDAO();

        Psicologo psicologo = new Psicologo(
                psicologoBean.getUsername(),
                psicologoBean.getNome(),
                psicologoBean.getCognome(),
                UserType.PSICOLOGO,
                psicologoBean.getGenere(),
                psicologoBean.getPassword());

                psicologo.setNomeStudio(psicologoBean.getNomeStudio());
                psicologo.setCostoOrario(psicologoBean.getCostoOrario());

        try {
            if (Boolean.TRUE.equals(utenteDAO.controllaUsernameERegistraNuovoUtente(psicologo))) {
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

    private void controlloDatiPaziente(PazienteBean pazienteBean) {
        controllaNome(pazienteBean.getNome());
        controllaCognome(pazienteBean.getCognome());
        controllaPassword(pazienteBean.getPassword());
        controllaAnni(pazienteBean.getAnni());
        controlloUsername(pazienteBean.getUsername());
        controlloGenere(pazienteBean.getGenere());
    }

    private void controlloDatiPsicologo(PsicologoBean psicologoBean) {
        controllaNome(psicologoBean.getNome());
        controllaCognome(psicologoBean.getCognome());
        controllaPassword(psicologoBean.getPassword());
        controllaNomeStudio(psicologoBean.getNomeStudio());
        controllaCostoOrario(psicologoBean.getCostoOrario());
        controlloUsername(psicologoBean.getUsername());
        controlloGenere(psicologoBean.getGenere());
    }


    private void controllaPassword(String password) {
        if (password.length() < 8 || password.length()>45 ||  password.chars().noneMatch(Character::isUpperCase) || password.chars().noneMatch(Character::isDigit) &&
                password.chars().noneMatch(c -> "!@#$%^&*(),.?\":{}|<>".indexOf(c) >= 0)) {
            throw new IllegalArgumentException("La password deve contenere da 8 a 45 caratteri, di cui almeno una lettere maiuscola e almeno un numero e/o un carattere speciale.");
        }
    }

    private void controllaNomeStudio(String nomeStudio) {
        if (nomeStudio.length() > 45) {
            throw new IllegalArgumentException("Username non può superare i 45 caratteri.");
        }
    }

    private void controllaCostoOrario(Integer costoOrario) {
        if (costoOrario<=0) {
            throw new IllegalArgumentException("Il costo orario non può essere uguale o minore di zero");
        }
    }

    private void controllaNome(String nome) {
        if (nome.length() > 45) {
            throw new IllegalArgumentException("Il nome non può superare i 45 caratteri.");
        }
    }
    private void controllaCognome(String cognome) {
        if (cognome.length() > 45) {
            throw new IllegalArgumentException("Il cognome non può superare i 45 caratteri.");
        }
    }
    private void controllaAnni(Integer anni) {
     if(anni <18 || anni > 100) {
         throw new IllegalArgumentException("Bisogna essere maggiorenni per potersi registrare");
     }
    }

    private void controlloUsername(String username) {
        if (username.length() > 45) {
            throw new IllegalArgumentException("Lo username non può superare i 45 caratteri.");
        }
    }

    private void controlloGenere(String genere) {
        if (genere.length() != 1 || (!genere.equals("M") && !genere.equals("F"))) {
            throw new IllegalArgumentException("Genere non valido. Deve essere 'M' o 'F'.");
        }
    }

}
