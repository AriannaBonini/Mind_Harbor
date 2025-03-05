package com.example.mindharbor.strumenti_utili.costanti;

import java.time.LocalTime;

public class OrariEDataValidiRichiestaAppuntamento {

    private OrariEDataValidiRichiestaAppuntamento() {
    }

    public static final LocalTime ORARIO_APERTURA = LocalTime.of(8, 30);
    public static final LocalTime ORARIO_CHIUSURA = LocalTime.of(18, 30);
    public static final LocalTime INIZIO_PAUSA = LocalTime.of(12, 0);
    public static final LocalTime FINE_PAUSA = LocalTime.of(13, 30);
    public static final String INFO_DATA="No Weekend";

    public static String stampaFasceOrarie() {
        return ORARIO_APERTURA + "-" + INIZIO_PAUSA + " / " + FINE_PAUSA + " - " + ORARIO_CHIUSURA;

    }
}
