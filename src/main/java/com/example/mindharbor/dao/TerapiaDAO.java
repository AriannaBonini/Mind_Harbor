package com.example.mindharbor.dao;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Terapia;
import com.example.mindharbor.model.TestPsicologico;
import com.example.mindharbor.model.Utente;

import java.util.List;

public interface TerapiaDAO {
    void aggiungiTerapia(Terapia terapia) throws EccezioneDAO;
    List<Terapia> getTerapie(Utente paziente) throws EccezioneDAO;
    Terapia getNuoveTerapie(Utente paziente) throws EccezioneDAO;
    boolean esistenzaTerapiaPerUnTest(TestPsicologico testPsicologico) throws EccezioneDAO;
}
