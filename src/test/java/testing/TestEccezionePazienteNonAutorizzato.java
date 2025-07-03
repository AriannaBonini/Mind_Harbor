package testing;

import com.example.mindharbor.beans.PazienteBean;
import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import com.example.mindharbor.eccezioni.EccezionePazienteNonAutorizzato;
import com.example.mindharbor.eccezioni.EccezioneSessioneUtente;
import com.example.mindharbor.model.Paziente;
import com.example.mindharbor.model.Psicologo;
import com.example.mindharbor.patterns.facade.DAOFactoryFacade;
import com.example.mindharbor.patterns.facade.TipoPersistenza;
import com.example.mindharbor.sessione.SessionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestEccezionePazienteNonAutorizzato {
    private PrescriviTerapia prescriviTerapia;

    @BeforeEach
    void preparazioneTest() throws EccezioneSessioneUtente{
        DAOFactoryFacade.getInstance().setTipoPersistenza(TipoPersistenza.MYSQL);
        prescriviTerapia = new PrescriviTerapia();

        // Simula uno psicologo con una lista pazienti
        Psicologo psicologo = new Psicologo("PsicologoUsername");
        List<Paziente> pazienti = new ArrayList<>();

        Paziente pazienteAutorizzato = new Paziente();
        pazienteAutorizzato.setUsername("UsernamePazienteValido");
        pazienteAutorizzato.setAnni(30);
        pazienteAutorizzato.setDiagnosi("Disturbo X");

        pazienti.add(pazienteAutorizzato);
        psicologo.setPazienti(pazienti);

        SessionManager.getInstance().login(psicologo);
    }

    @Test
    void testEccezionePazienteNonAutorizzatoLanciata() {
        // Creo un paziente NON presente nella lista
        PazienteBean pazienteNonAutorizzato = new PazienteBean();
        pazienteNonAutorizzato.setUsername("UsernamePazienteNonValido");

        // Verifica che venga lanciata l’eccezione corretta
        EccezionePazienteNonAutorizzato ex = assertThrows(
                EccezionePazienteNonAutorizzato.class,
                () -> prescriviTerapia.getSchedaPersonale(pazienteNonAutorizzato)
        );

        assertEquals("UsernamePazienteNonValido", ex.getUsernamePaziente());
        assertEquals("Il paziente non appartiene allo psicologo", ex.getMessage());
    }

    @AfterEach
    void puliziaDopoTest() {
        SessionManager.getInstance().logout();
    }
}
