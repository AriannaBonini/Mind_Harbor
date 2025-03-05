package com.example.mindharbor.dao.csv.terapia_dao_csv;

public class CostantiTerapiaCsv {
    private CostantiTerapiaCsv() {}

    protected static final String FILE_PATH="MindHarborDB/csv/terapia.csv";
    protected static final Integer INDICE_PSICOLOGO=0;
    protected static final Integer INDICE_PAZIENTE=1;
    protected static final Integer INDICE_TERAPIA=2;
    protected static final Integer INDICE_DATA_TERAPIA =3;
    protected static final Integer INDICE_NOTIFICA_PAZIENTE=5;
    protected static final Integer INDICE_DATA_TEST = 4;
    protected static final String NOTIFICA_PAZIENTE_CONSEGNATA = "0";
    protected static final String NOTIFICA_PAZIENTE_DA_CONSEGNARE = "1";
}
