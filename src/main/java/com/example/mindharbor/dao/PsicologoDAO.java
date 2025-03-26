package com.example.mindharbor.dao;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Psicologo;

public interface PsicologoDAO {
    Psicologo getInfoPsicologo(Psicologo psicologo) throws EccezioneDAO;
    void inserisciDatiPsicologo(Psicologo psicologo) throws EccezioneDAO;
}
