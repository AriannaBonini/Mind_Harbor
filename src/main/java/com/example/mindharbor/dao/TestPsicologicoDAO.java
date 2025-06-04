package com.example.mindharbor.dao;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.TestPsicologico;
import com.example.mindharbor.model.Utente;

import java.util.List;

public interface TestPsicologicoDAO {
    void assegnaTest(TestPsicologico test) throws EccezioneDAO;
    TestPsicologico getNotificaPazientePerTestAssegnato(Utente paziente) throws EccezioneDAO;
    void modificaStatoNotificaTest(Utente utente, Paziente pazienteSelezionato) throws EccezioneDAO;
    List<TestPsicologico> trovaListaTest(Utente paziente) throws EccezioneDAO;
    TestPsicologico trovaTestPassati(TestPsicologico testDaAggiungere) throws EccezioneDAO;
    TestPsicologico getNumTestSvoltiDaNotificare(Utente psicologo) throws EccezioneDAO;
    List<TestPsicologico> listaTestSvolti(TestPsicologico testPsicologico) throws EccezioneDAO;
    List<TestPsicologico> listaTestSvoltiSenzaPrescrizione(TestPsicologico testPsicologico) throws EccezioneDAO;
    Paziente numTestSvoltiPerPaziente(Utente paziente) throws EccezioneDAO;
    boolean getNumTestAssegnato(Utente paziente) throws EccezioneDAO;

}
