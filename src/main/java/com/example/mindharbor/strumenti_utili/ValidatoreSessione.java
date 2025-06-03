package com.example.mindharbor.strumenti_utili;

import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import java.util.List;

public class ValidatoreSessione {

    private ValidatoreSessione(){}

    // --- PSICOLOGO ---
    public static boolean psicologoDelPazienteCorrente(Psicologo psicologo) {
        return psicologo != null &&
                psicologo.getUsername() != null;
    }

    public static boolean controllaPresenzaDatiAnagraficiPsicologo(Psicologo psicologo) {
        return psicologo != null &&
                psicologo.getNome() != null &&
                psicologo.getCognome() != null &&
                psicologo.getGenere() != null;
    }

    public static boolean controllaPresenzaNomeECognomePsicologo(Psicologo psicologo) {
        return psicologo != null &&
                psicologo.getNome() != null &&
                psicologo.getCognome() != null;
    }
    public static boolean controllaPresenzaDatiStudioPsicologo(Psicologo psicologo) {
        return psicologo != null &&
                psicologo.getNomeStudio() != null &&
                psicologo.getCostoOrario() != null;
    }

    // --- PAZIENTI ---

    public static boolean controllaPresenzaDatiAnagraficiPaziente(Paziente p) {
        return p != null &&
                p.getUsername() != null &&
                p.getNome() != null &&
                p.getCognome() != null &&
                p.getGenere() != null;
    }

    public static boolean controllaPresenzaPazientiDelloPsicologoCorrente(List<Paziente> listaPazienti) {
        if (listaPazienti == null || listaPazienti.isEmpty()) {
            return false;
        }
        for (Paziente p : listaPazienti) {
            if (!controllaPresenzaDatiAnagraficiPaziente(p)) {
                return false;
            }
        }
        return true;
    }
    public static Integer controllaPresenzaPazienteNellaListaPazienti(List<Paziente> listaPazienti, String usernamePazienteSelezionato){
        if (listaPazienti== null || listaPazienti.isEmpty() || usernamePazienteSelezionato == null) {
            return -1;
        }
        for (int i = 0; i < listaPazienti.size(); i++) {
            Paziente p = listaPazienti.get(i);
            if (usernamePazienteSelezionato.equals(p.getUsername()) &&
                    controllaPresenzaDatiAnagraficiPaziente(p)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean controllaPresenzaAnniEDiagnosiDelPaziente(Integer indice, List<Paziente> listaPazienti) {
        if (listaPazienti == null || indice == null || indice < 0 || indice >= listaPazienti.size()) {
            return false;
        }
        Paziente paziente = listaPazienti.get(indice);
        return controllaPresenzaAnniEDiagnosiPaziente(paziente);
    }

    public static boolean controllaPresenzaAnniEDiagnosiPaziente(Paziente p) {
        return p != null &&
                p.getAnni() != null &&
                p.getDiagnosi() != null;
    }
}

