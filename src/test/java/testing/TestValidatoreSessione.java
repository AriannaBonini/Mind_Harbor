package testing;

import static org.junit.jupiter.api.Assertions.*;

import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.strumenti_utili.ValidatoreSessione;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class ValidatoreSessioneTest {

    // --- TEST PER PSICOLOGO ---

    @Test
    void testPsicologoDelPazienteCorrente_Valido() {
        Psicologo psicologo = new Psicologo();
        psicologo.setUsername("psicologo");
        assertTrue(ValidatoreSessione.psicologoDelPazienteCorrente(psicologo));
    }

    @Test
    void testPsicologoDelPazienteCorrente_Null() {
        assertFalse(ValidatoreSessione.psicologoDelPazienteCorrente(null));
    }

    @Test
    void testPsicologoDelPazienteCorrente_UsernameNull() {
        Psicologo psicologo = new Psicologo();
        psicologo.setUsername(null);
        assertFalse(ValidatoreSessione.psicologoDelPazienteCorrente(psicologo));
    }

    @Test
    void testControllaPresenzaDatiAnagraficiPsicologo_Completo() {
        Psicologo p = new Psicologo();
        p.setNome("Mario");
        p.setCognome("Rossi");
        p.setGenere("M");
        assertTrue(ValidatoreSessione.controllaPresenzaDatiAnagraficiPsicologo(p));
    }

    @Test
    void testControllaPresenzaDatiAnagraficiPsicologo_Mancante() {
        Psicologo p = new Psicologo();
        p.setNome("Mario");
        p.setCognome(null);
        p.setGenere("M");
        assertFalse(ValidatoreSessione.controllaPresenzaDatiAnagraficiPsicologo(p));
    }

    // --- TEST PER PAZIENTE ---

    @Test
    void testControllaPresenzaDatiAnagraficiPaziente_Completo() {
        Paziente paz = new Paziente();
        paz.setUsername("paziente1");
        paz.setNome("Anna");
        paz.setCognome("Bianchi");
        paz.setGenere("F");
        assertTrue(ValidatoreSessione.controllaPresenzaDatiAnagraficiPaziente(paz));
    }

    @Test
    void testControllaPresenzaDatiAnagraficiPaziente_Mancante() {
        Paziente paz = new Paziente();
        paz.setUsername("paziente1");
        paz.setNome(null);
        paz.setCognome("Bianchi");
        paz.setGenere("F");
        assertFalse(ValidatoreSessione.controllaPresenzaDatiAnagraficiPaziente(paz));
    }

    @Test
    void testControllaPresenzaPazientiDelloPsicologoCorrente_ListaValida() {
        List<Paziente> lista = new ArrayList<>();
        Paziente p1 = new Paziente();
        p1.setUsername("p1"); p1.setNome("Anna"); p1.setCognome("B"); p1.setGenere("F");
        Paziente p2 = new Paziente();
        p2.setUsername("p2"); p2.setNome("Luca"); p2.setCognome("R"); p2.setGenere("M");
        lista.add(p1);
        lista.add(p2);

        assertTrue(ValidatoreSessione.controllaPresenzaPazientiDelloPsicologoCorrente(lista));
    }

    @Test
    void testControllaPresenzaPazientiDelloPsicologoCorrente_ListaConPazienteIncompleto() {
        List<Paziente> lista = new ArrayList<>();
        Paziente p1 = new Paziente();
        p1.setUsername("p1"); p1.setNome(null); p1.setCognome("B"); p1.setGenere("F");
        lista.add(p1);

        assertFalse(ValidatoreSessione.controllaPresenzaPazientiDelloPsicologoCorrente(lista));
    }

    @Test
    void testControllaPresenzaPazienteNellaListaPazienti_Trovato() {
        List<Paziente> lista = new ArrayList<>();
        Paziente p1 = new Paziente();
        p1.setUsername("utente1"); p1.setNome("Anna"); p1.setCognome("B"); p1.setGenere("F");
        lista.add(p1);

        int indice = ValidatoreSessione.controllaPresenzaPazienteNellaListaPazienti(lista, "utente1");
        assertEquals(0, indice);
    }

    @Test
    void testControllaPresenzaPazienteNellaListaPazienti_NonTrovato() {
        List<Paziente> lista = new ArrayList<>();
        Paziente p1 = new Paziente();
        p1.setUsername("utente1"); p1.setNome("Anna"); p1.setCognome("B"); p1.setGenere("F");
        lista.add(p1);

        int indice = ValidatoreSessione.controllaPresenzaPazienteNellaListaPazienti(lista, "utente2");
        assertEquals(ValidatoreSessione.PAZIENTE_NON_TROVATO, indice);
    }

    @Test
    void testControllaPresenzaAnniEDiagnosiDelPaziente_Valido() {
        Paziente p = new Paziente();
        p.setAnni(30);
        p.setDiagnosi("Disturbo ansioso");
        List<Paziente> lista = new ArrayList<>();
        lista.add(p);

        assertTrue(ValidatoreSessione.controllaPresenzaAnniEDiagnosiDelPaziente(0, lista));
    }

    @Test
    void testControllaPresenzaAnniEDiagnosiDelPaziente_IndiceFuoriRange() {
        List<Paziente> lista = new ArrayList<>();
        assertFalse(ValidatoreSessione.controllaPresenzaAnniEDiagnosiDelPaziente(5, lista));
    }

    @Test
    void testControllaPresenzaAnniEDiagnosiPaziente_Valido() {
        Paziente p = new Paziente();
        p.setAnni(25);
        p.setDiagnosi("Depressione");
        assertTrue(ValidatoreSessione.controllaPresenzaAnniEDiagnosiPaziente(p));
    }

    @Test
    void testControllaPresenzaAnniEDiagnosiPaziente_Invalido() {
        Paziente p = new Paziente();
        p.setAnni(null);
        p.setDiagnosi(null);
        assertFalse(ValidatoreSessione.controllaPresenzaAnniEDiagnosiPaziente(p));
    }

}
