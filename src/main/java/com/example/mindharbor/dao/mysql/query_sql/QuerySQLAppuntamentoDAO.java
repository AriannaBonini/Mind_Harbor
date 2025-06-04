package com.example.mindharbor.dao.mysql.query_sql;

public abstract class QuerySQLAppuntamentoDAO {
    protected QuerySQLAppuntamentoDAO() {/*Costruttore vuoto*/}
    protected static final String DATA = "Data";
    protected static final String ORA = "Ora";
    protected static final String USERNAME_PAZIENTE = "Username_Paziente";
    protected static final String USERNAME_PSICOLOGO = "Username_Psicologo";
    protected static final String TABELLA_APPUNTAMENTO="Appuntamento";
    protected static final String ID_APPUNTAMENTO="ID_Appuntamento";
    protected static final String STATO_APPUNTAMENTO="statoAppuntamento";
    protected static final String STATO_NOTIFICA_PSICOLOGO ="statoNotificaPsicologo";
    protected static final String STATO_NOTIFICA_PAZIENTE="statoNotificaPaziente";
    protected static final String CONFRONTO="= ?";
    protected static final String UGUALE_A ="= a.";
    protected static final String UGUALE_1 ="= 1";
    protected static final String UPDATE="UPDATE";
    protected static final String SELECT="SELECT";
    protected static final String AND= " AND ";

    protected static final String TROVA_APPUNTAMENTI_PSICOLOGO_IN_PROGRAMMA= SELECT + " " + DATA + " , "  + ORA + " , " + USERNAME_PAZIENTE + " " +
            "FROM " + TABELLA_APPUNTAMENTO + " " +
            "WHERE " + STATO_APPUNTAMENTO + " " + UGUALE_1 + AND + USERNAME_PSICOLOGO + " " + CONFRONTO +
            AND + DATA + " >= NOW() ";

    protected static final String TROVA_APPUNTAMENTI_PSICOLOGO_PASSATI= SELECT + " " + DATA + " , "  + ORA + " , " + USERNAME_PAZIENTE + " " +
            "FROM " + TABELLA_APPUNTAMENTO + " " +
            "WHERE " + STATO_APPUNTAMENTO + " " + UGUALE_1 + AND + USERNAME_PSICOLOGO + " " + CONFRONTO +
            AND + DATA + " < NOW() ";
    protected static final String TROVA_APPUNTAMENTI_PAZIENTE_IN_PROGRAMMA = SELECT + " " + DATA + " , " + ORA + " " +
                    "FROM " + TABELLA_APPUNTAMENTO + " " +
                    "WHERE " + STATO_APPUNTAMENTO + " " + UGUALE_1 + AND + USERNAME_PAZIENTE + " " + CONFRONTO +
                    AND + DATA + " >= NOW() ";

    protected static final String TROVA_APPUNTAMENTI_PAZIENTE_PASSATI = SELECT + " " + DATA + " , " + ORA + " " +
                    "FROM " + TABELLA_APPUNTAMENTO + " " +
                    "WHERE " + STATO_APPUNTAMENTO + " " + UGUALE_1 + AND + USERNAME_PAZIENTE + " " + CONFRONTO +
                    AND + DATA + " < NOW() ";


    protected static final String INSERISCI_RICHIESTA_APPUNTAMENTO="INSERT INTO " + TABELLA_APPUNTAMENTO + " ( " +
            ID_APPUNTAMENTO + ", " +
            DATA + ", " +
            ORA + " , " +
            USERNAME_PAZIENTE + " , " +
            USERNAME_PSICOLOGO + " , " +
            STATO_APPUNTAMENTO + " , " +
            STATO_NOTIFICA_PSICOLOGO + " , " +
            STATO_NOTIFICA_PAZIENTE + " ) " +
            "VALUES (DEFAULT, ? , ? , ? , ? , DEFAULT, DEFAULT, DEFAULT ) ";

