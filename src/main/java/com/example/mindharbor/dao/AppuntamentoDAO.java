package com.example.mindharbor.dao;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Utente;

import java.util.List;

public interface AppuntamentoDAO {
    List<Appuntamento> trovaAppuntamentiPaziente(Utente paziente, String selectedTabName) throws EccezioneDAO;
    List<Appuntamento> trovaAppuntamentiPsicologo(Utente psicologo, String selectedTabName) throws EccezioneDAO;
    void insertRichiestaAppuntamento(Appuntamento appuntamento) throws EccezioneDAO;
    Integer getNumRicAppDaNotificare(Utente utente) throws EccezioneDAO;
    List<Appuntamento> trovaRichiesteAppuntamento(Utente utente) throws EccezioneDAO;
    void updateStatoNotifica(Appuntamento richiestaAppuntamento) throws EccezioneDAO;
    Appuntamento getInfoRichiesta(Appuntamento richiestaAppuntamento) throws EccezioneDAO;
    void updateRichiesta(Appuntamento appuntamento) throws EccezioneDAO;
    void eliminaRichiesteDiAppuntamentoPerAltriPsicologi(Appuntamento appuntamento) throws EccezioneDAO;
    void eliminaRichiesta(Appuntamento appuntamento) throws EccezioneDAO;
    boolean getDisp(Integer idAppuntamento, Utente utente) throws EccezioneDAO;
    void aggiornaStatoNotificaPaziente(Utente utente) throws EccezioneDAO;



}
