package com.example.mindharbor.dao;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Appuntamento;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.model.Utente;

import java.util.List;

public interface AppuntamentoDAO {
    List<Appuntamento> trovaAppuntamentiPaziente(Utente paziente, String selectedTabName) throws EccezioneDAO;
    List<Appuntamento> trovaAppuntamentiPsicologo(Utente psicologo, String selectedTabName) throws EccezioneDAO;
    void insertRichiestaAppuntamento(Appuntamento appuntamento) throws EccezioneDAO;
    Appuntamento notificheNuoviAppuntamentiPaziente(Paziente paziente) throws EccezioneDAO;
    Appuntamento notificheNuoviAppuntamentiPsicologo(Psicologo psicologo) throws EccezioneDAO;
    List<Appuntamento> trovaRichiesteAppuntamento(Psicologo psicologo) throws EccezioneDAO;
    void aggiornaStatoNotifica(Appuntamento richiestaAppuntamento) throws EccezioneDAO;
    Appuntamento getInfoRichiesta(Appuntamento richiestaAppuntamento) throws EccezioneDAO;
    void accettaRichiesta(Appuntamento appuntamento) throws EccezioneDAO;
    void eliminaRichiesteDiAppuntamentoPerAltriPsicologi(Appuntamento appuntamento) throws EccezioneDAO;
    void eliminaRichiesta(Appuntamento appuntamento) throws EccezioneDAO;
    boolean getDisp(Integer idAppuntamento, Psicologo psicologo) throws EccezioneDAO;
    void aggiornaStatoNotificaPaziente(Utente utente) throws EccezioneDAO;



}
