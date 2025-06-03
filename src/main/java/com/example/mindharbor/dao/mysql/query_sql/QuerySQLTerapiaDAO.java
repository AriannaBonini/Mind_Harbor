package com.example.mindharbor.dao.mysql.query_sql;


public abstract class QuerySQLTerapiaDAO {

    protected QuerySQLTerapiaDAO() {/*Costruttore vuoto*/}
    protected static final String PSICOLOGO= "Psicologo";
    protected static final String PAZIENTE="Paziente";
    protected static final String TERAPIA= "Terapia";
    protected static final String DATA_TERAPIA="DataOdierna";
    protected static final String DATA_TEST="DataTest";
    protected static final String TABELLA_TERAPIA="terapia";
    protected static final String NOTIFICA_PAZIENTE="NotificaPaziente";
    protected static final String TOTAL="Total";

    protected static final String INSERISCI_TERAPIA="INSERT INTO " + TABELLA_TERAPIA + " ( " +
            PSICOLOGO + " , " +
            PAZIENTE + " , " +
            TERAPIA + " , " +
            DATA_TERAPIA + " , " +
            DATA_TEST + "," +
            NOTIFICA_PAZIENTE + " ) " +
            "VALUES ( ? , ? , ? , ? , ?, DEFAULT ) " ;

    protected static final String TERAPIE_PAZIENTE="SELECT " +  PSICOLOGO + "," + TERAPIA + "," + DATA_TERAPIA + " " +
            "FROM " + TABELLA_TERAPIA + " " +
            "WHERE " + PAZIENTE + " = ? ";

    protected static final String AGGIORNA_NOTIFICA_PAZIENTE="UPDATE " + TABELLA_TERAPIA + " " +
            "SET " + NOTIFICA_PAZIENTE +  " = 0 " + " " +
            "WHERE " + PAZIENTE + " = ? ";

    protected static final String NOTIFICHE_NUOVE_TERAPIE_PAZIENTE="SELECT COUNT(*) AS Total " +
            "FROM " + TABELLA_TERAPIA + " " +
            "WHERE " + PAZIENTE + " = ?  AND " + NOTIFICA_PAZIENTE + " = 1 ";


    protected static final String ESISTE_TERAPIA = "SELECT 1 FROM " + TERAPIA + " " +
            "WHERE " + PAZIENTE + " = ? AND " + PSICOLOGO + " = ? AND " + DATA_TEST + " = ? " +
            "LIMIT 1";
}
