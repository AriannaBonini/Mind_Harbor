package com.example.mindharbor.sessione;


import com.example.mindharbor.eccezioni.EccezioneSessioneUtente;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static SessionManager instanza = null;
    private final Map<String, Utente> utentiLoggati;
    private String usernameUtenteCorrente;

    private SessionManager() {
        utentiLoggati = new HashMap<>();
        usernameUtenteCorrente = null;
    }

    public static synchronized SessionManager getInstance() {
        if (instanza == null) {
            instanza = new SessionManager();
        }
        return instanza;
    }


    public synchronized void login(Utente utente) throws EccezioneSessioneUtente {
        if (utentiLoggati.containsKey(utente.getUsername())) {
            throw new EccezioneSessioneUtente("Utente già loggato");
        }
        utentiLoggati.put(utente.getUsername(), utente);
        usernameUtenteCorrente = utente.getUsername();
    }

    public synchronized void logout() {
        if (usernameUtenteCorrente != null) {
            utentiLoggati.remove(usernameUtenteCorrente);
            usernameUtenteCorrente = null;
        }
    }

    public synchronized Utente getUtenteCorrente() {
        if (usernameUtenteCorrente == null) {
            return null;
        }
        return utentiLoggati.get(usernameUtenteCorrente);
    }

    public synchronized Psicologo getPsicologoCorrente() {
        Utente utente = getUtenteCorrente();
        if (utente instanceof Psicologo psicologo) {
            return psicologo;
        }
        return null;
    }
    public synchronized Paziente getPazienteCorrente() {
        Utente utente = getUtenteCorrente();
        if (utente instanceof Paziente paziente) {
            return paziente;
        }
        return null;
    }

    public synchronized Utente changeCurrentUser(String username) {
        Utente utente = utentiLoggati.get(username);
        if (utente != null) {
            usernameUtenteCorrente = username;
            return utente;
        }
        return null;
    }

}


