package com.example.mindharbor.dao.memoria;

import com.example.mindharbor.dao.UtenteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;
import com.example.mindharbor.tipo_utente.UserType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtenteDAOMemoria implements UtenteDAO {
    private final Map<String, Utente> utentiInMemoria = new HashMap<>();

    @Override
    public Utente trovaUtente(Utente credenzialiUtenteLogin) throws EccezioneDAO {
        try {
            for (Utente utente : utentiInMemoria.values()) {
                if (utente.getUsername().equals(credenzialiUtenteLogin.getUsername()) &&
                        utente.getPassword().equals(credenzialiUtenteLogin.getPassword())) {
                    return getTipoUtente(utente);
                }
            }
            return null;
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante la ricerca dell'utente: " + e.getMessage());
        }
    }

    protected Utente getTipoUtente(Utente utente) {
        UserType tipo;

        if ("Paziente".equalsIgnoreCase(utente.getUserType().toString())) {
            tipo = UserType.PAZIENTE;
        } else {
            tipo = UserType.PSICOLOGO;
        }

        return new Utente(
                utente.getUsername(),
                utente.getNome(),
                utente.getCognome(),
                tipo);
    }

    @Override
    public Utente trovaNomeCognome(Utente utente) throws EccezioneDAO {
        try {
            Utente trovato = utentiInMemoria.get(utente.getUsername());

            if (trovato != null) {
                return new Utente("", trovato.getNome(), trovato.getCognome(), "");
            }

            return null;
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante la ricerca di nome e cognome: " + e.getMessage());
        }
    }


    @Override
    public List<Psicologo> listaUtentiDiTipoPsicologo(Utente psicologo) throws EccezioneDAO {
        List<Psicologo> listaPsicologi = new ArrayList<>();

        try {
            if (psicologo != null && psicologo.getUsername() != null) {
                Utente utente = utentiInMemoria.get(psicologo.getUsername());
                if (utente instanceof Psicologo p) {
                    listaPsicologi.add(new Psicologo(p.getUsername(), p.getNome(), p.getCognome(), p.getGenere()));
                }
            } else {

                for (Utente utente : utentiInMemoria.values()) {
                    if (utente instanceof Psicologo p) {
                        listaPsicologi.add(new Psicologo(p.getUsername(), p.getNome(), p.getCognome(), p.getGenere()));
                    }
                }
            }
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante il recupero della lista degli psicologi: " + e.getMessage());
        }

        return listaPsicologi;
    }

    @Override
    public List<Appuntamento> richiestaAppuntamentiInfoPaziente(List<Appuntamento> richiesteAppuntamenti) throws EccezioneDAO {
        try {
            for (Appuntamento appuntamento : richiesteAppuntamenti) {
                String usernamePaziente = appuntamento.getPaziente().getUsername();
                Utente pazienteCompleto = utentiInMemoria.get(usernamePaziente);

                if (pazienteCompleto != null) {
                    appuntamento.getPaziente().setNome(pazienteCompleto.getNome());
                    appuntamento.getPaziente().setCognome(pazienteCompleto.getCognome());
                    appuntamento.getPaziente().setGenere(pazienteCompleto.getGenere());
                }
            }
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante il recupero delle info paziente: " + e.getMessage());
        }
        return richiesteAppuntamenti;
    }

    @Override
    public Utente trovaInfoUtente(Utente paziente) throws EccezioneDAO {
        try {
            Utente utenteCompleto = utentiInMemoria.get(paziente.getUsername());

            if (utenteCompleto != null) {
                return new Utente("", utenteCompleto.getNome(), utenteCompleto.getCognome(), utenteCompleto.getGenere());
            }

            return null;
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante il recupero info utente: " + e.getMessage());
        }
    }

    @Override
    public Boolean controllaUsernameERegistraNuovoUtente(Utente utente) throws EccezioneDAO {
        try {
            if (utentiInMemoria.containsKey(utente.getUsername())) {
                return false;
            }
            registraNuovoUtente(utente);
            return true;
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante la registrazione utente: " + e.getMessage());
        }
    }

    private void registraNuovoUtente(Utente utente) throws EccezioneDAO {
        if (utentiInMemoria.putIfAbsent(utente.getUsername(), utente) != null) {
            throw new EccezioneDAO("Username già esistente");
        }
    }






}
