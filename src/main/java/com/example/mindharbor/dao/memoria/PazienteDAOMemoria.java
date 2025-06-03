package com.example.mindharbor.dao.memoria;

import com.example.mindharbor.dao.PazienteDAO;
import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PazienteDAOMemoria implements PazienteDAO {
    private final Map<String, Paziente> pazientiInMemoria =new HashMap<>();

    @Override
    public List<Paziente> trovaPazienti(Utente psicologo) throws EccezioneDAO {
        try {
            List<Paziente> pazienteList = new ArrayList<>();

            for (Paziente paziente : pazientiInMemoria.values()) {
                if (paziente.getPsicologo() != null
                        && psicologo.getUsername().equals(paziente.getPsicologo().getUsername())) {
                    Paziente pazienteDaInserire= new Paziente(paziente.getUsername());

                    pazienteList.add(pazienteDaInserire);
                }
            }

            return pazienteList;
        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante il recupero dei pazienti: " + e.getMessage(), e);
        }
    }

    @Override
    public Paziente getInfoSchedaPersonale(Paziente pazienteSelezionato) throws EccezioneDAO {
        try {
            Paziente pazienteMemorizzato = pazientiInMemoria.get(pazienteSelezionato.getUsername());

            if (pazienteMemorizzato == null) {
                throw new EccezioneDAO("Paziente non presente in memoria.");
            }
            return new Paziente(pazienteMemorizzato.getAnni(),pazienteMemorizzato.getDiagnosi());

        } catch (Exception e) {
            throw new EccezioneDAO("Errore nel recupero delle informazioni della scheda personale: " + e.getMessage(), e);
        }
    }

    @Override
    public Paziente checkAnniPaziente(Paziente pazienteCorrente) throws EccezioneDAO {
        try {
            Paziente pazienteInMemoria = pazientiInMemoria.get(pazienteCorrente.getUsername());

            if (pazienteInMemoria == null) {
                throw new EccezioneDAO("Paziente non trovato.");
            }

            Paziente paziente = new Paziente();
            paziente.setAnni(pazienteInMemoria.getAnni());

            return paziente;

        } catch (Exception e) {
            throw new EccezioneDAO("Errore nel controllo degli anni del paziente: " + e.getMessage(), e);
        }
    }

    @Override
    public String getUsernamePsicologo(Utente paziente) throws EccezioneDAO {
        try {
            Paziente pazienteInMemoria = pazientiInMemoria.get(paziente.getUsername());

            if (pazienteInMemoria == null) {
                throw new EccezioneDAO("Paziente non trovato in memoria.");
            }

            if (pazienteInMemoria.getPsicologo() == null) {
                return null;
            }

            return pazienteInMemoria.getPsicologo().getUsername();

        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante il recupero dello username dello psicologo: " + e.getMessage(), e);
        }
    }

    @Override
    public void aggiungiPsicologoAlPaziente(Appuntamento appuntamento) throws EccezioneDAO {
        try {

            String usernamePaziente = appuntamento.getPaziente().getUsername();
            Paziente pazienteInMemoria = pazientiInMemoria.get(usernamePaziente);

            if (pazienteInMemoria == null) {
                throw new EccezioneDAO("Paziente non trovato in memoria.");
            }

            pazienteInMemoria.setPsicologo(new Psicologo(appuntamento.getPsicologo().getUsername()));

        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'associazione dello psicologo al paziente: " + e.getMessage(), e);
        }
    }

    @Override
    public void inserisciDatiPaziente(Paziente paziente) throws EccezioneDAO {
        Paziente pazienteDaInserire= new Paziente();
        try {
            if (pazientiInMemoria.containsKey(paziente.getUsername())) {
                throw new EccezioneDAO("Paziente già presente in memoria.");
            }
            pazienteDaInserire.setAnni(paziente.getAnni());
            pazienteDaInserire.setUsername(paziente.getUsername());

            pazientiInMemoria.put(paziente.getUsername(), pazienteDaInserire);

        } catch (Exception e) {
            throw new EccezioneDAO("Errore durante l'inserimento dei dati del paziente: " + e.getMessage(), e);
        }
    }

}


