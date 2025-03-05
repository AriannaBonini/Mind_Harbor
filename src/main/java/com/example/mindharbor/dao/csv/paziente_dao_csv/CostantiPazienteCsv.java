package com.example.mindharbor.dao.csv.paziente_dao_csv;

public class CostantiPazienteCsv {
    private CostantiPazienteCsv() {}

    protected static final String FILE_PATH="MindHarborDB/csv/paziente.csv";
    protected static final String ERRORE_LETTURA="Errore nella lettura del file CSV";
    protected static final String ERRORE_SCRITTURA="Errore nella scrittura nel file CSV:";

    protected static final Integer INDICE_ANNI=0;
    protected static final Integer INDICE_DIAGNOSI=1;
    protected static final Integer INDICE_PAZIENTE_USERNAME=2;
    protected static final Integer INDICE_PSICOLOGO_USERNAME=3;
}
