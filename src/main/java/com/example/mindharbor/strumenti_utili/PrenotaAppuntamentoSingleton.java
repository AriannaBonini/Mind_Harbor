package com.example.mindharbor.strumenti_utili;

import com.example.mindharbor.controller_applicativi.paziente.PrenotaAppuntamento;

public class PrenotaAppuntamentoSingleton {
   private static PrenotaAppuntamento prenotaAppuntamento=null;

    private PrenotaAppuntamentoSingleton() {}

    // Metodo sincronizzato per ottenere l'istanza unica
    public static synchronized PrenotaAppuntamento getInstance() {
        if (prenotaAppuntamento == null) {
            prenotaAppuntamento = new PrenotaAppuntamento();
        }
        return prenotaAppuntamento;
    }
}
