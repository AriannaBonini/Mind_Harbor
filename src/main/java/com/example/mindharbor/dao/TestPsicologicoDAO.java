package com.example.mindharbor.dao;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.TestPsicologico;
import com.example.mindharbor.model.Utente;

import java.util.List;

public interface TestPsicologicoDAO {
    void assegnaTest(TestPsicologico test) throws EccezioneDAO;
    Integer getNotificaPazientePerTestAssegnato(Utente paziente) throws EccezioneDAO;
    void modificaStatoNotificaTest(Utente utente, Paziente pazienteSelezionato) throws EccezioneDAO;
    List<TestPsicologico> trovaListaTest(Utente paziente) throws EccezioneDAO;
    Integer trovaTestPassati(TestPsicologico testDaAggiungere) throws EccezioneDAO;
    Integer getNumTestSvoltiDaNotificare(Utente psicologo) throws EccezioneDAO;
    Integer getNumTestSvoltiSenzaPrescrizione(Utente utentePsicologo, Paziente paziente) throws EccezioneDAO;
    List<TestPsicologico> listaTestSvoltiSenzaPrescrizione(String usernamePaziente, String usernamePsicologo) throws EccezioneDAO;
    Paziente numTestSvoltiPerPaziente(Utente paziente) throws EccezioneDAO;
    Integer getNumTestAssegnato(Paziente paziente) throws EccezioneDAO;

}
