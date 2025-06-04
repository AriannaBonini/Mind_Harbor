package com.example.mindharbor.dao;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Utente;

import java.util.List;

public interface AppuntamentoDAO {
    List<Appuntamento> trovaAppuntamentiPaziente(Appuntamento appuntamento) throws EccezioneDAO;
    List<Appuntamento> trovaAppuntamentiPsicologo(Appuntamento appuntamento) throws EccezioneDAO;
    void insertRichiestaAppuntamento(Appuntamento appuntamento) throws EccezioneDAO;
    Appuntamento notificheNuoviAppuntamentiPaziente(Utente paziente) throws EccezioneDAO;
    Appuntamento notificheNuoviAppuntamentiPsicologo(Utente psicologo) throws EccezioneDAO;
    List<Appuntamento> trovaRichiesteAppuntamento(Utente psicologo) throws EccezioneDAO;
    void aggiornaStatoNotifica(Appuntamento richiestaAppuntamento) throws EccezioneDAO;
    Appuntamento getInfoRichiesta(Appuntamento richiestaAppuntamento) throws EccezioneDAO;
    void accettaRichiesta(Appuntamento appuntamento) throws EccezioneDAO;
    void eliminaRichiesteDiAppuntamentoPerAltriPsicologi(Appuntamento appuntamento) throws EccezioneDAO;
    void eliminaRichiesta(Appuntamento appuntamento) throws EccezioneDAO;
    boolean getDisp(Appuntamento appuntamento) throws EccezioneDAO;
    void aggiornaStatoNotificaPaziente(Utente paziente) throws EccezioneDAO;

}
