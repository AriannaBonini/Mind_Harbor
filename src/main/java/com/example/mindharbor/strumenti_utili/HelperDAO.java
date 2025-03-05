package com.example.mindharbor.strumenti_utili;

import com.example.mindharbor.eccezioni.EccezioneDAO;
import com.example.mindharbor.model.Utente;
import java.sql.Connection;
import java.sql.PreparedStatement;

public interface HelperDAO  {
     PreparedStatement createPreparedStatement(Connection conn, Utente utente) throws EccezioneDAO;
}
