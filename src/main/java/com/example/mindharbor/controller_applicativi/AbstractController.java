package com.example.mindharbor.controller_applicativi;

import com.example.mindharbor.eccezioni.EccezioneSessioneUtente;
import com.example.mindharbor.enumerazioni.TipoUtente;
import com.example.mindharbor.model.Psicologo;

public abstract class AbstractController {
    protected abstract void assegnaSessione(String username, String nome, String cognome, TipoUtente tipoUtente, Psicologo psicologo) throws EccezioneSessioneUtente;
    protected abstract void assegnaSessione(String username, String nome, String cognome, TipoUtente tipoUtente) throws EccezioneSessioneUtente;
}
