package com.example.mindharbor.dao;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;

import java.util.List;

public interface PazienteDAO {
    List<Paziente> trovaPazienti(Utente psicologo) throws EccezioneDAO;
    Paziente getInfoSchedaPersonale(Paziente pazienteSelezionato) throws EccezioneDAO;
    Paziente checkAnniPaziente(Paziente paziente) throws EccezioneDAO;
    Psicologo getUsernamePsicologo(Utente paziente) throws EccezioneDAO;
    void aggiungiPsicologoAlPaziente(Appuntamento appuntamento) throws EccezioneDAO;
    void inserisciDatiPaziente(Paziente paziente) throws EccezioneDAO;
}
