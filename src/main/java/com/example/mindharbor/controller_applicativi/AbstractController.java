package com.example.mindharbor.controller_applicativi;

import com.example.mindharbor.eccezioni.EccezioneSessioneUtente;
import com.example.mindharbor.tipo_utente.UserType;

public abstract class AbstractController {
    protected abstract void storeSessionUtente(String username, String nome, String cognome, UserType userType,String usernamePsicologo) throws EccezioneSessioneUtente;
    protected abstract void storeSessionUtente(String username, String nome, String cognome, UserType userType) throws EccezioneSessioneUtente;
}
