package com.example.mindharbor.dao.mysql.query_sql;

public abstract class QuerySQLPazienteDAO {

    protected QuerySQLPazienteDAO() {/*Costruttore vuoto*/}
    protected static final String NOME= "Nome";
    protected static final String ETA= "Età";
    protected static final String DIAGNOSI= "Diagnosi";
    protected static final String TABELLA_PAZIENTE= "paziente";
    protected static final String PAZIENTE_USERNAME= "Paziente_Username";
    protected static final String PSICOLOGO_USERNAME= "Username_Psicologo";
    protected static final String SELECT="SELECT ";
    protected static final String TROVA_PAZIENTE= SELECT + PAZIENTE_USERNAME + " " +
            "FROM " + TABELLA_PAZIENTE + " " +
            "WHERE " + PSICOLOGO_USERNAME + " = ?";

    protected static final String INFO_SCHEDA_PERSONALE= SELECT + " " + ETA + " , " + DIAGNOSI + " " +
            "FROM " + TABELLA_PAZIENTE + " " +
            "WHERE " + PAZIENTE_USERNAME + " = ?";

    protected static final String CHECK_ANNI_PAZIENTE = SELECT + " " + ETA + " " +
            "FROM " + TABELLA_PAZIENTE + " " +
            "WHERE " + PAZIENTE_USERNAME + " = ? AND " + ETA + " = ?";

    protected static final String USERNAME_PSICOLOGO=" SELECT " + PSICOLOGO_USERNAME + " " +
            "FROM " + TABELLA_PAZIENTE + " " +
            "WHERE " +  PAZIENTE_USERNAME + " = ? ";

    protected static final String AGGIUNGI_PSICOLOGO_AL_PAZIENTE="UPDATE " + TABELLA_PAZIENTE + " " +
            "SET " + PSICOLOGO_USERNAME +  " = ? " + " " +
            "WHERE " + PAZIENTE_USERNAME + " = ? ";

    protected static final String INSERISCI_DATI_PAZIENTE =
            "INSERT INTO " + TABELLA_PAZIENTE + " (" +
                    PAZIENTE_USERNAME + ", " +
                    ETA + ") " +
                    "VALUES (?, ?)";

}
