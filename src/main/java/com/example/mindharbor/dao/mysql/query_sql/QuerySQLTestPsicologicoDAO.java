package com.example.mindharbor.dao.mysql.query_sql;


public abstract class QuerySQLTestPsicologicoDAO {
    protected QuerySQLTestPsicologicoDAO() {/*Costruttore vuoto*/}
    protected static final String DATA="DataOdierna";
    protected static final String RISULTATO="Risultato";
    protected static final String PSICOLOGO="Psicologo";
    protected static final String PAZIENTE="Paziente";
    protected static final String TEST="Test";
    protected static final String SVOLTO="Svolto";
    protected static final String STATO_PSICOLOGO="statoNotificaPsicologo";
    protected static final String TABELLA_TESTPSICOLOGICO= "testpsicologico";
    protected static final String TERAPIA= "terapia";
    protected static final String STATO= "statoNotificaPaziente";
    protected static final String DATA_TEST="DataTest";
    protected static final String TOTAL="Total";
    protected static final String SELECT="SELECT";
    protected static final String FROM="FROM";
    protected static final String UPDATE="UPDATE";
    protected static final String CONFRONTO=" = ? ";
    protected static final String ASSEGNA_TEST="INSERT INTO " + TABELLA_TESTPSICOLOGICO + " (" +
            DATA + ", " +
            RISULTATO + ", " +
            PSICOLOGO + ", " +
            PAZIENTE + ", " +
            TEST + ", " +
            STATO + ", " +
            SVOLTO + ", " +
            STATO_PSICOLOGO + ") " +
            "VALUES (?, DEFAULT, ?, ?, ?, DEFAULT, DEFAULT, DEFAULT)";

    protected static final String NOTIFICA_PAZIENTE_NUOVI_TEST ="SELECT COUNT(*) AS Total " + " " +
            FROM + " " + TABELLA_TESTPSICOLOGICO + " " +
            "WHERE " + PAZIENTE + " = ? " + " AND " +  STATO + " = 1;";

    protected static final String MODIFICA_STATO_NOTIFICA_PAZIENTE=UPDATE + " " +  TABELLA_TESTPSICOLOGICO + " " +
            "SET " +  STATO + " = 0 " +
            "WHERE " +  PAZIENTE +  " = ? " + " AND " +  STATO + " = 1";

    protected static final String MODIFICA_STATO_NOTIFICA_PSICOLOGO="UPDATE " + TABELLA_TESTPSICOLOGICO + " " +
            "SET " + STATO_PSICOLOGO +  " = 0 " +
            "WHERE " + PSICOLOGO + " = ? " + " AND " + STATO_PSICOLOGO + " = 1 " + " AND " +  PAZIENTE  + " " + CONFRONTO;


    protected static final String TROVA_LISTA_TEST_PAZIENTE=SELECT + " " + RISULTATO + " , " + TEST + " , " +  DATA + " , " +  SVOLTO  + " " +
            "FROM " + TABELLA_TESTPSICOLOGICO + " " +
            "WHERE " +  PAZIENTE + " " + CONFRONTO;


    protected static final String TROVA_ULTIMO_TEST= SELECT + " " + RISULTATO + " " +
            "FROM " + TABELLA_TESTPSICOLOGICO + " " +
            "WHERE " + DATA + " = (SELECT MAX( " + DATA + ") " +
            "FROM " + TABELLA_TESTPSICOLOGICO + " "  +
            "WHERE " + PAZIENTE + " = ? " + " AND " +  SVOLTO + " = 1 " + " AND " + TEST + " = ? ) ";

    protected static final String AGGIORNA_TEST_SVOLTO="UPDATE " +  TABELLA_TESTPSICOLOGICO + " " +
            "SET " + RISULTATO + " = ? , " +  SVOLTO + " = 1 , " +  STATO_PSICOLOGO + " = 1  " +
            "WHERE " + PAZIENTE + " = ? " + " AND " +  DATA + " " + CONFRONTO;

    protected static final String NOTIFICA_PSICOLOGO_TEST_SVOLTI="SELECT COUNT(*) AS Total " +
            FROM + " " + TABELLA_TESTPSICOLOGICO + " " +
            "WHERE " + STATO_PSICOLOGO + " = 1 " + " AND " + PSICOLOGO + " " + CONFRONTO;

    protected static final String NUMERO_TEST_SVOLTI_SENZA_PRESCRIZIONE="SELECT COUNT(*) AS Total " +
            "FROM " + TABELLA_TESTPSICOLOGICO + " " +
            "WHERE " + SVOLTO + " = 1 AND " + PSICOLOGO + " = ? AND " + PAZIENTE + " = ? AND " +
            " NOT EXISTS ( " +
            "   SELECT 1 " +
            "   FROM " + TERAPIA + " " +
            "   WHERE " + TERAPIA + "." + PAZIENTE + " = " + TABELLA_TESTPSICOLOGICO + "." + PAZIENTE + " " +
            "     AND " + TERAPIA + "." + PSICOLOGO + " = " + TABELLA_TESTPSICOLOGICO + "." + PSICOLOGO + " " +
            "     AND " + TERAPIA + "." + DATA_TEST + " = " + TABELLA_TESTPSICOLOGICO + "." + DATA +
            ")";

    protected static final String LISTA_TEST_SVOLTI_SENZA_PRESCRIZIONE="SELECT " + DATA + ", " + RISULTATO + ", " + TEST + " " +
            "FROM " + TABELLA_TESTPSICOLOGICO + " " +
            "WHERE " + SVOLTO + " = 1 AND " + PSICOLOGO + " = ? AND " + PAZIENTE + " = ? " +
            "AND NOT EXISTS ( " +
            "SELECT 1 " +
            "FROM " + TERAPIA + " " +
            "WHERE " + TABELLA_TESTPSICOLOGICO + "." + PSICOLOGO + " = " + TERAPIA + "." + PSICOLOGO + " " +
            "AND " + TABELLA_TESTPSICOLOGICO + "." + PAZIENTE + " = " + TERAPIA + "." + PAZIENTE + " " +
            "AND " + TABELLA_TESTPSICOLOGICO + "." + DATA + " = " + TERAPIA + "." +  DATA_TEST +  " )";

    protected static final String NUMERO_TEST_SVOLTI_PAZIENTE="SELECT SUM(statoNotificaPsicologo) " + " " +
            "FROM " + TABELLA_TESTPSICOLOGICO + " " +
            "WHERE " + PAZIENTE + " " + CONFRONTO;

    protected static final String TEST_ASSEGNATO_IN_DATA_ODIERNA = "SELECT COUNT(*) AS test_assegnato " +
            "FROM " + TABELLA_TESTPSICOLOGICO + " " +
            "WHERE DATE(" + DATA + ") = CURDATE() AND " + PAZIENTE + " " + CONFRONTO;
    //questa query mi restituisce TRUE se viene trovato almeno un test assegnato la paziente nella data odierna; altrimenti restituisce FALSE

}



