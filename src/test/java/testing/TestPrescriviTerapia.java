package testing;


import com.example.mindharbor.beans.RisultatiTestBean;
import com.example.mindharbor.controller_applicativi.psicologo.PrescriviTerapia;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


class TestPrescriviTerapia {  //Arianna Bonini
    private final PrescriviTerapia prescriviTerapia = new PrescriviTerapia();

    @ParameterizedTest
    @CsvSource({
            "0, 15, 100.0",   // risultatoPrecedente = 0, risultatoUltimoTest = 15 → limitato a 100%
            "15, 0, -100.0",  // risultatoPrecedente = 15, risultatoUltimoTest = 0 → limitato a -100%
            "20, 25, 25.0",   // valori normali
            "-10, -5, 50.0",  // valori negativi
            "0, 0, 0.0"       // entrambi zero
    })
    void testCalcolaProgressoParametrizzato(Integer risultatoPrecedente, Integer risultatoUltimoTest, Double risultatoAtteso) {
        RisultatiTestBean bean = new RisultatiTestBean();
        bean.setRisultatoTestPrecedente(risultatoPrecedente);
        bean.setRisultatoUltimoTest(risultatoUltimoTest);

        Double result = prescriviTerapia.calcolaProgresso(bean);

        assertEquals(risultatoAtteso, result, 0.0001);
    }

    @Test
    void testValoriNull() {
        RisultatiTestBean bean = new RisultatiTestBean();
        bean.setRisultatoTestPrecedente(null);
        bean.setRisultatoUltimoTest(null);

        Double result = prescriviTerapia.calcolaProgresso(bean);

        assertNull(result);
    }
}

