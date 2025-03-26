package com.example.mindharbor.dao.mysql.query_sql;

public abstract class QuerySQLPsicologoDAO {

    protected QuerySQLPsicologoDAO(){/*Costruttore vuoto*/}
    protected static final String USERNAME = "Psicologo_Username";
    protected static final String TABELLA_PSICOLOGO = "psicologo";
    protected static final String COSTO_ORARIO="CostoOrario";
    protected static final String NOME_STUDIO="Nome_Studio";

    protected static final String INFO_PSICOLOGO="SELECT " + COSTO_ORARIO + " , " + NOME_STUDIO + " " +
            "FROM " + TABELLA_PSICOLOGO + " " +
            "WHERE " + USERNAME + " = ? ";

    protected static final String INSERISCI_DATI_PSICOLOGO =
            "INSERT INTO " + TABELLA_PSICOLOGO + " (" +
                     USERNAME + ", " +
                    COSTO_ORARIO + ", " +
                    NOME_STUDIO + ") " +
                    "VALUES (?, ?, ?)";

}
