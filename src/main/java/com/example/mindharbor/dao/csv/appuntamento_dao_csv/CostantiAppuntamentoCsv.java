package com.example.mindharbor.dao.csv.appuntamento_dao_csv;

public class CostantiAppuntamentoCsv {
    private CostantiAppuntamentoCsv(){}

    protected static final String FILE_PATH = "MindHarborDB/csv/appuntamento.csv";
    protected static final Integer INDICE_ID_APPUNTAMENTO = 0;
    protected static final Integer INDICE_DATA = 1;
    protected static final Integer INDICE_ORA = 2;
    protected static final Integer INDICE_USERNAME_PAZIENTE = 3;
    protected static final Integer INDICE_USERNAME_PSICOLOGO = 4;
    protected static final Integer INDICE_STATO_APPUNTAMENTO = 5;
    protected static final Integer INDICE_STATO_NOTIFICA_PSICOLOGO = 6;
    protected static final Integer INDICE_STATO_NOTIFICA_PAZIENTE = 7;
    protected static final String RICHIESTA_IN_ATTESA = "0";
    protected static final String NOTIFICA_PAZIENTE_NON_ATTIVA = "0";
    protected static final String NOTIFICA_PSICOLOGO_ATTIVA = "1";
    protected static final Integer APPUNTAMENTO_NON_ACCETTATO = 0;
    protected static final String APPUNTAMENTO_ACCETTATO = "1";
    protected static final String NOTIFICA_PSICOLOGO_CONSEGNATA = "0";
    protected static final String NOTIFICA_PAZIENTE_CONSEGNATA = "0";
    protected static final String NOTIFICA_PAZIENTE_DA_CONSEGNARE = "1";
    protected static final String NOTIFICA_PSICOLOGO_DA_CONSEGNARE = "1";

}
