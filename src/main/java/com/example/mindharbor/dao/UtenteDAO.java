package com.example.mindharbor.dao;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;
import java.util.List;

public interface UtenteDAO {
    Utente trovaUtente(Utente credenzialiUtenteLogin) throws EccezioneDAO;
    Utente trovaNomeCognome(Utente utente) throws EccezioneDAO;
    List<Psicologo> listaUtentiDiTipoPsicologo(String usernamePsicologo) throws EccezioneDAO;
    List<Appuntamento> richiestaAppuntamentiInfoPaziente(List<Appuntamento> richiesteAppuntamenti) throws EccezioneDAO;
    Utente trovaInfoUtente(Utente paziente) throws EccezioneDAO;
    Boolean controllaUsernameERegistraNuovoUtente(Utente nuovoUtente) throws EccezioneDAO;
}
