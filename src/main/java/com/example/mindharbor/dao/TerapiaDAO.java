package com.example.mindharbor.dao;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Terapia;
import com.example.mindharbor.model.Utente;

import java.util.List;

public interface TerapiaDAO {
    void insertTerapia(Terapia terapia) throws EccezioneDAO;
    List<Terapia> getTerapie(Utente utente) throws EccezioneDAO;
    Integer getNuoveTerapie(Utente paziente) throws EccezioneDAO;
}