    protected static final String NUMERO_RICHIESTE_APPUNTAMENTI_DA_NOTIFICARE_PSICOLOGO="SELECT COUNT(*) AS Totale " +
            "FROM " + TABELLA_APPUNTAMENTO + " " +
            "WHERE " + USERNAME_PSICOLOGO + " = ?  AND " + STATO_NOTIFICA_PSICOLOGO + " " + UGUALE_1;

    protected static final String NUMERO_NUOVI_APPUNTAMENTI_DA_NOTIFICARE_PAZIENTE="SELECT COUNT(*) AS Totale " +
            "FROM " + TABELLA_APPUNTAMENTO + " " +
            "WHERE " + USERNAME_PAZIENTE + " = ?  AND " + STATO_NOTIFICA_PAZIENTE + " = 1 ";

    protected static final String TROVA_RICHIESTE_APPUNTAMENTI_PSICOLOGO="SELECT " + " " +
            STATO_NOTIFICA_PSICOLOGO + " , " +
            ID_APPUNTAMENTO + " , " +
            USERNAME_PAZIENTE + " " +
            "FROM " + TABELLA_APPUNTAMENTO + " " +
            "WHERE " + STATO_APPUNTAMENTO + " = 0 " + " AND " + USERNAME_PSICOLOGO +  " " + CONFRONTO;



    protected static final String UPDATE_STATO_NOTIFICA_PSICOLOGO=UPDATE + " " + TABELLA_APPUNTAMENTO + " " +
            "SET " + STATO_NOTIFICA_PSICOLOGO +  " = 0 " + " " +
            "WHERE " + ID_APPUNTAMENTO +  " " + CONFRONTO;


    protected static final String INFORMAZIONI_RICHIESTA_APPUNTAMENTO="SELECT " +
            DATA + " , " +
            ORA + " " +
            "FROM " + TABELLA_APPUNTAMENTO + " " +
            "WHERE " + ID_APPUNTAMENTO + " = ?";


    protected static final String RICHIESTA_DI_APPUNTAMENTO_ACCETTATA ="UPDATE " + TABELLA_APPUNTAMENTO + " " +
            "SET " + STATO_APPUNTAMENTO +  " = 1 " + " , " + STATO_NOTIFICA_PAZIENTE + " = 1 " + " " +
            "WHERE " + ID_APPUNTAMENTO + " = ? ";

    protected static final String ELIMINA_RICHIESTA_DI_APPUNTAMENTO="DELETE FROM " + TABELLA_APPUNTAMENTO + " " +
            "WHERE " + ID_APPUNTAMENTO + " = ? ";

    protected static final String CONTROLLA_DISPONIBILITA="SELECT " + " a. " + ID_APPUNTAMENTO + " " +
            "FROM " + TABELLA_APPUNTAMENTO + " a " +
            "WHERE " + " a. " + ID_APPUNTAMENTO + " = ? AND " + USERNAME_PSICOLOGO + " = ? " + " AND " +
            "NOT EXISTS ( SELECT 1 " +
            "FROM " + TABELLA_APPUNTAMENTO + " a1 " +
            "WHERE " + " a1. " + STATO_APPUNTAMENTO + " = 1 AND " +
            " a1. " + DATA +  " " + UGUALE_A + " "  + DATA + " AND " + " a1. " + USERNAME_PSICOLOGO + " = a. " + USERNAME_PSICOLOGO + " AND " +
            " a1. " + ORA +  " " + UGUALE_A + " "  + ORA + " ) ";

    protected static final String UPDATE_STATO_NOTIFICA_PAZIENTE="UPDATE " + TABELLA_APPUNTAMENTO + " " +
            "SET " + STATO_NOTIFICA_PAZIENTE +  " = 0 " +
            "WHERE " + USERNAME_PAZIENTE + " = ? AND " + STATO_NOTIFICA_PAZIENTE + " = 1 ";

    protected static final String ELIMINA_RICHIESTE_DI_APPUNTAMENTO="DELETE FROM " + TABELLA_APPUNTAMENTO + " " +
            "WHERE " + USERNAME_PAZIENTE + " " + CONFRONTO + " " +
            "AND " + USERNAME_PSICOLOGO + " <> ? ";
}
