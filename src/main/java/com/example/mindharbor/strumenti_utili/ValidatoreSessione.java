package com.example.mindharbor.strumenti_utili;

import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import java.util.List;

public class ValidatoreSessione {

    private ValidatoreSessione() {
        // Classe utility, costruttore privato per evitare istanziazione
    }

    public static final int PAZIENTE_NON_TROVATO = -1;

    // --- PSICOLOGO ---

    /**
     * Controlla se lo psicologo è valido verificando che non sia null e che username sia valorizzato.
     */
    public static boolean psicologoDelPazienteCorrente(Psicologo psicologo) {
        return isValidString(getUsername(psicologo));
    }

    /**
     * Controlla la presenza dei dati anagrafici dello psicologo: nome, cognome e genere.
     */
    public static boolean controllaPresenzaDatiAnagraficiPsicologo(Psicologo psicologo) {
        return psicologo != null &&
                isValidString(psicologo.getNome()) &&
                isValidString(psicologo.getCognome()) &&
                isValidString(psicologo.getGenere());
    }

    /**
     * Controlla la presenza del nome e cognome dello psicologo.
     */
    public static boolean controllaPresenzaNomeECognomePsicologo(Psicologo psicologo) {
        return psicologo != null &&
                isValidString(psicologo.getNome()) &&
                isValidString(psicologo.getCognome());
    }

    /**
     * Controlla la presenza dei dati dello studio dello psicologo: nomeStudio e costoOrario.
     */
    public static boolean controllaPresenzaDatiStudioPsicologo(Psicologo psicologo) {
        return psicologo != null &&
                isValidString(psicologo.getNomeStudio()) &&
                psicologo.getCostoOrario() != null;
    }

    // --- PAZIENTI ---

    /**
     * Controlla la presenza dei dati anagrafici del paziente: username, nome, cognome e genere.
     */
    public static boolean controllaPresenzaDatiAnagraficiPaziente(Paziente p) {
        return p != null &&
                isValidString(p.getUsername()) &&
                isValidString(p.getNome()) &&
                isValidString(p.getCognome()) &&
                isValidString(p.getGenere());
    }

    /**
     * Controlla che la lista dei pazienti non sia nulla o vuota e che ogni paziente abbia dati anagrafici completi.
     */
    public static boolean controllaPresenzaPazientiDelloPsicologoCorrente(List<Paziente> listaPazienti) {
        if (listaPazienti == null || listaPazienti.isEmpty()) {
            return false;
        }
        return listaPazienti.stream()
                .allMatch(ValidatoreSessione::controllaPresenzaDatiAnagraficiPaziente);
    }

    /**
     * Cerca nella lista dei pazienti quello con l'username specificato e che abbia dati anagrafici completi.
     * Ritorna l'indice se trovato, altrimenti PAZIENTE_NON_TROVATO.
     */
    public static int controllaPresenzaPazienteNellaListaPazienti(List<Paziente> listaPazienti, String usernamePazienteSelezionato) {
        if (listaPazienti == null || listaPazienti.isEmpty() || !isValidString(usernamePazienteSelezionato)) {
            return PAZIENTE_NON_TROVATO;
        }
        for (int i = 0; i < listaPazienti.size(); i++) {
            Paziente p = listaPazienti.get(i);
            if (usernamePazienteSelezionato.equals(p.getUsername()) && controllaPresenzaDatiAnagraficiPaziente(p)) {
                return i;
            }
        }
        return PAZIENTE_NON_TROVATO;
    }

    /**
     * Controlla la presenza degli anni e della diagnosi per il paziente all'indice specificato nella lista.
     */
    public static boolean controllaPresenzaAnniEDiagnosiDelPaziente(Integer indice, List<Paziente> listaPazienti) {
        if (listaPazienti == null || indice == null || indice < 0 || indice >= listaPazienti.size()) {
            return false;
        }
        return controllaPresenzaAnniEDiagnosiPaziente(listaPazienti.get(indice));
    }

    /**
     * Controlla che il paziente abbia dati sugli anni e diagnosi valorizzati.
     */
    public static boolean controllaPresenzaAnniEDiagnosiPaziente(Paziente p) {
        return p != null &&
                p.getAnni() != null &&
                isValidString(p.getDiagnosi());
    }

    // --- Metodi di supporto privati ---

    private static boolean isValidString(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String getUsername(Psicologo psicologo) {
        return psicologo != null ? psicologo.getUsername() : null;
    }
}

